/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import model.*;

import java.io.File;
/**
 *
 * @author vika
 */
public class XmlExportHandler extends ExportHandler {
    private final XmlMapper xmlMapper;

    public XmlExportHandler() {
        this.xmlMapper = XmlMapper.builder()
            .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
            .build();
    }

    @Override
    public boolean exportFile(MonsterSource source, File file) {
        if (file.getName().toLowerCase().endsWith(".xml")) {
            try {
                BestiaryWrapper wrapper = new BestiaryWrapper();
                wrapper.setMonsters(source.getMonsters());
                xmlMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, wrapper);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Error exporting XML file: " + e.getMessage(), e);
            }
        } else if (nextHandler != null) {
            return nextHandler.exportFile(source, file);
        }
        return false;
    }
} 