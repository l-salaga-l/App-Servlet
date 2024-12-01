package controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.appservlet.controller.EmployeeController;
import org.example.appservlet.controller.advice.ControllerAdvice;
import org.example.appservlet.dto.EmployeeDTO;
import org.example.appservlet.dto.TaskDTO;
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
        EmployeeDTO employee1 = new EmployeeDTO();
        EmployeeDTO employee2 = new EmployeeDTO();
        when(employeeService.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(get("/employee/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String id = "1";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        when(employeeService.findById(id)).thenReturn(employeeDTO);

        mockMvc.perform(get("/employee/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeeTasks() throws Exception {
        String id = "1";
        List<TaskDTO> tasks = Arrays.asList(new TaskDTO(), new TaskDTO());
        when(employeeService.findTasksByEmployeeId(id)).thenReturn(tasks);

        mockMvc.perform(get("/employee/{id}/tasks", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        String id = "1";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String jsonContent = objectMapper.writeValueAsString(employeeDTO);

        mockMvc.perform(post("/employee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Данные успешно обновлены."));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
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
