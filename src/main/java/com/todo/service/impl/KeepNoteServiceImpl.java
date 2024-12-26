package com.todo.service.impl;

import com.todo.dto.request.KeepNoteRequest;
import com.todo.dto.response.KeepNoteResponse;
import com.todo.entity.Note;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.KeepNoteMapper;
import com.todo.repository.NoteRepository;
import com.todo.service.KeepNoteService;
import com.todo.util.AppConstants;
import com.todo.util.RedisCacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeepNoteServiceImpl implements KeepNoteService {

    private final NoteRepository noteRepository;
    private final KeepNoteMapper keepNoteMapper;
    private final RedisCacheUtil<KeepNoteResponse> redisCacheUtil;

    @Override
    public KeepNoteResponse createKeepNote(KeepNoteRequest keepNoteRequest) {
        Note noteEntity = keepNoteMapper.toNoteEntity(keepNoteRequest);
        Note savedNote = noteRepository.save(noteEntity);
        KeepNoteResponse response = keepNoteMapper.toKeepNoteResponse(savedNote);
        redisCacheUtil.saveOrUpdate(String.valueOf(response.getKeepId()), response, AppConstants.KEEP_NOTE_MAP_NAME);
        return response;
    }

    @Override
    public KeepNoteResponse getKeepNoteById(int id) {
        KeepNoteResponse response = redisCacheUtil.get(String.valueOf(id), AppConstants.KEEP_NOTE_MAP_NAME);
        if (Objects.isNull(response)) {
            Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Keep Note not found."));
            response = keepNoteMapper.toKeepNoteResponse(note);
            redisCacheUtil.saveOrUpdate(String.valueOf(response.getKeepId()), response, AppConstants.KEEP_NOTE_MAP_NAME);
        }
        return response;
    }

    @Override
    public List<KeepNoteResponse> getAllKeepNote() {
        Collection<KeepNoteResponse> all = redisCacheUtil.getAll(AppConstants.KEEP_NOTE_MAP_NAME);
        List<KeepNoteResponse> responseList = new ArrayList<>(all);
        if (CollectionUtils.isEmpty(responseList)) {
            List<Note> noteList = noteRepository.findAll();
            responseList = keepNoteMapper.toKeepNoteResponseList(noteList);
            responseList.forEach(response -> redisCacheUtil.saveOrUpdate(String.valueOf(response.getKeepId()), response, AppConstants.KEEP_NOTE_MAP_NAME));
        }
        return responseList;
    }

    @Override
    public KeepNoteResponse updateKeepNoteById(int id, KeepNoteRequest keepNoteRequest) {
        Note existingNote = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Keep Note not found."));
        Note updateNote = keepNoteMapper.toNoteEntity(keepNoteRequest);
        updateNote.setKeepId(existingNote.getKeepId());
        Note updatedSavedNote = noteRepository.save(updateNote);
        KeepNoteResponse response = keepNoteMapper.toKeepNoteResponse(updatedSavedNote);
        redisCacheUtil.saveOrUpdate(String.valueOf(response.getKeepId()), response, AppConstants.KEEP_NOTE_MAP_NAME);
        return response;
    }

    @Override
    public void deleteKeepNoteById(int id) {
        Note existingNote = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Keep Note not found."));
        noteRepository.delete(existingNote);
        redisCacheUtil.delete(String.valueOf(existingNote.getKeepId()), AppConstants.KEEP_NOTE_MAP_NAME);
    }

    @Override
    public void deleteAllKeepNote() {
        noteRepository.deleteAll();
        redisCacheUtil.clearRMap(AppConstants.KEEP_NOTE_MAP_NAME);
    }
}
