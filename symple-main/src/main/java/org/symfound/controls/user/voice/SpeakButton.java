/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.voice;

import org.symfound.controls.user.type.picto.TextCommunicatorButton;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.text.FontWeight;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class SpeakButton extends TextCommunicatorButton {

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     */
    public SpeakButton(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
        initialize();
    }

    private void initialize() {
        bindToText();
        style();
        configureFont("Roboto", FontWeight.BOLD);
        concatStyleProperty().addListener((obversable, oldValue, newValue) -> {
            style();
        });
        speakControlProperty().addListener((observeableValue, oldValue, newValue) -> {
            style();
        });
    }

    /**
     *
     */
    public void style() {
        if (!getConcatStyle().isEmpty()) {
            this.setSymStyle("");
            getPrimaryControl().getStyleClass().clear();
            getPrimaryControl().setStyle(getConcatStyle());
        } else {
            getPrimaryControl().setStyle("");
            setCSS("control-" + getSpeakControl().toString().toLowerCase(), getPrimaryControl());
        }
    }


    @Override
    public void run() {
        switch (getSpeakControl()) {
            case SPEAK:
                speak(getCommText(),GMAIL_SKIP_CHARS);
                break;
            case STOP:
                stopSpeak();
                break;
        }
    }

    /**
     *
     */
    public List<String> GMAIL_SKIP_CHARS = Arrays.asList("\\-\\-", "\\+\\+", ">", "<", "https://");

    private ChoiceBox<SpeakControl> speakControlType;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setSpeakControl(speakControlType.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        speakControlType.setValue(getSpeakControl());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRow = createSettingRow("Control Type", "Next or Previous or Toggle");
        speakControlType = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                SpeakControl.SPEAK,
                SpeakControl.STOP
        )));
        speakControlType.setValue(getSpeakControl());
        speakControlType.maxHeight(80.0);
        speakControlType.maxWidth(360.0);
        speakControlType.getStyleClass().add("settings-text-area");
        settingsRow.add(speakControlType, 1, 0, 2, 1);

        actionSettings.add(settingsRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    /**
     *
     */
    public static final int MAX_SPEAK_CHARS = 1000;

    /**
     *
     * @param fullText
     */
    public void setTrimmedSpeakText(String fullText) {
        trimSpeakText(fullText, MAX_SPEAK_CHARS);
    }

    /**
     *
     * @param fullText
     * @param maxChars
     */
    public void trimSpeakText(String fullText, Integer maxChars) {
        // speakTextProperty().bindBidirectional(getIterator().modeProperty());
        if (fullText.length() > MAX_SPEAK_CHARS) {
            setCommText(fullText.substring(0, MAX_SPEAK_CHARS));
        } else {
            setCommText(fullText);
        }
    }

  

    private static final SpeakControl DEFAULT_SPEAK_CONTROL = SpeakControl.SPEAK;
    private ObjectProperty<SpeakControl> speakControl;

    /**
     *
     * @param value
     */
    public void setSpeakControl(SpeakControl value) {
        speakControlProperty().setValue(value);
        getPreferences().put("speakControl", value.toString());
    }

    /**
     *
     * @return
     */
    public SpeakControl getSpeakControl() {
        return speakControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<SpeakControl> speakControlProperty() {
        if (speakControl == null) {
            speakControl = new SimpleObjectProperty(SpeakControl.valueOf(getPreferences().get("speakControl", DEFAULT_SPEAK_CONTROL.toString())));
        }
        return speakControl;
    }
}
