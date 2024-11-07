-- Создаем схему базы данных

-- Удаляем ранее созданные таблицы
DROP TABLE IF EXISTS department CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS assignments CASCADE;


-- Создание таблицы department
CREATE TABLE department (
    id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL
);

-- Создание таблицы employee
CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    age INT CHECK (age > 0),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL
);

-- Создание таблицы task
CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    task_name VARCHAR(100) NOT NULL,
    deadline DATE NOT NULL
);

-- Создание таблицы для связки many-to-many (assignments)
CREATE TABLE assignments (
    employee_id INT NOT NULL,
    task_id INT NOT NULL,
    PRIMARY KEY (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Создание индекса на столбцах employee_id и task_id для оптимизации запросов на присоединение
CREATE INDEX idx_assignment_employee_task ON assignments(employee_id, task_id);
