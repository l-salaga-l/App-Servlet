package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.appservlet.controller.TaskController;
import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Employee;
import org.example.appservlet.model.Task;
import org.example.appservlet.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskServiceImpl taskService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasksSuccess() throws Exception {
        Iterable<Task> tasks = Arrays.asList(new Task(1, "Обновление системы", "2024-10-30", null),
                new Task(2, "Анализ рынка", "2024-11-15", null));

        when(request.getRequestURI()).thenReturn("/task/");
        when(taskService.findAll()).thenReturn(tasks);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(new ObjectMapper().writeValueAsString(tasks));
    }

    @Test
    public void testGetTaskByIdSuccess() throws Exception {
        Task task = new Task(1, "Обновление системы", "2024-10-30", null);

        when(request.getRequestURI()).thenReturn("/task/1");
        when(taskService.findById(1)).thenReturn(task);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/1");
        when(taskService.findById(1)).thenThrow(new NotFoundException());
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testGetTaskByIdBadRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/abc");
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).write("Неправильный формат идентификатора!");
    }

    @Test public void testGetEmployeesSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/1/employees");
        when(taskService.findEmployeesByTaskId(1)).thenReturn(Arrays.asList(new Employee(), new Employee()));
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetEmployeesNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/1/employees");
        when(taskService.findEmployeesByTaskId(1)).thenThrow(new NotFoundException());
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
    public void testPostTaskSuccess() throws Exception {
        Task taskToUpdate = new Task(0, "Обновление системы", "2024-12-30", null);
        Task existingTask = new Task(1, "Обновление системы", "2024-10-30", null);

        when(request.getRequestURI()).thenReturn("/task/1");
        when(response.getWriter()).thenReturn(printWriter);

        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(taskToUpdate));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);
        when(taskService.findById(1)).thenReturn(existingTask);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write("Данные обновлены успешно.");
    }

    @Test
    public void testPostTaskNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/1");
        when(response.getWriter()).thenReturn(printWriter);

        Task task = new Task(1, "Обновление системы", "2024-10-30", null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(task));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        Mockito.doThrow(new NotFoundException())
                .when(taskService)
                .update(any(Task.class));

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testPutTaskSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/task/");
        when(response.getWriter()).thenReturn(printWriter);

        Task task = new Task(1, "Обновление системы", "2024-10-30", null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(task));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);
        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testDeleteTaskSuccess() throws Exception {
        Integer taskId = 1;
        when(request.getRequestURI()).thenReturn("/task/" + taskId);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(taskService, times(1)).deleteById(taskId);
    }
}
