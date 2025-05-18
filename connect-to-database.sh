#!/bin/bash

echo "🔌 Connecting to PostgreSQL database 'microblog'..."

docker exec -it microblog-db psql -U user -d microblog
