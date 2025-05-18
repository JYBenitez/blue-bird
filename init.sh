#!/bin/bash

echo "🚀 Starting Blue Bird API setup..."

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

# Paso 4: Crear la cola SQS "create-tweet"
echo "📦 Creating SQS queue 'create-tweet'..."
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name create-tweet

# Paso 5: Verificar las colas
echo "📋 Listing available SQS queues:"
aws --endpoint-url=http://localhost:4566 sqs list-queues

# Paso 6: Verificar conexión a PostgreSQL (opcional)
echo "🔍 Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
  if docker exec microblog-db pg_isready -U user -d microblog > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready!"
    break
  fi
  echo "⏱️ Waiting for PostgreSQL... ($i)"
  sleep 2
done

# Paso 7: Logs
echo "📡 Attaching to docker-compose logs (Ctrl+C para salir)..."
docker-compose logs -f
