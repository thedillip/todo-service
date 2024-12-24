package com.todo.dto.response;

import com.todo.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoResponse {
    private UUID id;
    private String title;
    private String description;
    private TodoStatus status;
}
