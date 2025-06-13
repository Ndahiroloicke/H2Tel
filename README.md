
PS C:\Users\admin> ipconfig

Windows IP Configuration


Ethernet adapter Ethernet:

   Media State . . . . . . . . . . . : Media disconnected
   Connection-specific DNS Suffix  . :

Wireless LAN adapter Local Area Connection* 1:

   Media State . . . . . . . . . . . : Media disconnected
   Connection-specific DNS Suffix  . :

Wireless LAN adapter Local Area Connection* 2:

   Connection-specific DNS Suffix  . :
   Link-local IPv6 Address . . . . . : fe80::4dd0:bf8b:819d:f5aa%18
   IPv4 Address. . . . . . . . . . . : 192.168.137.1
   Subnet Mask . . . . . . . . . . . : 255.255.255.0
   Default Gateway . . . . . . . . . :

Wireless LAN adapter Wi-Fi:

   Connection-specific DNS Suffix  . :
   Link-local IPv6 Address . . . . . : fe80::f916:e27d:8054:a45e%5
   IPv4 Address. . . . . . . . . . . : 192.168.0.3
   Subnet Mask . . . . . . . . . . . : 255.255.255.0
   Default Gateway . . . . . . . . . : fe80::a29f:7aff:fe06:377b%5
                                       192.168.0.1

Ethernet adapter Bluetooth Network Connection:

   Media State . . . . . . . . . . . : Media disconnected
   Connection-specific DNS Suffix  . :
PS C:\Users\admin>
# Hotel Booking Management System API

This is the backend API for a Hotel Booking Management System. It provides endpoints for user authentication, hotel and room management, as well as booking and billing operations.

## Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL database
- Maven

### Database Setup
1. Install PostgreSQL if you haven't already
2. Create a database named `hotel_booking`
3. Update the database connection details in `src/main/resources/application.properties` if needed:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_booking
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run `./mvnw spring-boot:run` (Unix/Mac) or `mvnw.cmd spring-boot:run` (Windows)
4. The application will start on `http://localhost:8080`

## API Endpoints

### User Management

#### Register a new user
```
POST /api/users/register
```
Request body:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```
Notes:
- `role` can be either "CUSTOMER" or "ADMIN"
- Email must be unique

#### Login
```
POST /api/users/login
```
Request body:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

### Hotel Management

#### Get all hotels
```
GET /api/hotels
```

#### Get hotel by ID
```
GET /api/hotels/{id}
```

#### Create a new hotel
```
POST /api/hotels
```
Request body:
```json
{
  "name": "Grand Hotel",
  "location": "New York"
}
```

### Room Management

#### Get all rooms for a hotel
```
GET /api/hotels/{hotelId}/rooms
```

#### Create a new room
```
POST /api/rooms
```
Request body:
```json
{
  "hotelId": 1,
  "roomType": "Deluxe",
  "price": 150.0,
  "isAvailable": true
}
```
IMPORTANT: Use camelCase for property names (`hotelId`, `roomType`, `isAvailable`) not snake_case.

#### Check room availability
```
GET /api/hotels/{hotelId}/available-rooms?checkIn=2023-06-01&checkOut=2023-06-05
```

### Booking Management

#### Create a booking
```
POST /api/bookings?userId={userId}
```
Request body:
```json
{
  "roomId": 1,
  "checkIn": "2023-06-01",
  "checkOut": "2023-06-05"
}
```

#### Get user's bookings
```
GET /api/bookings/my/{userId}
```

#### Cancel a booking
```
DELETE /api/bookings/{id}?userId={userId}
```

### Billing

#### Get billing for a booking
```
GET /api/billings/{bookingId}?userId={userId}
```

## Common HTTP Status Codes

- 200 OK: Request successful
- 201 Created: Resource created successfully
- 400 Bad Request: Invalid input or validation error
- 401 Unauthorized: Authentication error
- 403 Forbidden: Permission denied
- 404 Not Found: Resource not found
- 500 Internal Server Error: Server-side error

## Example Requests Using Postman

### Register a User
```
POST http://localhost:8080/api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

### Create a Hotel
```
POST http://localhost:8080/api/hotels
Content-Type: application/json

{
  "name": "Serena Hotel",
  "location": "Kigali"
}
```

### Add a Room
```
POST http://localhost:8080/api/rooms
Content-Type: application/json

{
  "hotelId": 1,
  "roomType": "VIP",
  "price": 200.0,
  "isAvailable": true
}
```

### Make a Booking
```
POST http://localhost:8080/api/bookings?userId=1
Content-Type: application/json

{
  "roomId": 1,
  "checkIn": "2023-06-01",
  "checkOut": "2023-06-05"
}
```

## Troubleshooting

- **Database Connection Issues**: Ensure PostgreSQL is running and the credentials in application.properties are correct
- **400 Bad Request**: Check your JSON request format, especially property names (camelCase vs snake_case)
- **404 Not Found**: Verify the API endpoint URL and parameters

## Data Model

- **User**: id, name, email, password, role
- **Hotel**: id, name, location
- **Room**: id, hotelId, roomType, price, isAvailable
- **Booking**: id, userId, roomId, checkIn, checkOut, status
- **Billing**: id, bookingId, amount, generatedAt 
