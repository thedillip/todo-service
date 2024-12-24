package com.todo.service.impl;

import com.todo.dto.request.TodoRequest;
import com.todo.dto.response.TodoResponse;
import com.todo.entity.Todo;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.TodoMapper;
import com.todo.repository.TodoRepository;
import com.todo.service.TodoService;
import com.todo.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TodoMapper todoMapper;

    @Override
    public TodoResponse createTodo(TodoRequest todoRequest) {
        Todo todoEntity = todoMapper.toTodoEntity(todoRequest);
        Todo savedTodo = todoRepository.save(todoEntity);
        TodoResponse response = todoMapper.toTodoResponse(savedTodo);
        redisTemplate.opsForHash().put(AppConstants.TODO_CACHE_KEY, response.getId().toString(), response);
        return response;
    }

    @Override
    public TodoResponse getTodoById(UUID id) {
        TodoResponse response = (TodoResponse) redisTemplate.opsForHash().get(AppConstants.TODO_CACHE_KEY, id.toString());
        if (!Objects.isNull(response))
            return response;
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found."));
        response = todoMapper.toTodoResponse(todo);
        redisTemplate.opsForHash().put(AppConstants.TODO_CACHE_KEY, response.getId().toString(), response);
        return response;
    }

    @Override
    public List<TodoResponse> getAllTodo() {
        List<Object> todosAsObjects = redisTemplate.opsForHash().values(AppConstants.TODO_CACHE_KEY);
        List<TodoResponse> response = todosAsObjects.stream()
                .filter(TodoResponse.class::isInstance)
                .map(TodoResponse.class::cast)
                .toList();
        if (!CollectionUtils.isEmpty(response))
            return response;
        List<TodoResponse> todoResponseList = todoMapper.toTodoResponseList(todoRepository.findAll());
        todoResponseList.forEach(todoResponse ->
                redisTemplate.opsForHash().put(AppConstants.TODO_CACHE_KEY, todoResponse.getId().toString(), todoResponse)
        );
        return todoResponseList;
    }

    @Override
    public TodoResponse updateTodo(UUID id, TodoRequest todoRequest) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found."));
        Todo updateTodo = todoMapper.toTodoEntity(todoRequest);
        updateTodo.setId(todo.getId());
        todoRepository.save(updateTodo);
        TodoResponse response = todoMapper.toTodoResponse(updateTodo);
        redisTemplate.opsForHash().put(AppConstants.TODO_CACHE_KEY, response.getId().toString(), response);
        return response;
    }

    @Override
    public void deleteTodoById(UUID id) {
        todoRepository.deleteById(id);
        redisTemplate.opsForHash().delete(AppConstants.TODO_CACHE_KEY, id.toString());
    }
}
