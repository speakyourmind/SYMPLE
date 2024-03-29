/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.comm.file.PathReader;
import org.symfound.comm.file.PathWriter;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;
import org.symfound.main.HomeController;
import org.symfound.text.TextAnalyzer;
import org.symfound.text.TextOperator;
import static org.symfound.text.TextOperator.EOL;
import org.symfound.text.prediction.local.Predictor;
import static org.symfound.text.prediction.local.Predictor.SPACE;

import org.symfound.voice.main.TTSLauncher;

/**
 *
 * @author Javed Gangjee
 */
public class ActiveTextArea extends AppableControl {

    private static final String NAME = ActiveTextArea.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private static final Boolean DEFAULT_WRAP_TEXT = Boolean.TRUE;
    //Autosave variables
    /**
     *
     */
    public static String savePath;// To Do: Remove

    /**
     *
     */
    public static String readPath;// To Do: Remove

    /**
     *
     */
    public static Boolean backspaceMode = Boolean.FALSE;

    /**
     *
     */
    public static final String KEY = "Main Text";
    public static final String DESCRIPTION = "Typing Area";

    /**
     *
     */
    public ActiveTextArea() {
        super("", KEY, "", "default");
        initialize();
    }

    private void initialize() {
        savePath = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Saved/";
        PathWriter savePathWriter = new PathWriter(savePath);
        savePathWriter.file.mkdirs();

        readPath = savePath + "Current.txt";
        PathWriter currentPathWriter = new PathWriter(readPath);
        try {
            currentPathWriter.create();
        } catch (IOException ex) {
            LOGGER.fatal("Unable to create working file path", ex);
        }

        StringProperty activeTextProperty = getUser().getTyping().activeTextProperty();

        setText(getUser().getTyping().getActiveText());
        activeTextProperty.bindBidirectional(textProperty());
        loadCurrentText();
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (savePath != null && readPath != null) {
                getTextOperator().autoSave(savePath, readPath);
            }
        });
        setCSS("main-text", get());
        get().requestFocus();
        configureStyle("Roboto", FontWeight.NORMAL);
        setSelectable(false);

        handleScratchpad();

        /*   scratchpadProperty().addListener((observable, oldValue, newValue) -> {
        handleScratchpad();
        });*/
    }

    public void handleScratchpad() {
        if (getScratchpad().actionKey != 0) {
            handle(getScratchpad().actionKey, getScratchpad().text);
            setScratchpad(new KeyAction(0, ""));
        }
    }

    private void loadCurrentText() {
        try {
            PathReader fileReader = new PathReader(readPath);
            String fileText = fileReader.getFileText();
//           getUser().getTyping().setActiveText(fileText);
            get().setText(fileText);
            get().positionCaret(get().getLength());
        } catch (IOException ex) {
            LOGGER.fatal("Could not load active text value", ex);
        }
    }
    private TextOperator textOperator;

    /**
     *
     * @return
     */
    public final TextOperator getTextOperator() {
        if (textOperator == null) {
            textOperator = new TextOperator(get());
        }
        return textOperator;
    }

    private static Predictor predictor;

    /**
     *
     * @return
     */
    public static final Predictor getPredictor() {
        if (predictor == null) {
            predictor = new Predictor(get());
        }
        return predictor;
    }

    /**
     * Represents the text on the root <code>AnimatedButton</code>
     *
     * @return <code>AnimatedButton</code> textProperty
     */
    @Override
    public StringProperty textProperty() {
        return get().textProperty();
    }

    /**
     *
     * @param actionKeyCode
     * @param srcText
     */
    public void handle(Integer actionKeyCode, String srcText) {
        /*    if (getUser().getTyping().needsAutoComplete()) {
        if (srcText.length() > 1) {
        //Select the previous word in order to read the length
        deletePreviousWord();
        }
        }*/

        switch (actionKeyCode) {
            case ActionKeyCode.ENTER:
                // Must delete caret before autocomplete, append & save
                getTextOperator().appendLetter(EOL, getUser().getTyping().isUpperCase());
                break;
            case ActionKeyCode.BLANK:
                type("");
                break;
            case ActionKeyCode.BACK_SPACE:
                backspace();
                break;
            case ActionKeyCode.SPACE:
                space();
                break;
            case ActionKeyCode.CLEAR:
                setText(" ");
                break;
            default:
                get().replaceSelection("");
                type(srcText);
                backspaceMode = Boolean.FALSE;
                break;
        }
    }

    /**
     *
     */
    public void backspace() {
        // Deletes selected letters in case of autocomplete
        get().replaceSelection("");
        if (backspaceMode) {
            get().deletePreviousChar();
            deletePreviousWord();
        } else {
            get().deletePreviousChar();
        }
    }

    /**
     *
     */
    public void deletePreviousWord() {
        // Select previous word
        get().selectPreviousWord();
        // Delete the selected previous word
        get().replaceSelection("");
    }

    /**
     *
     */
    public void space() {
        if (get().getSelectedText().length() > 0) {
            backspaceMode = Boolean.TRUE;
        } else {
            backspaceMode = Boolean.FALSE;
        }
        getSession().setMutex(false);
        // Add space to mainText area
        get().appendText(" ");
        // Add auto completed word
        addAutocompleteWord();
    }

    /**
     *
     * @param addTxt
     */
    public void type(String addTxt) {
        Typing typing = getUser().getTyping();
        getTextOperator().appendLetter(addTxt, typing.isUpperCase());
        TextAnalyzer textAnalyzer = new TextAnalyzer(addTxt);
        // If the added text is a period or a phrase
        if (textAnalyzer.isSentence()) {
            typing.activeTextProperty().concat(SPACE);
            // Attempts to update user dictionary
            getPredictor().updateUserDictionary();
            TTSLauncher ttsLauncher = getSession().getTTSManager().getLauncher();
            String voice = getUser().getSpeech().getSpeakingVoice();
            //Speak using MaryTTS
            ttsLauncher.getCurrentPlayer().play(true, voice);
        } else {
            addAutocompleteWord();
        }
    }

    private void addAutocompleteWord() {
        Typing typing = getUser().getTyping();
        if (typing.needsAutoComplete()) {
            Platform.runLater(() -> {
                Double autoCompleteTime = typing.getAutoCompleteTime();
                getPredictor().setAutoComplete(typing.getPredictionGenerator(),
                        autoCompleteTime, typing.isUpperCase());
                backspaceMode = Boolean.FALSE;
            });
        }
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        addToPane(get());
    }

    private static TextArea textArea;

    /**
     *
     * @return
     */
    public static TextArea get() {
        if (textArea == null) {
            textArea = new TextArea();
            //setCSS("main-text", textArea);
            textArea.setWrapText(DEFAULT_WRAP_TEXT);
        }
        return textArea;
    }

    @Override
    public void run() {

    }

    /**
     *
     * @return
     */
    @Override
    public EditAppButton getEditAppButton() {
        if (editAppButton == null) {
            EditDialog editDialog = new EditDialog("Edit Text Area") {
                private TextArea overrideStyleField;
                private TextField rowExpand;
                private TextField columnExpand;

                @Override
                public void setSettings() {
                    setOverrideStyle(overrideStyleField.getText());
                    setRowExpand(Integer.valueOf(rowExpand.getText()));
                    setColumnExpand(Integer.valueOf(columnExpand.getText()));
                }

                @Override
                public void resetSettings() {
                    overrideStyleField.setText(getOverrideStyle());
                    rowExpand.setText(String.valueOf(getRowExpand()));
                    columnExpand.setText(String.valueOf(getColumnExpand()));
                }

                @Override
                public BuildableGrid addSettingControls() {
                    SettingsRow settingsRow3 = createSettingRow("Style", "CSS Style code");

                    overrideStyleField = new TextArea();
                    overrideStyleField.setStyle("-fx-font-size:1.6em;");
                    overrideStyleField.setText(getOverrideStyle());
                    overrideStyleField.maxHeight(80.0);
                    overrideStyleField.maxWidth(360.0);
                    overrideStyleField.getStyleClass().add("settings-text-area");
                    settingsRow3.add(overrideStyleField, 1, 0, 2, 1);

                    SettingsRow settingsRow5 = createSettingRow("Expand button", "Row x Column");

                    rowExpand = new TextField();
                    rowExpand.setText(getRowExpand().toString());
                    rowExpand.maxHeight(80.0);
                    rowExpand.maxWidth(60.0);
                    rowExpand.getStyleClass().add("settings-text-area");
                    settingsRow5.add(rowExpand, 1, 0, 1, 1);

                    columnExpand = new TextField();
                    columnExpand.setText(getColumnExpand().toString());
                    columnExpand.maxHeight(80.0);
                    columnExpand.maxWidth(60.0);
                    columnExpand.getStyleClass().add("settings-text-area");
                    settingsRow5.add(columnExpand, 2, 0, 1, 1);

                    List<SettingsRow> settings = new ArrayList<>();
                    settings.add(settingsRow3);
                    settings.add(settingsRow5);
                    BuildableGrid grid = buildSettingsGrid(settings);

                    return grid;
                }

            };

            editAppButton = new EditAppButton(editDialog);
            editAppButton.setPane("apMain");
        }
        return editAppButton;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }

    /**
     *
     */
    @Override
    public void configureStyle(String fontFamily, FontWeight fw) {
        updateStyle(fontFamily, fw);
        overrideStyleProperty().addListener((obversable1, oldValue1, newValue1) -> {
            updateStyle(fontFamily, fw);
        });
    }

    public void updateStyle(String fontFamily, FontWeight fw) {
        if (!getOverrideStyle().isEmpty()) {
            LOGGER.info("Setting style for " + getKey() + "." + getIndex() + " to " + getOverrideStyle());
            this.setSymStyle("");
            get().getStyleClass().clear();
            get().setStyle(getOverrideStyle());
        } else {
            setCSS("main-text", get());
            get().setStyle("");
        }
    }

    private static ObjectProperty<KeyAction> scratchpad;

    /**
     *
     * @param value
     */
    public static void setScratchpad(KeyAction value) {
        scratchpadProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public static KeyAction getScratchpad() {
        return scratchpadProperty().getValue();
    }

    /**
     *
     * @return
     */
    public static ObjectProperty<KeyAction> scratchpadProperty() {
        if (scratchpad == null) {
            scratchpad = new SimpleObjectProperty<>(new KeyAction(0, ""));
        }
        return scratchpad;
    }

}
