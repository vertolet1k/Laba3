/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import model.*;

import java.io.File;
import java.util.List;
import java.util.Map;
/**
 *
 * @author vika
 */
public class YamlImportHandler extends ImportHandler {
    private final ObjectMapper mapper;

    public YamlImportHandler() {
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public MonsterSource importFile(File file) {
        if (file.getName().toLowerCase().endsWith(".yaml") || 
            file.getName().toLowerCase().endsWith(".yml")) {
            try {
                var typeFactory = mapper.getTypeFactory();
                var listMonsterType = typeFactory.constructCollectionType(List.class, Monster.class);
                var mapType = typeFactory.constructMapType(
                    Map.class,
                    typeFactory.constructType(String.class),
                    listMonsterType
                );
                
                Map<String, List<Monster>> data = mapper.readValue(file, mapType);
                List<Monster> monsters = data.get("bestiarum");
                
                if (monsters != null) {
                    return new MonsterSource(file.getName(), "YAML", monsters);
                } else {
                    throw new RuntimeException("No monsters found in YAML file");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error importing YAML file: " + e.getMessage(), e);
            }
        } else if (nextHandler != null) {
            return nextHandler.importFile(file);
        }
        return null;
    }
} 