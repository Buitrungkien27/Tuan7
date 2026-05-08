# Booking Service - Machine 3 Setup

Machine role:
- Machine 3 IP: 172.16.54.180
- Runs: booking-service only (khong chay Docker)
- Uses User Service on Machine 1 and Movie Service on Machine 2
- Infra machine: 172.16.50.24 (Kafka + DB)

## 1) Update .env
```powershell
Set-Location <path-to-kttk\booking-service>
Get-Content .env
```

Dam bao .env co gia tri:
- BOOKING_DB_URL=jdbc:postgresql://172.16.50.24:5433/bookingdb
- KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092
- USER_SERVICE_BASE_URL=http://172.16.49.152:8081
- MOVIE_SERVICE_BASE_URL=http://172.16.51.94:8082

## 2) Start booking-service (using .env)
```powershell
Set-Location <path-to-kttk\booking-service>
Get-Content .env
.\start-with-env.ps1
```

## 3) Quick health checks
```powershell
Test-NetConnection 172.16.50.24 -Port 5433
Test-NetConnection 172.16.50.24 -Port 9092
Test-NetConnection 172.16.49.152 -Port 8081
Test-NetConnection 172.16.51.94 -Port 8082
Test-NetConnection 172.16.54.180 -Port 8083
```

## 4) Public endpoint for frontend
- Frontend points to: http://172.16.54.180:8083
