--liquibase formatted sql
--changeset localFilePath:01.000.00/task.sql

CREATE TABLE task (
  id SERIAL PRIMARY KEY,
  task_name VARCHAR(100) NOT NULL,
  deadline DATE NOT NULL
);