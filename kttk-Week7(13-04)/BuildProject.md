# BuildProject

## 0) File setup theo tung may

- May 1 (User Service): `user-service/MACHINE_SETUP.md`
- May 2 (Movie Service): `movie-service/MACHINE_SETUP.md`
- May 3 (Booking Service): `booking-service/MACHINE_SETUP.md`
- May 4 (Paynoti Service): `paynoti-service/MACHINE_SETUP.md`
- May 5 (Frontend + Infra Docker): `frontend/MACHINE_SETUP.md`, `infra-machine-5/MACHINE_SETUP.md`

## 0.1 Mo hinh dang su dung (CAP NHAT MOI)

- Dung mo hinh 5 may (cap nhat moi): may 1-4 chay service bang IntelliJ, may 5 chay Docker ha tang.
- Tat ca container ha tang dat tren may 5:
  - Kafka: port 9092
  - user-db: port 5432
  - booking-db: port 5433
- Cac file `.env` cua `user-service`, `booking-service`, `paynoti-service` da doi sang IP may 5: `172.16.50.24`.

## 1) Muc tieu va ket qua doi chieu rubric

### 1.1 Trang thai hien tai sau khi fix
- Dung Event-Driven Architecture: DAT
- Publish/Consume dung event: DAT
- Flow end-to-end hoat dong: DAT
- Frontend chi goi 1 service (Gateway/BFF): DAT
- Khong goi truc tiep giua cac service business: DAT
- Demo + log ro rang: DAT

### 1.2 Cac diem da fix de khop rubric
- Frontend da chuyen sang goi duy nhat Booking Service (dong vai tro Gateway/BFF) tai cong 8083
- Movie Service da bo sung API sua phim `PUT /movies/{id}`
- Payment mac dinh da chuyen ve random theo de bai:
  - `payment.simulation.mode=${PAYMENT_SIMULATION_MODE:random}`
- Da dockerize full stack trong `docker-compose.yml`:
  - `kafka`, `user-db`, `booking-db`, `user-service`, `movie-service`, `booking-service`, `paynoti-service`, `frontend`

## 2) Kien truc event-driven

### 2.1 Event
- `USER_REGISTERED`
- `BOOKING_CREATED`
- `PAYMENT_COMPLETED`
- `BOOKING_FAILED`

### 2.2 Luong chinh
- Frontend -> Booking Service Gateway (`/gateway/...`) cho user/movie
- Frontend -> Booking Service (`/bookings...`) cho booking
- Booking Service publish `BOOKING_CREATED`
- Payment Service consume `BOOKING_CREATED`, xu ly random success/fail
- Payment Service publish `PAYMENT_COMPLETED` hoac `BOOKING_FAILED`
- Booking Service consume 2 event tren de cap nhat trang thai booking
- Notification consume 2 event tren de in log

## 3) API theo tung service

### 3.1 User Service (8081)
- `POST /users/register`
- `POST /users/login`
- Register se publish `USER_REGISTERED`

### 3.2 Movie Service (8082)
- `GET /movies`
- `POST /movies`
- `PUT /movies/{id}`

### 3.3 Booking Service CORE + Gateway (8083)
- Booking API:
  - `POST /bookings`
  - `GET /bookings`
  - `GET /bookings/{id}`
  - `GET /bookings/user/{username}`
- Gateway API cho frontend:
  - `POST /gateway/users/register`
  - `POST /gateway/users/login`
  - `GET /gateway/movies`
  - `POST /gateway/movies`
  - `PUT /gateway/movies/{id}`

### 3.4 Payment + Notification Service (8084)
- Consume `BOOKING_CREATED`
- Publish `PAYMENT_COMPLETED` hoac `BOOKING_FAILED`
- Notification log ket qua booking

## 4) Frontend goi 1 service nhu the nao

Frontend chi dung 1 base URL:
- `REACT_APP_GATEWAY_BASE_URL` (mac dinh `http://localhost:8083`)

