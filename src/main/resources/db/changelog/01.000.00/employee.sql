--liquibase formatted sql
--changeset localFilePath:01.000.00/employee.sql

CREATE TABLE employee (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  age INT CHECK (age > 0),
  department_id INT,
  FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL
);