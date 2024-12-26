package com.todo.mapper;

import com.todo.dto.request.KeepNoteRequest;
import com.todo.dto.response.KeepNoteResponse;
import com.todo.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KeepNoteMapper {
    @Mapping(target = "keepId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Note toNoteEntity(KeepNoteRequest keepRequest);

    KeepNoteResponse toKeepNoteResponse(Note note);

    List<KeepNoteResponse> toKeepNoteResponseList(List<Note> notes);

    @Mapping(target = "keepId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateNoteFromRequest(@MappingTarget Note note, KeepNoteRequest keepRequest);
}
