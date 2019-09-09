package org.symfound.main.settings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Navigation;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.characteristic.Speech;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.comm.file.PathReader;
import org.symfound.comm.file.PathWriter;
import org.symfound.controls.system.OnOffButton;
import org.symfound.main.FullSession;
import org.symfound.controls.user.voice.TTSManager;
import org.symfound.main.builder.UI;
import org.symfound.tools.iteration.ModeIterator;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class AppSettingsController extends SettingsControllerBase {

    private static final String NAME = AppSettingsController.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    @FXML
    private AnchorPane apMain;

    /**
     *
     */
    @FXML
    public Button btnUpdateSettings;

    /**
     *
     */
    @FXML
    public Button btnHomeFolder;

    /**
     *
     */
    @FXML
    public TextField homeFolderField;
    @FXML
    private Slider timeoutField;
    @FXML
    private Slider autoCompleteTimeField;

    /**
     *
     */
    @FXML
    public GridPane gpTier;

    /**
     *
     */
    @FXML
    public GridPane gpMain;
    @FXML
    private Slider userDictWeightField;
    @FXML
    private OnOffButton btnAutocomplete;
    @FXML
    private OnOffButton btnOnFirstClick;
    @FXML
    private OnOffButton btnSpeakSelection;
    @FXML
    private OnOffButton btnHighlightBorder;
    @FXML
    private OnOffButton btnFullScreen;
    @FXML
    private OnOffButton btnPlayScourSound;

    /**
     *
     */
    @FXML
    public TextField playScourField;
    @FXML
    private OnOffButton btnPlaySelectSound;

    @FXML
    public TextField accountSIDField;
    @FXML
    public TextField authTokenField;
    @FXML
    public TextField fromNumberField;
    /**
     *
     */
    @FXML
    public TextField playSelectionField;

    /**
     *
     */
    // @FXML
    //public TextField appBuildOrderField;
    /**
     *
     */
    @FXML
    public TextField ttsBuildOrderField;

    /**
     *
     */
    @FXML
    public TextField buttonMapField;
    @FXML
    private Slider volumeField;
    @FXML
    private ChoiceBox<String> cbReadingVoice;
    @FXML
    private ChoiceBox<String> cbVoice;

    /**
     *
     */
    public ModeIterator<String> fileChooser;

    /**
     *
     */
    public List<String> savedFiles;

    @FXML
    private void changeDirectory(MouseEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(btnHomeFolder.getScene().getWindow());
        if (selectedDirectory == null) {
            homeFolderField.setText(getUser().getContent().getHomeFolder());
        } else {
            homeFolderField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void cancelSettings(MouseEvent e) {
        cancelSettings();
    }

    @FXML
    private void updateSettings(MouseEvent e) throws IOException {
        updateSettings();
    }

    /**
     * Set the values of the preference fields to current variable values.
     */
    @Override
    public void resetSettings() {
        LOGGER.info("Resetting Application Settings");
        currentFilePath = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Current.txt";
        txtCurrent.textProperty().bind(getUser().getTyping().activeTextProperty());
        //getUser().getTyping().activeTextProperty().bindBidirectional(txtCurrent.textProperty());
        // NAVIGATION
        final Navigation navigation = getUser().getNavigation();
        timeoutField.setValue(navigation.getTimeout());
//        appBuildOrderField.setText(navigation.getAppBuildOrder().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""));
        ttsBuildOrderField.setText(navigation.getTTSBuildOrder().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""));

        //      menuOrderField.setText(navigation.getMenuOrder().asString());
        buttonMapField.setText(navigation.getButtonMap().toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(" ", ""));

        btnOnFirstClick.setValue(navigation.onFirstClick());
        btnSpeakSelection.setValue(navigation.speakSelection());
        btnHighlightBorder.disableProperty().bind(btnFullScreen.valueProperty());
        btnHighlightBorder.setValue(navigation.highlightBorder());
        btnFullScreen.setValue(navigation.fullScreen());
        btnPlayScourSound.setValue(navigation.playScourSound());
        playScourField.setText(navigation.getScourSound());
        btnPlaySelectSound.setValue(navigation.playSelectSound());
        playSelectionField.setText(navigation.getSelectionSound());
        // SPEAKING
        TTSManager ttsPlayer = getSession().getTTSManager();
        cbVoice.setItems(FXCollections.observableArrayList(ttsPlayer.getLauncher().getVoiceNames()));
        final Speech speech = getUser().getSpeech();
        cbVoice.setValue(speech.getSpeakingVoice());
        cbVoice.valueProperty().addListener((observalueValue, oldValue, newValue) -> {
            if (newValue != null) {
                ttsPlayer.getIterator().set(newValue);
            }
        });
        // TYPING
        final Typing communication = getUser().getTyping();
        btnAutocomplete.setValue(communication.needsAutoComplete());
        autoCompleteTimeField.setValue(communication.getAutoCompleteTime());
        userDictWeightField.setValue(communication.getDictionaryWeight());

        // MEDIA
        homeFolderField.setText(getUser().getContent().getHomeFolder());
        volumeField.setValue(getUser().getInteraction().getVolume() * 10.0);

        accountSIDField.setText(getUser().getSocial().getTwilioAccountSID());
        authTokenField.setText(getUser().getSocial().getTwilioAuthToken());
        fromNumberField.setText(getUser().getSocial().getTwilioFromNumber());

    }

    /**
     *
     */
    @Override
    public void setSettings() {
        LOGGER.info("Setting Main Settings");
        // NAVIGATION
        final Navigation navigation = getUser().getNavigation();
        navigation.setTimeout(timeoutField.getValue());
        //navigation.setAppBuildOrder(FXCollections.observableArrayList(Arrays.asList(appBuildOrderField.getText().split(BUTTON_DELIMITER))));
        navigation.setTTSBuildOrder(FXCollections.observableArrayList(Arrays.asList(ttsBuildOrderField.getText().split(BUTTON_DELIMITER))));
        //navigation.setButtonOrder(FXCollections.observableArrayList(Arrays.asList(buttonOrderField.getText().split(BUILD_DELIMITER))));
        ParallelList<String, String> parallelList = new ParallelList<>();
        /*        String[] menuPairs = menuOrderField.getText().split(BUTTON_DELIMITER);
        for (String pair : menuPairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            parallelList.put(keyValue[0], keyValue[1]);
        }
        navigation.setMenuOrder(parallelList);*/
        Map<String, Integer> buttonHashMap = new HashMap<>();
        String[] pairs = buttonMapField.getText().split(BUTTON_DELIMITER);
        for (String pair : pairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            buttonHashMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
        }
        navigation.setButtonMap(buttonHashMap);

        navigation.setOnFirstClick(btnOnFirstClick.getValue());
        navigation.setSpeakSelection(btnSpeakSelection.getValue());
        navigation.setHighlightBorder(btnHighlightBorder.getValue());
        navigation.setFullScreen(btnFullScreen.getValue());
        navigation.setPlayScourSound(btnPlayScourSound.getValue());
        navigation.setScourSound(playScourField.getText());
        navigation.setPlaySelectSound(btnPlaySelectSound.getValue());

        navigation.setSelectionSound(playSelectionField.getText());
        // SPEAKING
        final Speech speech = getUser().getSpeech();
        speech.setSpeakingVoice(cbVoice.getValue());
        LOGGER.info("Voice set to " + getUser().getSpeech().getSpeakingVoice());

        // TYPING
        final Typing communication = getUser().getTyping();
        communication.setAutoComplete(btnAutocomplete.getValue());
        communication.setAutoCompleteTime(autoCompleteTimeField.getValue());
        communication.setDictionaryWeight((int) userDictWeightField.getValue());

        // MEDIA
        getUser().getContent().setHomeFolder(homeFolderField.getText());
        getUser().getInteraction().setVolume(volumeField.getValue() * 0.1);

        //SOCIAL
        getUser().getSocial().setTwilioAccountSID(accountSIDField.getText());
        getUser().getSocial().setTwilioAuthToken(authTokenField.getText());
        getUser().getSocial().setTwilioFromNumber(fromNumberField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        loadFileNames();
        resetSettings();
        playScourField.visibleProperty().bind(btnPlayScourSound.valueProperty());
        playSelectionField.visibleProperty().bind(btnPlaySelectSound.valueProperty());
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                FullSession.getMainUI().getStack().currentProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1.get().contains(FullSession.APP_SETTINGS)) {
                        resetSettings();
                    }
                });
            }
        });

        cbSavedFiles.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                fileChooser.set(newValue.intValue());
                final String homeFolder = getUser().getContent().getHomeFolder();
                final String path = homeFolder + "/Documents/SYMPLE/" + fileChooser.get();
                try {
                    PathReader fileReader = new PathReader(path);
                    String previewText = fileReader.getFileText();
                    if (previewText != null) {
                        txtPreview.setText(previewText);
                    }
                } catch (IOException ex) {
                    LOGGER.fatal("Unable to load preview text from " + path, ex);
                }
            }
        });

    }

    /**
     *
     */
    public String currentFilePath;

    @FXML
    private TextArea txtPreview;
    @FXML
    private TextArea txtCurrent;
    @FXML
    private ChoiceBox<String> cbSavedFiles;
    @FXML
    private TextArea txtRename;

    @FXML
    private void deleteFile(MouseEvent e) throws IOException {
        PathWriter pathWriter = new PathWriter(getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/" + fileChooser.get());
        pathWriter.deleteFile();
        //TODO: Need to add popup confirmation.
        loadFileNames();
        PathReader pathReader = new PathReader(getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/" + fileChooser.get());
        txtPreview.setText(pathReader.getFileText());

    }

    @FXML
    private void setFile(MouseEvent e) throws IOException {
        setWorkingFile();
        showFileChooser(false);
    }

    @FXML
    private void clearFile(MouseEvent e) throws IOException {
        PathWriter currentPathWriter = new PathWriter(currentFilePath);
        currentPathWriter.deleteFile();
        currentPathWriter.create();
        PathReader currentPathReader = new PathReader(currentFilePath);
        getUser().getTyping().setActiveText(currentPathReader.getFileText());
    }

    /**
     *
     * @throws IOException
     */
    private void setWorkingFile() throws IOException {
        PathReader pathReader = new PathReader(getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/" + fileChooser.get());
        String setText = pathReader.getFileText();

        PathWriter currentPathWriter = new PathWriter(currentFilePath);
        currentPathWriter.deleteFile();
        currentPathWriter.writeToFile(setText, false, true);

        PathReader currentPathReader = new PathReader(currentFilePath);
        txtCurrent.setText(currentPathReader.getFileText());

    }

    @FXML
    private void launchFileChooser(MouseEvent e) {
        showFileChooser(true);
    }

    @FXML
    private void hideFileChooser(MouseEvent e) {
        showFileChooser(false);
    }

    /**
     *
     * @param isShown
     */
    public void showFileChooser(Boolean isShown) {
        if (gpTier != null) {
            // Hide the top tier
            gpTier.setVisible(isShown);

        }
        if (gpMain != null) {
            // Show the main screen
            gpMain.setDisable(isShown);
        }

        if (isShown) {
            gpTier.toFront();
        } else {
            gpTier.toBack();
        }
    }

    /**
     *
     */
    public void loadFileNames() {
        savedFiles = new ArrayList<>();
        PathReader savedFileManager = new PathReader(getUser().getContent().getHomeFolder() + "/save/");
        List<String> allFiles = savedFileManager.getFolderFileNames();
        allFiles.stream().forEach((fileName) -> {
            ExtensionAnalyzer txtChecker = new ExtensionAnalyzer(fileName);
            Boolean isFile = !fileName.contains("Current") && txtChecker.isTextFile();
            if (isFile) {
                savedFiles.add(fileName);

            }
        });

        fileChooser = new ModeIterator<>(savedFiles);
        if (savedFiles.size() > 0) {
            fileChooser.set(savedFiles.get(0));
            cbSavedFiles.setItems(FXCollections.observableArrayList(savedFiles));
            cbSavedFiles.setValue(savedFiles.get(0));

        }
    }

    private static final List<String> SHORTCUT_TYPES = Arrays.asList(
            "SHIFT", "TAB", "ALT", "CTRL");

    private List<String> shortcuts;

    private List<String> getShortcuts() {
        if (shortcuts == null) {
            shortcuts = new ArrayList<>(SHORTCUT_TYPES);
        }
        return shortcuts;
    }
}
