package com.example.todoapp.service;

import com.example.todoapp.dto.TodoItemRequest;
import com.example.todoapp.dto.TodoItemResponse;
import com.example.todoapp.exception.TodoItemNotFoundException;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public List<TodoItemResponse> getAllTodoItems() {
        return todoItemRepository.findAllOrderedByCompletionAndDate()
                .stream()
                .map(TodoItemResponse::new)
                .collect(Collectors.toList());
    }

    public TodoItemResponse getTodoItemById(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Todo item not found with id: " + id));
        return new TodoItemResponse(todoItem);
    }

    public TodoItemResponse createTodoItem(TodoItemRequest request) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setTargetCompletionDate(request.getTargetCompletionDate());

        TodoItem savedItem = todoItemRepository.save(todoItem);
        return new TodoItemResponse(savedItem);
    }

    public TodoItemResponse updateTodoItem(Long id, TodoItemRequest request) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Todo item not found with id: " + id));

        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setTargetCompletionDate(request.getTargetCompletionDate());
        todoItem.setUpdatedAt(LocalDateTime.now());

        TodoItem updatedItem = todoItemRepository.save(todoItem);
        return new TodoItemResponse(updatedItem);
    }

    public TodoItemResponse markAsCompleted(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Todo item not found with id: " + id));

        todoItem.setCompleted(true);
        todoItem.setUpdatedAt(LocalDateTime.now());

        TodoItem updatedItem = todoItemRepository.save(todoItem);
        return new TodoItemResponse(updatedItem);
    }

    public TodoItemResponse markAsIncomplete(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("Todo item not found with id: " + id));

        todoItem.setCompleted(false);
        todoItem.setUpdatedAt(LocalDateTime.now());

        TodoItem updatedItem = todoItemRepository.save(todoItem);
        return new TodoItemResponse(updatedItem);
    }

    public void deleteTodoItem(Long id) {
        if (!todoItemRepository.existsById(id)) {
            throw new TodoItemNotFoundException("Todo item not found with id: " + id);
        }
        todoItemRepository.deleteById(id);
    }

    public void deleteCompletedItems() {
        List<TodoItem> completedItems = todoItemRepository.findByCompletedTrue();
        todoItemRepository.deleteAll(completedItems);
    }

    public List<TodoItemResponse> getCompletedItems() {
        return todoItemRepository.findByCompletedTrue()
                .stream()
                .map(TodoItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponse> getPendingItems() {
        return todoItemRepository.findByCompletedFalse()
                .stream()
                .map(TodoItemResponse::new)
                .collect(Collectors.toList());
    }
}