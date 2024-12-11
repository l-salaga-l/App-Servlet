--liquibase formatted sql
--changeset localFilePath:01.000.00/assigments.sql

CREATE TABLE assignments (
 employee_id INT NOT NULL,
 task_id INT NOT NULL,
 PRIMARY KEY (employee_id, task_id),
 FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
 FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

CREATE INDEX idx_assignment_employee_task ON assignments(employee_id, task_id);
