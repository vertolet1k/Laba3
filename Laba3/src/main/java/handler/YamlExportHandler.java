/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import model.MonsterSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author vika
 */
public class YamlExportHandler extends ExportHandler {
    private final ObjectMapper mapper;

    public YamlExportHandler() {
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public boolean exportFile(MonsterSource source, File file) {
        if (file.getName().toLowerCase().endsWith(".yaml") || 
            file.getName().toLowerCase().endsWith(".yml")) {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("bestiarum", source.getMonsters());
                mapper.writeValue(file, data);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Error exporting YAML file: " + e.getMessage());
            }
        } else if (nextHandler != null) {
            return nextHandler.exportFile(source, file);
        }
        return false;
    }
} 
