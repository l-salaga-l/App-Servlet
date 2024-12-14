package controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.appservlet.controller.EmployeeController;
import org.example.appservlet.controller.advice.ControllerAdvice;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @InjectMocks
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        EmployeeResponseDto employee1 = new EmployeeResponseDto();
        EmployeeResponseDto employee2 = new EmployeeResponseDto();
        when(employeeService.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/employee/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String id = "1";
        EmployeeResponseDto employeeDTO = new EmployeeResponseDto();
        when(employeeService.findById(id)).thenReturn(employeeDTO);

        mockMvc.perform(get("/employee/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeeTasks() throws Exception {
        String id = "1";
        List<TaskResponseDto> tasks = Arrays.asList(new TaskResponseDto(), new TaskResponseDto());
        when(employeeService.findTasksByEmployeeId(id)).thenReturn(tasks);

        mockMvc.perform(get("/employee/{id}/tasks", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        String id = "1";
        EmployeeResponseDto employeeDTO = new EmployeeResponseDto();
        String jsonContent = objectMapper.writeValueAsString(employeeDTO);

        mockMvc.perform(post("/employee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Данные успешно обновлены."));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeResponseDto employeeDTO = new EmployeeResponseDto();
        String jsonContent = objectMapper.writeValueAsString(employeeDTO);

        mockMvc.perform(put("/employee/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Запись успешно сохранена."));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        String id = "1";

        mockMvc.perform(delete("/employee/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Запись успешно удалена."));
    }
}
