# User Service - Machine 1 Setup

Machine role:
- Machine 1 IP: 172.16.49.152
- Runs: user-service only (khong chay Docker)
- Infra machine: 172.16.50.24 (Kafka + DB)

## 1) Update .env
```powershell
Set-Location <path-to-kttk\user-service>
Get-Content .env
```

Dam bao .env co gia tri:
- DB_URL=jdbc:postgresql://172.16.50.24:5432/userdb
- KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092

## 2) Start user-service (using .env)
```powershell
Set-Location <path-to-kttk\user-service>
Get-Content .env
.\start-with-env.ps1
```

## 3) Quick health checks
```powershell
Test-NetConnection 172.16.50.24 -Port 5432
Test-NetConnection 172.16.50.24 -Port 9092
Test-NetConnection 172.16.49.152 -Port 8081
```

## 4) What other machines use from this machine
- Booking Service calls: http://172.16.49.152:8081
