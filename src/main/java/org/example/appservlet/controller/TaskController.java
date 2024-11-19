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
import org.example.appservlet.repository.impl.TaskRepositoryImpl;
import org.example.appservlet.service.TaskService;
import org.example.appservlet.service.impl.TaskServiceImpl;
import org.example.appservlet.util.ExtractNumber;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/task/*"})
public class TaskController extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init() {
        taskService = new TaskServiceImpl(new TaskRepositoryImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.equals("/task/")) {
                response = handleGetAllTasks();
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/task/[^/]+/?$")) {
                response = handleGetTaskById(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/task/[^/]+/employees$")) {
                response = handleGetEmployee(req);
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
            if (requestURI.matches("^/task/[^/]+$")) {
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
            if (requestURI.equals("/task/")) {
                response = handlePutNewTask(req);
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
            if (requestURI.matches("^/task/[^/]+$")) {
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

    private String handleGetAllTasks() throws JsonProcessingException {
        Iterable<Task> tasks = taskService.findAll();

        return new ObjectMapper().writeValueAsString(tasks);
    }

    private String handleGetTaskById(HttpServletRequest req) throws NumberFormatException, NotFoundException, JsonProcessingException {
        Integer taskId = ExtractNumber.apply(req.getRequestURI());
        Task task = taskService.findById(taskId);

        return new ObjectMapper().writeValueAsString(task);
    }

    private String handleGetEmployee(HttpServletRequest req) throws NumberFormatException, NotFoundException, JsonProcessingException {
        Integer taskId = ExtractNumber.apply(req.getRequestURI());
        Iterable<Employee> employees = taskService.findEmployeesByTaskId(taskId);

        return new ObjectMapper().writeValueAsString(employees);
    }

    private String handlePostUpdate(HttpServletRequest req) throws NumberFormatException, NotFoundException, IOException {
        Integer taskId = ExtractNumber.apply(req.getRequestURI());

        Task task = new ObjectMapper().readValue(req.getInputStream(), Task.class);
        task.setId(taskId);

        taskService.update(task);

        return "Данные обновлены успешно.";
    }

    private String handlePutNewTask(HttpServletRequest req) throws IOException {
        Task task = new ObjectMapper().readValue(req.getInputStream(), Task.class);
        task = taskService.save(task);

        return "Данные успешно сохранены.\n" + new ObjectMapper().writeValueAsString(task);
    }

    private String handleDelete(HttpServletRequest req) throws NumberFormatException, NotFoundException {
        Integer taskId = ExtractNumber.apply(req.getRequestURI());
        taskService.deleteById(taskId);

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
