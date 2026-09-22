package util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CategoryConfig {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
    private static final TypeReference<Map<Integer, String>> REF= new TypeReference<>() {};
    private static final String URL = "categoryType.json";
    private static final Map<Integer, String> CATEGORIES = readJSON();

    public static Map<Integer, String> readJSON() {
        Map<Integer, String> categories = new HashMap<>();
        try (MappingIterator<Map<Integer, String>> mt = OBJECTMAPPER.readerFor(REF).readValues(new File(URL))) {
            while (mt.hasNext()) {
                categories.putAll(mt.next());
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось обработать JSON-файл", e);
        }
        return categories;
    }

    public static Map<Integer, String> getAllCategories() {
        return CategoryConfig.CATEGORIES;
    }

    public static boolean checkIfCategoryExists(String name) {
        return CategoryConfig.CATEGORIES.containsValue(name);
    }
}
