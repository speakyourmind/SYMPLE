/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.gmail;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import static org.symfound.controls.AppableControl.LOGGER;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaViewer;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class GMailViewer extends MediaViewer {

    /**
     *
     */
    public static final String KEY = "GMail";

    /**
     *
     * @param index
     */
    public GMailViewer(String index) {
        super("background", KEY, "", index);
        initialize();
    }

    private void initialize() {
        getGMailManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

        reload();
        filterProperty().addListener((observable, oldValue, newValue) -> {
            reload();
        });
        statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            setDisable(!newValue1.equals(ScreenStatus.PLAYING));
        });
        configureStyle();
    }

    /**
     *
     */
    @Override
    public void reload() {
        setStatus(ScreenStatus.READY);
        LOGGER.info("Reloading GMail data with filter:" + getFilter());
        getGMailManager().setFilter(getFilter());
        setStatus(ScreenStatus.REQUESTED);
        Thread thread = new Thread(getGMailManager());
        thread.start();
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        addToPane(getTextArea());
    }

    /**
     *
     */
    @Override
    public void configureStyle() {
        updateStyle();
        overrideStyleProperty().addListener((obversable1, oldValue1, newValue1) -> {
            updateStyle();
        });
    }

    /**
     *
     */
    public void updateStyle() {
        if (!getOverrideStyle().isEmpty()) {
            LOGGER.info("Setting style for " + getKey() + "." + getIndex() + " to " + getOverrideStyle());
            this.setSymStyle("");
            getTextArea().getStyleClass().clear();
            getTextArea().setStyle(getOverrideStyle());
        } else {
            setCSS("main-text", getTextArea());
            getTextArea().setStyle("");
        }
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends GMailViewer> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private TextArea text;

    /**
     *
     * @return
     */
    public TextArea getTextArea() {
        if (text == null) {
            text = new TextArea();
            text.setMaxHeight(Double.POSITIVE_INFINITY);
            text.setMaxWidth(Double.POSITIVE_INFINITY);
            text.setWrapText(true);
        }

        return text;
    }

    // TODO: Commonize
    private TextArea filterField;
    private TextField rowExpandField;
    private TextField columnExpandField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setFilter(filterField.getText());
        setOverrideStyle(overrideStyleField.getText());
        setSelectable(selectableButton.getValue());
        setRowExpand(Integer.valueOf(rowExpandField.getText()));
        setColumnExpand(Integer.valueOf(columnExpandField.getText()));
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        filterField.setText(getFilter());
        overrideStyleField.setText(getOverrideStyle());
        selectableButton.setValue(isSelectable());
        rowExpandField.setText(String.valueOf(getRowExpand()));
        columnExpandField.setText(String.valueOf(getColumnExpand()));
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRow = createSettingRow("Filter", "Mail to be loaded");
        filterField = new TextArea();
        filterField.setText(getFilter());
        filterField.maxHeight(80.0);
        filterField.maxWidth(360.0);
        filterField.getStyleClass().add("settings-text-area");
        settingsRow.add(filterField, 1, 0, 2, 1);

        SettingsRow settingsRow3 = createSettingRow("Style", "CSS Style code");

        overrideStyleField = new TextArea();
        overrideStyleField.setStyle("-fx-font-size:1.6em;");
        overrideStyleField.setText(getOverrideStyle());
        overrideStyleField.maxHeight(80.0);
        overrideStyleField.maxWidth(360.0);
        overrideStyleField.getStyleClass().add("settings-text-area");
        settingsRow3.add(overrideStyleField, 1, 0, 2, 1);

        SettingsRow settingsRow4 = createSettingRow("Selectable", "Skip scanning process?");

        selectableButton = new OnOffButton("YES", "NO");
        selectableButton.setMaxSize(180.0, 60.0);
        selectableButton.setValue(isSelectable());
        GridPane.setHalignment(selectableButton, HPos.LEFT);
        GridPane.setValignment(selectableButton, VPos.CENTER);
        settingsRow4.add(selectableButton, 1, 0, 1, 1);

        SettingsRow settingsRow5 = createSettingRow("Expand button", "Row x Column");

        rowExpandField = new TextField();
        rowExpandField.setText(getRowExpand().toString());
        rowExpandField.maxHeight(80.0);
        rowExpandField.maxWidth(60.0);
        rowExpandField.getStyleClass().add("settings-text-area");
        settingsRow5.add(rowExpandField, 1, 0, 1, 1);

        columnExpandField = new TextField();
        columnExpandField.setText(getColumnExpand().toString());
        columnExpandField.maxHeight(80.0);
        columnExpandField.maxWidth(60.0);
        columnExpandField.getStyleClass().add("settings-text-area");
        settingsRow5.add(columnExpandField, 2, 0, 1, 1);

        settings.add(settingsRow);
        settings.add(settingsRow3);
        settings.add(settingsRow4);
        settings.add(settingsRow5);

        Tab generalTab = buildTab("GENERAL", settings);

        List<Tab> tabs = new ArrayList<>();
        tabs.add(generalTab);
        
        return tabs;
    }

    private static GMailManager manager;

    /**
     *
     * @return
     */
    public static GMailManager getGMailManager() {
        if (manager == null) {
            manager = new GMailManager();
        }

        return manager;
    }

    private StringProperty filter;

    /**
     *
     * @param value
     */
    public void setFilter(String value) {
        filterProperty().set(value);
        getPreferences().put("filter", value);
    }

    /**
     *
     * @return
     */
    public String getFilter() {
        return filterProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty filterProperty() {
        if (filter == null) {
            filter = new SimpleStringProperty(getPreferences().get("filter", "in:sent after:2018/04/06"));
        }
        return filter;
    }

    /**
     *
     */
    @Override
    public void play() {
        setStatus(ScreenStatus.LOADING);
        final String current = getGMailManager().getIterator().get();
        LOGGER.info("Current message length:" + current.length() + " with text:" + current);
        String cleanedMessage = current.trim().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
        LOGGER.info("Current message cleaned up :" + cleanedMessage.length() + " with text:" + current);
        if (cleanedMessage.isEmpty()) {
            LOGGER.warn("Message is empty. Attempting to get next message");
            getGMailManager().getIterator().next();
            play();
        } else {
            LOGGER.info("Current message is not empty. Updating text area to: " + current);
            Platform.runLater(() -> {
                getTextArea().setText(current);
                setStatus(ScreenStatus.PLAYING);
                toBack();
            });
            addHold();
        }
    }

    /**
     *
     */
    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
