package me.sallyio.neuronutil.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ScriptHandler {
    private final File file;
    private final ObjectMapper objectMapper;
    private ObjectNode dataStore;

    public ScriptHandler(String fileName) {
        this.file = new File(fileName);
        this.objectMapper = new ObjectMapper();
        loadData();
    }

    // Load data from the file or create a new JSON structure if the file doesn't exist
    private void loadData() {
        try {
            if (file.exists()) {
                dataStore = (ObjectNode) objectMapper.readTree(file);
            } else {
                dataStore = objectMapper.createObjectNode();
                saveData();
            }
        } catch (IOException e) {
            dataStore = objectMapper.createObjectNode();
        }
    }

    // Save data to the file
    private void saveData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, dataStore);
        } catch (IOException ignored) {

        }
    }

    // Add or update a key with availability and data
    public void putEntry(String key, boolean available, String data) {
        ObjectNode entry = objectMapper.createObjectNode();
        entry.put("available", available);
        entry.put("script", data);
        dataStore.set(key, entry);
        saveData();
    }

    // Remove a key
    public void removeEntry(String key) {
        dataStore.remove(key);
        saveData();
    }

    // Retrieve an entry as a JsonNode (you can then get specific fields from it)
    public JsonNode getEntry(String key) {
        return dataStore.get(key);
    }

    // Check if a key exists
    public boolean containsKey(String key) {
        return dataStore.has(key);
    }
    // Update only the availability status for a specific key
    public void updateAvailability(String key, boolean available) {
        JsonNode entry = dataStore.get(key);
        if (entry != null && entry.isObject()) {
            ((ObjectNode) entry).put("available", available);
            saveData();
        }
    }

    // Update only the data field for a specific key
    public void updateScript(String key, String newData) {
        JsonNode entry = dataStore.get(key);
        if (entry != null && entry.isObject()) {
            ((ObjectNode) entry).put("script", newData);
            saveData();
        }
    }

    public Set<String> getAllKeys() {
        Set<String> keys = new HashSet<>();
        Iterator<String> fieldNames = dataStore.fieldNames();
        while (fieldNames.hasNext()) {
            keys.add(fieldNames.next());
        }
        return keys;
    }
}