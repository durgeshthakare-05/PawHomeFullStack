-- Reference schema. The backend creates these tables automatically (spring.jpa.hibernate.ddl-auto=update).
CREATE DATABASE IF NOT EXISTS pawhome;
USE pawhome;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  phone VARCHAR(30),
  city VARCHAR(100),
  created_at DATETIME
);

CREATE TABLE pets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(50) NOT NULL,
  breed VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  gender VARCHAR(20),
  description VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  created_at DATETIME
);

CREATE TABLE requests (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  pet_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  kind VARCHAR(10) NOT NULL,
  note VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  created_at DATETIME,
  FOREIGN KEY (pet_id) REFERENCES pets(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
