#!/bin/bash

echo "🚀 Starting Blue Bird API setup..."

# ✅ Exportar credenciales dummy para LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test

# Paso 1: Detener contenedores y eliminar volúmenes
echo "🧹 Stopping containers and removing volumes..."
docker-compose down -v

# Paso 2: Construir y levantar contenedores en segundo plano
echo "🔧 Building and starting containers..."
docker-compose up --build -d

# Paso 3: Esperar a que LocalStack (SQS) esté listo (puerto 4566)
echo "⏳ Waiting for LocalStack (SQS) to be ready..."
for i in {1..30}; do
  if nc -z localhost 4566; then
    echo "✅ LocalStack port 4566 is up!"
    break
  fi
  echo "⏱️ Waiting for LocalStack... ($i)"
  sleep 2
done


# Paso 4: Crear la cola DLQ
echo "📦 Creating DLQ queue 'create-tweet-dlq'..."
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name create-tweet-dlq

# Paso 5: Obtener ARN de la DLQ
DLQ_ARN=$(aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/create-tweet-dlq \
  --attribute-names QueueArn \
  --query 'Attributes.QueueArn' \
  --output text)

echo "📎 DLQ ARN: $DLQ_ARN"


# Paso 6: Crear la cola principal con RedrivePolicy que apunta a la DLQ
echo "📦 Creating main queue 'create-tweet' with DLQ configuration..."
aws --endpoint-url=http://localhost:4566 sqs create-queue \
  --queue-name create-tweet \
  --attributes "{\"RedrivePolicy\":\"{\\\"deadLetterTargetArn\\\":\\\"$DLQ_ARN\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\"}"

# Paso 6: Crear la cola de notificacion de tweets
echo "📦 Creating main queue 'notify-new-tweet' with DLQ configuration..."

aws --endpoint-url=http://localhost:4566 sqs create-queue \
   --queue-name notify-new-tweet

# Paso 7: Verificar las colas
echo "📋 Listing available SQS queues:"
aws --endpoint-url=http://localhost:4566 sqs list-queues

# Paso 8: Verificar conexión a PostgreSQL (opcional)
echo "🔍 Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
  if docker exec microblog-db pg_isready -U user -d microblog > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready!"
    break
  fi
  echo "⏱️ Waiting for PostgreSQL... ($i)"
  sleep 2
done

# Paso 9: Logs
echo "📡 Attaching to docker-compose logs (Ctrl+C para salir)..."
docker-compose logs -f
