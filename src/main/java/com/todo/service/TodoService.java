package com.todo.service;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;

import java.util.List;
import java.util.UUID;

public interface TodoService {
    TodoResponse createTodo(TodoRequest todoRequest);

    TodoResponse getTodoById(UUID id);

    List<TodoResponse> getAllTodo();

    TodoResponse updateTodo(UUID id, TodoRequest todoRequest);

    void deleteTodoById(UUID id);
}