File service frontend:
- `frontend/src/services/userApi.js` -> goi `/gateway/users/...`
- `frontend/src/services/movieApi.js` -> goi `/gateway/movies...`
- `frontend/src/services/bookingApi.js` -> goi `/bookings...`

=> Frontend khong goi truc tiep user-service/movie-service nua.

## 5) Trien khai LAN 5 may (muc tieu)

## 5.1 So do 5 may
- May 1: User Service
- May 2: Movie Service
- May 3: Booking Service (Gateway + Core)
- May 4: Payment + Notification Service
- May 5: Frontend
- Broker Kafka va DB co the dat may rieng hoac dat chung voi may backend tuy bai demo

## 5.2 IP LAN thuc te cua ban
- User Service (May 1): `172.16.49.152:8081`
- Movie Service (May 2): `172.16.51.94:8082`
- Booking Service (May 3): `172.16.54.180:8083`
- Payment/Notification + Kafka (May 4): `172.16.54.178:8084`, Kafka `172.16.54.178:9092`
- Frontend (May 5): `172.16.50.24:8085`
- User DB (cung May 1): `172.16.49.152:5432`
- Booking DB (cung May 3): `172.16.54.180:5432`

## 5.3 Rule doi localhost sang LAN
- Moi `localhost` trong config phai doi thanh IP may dich
- Frontend (May 5) chi tro den Booking Service Gateway:
  - `REACT_APP_GATEWAY_BASE_URL=http://172.16.54.180:8083`
- User Service:
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.54.178:9092`
  - `DB_URL=jdbc:postgresql://172.16.49.152:5432/userdb`
- Booking Service:
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.54.178:9092`
  - `BOOKING_DB_URL=jdbc:postgresql://172.16.54.180:5432/bookingdb`
  - `USER_SERVICE_BASE_URL=http://172.16.49.152:8081`
  - `MOVIE_SERVICE_BASE_URL=http://172.16.51.94:8082`
- Paynoti Service:
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.54.178:9092`

## 6) Docker chay o may nao

### 6.1 Neu demo local 1 may
- Chay tat ca container trong `docker-compose.yml`
- Frontend cung la 1 container rieng, nhung frontend KHONG chua DB/Kafka
- DB/Kafka la container ha tang dung chung

### 6.2 Neu trien khai 5 may that
- Ban hien tai CHI CO 5 MAY, KHONG CO may thu 6 cho ha tang.
- Phuong an khuyen nghi de dung du 5 may:
  - May 1 (User): chay `user-service` + `user-db`
  - May 2 (Movie): chay `movie-service`
  - May 3 (Booking): chay `booking-service` + `booking-db`
  - May 4 (Payment/Notification): chay `paynoti-service` + `kafka`
  - May 5 (Frontend): chay `frontend`

### 6.3 Tra loi truc tiep cau hoi "container chay o dau"
- `user-db` chay tren May 1 (cung may voi User Service)
- `booking-db` chay tren May 3 (cung may voi Booking Service)
- `kafka` chay tren May 4 (cung may voi Paynoti Service)
- `frontend` tren May 5 KHONG can chay 3 container tren

### 6.4 Env can dat theo phuong an 5 may
- User Service (May 1):
  - `DB_URL=jdbc:postgresql://localhost:5432/userdb`
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.54.178:9092` (IP May 4)
- Movie Service (May 2):
  - khong can DB/Kafka
- Booking Service (May 3):
  - `BOOKING_DB_URL=jdbc:postgresql://localhost:5432/bookingdb`
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.54.178:9092` (IP May 4)
  - `USER_SERVICE_BASE_URL=http://172.16.49.152:8081` (IP May 1)
  - `MOVIE_SERVICE_BASE_URL=http://172.16.51.94:8082` (IP May 2)
- Paynoti Service (May 4):
  - `KAFKA_BOOTSTRAP_SERVERS=localhost:9092`
