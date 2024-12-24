package com.todo.controller;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/todos")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody final TodoRequest todoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todoRequest));
    }

    @GetMapping(value = "/{todoId}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable(name = "todoId") final UUID id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodo() {
        return ResponseEntity.ok(todoService.getAllTodo());
    }

    @PutMapping(value = "/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable(name = "todoId") final UUID id, @RequestBody final TodoRequest todoRequest) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest));
    }

    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable(name = "todoId") final UUID id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }
}
