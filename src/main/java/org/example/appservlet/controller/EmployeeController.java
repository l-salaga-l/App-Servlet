package org.example.appservlet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.repository.impl.EmployeeRepositoryImpl;
import org.example.appservlet.service.EmployeeService;
import org.example.appservlet.service.impl.EmployeeServiceImpl;
import org.example.appservlet.util.ExtractNumber;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/employee/*"})
public class EmployeeController extends HttpServlet {
    private EmployeeService employeeService;

    @Override
    public void init() {
        employeeService = new EmployeeServiceImpl(new EmployeeRepositoryImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.equals("/employee/")) {
                response = handleGetAllEmployees();
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/employee/[^/]+?$")) {
                response = handleGetEmployeeById(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/employee/[^/]+/tasks$")) {
                response = handleGetTasks(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response = "Нет записи с данным ID!";
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (JsonProcessingException e) {
            response = "Ошибка в обработке запроса!";
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        writeResponse(resp, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.matches("^/employee/[^/]+$")) {
                response = handlePostUpdate(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response = "Нет записи с данным ID!";
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            response = "Ошибка в доступе!";
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        writeResponse(resp, response);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.equals("/employee/")) {
                response = handlePutNewEmployee(req);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            response = "Ошибка в доступе!";
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        writeResponse(resp, response);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.matches("^/employee/[^/]+$")) {
                response = handleDelete(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response = "Нет записи с данным ID!";
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        writeResponse(resp, response);
    }

    private String handleGetAllEmployees() throws JsonProcessingException {
        Iterable<Employee> employees = employeeService.findAll();

        return new ObjectMapper().writeValueAsString(employees);
    }

    private String handleGetEmployeeById(HttpServletRequest req) throws NotFoundException, JsonProcessingException {
        Integer employeeId = ExtractNumber.apply(req.getRequestURI());
        Employee employee = employeeService.findById(employeeId);

        return new ObjectMapper().writeValueAsString(employee);
    }

    private String handleGetTasks(HttpServletRequest req) throws NotFoundException, JsonProcessingException {
        Integer employeeId = ExtractNumber.apply(req.getRequestURI());
        Iterable<Task> tasks = employeeService.findTasksByEmployeeId(employeeId);

        return new ObjectMapper().writeValueAsString(tasks);
    }

    private String handlePostUpdate(HttpServletRequest req) throws NotFoundException, IOException {
        Integer employeeId = ExtractNumber.apply(req.getRequestURI());

        Employee employee_new = new ObjectMapper().readValue(req.getInputStream(), Employee.class);
        employee_new.setId(employeeId);

        employeeService.update(employee_new);

        return "Данные обновлены успешно.";
    }

    private String handlePutNewEmployee(HttpServletRequest req) throws IOException {
        Employee employee = new ObjectMapper().readValue(req.getInputStream(), Employee.class);
        employee = employeeService.save(employee);

        return "Данные успешно сохранены.\n" + new ObjectMapper().writeValueAsString(employee);
    }

    private String handleDelete(HttpServletRequest req) throws NumberFormatException, NotFoundException {
        Integer employeeId = ExtractNumber.apply(req.getRequestURI());
        employeeService.deleteById(employeeId);

        return "Запись удалена успешно.";
    }

    private void writeResponse(HttpServletResponse resp, String response) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("Windows-1251");

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.flush();
    }
}
