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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KeepNoteServiceImpl implements KeepNoteService {

    private final NoteRepository noteRepository;
    private final KeepNoteMapper keepNoteMapper;
    private final MessageSource messageSource;
    private final RedisCacheUtil<Integer, KeepNoteResponse> redisCacheUtil;

    private static final long TTL_EXPIRY_TIME = 120;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    @Override
    public KeepNoteResponse createKeepNote(KeepNoteRequest keepNoteRequest) {
        Note noteEntity = keepNoteMapper.toNoteEntity(keepNoteRequest);
        Note savedNote = noteRepository.save(noteEntity);
        KeepNoteResponse response = keepNoteMapper.toKeepNoteResponse(savedNote);
        redisCacheUtil.saveOrUpdate(response.getKeepId(), response, AppConstants.KEEP_NOTE_MAP_NAME, TTL_EXPIRY_TIME, TIME_UNIT);
        return response;
    }

    @Override
    public KeepNoteResponse getKeepNoteById(int id) {
        KeepNoteResponse response = redisCacheUtil.get(id, AppConstants.KEEP_NOTE_MAP_NAME);
        if (Objects.isNull(response)) {
            Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("keepnote.not.found", new Object[]{id}, Locale.ENGLISH)));
            response = keepNoteMapper.toKeepNoteResponse(note);
            redisCacheUtil.saveOrUpdate(response.getKeepId(), response, AppConstants.KEEP_NOTE_MAP_NAME, TTL_EXPIRY_TIME, TIME_UNIT);
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
            responseList.forEach(response -> redisCacheUtil.saveOrUpdate(response.getKeepId(), response, AppConstants.KEEP_NOTE_MAP_NAME, TTL_EXPIRY_TIME, TIME_UNIT));
        }
        return responseList;
    }

    @Override
    public KeepNoteResponse updateKeepNoteById(int id, KeepNoteRequest keepNoteRequest) {
        Note existingNote = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("keepnote.not.found", new Object[]{id}, Locale.ENGLISH)));
        Note updateNote = keepNoteMapper.toNoteEntity(keepNoteRequest);
        updateNote.setKeepId(existingNote.getKeepId());
        Note updatedSavedNote = noteRepository.save(updateNote);
        KeepNoteResponse response = keepNoteMapper.toKeepNoteResponse(updatedSavedNote);
        redisCacheUtil.saveOrUpdate(response.getKeepId(), response, AppConstants.KEEP_NOTE_MAP_NAME, TTL_EXPIRY_TIME, TIME_UNIT);
        return response;
    }

    @Override
    public void deleteKeepNoteById(int id) {
        Note existingNote = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("keepnote.not.found", new Object[]{id}, Locale.ENGLISH)));
        noteRepository.delete(existingNote);
        redisCacheUtil.delete(existingNote.getKeepId(), AppConstants.KEEP_NOTE_MAP_NAME);
    }

    @Override
    public void deleteAllKeepNote() {
        noteRepository.deleteAll();
        redisCacheUtil.clearRMap(AppConstants.KEEP_NOTE_MAP_NAME);
    }
}
