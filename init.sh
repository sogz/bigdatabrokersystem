#!/usr/bin/env bash

until printf "" 2>>/dev/null >>/dev/tcp/cassandra1/9042; do 
    sleep 25;
    echo "Waiting for cassandra...";
done

echo "Creating keyspace and table..."
cqlsh cassandra1 -e "CREATE KEYSPACE brokertest WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 2};"