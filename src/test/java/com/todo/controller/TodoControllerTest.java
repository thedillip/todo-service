package com.todo.controller;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.enums.TodoStatus;
import com.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void testCreateTodo() throws Exception {
        TodoRequest todoRequest = new TodoRequest("Test Todo", "Description", TodoStatus.PENDING);
        TodoResponse todoResponse = new TodoResponse(UUID.randomUUID(), "Test Todo", "Description", TodoStatus.PENDING);

        when(todoService.createTodo(any(TodoRequest.class))).thenReturn(todoResponse);

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Todo\", \"description\": \"Description\", \"status\": \"PENDING\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(todoService, times(1)).createTodo(any(TodoRequest.class));
    }

    @Test
    void testGetTodoById() throws Exception {
        UUID todoId = UUID.randomUUID();
        TodoResponse todoResponse = new TodoResponse(todoId, "Test Todo", "Description", TodoStatus.PENDING);

        when(todoService.getTodoById(todoId)).thenReturn(todoResponse);

        mockMvc.perform(get("/api/v1/todos/{todoId}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(todoService, times(1)).getTodoById(todoId);
    }

    @Test
    void testGetAllTodo() throws Exception {
        TodoResponse todoResponse = new TodoResponse(UUID.randomUUID(), "Test Todo", "Description", TodoStatus.PENDING);
        List<TodoResponse> todoList = List.of(todoResponse);

        when(todoService.getAllTodo()).thenReturn(todoList);

        mockMvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Todo"))
                .andExpect(jsonPath("$[0].description").value("Description"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(todoService, times(1)).getAllTodo();
    }

    @Test
    void testUpdateTodo() throws Exception {
        UUID todoId = UUID.randomUUID();
        TodoRequest todoRequest = new TodoRequest("Updated Todo", "Updated Description", TodoStatus.COMPLETED);
        TodoResponse todoResponse = new TodoResponse(todoId, "Updated Todo", "Updated Description", TodoStatus.COMPLETED);

        when(todoService.updateTodo(any(UUID.class), any(TodoRequest.class))).thenReturn(todoResponse);

        mockMvc.perform(put("/api/v1/todos/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Updated Todo\", \"description\": \"Updated Description\", \"status\": \"COMPLETED\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(todoService, times(1)).updateTodo(any(UUID.class), any(TodoRequest.class));
    }

    @Test
    void testDeleteTodoById() throws Exception {
        UUID todoId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/todos/{todoId}", todoId))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodoById(todoId);
    }
}
