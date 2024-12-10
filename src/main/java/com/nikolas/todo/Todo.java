package com.nikolas.todo;

public record Todo(Integer userId, Integer id, String title, boolean completed) {
}
