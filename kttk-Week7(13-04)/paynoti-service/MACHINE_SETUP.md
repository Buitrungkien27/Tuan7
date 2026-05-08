# Paynoti Service - Machine 4 Setup

Machine role:
- Machine 4 IP: 172.16.54.178
- Runs: paynoti-service only (khong chay Docker)
- Infra machine: 172.16.50.24 (Kafka + DB)

## 1) Update .env
```powershell
Set-Location <path-to-kttk\paynoti-service>
Get-Content .env
```

Dam bao .env co gia tri:
- KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092

## 2) Start paynoti-service (using .env)
```powershell
Set-Location <path-to-kttk\paynoti-service>
Get-Content .env
.\start-with-env.ps1
```

## 3) Quick health checks
```powershell
Test-NetConnection 172.16.50.24 -Port 9092
Test-NetConnection 172.16.54.178 -Port 8084
```
