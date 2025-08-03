package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoItemRequest;
import com.example.todoapp.dto.TodoItemResponse;
import com.example.todoapp.service.TodoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoItemController {

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping
    public ResponseEntity<List<TodoItemResponse>> getAllTodoItems() {
        List<TodoItemResponse> todoItems = todoItemService.getAllTodoItems();
        return ResponseEntity.ok(todoItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemResponse> getTodoItemById(@PathVariable Long id) {
        TodoItemResponse todoItem = todoItemService.getTodoItemById(id);
        return ResponseEntity.ok(todoItem);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TodoItemResponse>> getCompletedItems() {
        List<TodoItemResponse> completedItems = todoItemService.getCompletedItems();
        return ResponseEntity.ok(completedItems);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TodoItemResponse>> getPendingItems() {
        List<TodoItemResponse> pendingItems = todoItemService.getPendingItems();
        return ResponseEntity.ok(pendingItems);
    }

    @PostMapping
    public ResponseEntity<TodoItemResponse> createTodoItem(@Valid @RequestBody TodoItemRequest request) {
        TodoItemResponse createdItem = todoItemService.createTodoItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemResponse> updateTodoItem(@PathVariable Long id,
                                                           @Valid @RequestBody TodoItemRequest request) {
        TodoItemResponse updatedItem = todoItemService.updateTodoItem(id, request);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoItemResponse> markAsCompleted(@PathVariable Long id) {
        TodoItemResponse updatedItem = todoItemService.markAsCompleted(id);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TodoItemResponse> markAsIncomplete(@PathVariable Long id) {
        TodoItemResponse updatedItem = todoItemService.markAsIncomplete(id);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.ok(Map.of("message", "Todo item deleted successfully"));
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Map<String, String>> deleteCompletedItems() {
        todoItemService.deleteCompletedItems();
        return ResponseEntity.ok(Map.of("message", "All completed items deleted successfully"));
    }
}
