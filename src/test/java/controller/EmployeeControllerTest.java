package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.appservlet.controller.EmployeeController;
import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    @Mock
    private EmployeeServiceImpl employeeService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployeesSuccess() throws Exception {
        Iterable<Employee> employees = Arrays.asList(new Employee(1, "Анна", "Смирнова", "anna.smirnova@example.ru", 30, null, null),
                                                    new Employee(2, "Алексей", "Петров", "aleksey.petrov@example.com", 27, null, null));

        when(request.getRequestURI()).thenReturn("/employee/");
        when(employeeService.findAll()).thenReturn(employees);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(new ObjectMapper().writeValueAsString(employees));
    }

    @Test
    public void testGetEmployeeByIdSuccess() throws Exception {
        Employee employee = new Employee(1, "Анна", "Смирнова", "anna.smirnova@example.ru", 30, null, null);

        when(request.getRequestURI()).thenReturn("/employee/1");
        when(employeeService.findById(1)).thenReturn(employee);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetEmployeeByIdNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/1");
        when(employeeService.findById(1)).thenThrow(new NotFoundException());
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testGetEmployeeByIdBadRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/abc");
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).write("Неправильный формат идентификатора!");
    }

    @Test public void testGetEmployeeTasksSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/1/tasks");
        when(employeeService.findTasksByEmployeeId(1)).thenReturn(Arrays.asList(new Task(), new Task()));
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetEmployeeTasksNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/1/tasks");
        when(employeeService.findTasksByEmployeeId(1)).thenThrow(new NotFoundException());
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testInvalidUrl() throws Exception {
        when(request.getRequestURI()).thenReturn("/unknown/url");
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Не найдена страница!");
    }

    @Test
    public void testPostEmployeeSuccess() throws Exception {
        Employee employeeToUpdate = new Employee(0, "Мария", null, "maria.sokolova@example.ru", 0, null, null);
        Employee existingEmployee = new Employee(1, "Мария", "Соколова", "sokolova@gmail.ru", 30, null, null);

        when(request.getRequestURI()).thenReturn("/employee/1");
        when(response.getWriter()).thenReturn(printWriter);

        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(employeeToUpdate));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);
        when(employeeService.findById(1)).thenReturn(existingEmployee);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write("Данные обновлены успешно.");
    }

    @Test
    public void testPostEmployeeNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/1");
        when(response.getWriter()).thenReturn(printWriter);

        Employee employee = new Employee(1, "Анна", "Смирнова", "anna.smirnova@example.ru", 30, null, null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(employee));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        Mockito.doThrow(new NotFoundException())
                .when(employeeService)
                .update(any(Employee.class));

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testPutEmployeeSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/employee/");
        when(response.getWriter()).thenReturn(printWriter);

        Employee new_employee = new Employee(1, "Мария", "Соколова", "sokolova@gmail.ru", 30, null, null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(new_employee));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);
        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testDeleteEmployeeSuccess() throws Exception {
        Integer employeeId = 1;
        when(request.getRequestURI()).thenReturn("/employee/" + employeeId);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(employeeService, times(1)).deleteById(employeeId);
    }
}
