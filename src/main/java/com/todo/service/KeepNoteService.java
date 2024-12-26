package com.todo.service;

import com.todo.dto.request.KeepNoteRequest;
import com.todo.dto.response.KeepNoteResponse;

import java.util.List;
import java.util.Map;

public interface KeepNoteService {
    KeepNoteResponse createKeepNote(KeepNoteRequest keepNoteRequest);

    KeepNoteResponse getKeepNoteById(int id);

    List<KeepNoteResponse> getAllKeepNote();

    KeepNoteResponse updateKeepNoteById(int id, KeepNoteRequest keepNoteRequest);

    void deleteKeepNoteById(int id);

    void deleteAllKeepNote();
}
