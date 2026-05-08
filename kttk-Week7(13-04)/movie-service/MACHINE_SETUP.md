# Movie Service - Machine 2 Setup

Machine role:
- Machine 2 IP: 172.16.51.94
- Runs: movie-service only

## 0) Note
- Machine 2 khong can DB/Kafka local.
- DB/Kafka duoc dat tren may 5 (infra machine, IP 172.16.50.24).

## 1) Start movie-service (using .env)
```powershell
Set-Location <path-to-kttk\movie-service>
Get-Content .env
.\start-with-env.ps1
```

## 2) Quick health checks
```powershell
Test-NetConnection 172.16.51.94 -Port 8082
```

## 3) API smoke test
```powershell
Invoke-RestMethod -Method GET -Uri "http://172.16.51.94:8082/movies"
```

## 4) What other machines use from this machine
- Booking Service calls: http://172.16.51.94:8082
