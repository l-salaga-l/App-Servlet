--liquibase formatted sql
--changeset localFilePath:01.000.00/department.sql

CREATE TABLE department (
    id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL
);