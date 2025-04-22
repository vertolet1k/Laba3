/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.MonsterSource;
import model.BestiaryWrapper;

import java.io.File;
/**
 *
 * @author vika
 */
public class XmlImportHandler extends ImportHandler {
    private final XmlMapper xmlMapper;

    public XmlImportHandler() {
        this.xmlMapper = XmlMapper.builder()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    }

    @Override
    public MonsterSource importFile(File file) {
        if (file.getName().toLowerCase().endsWith(".xml")) {
            try {
                BestiaryWrapper wrapper = xmlMapper.readValue(file, BestiaryWrapper.class);
                if (wrapper.getMonsters() == null || wrapper.getMonsters().isEmpty()) {
                    throw new RuntimeException("No monsters found in XML file");
                }
                return new MonsterSource(file.getName(), "XML", wrapper.getMonsters());
            } catch (Exception e) {
                throw new RuntimeException("Error importing XML file: " + e.getMessage(), e);
            }
        } else if (nextHandler != null) {
            return nextHandler.importFile(file);
        }
        return null;
    }
} 