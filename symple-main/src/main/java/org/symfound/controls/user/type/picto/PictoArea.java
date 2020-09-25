/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.type.picto;

import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.grid.editor.ReplaceKeyButton;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.ScriptButton;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed
 */
public class PictoArea extends AppableControl {

    /**
     *
     */
    public static final String NAME = PictoArea.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Area";

    public PictoArea(String index) {
        super("picto-area", KEY, "", index);
        initialize();
    }

    private void initialize() {
        addToPane(getConfigurableGrid());
        speakableProperty().bindBidirectional(selectableProperty());
        setSpeakable(false);
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        primary.setWrapText(Boolean.TRUE);
        load(primary);
        setCSS("transparent", primary);
        setSelection(primary);
    }

    @Override
    public void run() {
        if (isSpeakable()) {
            updatePictoText();
            speak(getPictoText());

        }
        if (autoClear()) {
            clear();
        }
    }

    public void clear() {
        final ParallelList<String, String> order = new ParallelList<>();
        order.getFirstList().add("Replace Key");
        order.getSecondList().add("default");

        getConfigurableGrid().setOrder(order);
        getConfigurableGrid().reload();
        setPictoText("");
    }

    /**
     *
     * @param button
     */
    public void add(ScriptButton button) {
        final ConfigurableGrid configurableGrid = getConfigurableGrid();
        final ParallelList<String, String> order = configurableGrid.getOrder();
        order.getFirstList().add(ScriptButton.KEY);
        order.getSecondList().add(button.getIndex());
        configurableGrid.setOrder(order);

        if (configurableGrid.getOrder().getFirstList().contains("None")) {
            configurableGrid.getOrder().remove("None");
        }
        if (configurableGrid.getOrder().getFirstList().contains(ReplaceKeyButton.KEY)) {
            configurableGrid.getOrder().remove(ReplaceKeyButton.KEY);
        }
        configurableGrid.getGridManager().setOverrideColumn(order.size().doubleValue()+1.0);
        configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
        configurableGrid.triggerReload();
    }

    private ConfigurableGrid pictoGrid;

    /**
     *
     * @return
     */
    public ConfigurableGrid getConfigurableGrid() {
        if (pictoGrid == null) {
            pictoGrid = new ConfigurableGrid();
            pictoGrid.setIndex(getIndex());
           // pictoGrid.setMinDifficulty(10.0);
            pictoGrid.getGridManager().setOverrideRow(1.0);
            pictoGrid.getGridManager().setOverrideColumn(10.0);
            pictoGrid.configure();
            pictoGrid.setMaxWidth(this.getWidth());
            pictoGrid.setDisable(true);
          
            setCSS("picto-area", pictoGrid);
        }
        return pictoGrid;
    }

    private StringProperty pictoText;

    /**
     *
     */
    public void updatePictoText() {
        String text = "";
        final int size = getConfigurableGrid().getChildren().size();
        for (int i = 0; i < size; i++) {
            Node node = getConfigurableGrid().getChildren().get(i);
            if (node instanceof AppableControl) {
                AppableControl button = (AppableControl) node;
                final String currentButtonText = button.getSpeakText();
                text = text + currentButtonText + " ";
            } else {
                LOGGER.warn("Unreadable item in picto area");
            }
        }
        setPictoText(text);
    }

    /**
     *
     * @param value
     */
    public void setPictoText(String value) {
        pictoTextProperty().set(value);
        getPreferences().put("text", value);
    }

    /**
     *
     * @return
     */
    public String getPictoText() {
        return pictoTextProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoTextProperty() {
        if (pictoText == null) {
            pictoText = new SimpleStringProperty(getPreferences().get("text", ""));
        }
        return pictoText;
    }
    private BooleanProperty autoClear;

    /**
     *
     * @param value
     */
    public void setAutoClear(Boolean value) {
        autoClearProperty().set(value);
        getPreferences().put("autoClear", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean autoClear() {
        return autoClearProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty autoClearProperty() {
        if (autoClear == null) {
            autoClear = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("autoClear", "false")));
        }
        return autoClear;
    }

    private OnOffButton autoClearButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setAutoClear(autoClearButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        autoClearButton.setValue(autoClear());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow autoClearRow = createSettingRow("Auto Clear", "Clear the text when the area is clicked");
        autoClearButton = new OnOffButton("YES", "NO");
        autoClearButton.setMaxSize(180.0, 60.0);
        autoClearButton.setValue(autoClear());
        GridPane.setHalignment(autoClearButton, HPos.LEFT);
        GridPane.setValignment(autoClearButton, VPos.CENTER);

        autoClearRow.add(autoClearButton, 1, 0, 1, 1);

        generalSettings.add(autoClearRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
