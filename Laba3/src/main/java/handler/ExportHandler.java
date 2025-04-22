/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import model.MonsterSource;
import java.io.File;
/**
 *
 * @author vika
 */
public abstract class ExportHandler {
    protected ExportHandler nextHandler;

    public void setNextHandler(ExportHandler handler) {
        this.nextHandler = handler;
    }

    public abstract boolean exportFile(MonsterSource source, File file);
} 