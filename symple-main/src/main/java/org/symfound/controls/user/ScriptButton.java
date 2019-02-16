/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;

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
        if (isSpeakable()) {
            speak(getSpeakText());
        }
        if (isTypable()) {
            type(getPrimaryControl().getText());
        }

    }

    public void type(String text) {
        if (getTextArea() != null) {
            getTextArea().handle(getActionKey(), text);
        }
        if (getPictoArea() != null) {
            getPictoArea().add(this);
        }
    }

    private TextField keyCodeField;
    private OnOffButton speakableButton;
    private TextArea speakTextArea;
    private OnOffButton typableButton;

    @Override
    public void setAppableSettings() {
        setKeyCodeConfig(Integer.valueOf(keyCodeField.getText()));
        setSpeakable(speakableButton.getValue());
        setSpeakText(speakTextArea.getText());
        setTypable(typableButton.getValue());
        super.setAppableSettings();
    }

    @Override
    public void resetAppableSettings() {
        keyCodeField.setText(getKeyCodeConfig().toString());
        speakableButton.setValue(isSpeakable());
        speakTextArea.setText(getSpeakText());
        typableButton.setValue(isTypable());
        super.resetAppableSettings();
    }

    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRowA = createSettingRow("Key Code", "Code for Key Action");

        keyCodeField = new TextField();
        keyCodeField.setText(getKeyCodeConfig().toString());
        keyCodeField.prefHeight(80.0);
        keyCodeField.prefWidth(360.0);
        keyCodeField.getStyleClass().add("settings-text-area");
        settingsRowA.add(keyCodeField, 1, 0, 2, 1);

        SettingsRow speakableRow = createSettingRow("Speakable", "Say the phrase on the button");
        speakableButton = new OnOffButton("YES", "NO");
        speakableButton.setMaxSize(180.0, 60.0);
        speakableButton.setValue(isSpeakable());
        GridPane.setHalignment(speakableButton, HPos.LEFT);
        GridPane.setValignment(speakableButton, VPos.CENTER);
        speakableRow.add(speakableButton, 1, 0, 1, 1);

        speakTextArea = new TextArea();
        speakTextArea.disableProperty().bind(Bindings.not(speakableButton.valueProperty()));
        speakTextArea.setText(getSpeakText());
        GridPane.setMargin(speakTextArea, new Insets(10.0));
        speakTextArea.prefHeight(80.0);
        speakTextArea.prefWidth(360.0);
        speakTextArea.getStyleClass().add("settings-text-area");
        speakableRow.add(speakTextArea, 2, 0, 1, 1);

        SettingsRow settingsRowC = createSettingRow("Typable", "Type the phrase on the button");
        typableButton = new OnOffButton("YES", "NO");
        typableButton.setMaxSize(180.0, 60.0);
        typableButton.setValue(isTypable());
        GridPane.setHalignment(typableButton, HPos.LEFT);
        GridPane.setValignment(typableButton, VPos.CENTER);
        settingsRowC.add(typableButton, 1, 0, 1, 1);

        settings.add(settingsRowA);
        settings.add(speakableRow);
        settings.add(settingsRowC);
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

    private StringProperty speakText;

    /**
     *
     * @param value
     */
    public void setSpeakText(String value) {
        speakTextProperty().set(value);
        getPreferences().put("speakText", value);
    }

    /**
     *
     * @return
     */
    public String getSpeakText() {
        return speakTextProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty speakTextProperty() {
        if (speakText == null) {
            speakText = new SimpleStringProperty(getPreferences().get("speakText", getText()));
        }
        return speakText;
    }
}
