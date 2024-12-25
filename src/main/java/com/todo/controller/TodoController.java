package com.todo.controller;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Todo Controller", description = "APIs for managing Todo items")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create a new Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Todo created successfully",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody final TodoRequest todoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todoRequest));
    }

    @Operation(summary = "Get a Todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Todo",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @GetMapping(value = "/{todoId}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable(name = "todoId") final UUID id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @Operation(summary = "Get all Todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Todos retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodo() {
        return ResponseEntity.ok(todoService.getAllTodo());
    }

    @Operation(summary = "Update an existing Todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo updated successfully",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @PutMapping(value = "/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable(name = "todoId") final UUID id, @Valid @RequestBody final TodoRequest todoRequest) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest));
    }

    @Operation(summary = "Delete a Todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todo deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable(name = "todoId") final UUID id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }
}
