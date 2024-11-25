package org.example.appservlet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.appservlet.repository.impl.DepartmentRepositoryImpl;
import org.example.appservlet.service.DepartmentService;
import org.example.appservlet.service.dto.DepartmentDTO;
import org.example.appservlet.service.dto.EmployeeDTO;
import org.example.appservlet.service.impl.DepartmentServiceImpl;
import org.example.appservlet.util.ExtractNumber;
import org.hibernate.ObjectNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/department/*"})
public class DepartmentController extends HttpServlet {
    private DepartmentService departmentService;

    @Override
    public void init() {
        departmentService = new DepartmentServiceImpl(new DepartmentRepositoryImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.equals("/department/")) {
                response = handleGetAllDepartments();
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/department/[^/]+$")) {
                response = handleGetDepartmentById(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches("^/department/[^/]+/employees$")) {
                response = handleGetEmployee(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ObjectNotFoundException e) {
            response = "Нет записи с данным ID!";
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (JsonProcessingException e) {
            response = "Ошибка в обработке запроса!\n" + e.getMessage();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        writeResponse(resp, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.matches("^/department/[^/]+$")) {
                response = handlePostUpdate(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ObjectNotFoundException e) {
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
            if (requestURI.equals("/department/")) {
                response = handlePutNewDepartment(req);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            response = e.getMessage();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        writeResponse(resp, response);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestURI = req.getRequestURI();
        String response;

        try {
            if (requestURI.matches("^/department/[^/]+$")) {
                response = handleDelete(req);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response = "Не найдена страница!";
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response = "Неправильный формат идентификатора!";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ObjectNotFoundException e) {
            response = "Нет записи с данным ID!";
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        writeResponse(resp, response);
    }

    private String handleGetAllDepartments() throws JsonProcessingException {
        Iterable<DepartmentDTO> departments = departmentService.findAll();

        return new ObjectMapper().writeValueAsString(departments);
    }

    private String handleGetDepartmentById(HttpServletRequest req) throws JsonProcessingException, ObjectNotFoundException, NumberFormatException {
        Integer departmentId = ExtractNumber.apply(req.getRequestURI());
        DepartmentDTO department = departmentService.findById(departmentId);

        return new ObjectMapper().writeValueAsString(department);
    }

    private String handleGetEmployee(HttpServletRequest req) throws JsonProcessingException, ObjectNotFoundException {
        Integer departmentId = ExtractNumber.apply(req.getRequestURI());
        Iterable<EmployeeDTO> employees = departmentService.findEmployeeByDepartmentId(departmentId);

        return new ObjectMapper().writeValueAsString(employees);
    }

    private String handlePostUpdate(HttpServletRequest req) throws IOException, ObjectNotFoundException {
        Integer departmentId = ExtractNumber.apply(req.getRequestURI());

        DepartmentDTO department = new ObjectMapper().readValue(req.getInputStream(), DepartmentDTO.class);
        department.setId(departmentId);

        departmentService.update(department);

        return "Данные обновлены успешно.";
    }

    private String handlePutNewDepartment(HttpServletRequest req) throws IOException {
        DepartmentDTO department = new ObjectMapper().readValue(req.getInputStream(), DepartmentDTO.class);
        department = departmentService.save(department);

        return "Данные успешно сохранены.\n" + new ObjectMapper().writeValueAsString(department);
    }

    private String handleDelete(HttpServletRequest req) throws NumberFormatException, ObjectNotFoundException {
        Integer departmentId = ExtractNumber.apply(req.getRequestURI());
        departmentService.deleteById(departmentId);

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
