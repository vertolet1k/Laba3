/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
/**
 *
 * @author vika
 */
public class MonsterSource {
    private String fileName;
    private String fileType;
    private List<Monster> monsters;

    public MonsterSource(String fileName, String fileType, List<Monster> monsters) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.monsters = monsters;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    @Override
    public String toString() {
        return fileName;
    }
} 