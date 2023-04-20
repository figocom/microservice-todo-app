package com.example.todoappmicroservicetodoapi.controller;

import com.example.todoappmicroservicetodoapi.model.Todo;
import com.example.todoappmicroservicetodoapi.payload.TodoDTO;
import com.example.todoappmicroservicetodoapi.payload.TodoUpdateDTO;
import com.example.todoappmicroservicetodoapi.response.WebResponse;
import com.example.todoappmicroservicetodoapi.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/addTodo")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody TodoDTO todoDTO) {
        Todo response=todoService.addTodo(todoDTO);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/updateTodo")
    public ResponseEntity<Todo> updateTodo(@Valid @RequestBody TodoUpdateDTO todoUpdateDTO) {
        Todo response=todoService.updateTodo(todoUpdateDTO);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getTodos")
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> response=todoService.getTodos();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getTodo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo response=todoService.getTodoById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getTodosByUserId/{id}")
    public ResponseEntity<List<Todo>> getTodoByUserName(@PathVariable Long id) {
        List<Todo> response=todoService.getTodoByUserId(id);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/deleteTodo/{id}")
    public ResponseEntity<Long> deleteTodoById(@PathVariable Long id) {
        Long response=todoService.deleteTodoById(id);
        return ResponseEntity.ok(response);
    }
}
