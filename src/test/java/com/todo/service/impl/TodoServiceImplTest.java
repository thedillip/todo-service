package com.todo.service.impl;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.entity.Todo;
import com.todo.enums.TodoStatus;
import com.todo.mapper.TodoMapper;
import com.todo.repository.TodoRepository;
import com.todo.util.AppConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void testCreateTodo() {
        TodoRequest request = TodoRequest.builder().title("Test Todo").description("Description").status(TodoStatus.PENDING).build();

        Todo todoEntity = Todo.builder().id(UUID.randomUUID()).title("Test Todo").description("Description").status(TodoStatus.PENDING).build();

        Todo savedTodo = Todo.builder().id(todoEntity.getId()).title("Test Todo").description("Description").status(TodoStatus.PENDING).build();

        TodoResponse response = TodoResponse.builder().id(savedTodo.getId()).title("Test Todo").description("Description").status(TodoStatus.PENDING).build();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        when(todoMapper.toTodoEntity(request)).thenReturn(todoEntity);
        when(todoRepository.save(todoEntity)).thenReturn(savedTodo);
        when(todoMapper.toTodoResponse(savedTodo)).thenReturn(response);

        TodoResponse result = todoService.createTodo(request);

        assertNotNull(result);
        assertEquals("Test Todo", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertEquals(TodoStatus.PENDING, result.getStatus());
        verify(hashOperations).put(AppConstants.TODO_CACHE_KEY, result.getId().toString(), response);
    }

    @Test
    void testGetTodoByIdFromCache() {
        UUID id = UUID.randomUUID();
        TodoResponse response = TodoResponse.builder().id(id).title("Cached Todo").description("Cached Description").status(TodoStatus.COMPLETED).build();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(AppConstants.TODO_CACHE_KEY, id.toString())).thenReturn(response);

        TodoResponse result = todoService.getTodoById(id);

        assertNotNull(result);
        assertEquals("Cached Todo", result.getTitle());
        verify(todoRepository, never()).findById(any());
    }

    @Test
    void testGetTodoByIdFromDatabase() {
        UUID id = UUID.randomUUID();
        Todo todo = Todo.builder().id(id).title("Database Todo").description("Database Description").status(TodoStatus.PENDING).build();

        TodoResponse response = TodoResponse.builder().id(id).title("Database Todo").description("Database Description").status(TodoStatus.PENDING).build();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(AppConstants.TODO_CACHE_KEY, id.toString())).thenReturn(null);
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoMapper.toTodoResponse(todo)).thenReturn(response);

        TodoResponse result = todoService.getTodoById(id);

        assertNotNull(result);
        assertEquals("Database Todo", result.getTitle());
        verify(hashOperations).put(AppConstants.TODO_CACHE_KEY, id.toString(), response);
    }

    @Test
    void testGetAllTodo() {
        TodoResponse response = TodoResponse.builder().id(UUID.randomUUID()).title("Cached Todo").description("Cached Description").status(TodoStatus.COMPLETED).build();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        when(hashOperations.values(AppConstants.TODO_CACHE_KEY)).thenReturn(List.of(response));

        List<TodoResponse> result = todoService.getAllTodo();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cached Todo", result.getFirst().getTitle());
    }

    @Test
    void testUpdateTodo() {
        UUID id = UUID.randomUUID();
        Todo existingTodo = Todo.builder().id(id).title("Old Title").description("Old Description").status(TodoStatus.PENDING).build();

        TodoRequest request = TodoRequest.builder().title("Updated Title").description("Updated Description").status(TodoStatus.COMPLETED).build();

        Todo updatedTodo = Todo.builder().id(id).title("Updated Title").description("Updated Description").status(TodoStatus.COMPLETED).build();

        TodoResponse response = TodoResponse.builder().id(id).title("Updated Title").description("Updated Description").status(TodoStatus.COMPLETED).build();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoMapper.toTodoEntity(request)).thenReturn(updatedTodo);
        when(todoRepository.save(updatedTodo)).thenReturn(updatedTodo);
        when(todoMapper.toTodoResponse(updatedTodo)).thenReturn(response);

        TodoResponse result = todoService.updateTodo(id, request);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(hashOperations).put(AppConstants.TODO_CACHE_KEY, id.toString(), response);
    }

    @Test
    void testDeleteTodoById() {
        UUID id = UUID.randomUUID();

        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);
        doNothing().when(todoRepository).deleteById(id);
        when(hashOperations.delete(AppConstants.TODO_CACHE_KEY, id.toString())).thenReturn(1L); // Assuming it returns Long

        todoService.deleteTodoById(id);

        verify(todoRepository).deleteById(id);
        verify(hashOperations).delete(AppConstants.TODO_CACHE_KEY, id.toString());
    }
}