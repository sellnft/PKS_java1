package org.example.spring_kr2.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class CategoryConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer, String> categories = new HashMap<>();

    @PostConstruct
    public void init() {
        try (InputStream is = new ClassPathResource("categoryType.json").getInputStream()) {
            TypeReference<Map<Integer, String>> typeRef = new TypeReference<>() {};
            categories = objectMapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка инициализации категорий", e);
        }
    }

    public Map<Integer, String> getAllCategories() {
        return categories;
    }

    public boolean checkIfCategoryExists(String name) {
        return categories.containsValue(name);
    }
}