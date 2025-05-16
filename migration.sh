#!/bin/bash

echo "Starting Blue Bird API setup..."

# Stop all containers and remove volumes
echo "Stopping containers and removing volumes..."
docker-compose down -v

# Build and start the containers
echo "Building and starting containers..."
docker-compose up --build

# The script will keep running as docker-compose up keeps the containers running 