# NttDataChallenge

# Comandos Docker
docker compose up --build
docker compose ps
docker compose logs -f accounts-service
docker compose down -v

# ver eventos
docker exec -it bank-kafka kafka-topics \

docker exec -it bank-kafka kafka-topics --bootstrap-server localhost:9092 --list

# Monitorear Consola Consumer 
 docker exec -it bank-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic customers.events \
  --from-beginning

# Monitorear Consola Account
docker exec -it bank-kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic accounts.events --from-beginning

# Mostrar Bdd
docker exec -it bank-mysql mysql -uroot -proot -e "SHOW DATABASES LIKE 'bank_customers';"

# Ver Tablas Customers
docker exec -it bank-mysql mysql -uroot -proot -e "USE bank_customers; SHOW TABLES;"

docker exec -it bank-mysql mysql -uroot -proot -e "USE bank_customers; SELECT id, identification, name FROM customers ORDER BY id;"

# Ver tablas Accounts
docker exec -it bank-mysql mysql -uroot -proot -e "USE bank_accounts; SELECT id, customer_id, account_number, type, balance, status FROM accounts ORDER BY id;"

# Generar Archivo bdd.sql
docker exec -i  bank-mysql mysqldump -uroot -proot --databases bank_accounts bank_customers > BaseDatos.sql
