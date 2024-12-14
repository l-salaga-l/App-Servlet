package controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.appservlet.controller.DepartmentController;
import org.example.appservlet.controller.advice.ControllerAdvice;
import org.example.appservlet.dto.response.DepartmentResponseDto;
import org.example.appservlet.dto.response.EmployeeResponseDto;
import org.example.appservlet.service.DepartmentService;

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
public class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @InjectMocks
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        DepartmentResponseDto department1 = new DepartmentResponseDto();
        DepartmentResponseDto department2 = new DepartmentResponseDto();
        when(departmentService.findAll()).thenReturn(Arrays.asList(department1, department2));

        mockMvc.perform(get("/department/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        String id = "1";
        DepartmentResponseDto departmentDTO = new DepartmentResponseDto();
        when(departmentService.findById(id)).thenReturn(departmentDTO);

        mockMvc.perform(get("/department/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEmployeesByDepartment() throws Exception {
        String id = "1";
        List<EmployeeResponseDto> employees = Arrays.asList(new EmployeeResponseDto(), new EmployeeResponseDto());
        when(departmentService.findEmployeeByDepartmentId(id)).thenReturn(employees);

        mockMvc.perform(get("/department/{id}/employees", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        String id = "1";
        DepartmentResponseDto departmentDTO = new DepartmentResponseDto();
        String jsonContent = objectMapper.writeValueAsString(departmentDTO);

        mockMvc.perform(post("/department/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Данные успешно обновлены."));
    }

    @Test
    public void testCreateDepartment() throws Exception {
        DepartmentResponseDto departmentDTO = new DepartmentResponseDto();
        String jsonContent = objectMapper.writeValueAsString(departmentDTO);

        mockMvc.perform(put("/department/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Запись успешно сохранена."));
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        String id = "1";

        mockMvc.perform(delete("/department/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Запись успешно удалена."));
    }
}
