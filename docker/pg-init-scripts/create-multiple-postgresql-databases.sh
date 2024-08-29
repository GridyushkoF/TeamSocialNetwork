#!/bin/bash
set -e
set -u

function create_user_and_database() {
    local database=$1
    echo "Creating user and database '$database'"

    # Check if the user already exists, and drop it if it does
    if psql -t -c "SELECT 1 FROM pg_roles WHERE rolname='$database'" | grep -q 1; then
        echo "User '$database' already exists. Dropping user."
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -c "DROP USER $database;"
    fi

    # Check if the database already exists, and drop it if it does
    if psql -t -c "SELECT 1 FROM pg_database WHERE datname='$database'" | grep -q 1; then
        echo "Database '$database' already exists. Dropping database."
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -c "DROP DATABASE $database;"
    fi

    # Create the user and database
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER $database;
        CREATE DATABASE $database;
        GRANT ALL PRIVILEGES ON DATABASE $database TO $database;
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
    echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
    for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
        create_user_and_database $db
    done
    echo "Multiple databases created"
fi