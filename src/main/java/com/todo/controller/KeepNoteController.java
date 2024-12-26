package com.todo.controller;

import com.todo.dto.request.KeepNoteRequest;
import com.todo.dto.response.ErrorResponse;
import com.todo.dto.response.KeepNoteResponse;
import com.todo.service.KeepNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/keepnotes")
@RequiredArgsConstructor
@Tag(name = "Keep Note Controller", description = "APIs for managing Keep Note items")
public class KeepNoteController {
    private final KeepNoteService keepNoteService;


    @Operation(summary = "Create a new Keep Note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Keep Note created successfully",
                    content = @Content(schema = @Schema(implementation = KeepNoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<KeepNoteResponse> createKeepNote(@Valid @RequestBody final KeepNoteRequest keepNoteRequest) {
        return new ResponseEntity<>(keepNoteService.createKeepNote(keepNoteRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a Keep Note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Keep Note",
                    content = @Content(schema = @Schema(implementation = KeepNoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Keep Note not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{keepNoteId}")
    public ResponseEntity<KeepNoteResponse> getKeepNoteById(@PathVariable(name = "keepNoteId") final int id) {
        return ResponseEntity.ok(keepNoteService.getKeepNoteById(id));
    }

    @Operation(summary = "Get all Keep Note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Keep Note retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<KeepNoteResponse>> getAllKeepNote() {
        return ResponseEntity.ok(keepNoteService.getAllKeepNote());
    }

    @Operation(summary = "Update an existing Keep Note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Keep Note updated successfully",
                    content = @Content(schema = @Schema(implementation = KeepNoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Keep Note not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{keepNoteId}")
    public ResponseEntity<KeepNoteResponse> updateKeepNoteById(@PathVariable(name = "keepNoteId") final int id, @Valid @RequestBody final KeepNoteRequest keepNoteRequest) {
        return ResponseEntity.ok(keepNoteService.updateKeepNoteById(id, keepNoteRequest));
    }

    @Operation(summary = "Delete a Keep Note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Keep Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Keep Note not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(value = "/{keepNoteId}")
    public ResponseEntity<Void> deleteKeepNoteById(@PathVariable(name = "keepNoteId") final int id) {
        keepNoteService.deleteKeepNoteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all Keep Note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List of Keep Note deleted successfully")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAllKeepNote() {
        keepNoteService.deleteAllKeepNote();
        return ResponseEntity.noContent().build();
    }
}
