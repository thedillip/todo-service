package com.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeepNoteRequest {

    @NotBlank(message = "Keep Title must not be blank.")
    private String keepTitle;

    @NotBlank(message = "Keep Note must not be blank.")
    private String keepNote;
}
