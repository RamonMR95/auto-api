activemq/apache-activemq-5.14.5/bin/activemq start &
java -DDB_SERVER_NAME=$DB_HOST_NAME -DDB_PORT=$DB_PORT -DDB_NAME=$DB_NAME -DDB_USER=$DB_USER -DDB_PASSWORD=$DB_PASSWORD -jar auto-api.jar