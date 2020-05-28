package dev.jainchiranjeev.notes.utils;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.jainchiranjeev.notes.models.ToDoModel;

public class TodoConverters {
    @TypeConverter
    public String listToString(List<ToDoModel>todoList) {
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
    public List<ToDoModel> stringToList(String jsonString) {
        List<ToDoModel> todoList = new ArrayList<>();
        if(jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            todoList = Arrays.asList(objectMapper.readValue(jsonString, ToDoModel[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return todoList;
    }
}
