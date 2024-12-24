package com.todo.dto.request;

import com.todo.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {
    private String title;
    private String description;
    private TodoStatus status;
}
