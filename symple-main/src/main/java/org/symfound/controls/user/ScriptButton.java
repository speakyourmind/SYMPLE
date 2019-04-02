/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public class ScriptButton extends TypingControl {

    /**
     *
     */
    public static final String KEY = "Script";

    /**
     *
     */
    public ScriptButton() {
        super("type-button", KEY, "-1");
    }

    /**
     *
     * @param index
     */
    public ScriptButton(String index) {
        super("type-button", KEY, index);
        initialize();
    }

    private void initialize() {
        configureStyle();

        setActionKey(getKeyCodeConfig());
        keyCodeConfigProperty().addListener((obversable, oldValue, newValue) -> {
            setActionKey(getKeyCodeConfig());
        });

    }

    @Override
    public void run() {
        super.run();
        if (isTypable()) {
            type(getPrimaryControl().getText());
        }

        UI ui = (UI) getScene().getWindow();
        if (ui.inEditMode()) {
            LOGGER.info("Exiting edit mode before navigating");
            ui.setEditMode(Boolean.FALSE);
        }
    }

    /**
     *
     * @param text
     */
    public void type(String text) {
        if (getTextArea() != null) {
            getTextArea().handle(getActionKey(), text);
        }
        if (getPictoArea() != null) {
            getPictoArea().add(this);
        }
    }

    private TextField keyCodeField;
    private OnOffButton typableButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setKeyCodeConfig(Integer.valueOf(keyCodeField.getText()));

        setTypable(typableButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        keyCodeField.setText(getKeyCodeConfig().toString());

        typableButton.setValue(isTypable());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow typableRow = createSettingRow("Typable", "Type the phrase on the button");
        typableButton = new OnOffButton("YES", "NO");
        typableButton.setMaxSize(180.0, 60.0);
        typableButton.setValue(isTypable());
        GridPane.setHalignment(typableButton, HPos.LEFT);
        GridPane.setValignment(typableButton, VPos.CENTER);

        typableRow.add(typableButton, 1, 0, 1, 1);

        keyCodeField = new TextField();
        keyCodeField.setText(getKeyCodeConfig().toString());
        keyCodeField.setMaxSize(180.0, 60.0);
        keyCodeField.getStyleClass().add("settings-text-area");
        typableRow.add(keyCodeField, 2, 0, 1, 1);

        settings.add(typableRow);
        List<Tab> tabs = super.addAppableSettings();
        return tabs;
    }

    /**
     *
     */
    public static final String DEFAULT_KEY_CODE_CONFIG = String.valueOf(ActionKeyCode.UNASSIGNED);

    /**
     *
     */
    public IntegerProperty keyCodeConfig;

    /**
     *
     * @param value
     */
    public void setKeyCodeConfig(Integer value) {
        keyCodeConfigProperty().setValue(value);
        getPreferences().put("keyCodeConfig", value.toString());

    }

    /**
     *
     * @return
     */
    public Integer getKeyCodeConfig() {
        return keyCodeConfigProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty keyCodeConfigProperty() {
        if (keyCodeConfig == null) {
            keyCodeConfig = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("keyCodeConfig", DEFAULT_KEY_CODE_CONFIG)));
        }
        return keyCodeConfig;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ScriptButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private BooleanProperty typable;

    /**
     *
     * @param value
     */
    public void setTypable(Boolean value) {
        typableProperty().set(value);
        getPreferences().put("typable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isTypable() {
        return typableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty typableProperty() {
        if (typable == null) {
            typable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("typable", "false")));
        }
        return typable;
    }

}
