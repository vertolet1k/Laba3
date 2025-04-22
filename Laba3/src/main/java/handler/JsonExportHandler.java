/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.MonsterSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author vika
 */
public class JsonExportHandler extends ExportHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean exportFile(MonsterSource source, File file) {
        if (file.getName().toLowerCase().endsWith(".json")) {
            try {
                Map<String, Object> root = new HashMap<>();
                root.put("bestiarum", source.getMonsters());
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Error exporting JSON file: " + e.getMessage());
            }
        } else if (nextHandler != null) {
            return nextHandler.exportFile(source, file);
        }
        return false;
    }
} 