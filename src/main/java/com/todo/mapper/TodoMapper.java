package com.todo.mapper;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Todo toTodoEntity(TodoRequest todoRequest);

    TodoResponse toTodoResponse(Todo todo);

    List<TodoResponse> toTodoResponseList(List<Todo> todos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateTodoFromRequest(@MappingTarget Todo todo, TodoRequest todoRequest);
}
