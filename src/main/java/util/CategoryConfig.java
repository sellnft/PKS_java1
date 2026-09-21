package util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CategoryConfig {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
    private static final TypeReference<Map<Integer, String>> REF= new TypeReference<>() {};
    private static final String URL = "categoryType.json";

    public Map<Integer, String> readJSON() {
        Map<Integer, String> categories = new HashMap<>();
        File jsonFile = new File(CategoryConfig.URL);

        try(MappingIterator<Map<Integer, String>> it = CategoryConfig.OBJECTMAPPER.readerFor(CategoryConfig.REF).readValues(CategoryConfig.URL)) {
            while (it.hasNext()) {
                categories.putAll(it.next());
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось считать JSON", e);
        }
        return categories;
    }
}