- Frontend (May 5):
  - `REACT_APP_GATEWAY_BASE_URL=http://172.16.54.180:8083` (IP May 3)

## 7) docker-compose hien tai (full stack)

`docker-compose.yml` da co cac service:
- `kafka`
- `user-db`
- `booking-db`
- `user-service`
- `movie-service`
- `booking-service`
- `paynoti-service`
- `frontend`

Lenh chay:
```powershell
docker compose up -d --build
```

## 8) Kich ban test bat buoc demo

1. Dang ky user -> co `USER_REGISTERED`
2. Chon phim va dat ve -> co `BOOKING_CREATED`
3. Payment xu ly random -> publish `PAYMENT_COMPLETED` hoac `BOOKING_FAILED`
4. Notification log ket qua
5. Frontend trang Result hien status dung
6. Booking history lay du lieu tu DB

## 9) Lenh check nhanh

- Check user DB:
```powershell
docker compose exec -T user-db psql -U postgres -d userdb -c "SELECT id, username FROM users ORDER BY id DESC;"
```

- Check booking DB:
```powershell
docker compose exec -T booking-db psql -U postgres -d bookingdb -c "SELECT id, username, movie, seats, payment_method, status FROM bookings ORDER BY id DESC;"
```

- Check kafka topics:
```powershell
docker compose exec -T kafka rpk topic list --brokers=kafka:9092
```

## 10) Tra loi truc tiep cau hoi cua ban

- Frontend co chay cung container user-db, booking-db, kafka khong?
  - Khong.
  - Frontend la service rieng.
  - DB/Kafka la ha tang backend, frontend chi goi HTTP API.

- Chay LAN 5 may co can sua code khong?
  - Khong can sua logic.
  - Chi can doi bien moi truong tu localhost sang IPv4 cua tung may.

## 11) Command copy-paste theo tung may (PowerShell)

Luu y:
- Thay IP mau ben duoi bang IP that trong LAN cua ban.
- Chay dung thu tu de tranh loi ket noi.

Thu tu khoi dong de nghi:
1. May 4 (Kafka)
2. May 1 (user-db + user-service)
3. May 2 (movie-service)
4. May 3 (booking-db + booking-service)
5. May 4 (paynoti-service)
6. May 5 (frontend)

### May 4 - Kafka (IP that: 172.16.54.178)

```powershell
docker run -d --name movie-kafka --restart unless-stopped -p 9092:9092 docker.redpanda.com/redpandadata/redpanda:v24.2.9 redpanda start --overprovisioned --smp 1 --memory 512M --reserve-memory 0M --node-id 0 --check=false --kafka-addr PLAINTEXT://0.0.0.0:9092 --advertise-kafka-addr PLAINTEXT://172.16.54.178:9092
```

Kiem tra:
```powershell
Test-NetConnection -ComputerName 172.16.54.178 -Port 9092
```

### May 1 - User Service + user-db (IP that: 172.16.49.152)

Chay DB:
```powershell
docker run -d --name user-db --restart unless-stopped -e POSTGRES_DB=userdb -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
```

Chay service:
```powershell
Set-Location <duong-dan-den-user-service>
$env:SERVER_PORT="8081"
$env:DB_URL="jdbc:postgresql://localhost:5432/userdb"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:KAFKA_BOOTSTRAP_SERVERS="172.16.54.178:9092"
.\mvnw.cmd spring-boot:run
```

### May 2 - Movie Service (IP that: 172.16.51.94)

```powershell
Set-Location <duong-dan-den-movie-service>
$env:SERVER_PORT="8082"
.\mvnw.cmd spring-boot:run
```

### May 3 - Booking Service + booking-db (IP that: 172.16.54.180)

Chay DB:
```powershell
docker run -d --name booking-db --restart unless-stopped -e POSTGRES_DB=bookingdb -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
```

