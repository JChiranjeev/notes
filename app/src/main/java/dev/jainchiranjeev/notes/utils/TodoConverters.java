package dev.jainchiranjeev.notes.utils;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.jainchiranjeev.notes.models.TodoModel;

public class TodoConverters {
    @TypeConverter
    public String listToString(List<TodoModel>todoList) {
        String todoJson = null;
        if(todoList == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            todoJson = objectMapper.writeValueAsString(todoList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return todoJson;
    }

    @TypeConverter
    public List<TodoModel> stringToList(String jsonString) {
        List<TodoModel> todoList = new ArrayList<>();
        if(jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            todoList = Arrays.asList(objectMapper.readValue(jsonString, TodoModel[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return todoList;
    }
}
