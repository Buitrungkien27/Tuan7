# Infra (Kafka + DB) - Machine 5 Setup

Machine role:
- Machine 5 IP: 172.16.50.24
- Runs Docker only: kafka, user-db, booking-db
- Machines 1-4 run service by IntelliJ (spring-boot:run)

## 1) Start infra containers
```powershell
Set-Location <path-to-kttk\infra-machine-5>
docker compose -f docker-compose.infra.yml up -d
```

## 2) Verify ports on machine 5
```powershell
Test-NetConnection localhost -Port 9092
Test-NetConnection localhost -Port 5432
Test-NetConnection localhost -Port 5433
```

## 3) Verify from other machines in LAN
```powershell
Test-NetConnection 172.16.50.24 -Port 9092
Test-NetConnection 172.16.50.24 -Port 5432
Test-NetConnection 172.16.50.24 -Port 5433
```

## 4) DB quick checks
```powershell
docker exec -it user-db psql -U postgres -d userdb -c "SELECT 1;"
docker exec -it booking-db psql -U postgres -d bookingdb -c "SELECT 1;"
```

## 5) Kafka topic quick check
```powershell
docker exec -it movie-kafka rpk topic list --brokers=localhost:9092
```
