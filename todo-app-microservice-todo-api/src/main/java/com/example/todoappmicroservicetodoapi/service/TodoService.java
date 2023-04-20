package com.example.todoappmicroservicetodoapi.service;

import com.example.todoappmicroservicetodoapi.exception.ItemNotFoundException;
import com.example.todoappmicroservicetodoapi.model.Todo;
import com.example.todoappmicroservicetodoapi.payload.TodoDTO;
import com.example.todoappmicroservicetodoapi.payload.TodoUpdateDTO;
import com.example.todoappmicroservicetodoapi.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo addTodo(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCreatedBy(todoDTO.getCreatedBy());
        return todoRepository.save(todo);
    }

    public Todo updateTodo(TodoUpdateDTO todoUpdateDTO) {
        Todo todo = null;
        try {
            todo = todoRepository.findById(todoUpdateDTO.getId()).orElseThrow(() -> new ItemNotFoundException("Todo not found with id " + todoUpdateDTO.getId()));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        todo.setTitle(todoUpdateDTO.getTitle());
        todo.setDescription(todoUpdateDTO.getDescription());
        todo.setCompleted(todoUpdateDTO.isCompleted());
        todo.setUpdatedBy(todoUpdateDTO.getUpdatedBy());
        todo.setUpdatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(Long id) {
        Todo todo = null;
        try {
            todo = todoRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Todo not found with id " + id));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        return todo;
    }

    public List<Todo> getTodoByUserId(Long id) {
        return todoRepository.findAllByCreatedBy(id);
    }

    public Long deleteTodoById(Long id) {
        try {
            todoRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Todo not found with id " + id));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        todoRepository.deleteById(id);
        return id;
    }
}