Chay service:
```powershell
Set-Location <duong-dan-den-booking-service>
$env:SERVER_PORT="8083"
$env:BOOKING_DB_URL="jdbc:postgresql://localhost:5432/bookingdb"
$env:BOOKING_DB_USERNAME="postgres"
$env:BOOKING_DB_PASSWORD="postgres"
$env:KAFKA_BOOTSTRAP_SERVERS="172.16.54.178:9092"
$env:USER_SERVICE_BASE_URL="http://172.16.49.152:8081"
$env:MOVIE_SERVICE_BASE_URL="http://172.16.51.94:8082"
.\mvnw.cmd spring-boot:run
```

### May 4 - Paynoti Service (cung may Kafka)

```powershell
Set-Location <duong-dan-den-paynoti-service>
$env:SERVER_PORT="8084"
$env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
$env:PAYMENT_SIMULATION_MODE="random"
.\mvnw.cmd spring-boot:run
```

### May 5 - Frontend (IP that: 172.16.50.24)

```powershell
Set-Location <duong-dan-den-frontend>
Set-Content -Path .env -Value "REACT_APP_GATEWAY_BASE_URL=http://172.16.54.180:8083`nPORT=8085"
npm install
npm start
```

Truy cap:
```text
http://172.16.50.24:8085
```

### Kiem tra nhanh sau khi khoi dong

Tu may frontend (hoac may bat ky trong LAN):
```powershell
Test-NetConnection 172.16.49.152 -Port 8081
Test-NetConnection 172.16.51.94 -Port 8082
Test-NetConnection 172.16.54.180 -Port 8083
Test-NetConnection 172.16.54.178 -Port 8084
Test-NetConnection 172.16.54.178 -Port 9092
```

Check DB user (tren May 1):
```powershell
docker exec -it user-db psql -U postgres -d userdb -c "SELECT id, username FROM users ORDER BY id DESC;"
```

Check DB booking (tren May 3):
```powershell
docker exec -it booking-db psql -U postgres -d bookingdb -c "SELECT id, username, movie, seats, payment_method, status FROM bookings ORDER BY id DESC;"
```

## 12) PHIEN BAN CHUAN hien tai (INFRA tren MAY 5)

Phan nay thay the cho cac huong dan 5 may o tren.

### 12.1 Mapping 5 may
- May 1: user-service (khong Docker)
- May 2: movie-service (khong Docker)
- May 3: booking-service (khong Docker)
- May 4: paynoti-service (khong Docker)
- May 5: frontend + Docker infra (kafka + user-db + booking-db)

### 12.2 Port tren may 5
- Kafka: `172.16.50.24:9092`
- user-db: `172.16.50.24:5432`
- booking-db: `172.16.50.24:5433`

### 12.3 Env can dat
- user-service (.env):
  - `DB_URL=jdbc:postgresql://172.16.50.24:5432/userdb`
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092`
- booking-service (.env):
  - `BOOKING_DB_URL=jdbc:postgresql://172.16.50.24:5433/bookingdb`
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092`
  - `USER_SERVICE_BASE_URL=http://172.16.49.152:8081`
  - `MOVIE_SERVICE_BASE_URL=http://172.16.51.94:8082`
- paynoti-service (.env):
  - `KAFKA_BOOTSTRAP_SERVERS=172.16.50.24:9092`
- frontend (.env):
  - `REACT_APP_GATEWAY_BASE_URL=http://172.16.54.180:8083`

### 12.4 Thu tu khoi dong
1. May 5: `infra-machine-5/MACHINE_SETUP.md` (khoi dong Docker ha tang truoc)
2. May 1: `user-service/MACHINE_SETUP.md`
3. May 2: `movie-service/MACHINE_SETUP.md`
4. May 3: `booking-service/MACHINE_SETUP.md`
5. May 4: `paynoti-service/MACHINE_SETUP.md`
6. May 5: `frontend/MACHINE_SETUP.md`
