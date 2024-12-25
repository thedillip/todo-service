package com.todo.dto.request;

import com.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequest {
    @NotBlank(message = "Title must not be blank.")
    private String title;

    @NotBlank(message = "Description must not be blank.")
    private String description;

    @NotNull(message = "Status must not be null.")
    private TodoStatus status;
}
