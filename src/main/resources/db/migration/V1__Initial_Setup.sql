-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Hotels table
CREATE TABLE IF NOT EXISTS hotels (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    room_type VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_rooms_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id) ON DELETE CASCADE
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_room FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE
);

-- Billings table
CREATE TABLE IF NOT EXISTS billings (
    id SERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    amount DOUBLE PRECISION NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_billings_booking FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE CASCADE
);

-- Create or replace the function that calculates the billing amount
CREATE OR REPLACE FUNCTION calculate_billing_amount()
RETURNS TRIGGER AS $$
DECLARE
    days INTEGER;
    price DOUBLE PRECISION;
BEGIN
    -- Calculate the number of days between check-in and check-out
    days := (NEW.check_out - NEW.check_in);
    
    -- Get the room price
    SELECT rooms.price INTO price FROM rooms WHERE rooms.id = NEW.room_id;
    
    -- Insert into billings table
    INSERT INTO billings (booking_id, amount, generated_at)
    VALUES (NEW.id, price * days, NOW());
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger that executes the function after a booking is inserted
CREATE TRIGGER after_booking_insert
AFTER INSERT ON bookings
FOR EACH ROW
EXECUTE FUNCTION calculate_billing_amount();

-- Insert initial admin user (password is 'admin' encoded with BCrypt)
INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@hotel.com', '$2a$10$H7cYjcNe5SIY6V1ELgmVd.Ns4Y7QEDMYFrm7uB5H4JdP3CZ0nzuG6', 'ADMIN')
ON CONFLICT (email) DO NOTHING; 