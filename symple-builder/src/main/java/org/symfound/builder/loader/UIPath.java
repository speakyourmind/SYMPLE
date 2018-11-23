/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.loader;

import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.comm.file.ExtensionAnalyzer;

/**
 *
 * @author Javed Gangjee
 */
public class UIPath {

    private final String initValue;
    private final ExtensionAnalyzer extensionAnalyzer;
    private ResourceBundle rb;

    /**
     *
     * @param initValue
     */
    public UIPath(String initValue) {
        extensionAnalyzer = new ExtensionAnalyzer(initValue);
        if (!extensionAnalyzer.isScreen()) {
            initValue = extensionAnalyzer.addExtension(ExtensionAnalyzer.FXML_EXTENSION);
        }
        this.initValue = initValue;
    }

    private StringProperty fileName;

    /**
     *
     * @return
     */
    public String get() {
        return fileNameProperty().getValue();
    }

    private StringProperty fileNameProperty() {
        if (fileName == null) {
            fileName = new SimpleStringProperty(initValue);
        }
        return fileName;
    }
    private IntegerProperty difficulty;

    /**
     *
     * @param value
     */
    public void setDifficulty(Integer value) {
        difficultyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getDifficulty() {
        return difficultyProperty().getValue();
    }

    /**
     * Represents the difficulty of this UI. This is currently defined as the
     * number of maximum number of buttons in each dimension at any given time
     * while using the UI.
     * <b>NOTE:</b> This is currently set manually.
     *
     * @return
     */
    public IntegerProperty difficultyProperty() {
        if (difficulty == null) {
            difficulty = new SimpleIntegerProperty(Integer.valueOf(getBundle().getString("difficulty")));
        }
        return difficulty;
    }

    private IntegerProperty priority;

    /**
     *
     * @param value
     */
    public void setPriority(Integer value) {
        priorityProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getPriority() {
        return priorityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty priorityProperty() {
        if (priority == null) {
            priority = new SimpleIntegerProperty(Integer.valueOf(getBundle().getString("priority")));
        }
        return priority;
    }

    /**
     * Gets the <code>ResourceBundle</code> from a package name extracted from
     * the FXML file path of this UI.
     *
     * <b>IMPORTANT:</b> File must be of the same name as the fxml and placed in
     * the same folder/package.
     *
     * @return resource bundle
     */
    public ResourceBundle getBundle() {
        if (rb == null) {
            String name = extensionAnalyzer.getWithoutExtension(ExtensionAnalyzer.FXML_EXTENSION);
            String propertiesFile = name.replaceFirst("/", "").replace("/", ".").replace("\\", "");
            rb = ResourceBundle.getBundle(propertiesFile);
        }
        return rb;
    }

}
