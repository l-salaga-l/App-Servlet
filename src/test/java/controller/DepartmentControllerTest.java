package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.appservlet.controller.DepartmentController;
import org.example.appservlet.exception.NotFoundException;
import org.example.appservlet.model.Department;
import org.example.appservlet.model.Employee;
import org.example.appservlet.service.impl.DepartmentServiceImpl;
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

public class DepartmentControllerTest {

    @Mock
    private DepartmentServiceImpl departmentService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private DepartmentController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDepartmentsSuccess() throws Exception {
        Iterable<Department> departments = Arrays.asList(new Department(1, "Маркетинг", "Москва", null),
                                                        new Department(2, "ИТ-отдел", "Казань", null));

        when(request.getRequestURI()).thenReturn("/department/");
        when(departmentService.findAll()).thenReturn(departments);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(new ObjectMapper().writeValueAsString(departments));
    }

    @Test
    public void testGetDepartmentByIdSuccess() throws Exception {
        Department department = new Department(1, "Маркетинг", "Москва", null);

        when(request.getRequestURI()).thenReturn("/department/1");
        when(departmentService.findById(1)).thenReturn(department);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetDepartmentByIdNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/1");
        when(departmentService.findById(1)).thenThrow(new NotFoundException());
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testGetDepartmentByIdBadRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/abc");
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).write("Неправильный формат идентификатора!");
    }

    @Test public void testGetEmployeesSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/1/employees");
        when(departmentService.findEmployeeByDepartmentId(1)).thenReturn(Arrays.asList(new Employee(), new Employee()));
        when(response.getWriter()).thenReturn(printWriter);

        controller.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testGetEmployeesNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/1/employees");
        when(departmentService.findEmployeeByDepartmentId(1)).thenThrow(new NotFoundException());
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
    public void testPostDepartmentSuccess() throws Exception {
        Department departmentToUpdate = new Department(0, "Маркетинг", "Нижний Новгород", null);
        Department existingDepartment = new Department(1, "Маркетинг", "Москва", null);

        when(request.getRequestURI()).thenReturn("/department/1");
        when(response.getWriter()).thenReturn(printWriter);

        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(departmentToUpdate));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);
        when(departmentService.findById(1)).thenReturn(existingDepartment);

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(printWriter).write("Данные обновлены успешно.");
    }

    @Test
    public void testPostEmployeeNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/1");
        when(response.getWriter()).thenReturn(printWriter);

        Department department = new Department(1, "Маркетинг", "Москва", null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(department));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);

        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        Mockito.doThrow(new NotFoundException())
                .when(departmentService)
                .update(any(Department.class));

        controller.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(printWriter).write("Нет записи с данным ID!");
    }

    @Test
    public void testPutDepartmentSuccess() throws Exception {
        when(request.getRequestURI()).thenReturn("/department/");
        when(response.getWriter()).thenReturn(printWriter);

        Department department = new Department(1, "Маркетинг", "Москва", null);
        InputStream inputStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(department));
        DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(inputStream);
        when(request.getInputStream()).thenReturn(delegatingServletInputStream);

        controller.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(printWriter).write(any(String.class));
    }

    @Test
    public void testDeleteDepartmentSuccess() throws Exception {
        Integer departmentId = 1;
        when(request.getRequestURI()).thenReturn("/department/" + departmentId);
        when(response.getWriter()).thenReturn(printWriter);

        controller.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(departmentService, times(1)).deleteById(departmentId);
    }
}
