package controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.appservlet.controller.TaskController;
import org.example.appservlet.controller.advice.ControllerAdvice;
import org.example.appservlet.dto.request.TaskRequestDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.dto.response.TaskResponseDto;
import org.example.appservlet.service.TaskService;

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
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @InjectMocks
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        TaskResponseDto task1 = new TaskResponseDto();
        TaskResponseDto task2 = new TaskResponseDto();
        when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/task/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetTaskById() throws Exception {
        String id = "1";
        TaskResponseDto task = new TaskResponseDto();
        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(get("/task/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeesByTask() throws Exception {
        String id = "1";
        List<EmployeeResponseDto> employees = Arrays.asList(new EmployeeResponseDto(), new EmployeeResponseDto());
        when(taskService.findEmployeesByTaskId(id)).thenReturn(employees);

        mockMvc.perform(get("/task/{id}/employees", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateTask() throws Exception {
        String id = "1";
        TaskResponseDto taskDTO = new TaskResponseDto();
        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post("/task/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Данные успешно обновлены."));
    }

    @Test
    public void testCreateTask() throws Exception {
        TaskResponseDto taskDTO = new TaskResponseDto();
        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put("/task/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Запись успешно сохранена."));
    }

    @Test
    public void testDeleteTask() throws Exception {
        String id = "1";

        mockMvc.perform(delete("/task/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Запись успешно удалена."));
    }
}
