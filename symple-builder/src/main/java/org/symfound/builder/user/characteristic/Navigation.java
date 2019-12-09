/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import org.symfound.builder.characteristic.Characteristic;
import org.symfound.text.TextOperator;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class Navigation extends Characteristic {

    /**
     *
     */
    public static final String BUTTON_DELIMITER = ",";

    /**
     *
     */
    public static final String KEY_DELIMITER = "=";

    /**
     *
     * @param preferences
     * @param properties
     */
    public Navigation(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String BUTTON_MAP_KEY = "app.navigation.buttonMap";
    private ObjectProperty<Map<String, Integer>> buttonMap;

    /**
     *
     * @param value
     */
    public void setButtonMap(Map<String, Integer> value) {
        buttonMapProperty().setValue(value);
        getPreferences().put(BUTTON_MAP_KEY, value.toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(" ", ""));
    }

    /**
     *
     * @return
     */
    public Map<String, Integer> getButtonMap() {
        return buttonMapProperty().getValue();
    }

    /**
     *
     * @return
     */
    public Map<String, Integer> getDefaultButtonMap() {
        final String buttonConfig = getPreference(BUTTON_MAP_KEY);
        Map<String, Integer> buttonHashMap = new HashMap<>();
        String[] pairs = buttonConfig.split(BUTTON_DELIMITER);
        for (String pair : pairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            buttonHashMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
        }
        return buttonHashMap;
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Map<String, Integer>> buttonMapProperty() {
        if (buttonMap == null) {
            Map<String, Integer> list = getDefaultButtonMap();
            buttonMap = new SimpleObjectProperty<>(list);
        }
        return buttonMap;
    }

    private static final String MENU_ORDER_KEY = "app.navigation.menuOrder";
    private ObjectProperty<ParallelList<String, String>> menuOrder;

    /**
     *
     * @param value
     */
    public void setMenuOrder(ParallelList<String, String> value) {
        menuOrderProperty().setValue(value);
        String storedValue = "";
        for (int i = 0; i < value.size(); i++) {
            storedValue = storedValue + value.getFirstList().get(i) + "=" + value.getSecondList().get(i) + BUTTON_DELIMITER;
        }
        getPreferences().put(MENU_ORDER_KEY, storedValue);
    }

    /**
     *
     * @return
     */
    public ParallelList<String, String> getMenuOrder() {
        return menuOrderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ParallelList<String, String> getDefaultMenuOrder() {
        final String menuConfig = getPreference(MENU_ORDER_KEY);
        ParallelList<String, String> parallelList = new ParallelList<>();
        String[] pairs = menuConfig.split(BUTTON_DELIMITER);
        for (String pair : pairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            parallelList.add(keyValue[0], keyValue[1]);
        }

        return parallelList;
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ParallelList<String, String>> menuOrderProperty() {
        if (menuOrder == null) {
            ParallelList<String, String> list = getDefaultMenuOrder();
            menuOrder = new SimpleObjectProperty<>(list);
        }
        return menuOrder;
    }

    /**
     *
     *
     * public static final String BUILD_ORDER_KEY =
     * "app.navigation.appBuildOrder"; private ListProperty<String>
     * appBuildOrder;
     *
     * public void setAppBuildOrder(ObservableList<String> value) {
     * appBuildOrderProperty().setValue(value);
     * getPreferences().put(BUILD_ORDER_KEY,
     * value.toString().replaceAll("[\\[\\]]", "").replaceAll(" ", ""));
     *
     * }
     * public ObservableList<String> getAppBuildOrder() { return
     * appBuildOrderProperty().getValue(); } public ListProperty<String>
     * appBuildOrderProperty() { if (appBuildOrder == null) { TextOperator
     * textOperator = new TextOperator(); final String buildConfig =
     * getPreference(BUILD_ORDER_KEY); final List<String> buildList =
     * textOperator.splitAsList(buildConfig, BUTTON_DELIMITER);
     * ObservableList<String> list =
     * FXCollections.observableArrayList(buildList); appBuildOrder = new
     * SimpleListProperty<>(list); } return appBuildOrder; }
     */
    /**
     *
     */
    public static final String TTS_BUILD_ORDER_KEY = "app.navigation.ttsBuildOrder";
    private ListProperty<String> ttsBuildOrder;

    /**
     *
     * @param value
     */
    public void setTTSBuildOrder(ObservableList<String> value) {
        ttsBuildOrderProperty().setValue(value);
        getPreferences().put(TTS_BUILD_ORDER_KEY, value.toString().replaceAll("[\\[\\]]", "").replaceAll(" ", ""));

    }

    /**
     *
     * @return
     */
    public ObservableList<String> getTTSBuildOrder() {
        return ttsBuildOrderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ListProperty<String> ttsBuildOrderProperty() {
        if (ttsBuildOrder == null) {
            TextOperator textOperator = new TextOperator();
            final String buildConfig = getPreference(TTS_BUILD_ORDER_KEY);
            final List<String> buildList = textOperator.splitAsList(buildConfig, BUTTON_DELIMITER);
            ObservableList<String> list = FXCollections.observableArrayList(buildList);
            ttsBuildOrder = new SimpleListProperty<>(list);
        }
        return ttsBuildOrder;
    }

    private static final String SHOW_TITLE_KEY = "app.navigation.showlabel";
    private BooleanProperty showTitle;

    /**
     *
     * @param value
     */
    public void setShowTitle(Boolean value) {
        showTitleProperty().setValue(value);
        getPreferences().put(SHOW_TITLE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean showTitle() {
        return showTitleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty showTitleProperty() {
        if (showTitle == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(SHOW_TITLE_KEY));
            showTitle = new SimpleBooleanProperty(initValue);
        }
        return showTitle;
    }

    private static final String TIMEOUT_KEY = "app.general.timeout";
    private DoubleProperty timeout;

    /**
     *
     * @param value
     */
    public void setTimeout(Double value) {
        timeoutProperty().setValue(value);
        getPreferences().put(TIMEOUT_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getTimeout() {
        return timeoutProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty timeoutProperty() {
        if (timeout == null) {
            Double initValue = Double.valueOf(getPreference(TIMEOUT_KEY));
            timeout = new SimpleDoubleProperty(initValue);
        }
        return timeout;
    }
    private static final String PLAY_SELECT_SOUND_KEY = "app.navigation.playSound";
    private BooleanProperty playSelectSound;

    /**
     * Sets whether the user requires autocomplete while app.typing.
     *
     * @param value true if required, false otherwise
     */
    public void setPlaySelectSound(Boolean value) {
        playSelectSoundProperty().setValue(value);
        getPreferences().put(PLAY_SELECT_SOUND_KEY, value.toString());
    }

    /**
     * Gets whether the user requires autocomplete while app.typing.
     *
     * @return requires if true, false otherwise
     */
    public Boolean playSelectSound() {
        return playSelectSoundProperty().getValue();
    }

    /**
     * Represents whether the user requires autocomplete while app.typing.
     *
     * @return autocomplete
     */
    public BooleanProperty playSelectSoundProperty() {
        if (playSelectSound == null) {
            Boolean initValue = Boolean.parseBoolean(this.getPreference(PLAY_SELECT_SOUND_KEY, "false"));
            playSelectSound = new SimpleBooleanProperty(initValue);
        }
        return playSelectSound;
    }

    private static final String PLAY_SCOUR_SOUND_KEY = "app.navigation.playSound";
    private BooleanProperty playScourSound;

    /**
     * Sets whether the user requires autocomplete while app.typing.
     *
     * @param value true if required, false otherwise
     */
    public void setPlayScourSound(Boolean value) {
        playScourSoundProperty().setValue(value);
        getPreferences().put(PLAY_SCOUR_SOUND_KEY, value.toString());
    }

    /**
     * Gets whether the user requires autocomplete while app.typing.
     *
     * @return requires if true, false otherwise
     */
    public Boolean playScourSound() {
        return playScourSoundProperty().getValue();
    }

    /**
     * Represents whether the user requires autocomplete while app.typing.
     *
     * @return autocomplete
     */
    public BooleanProperty playScourSoundProperty() {
        if (playScourSound == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(PLAY_SCOUR_SOUND_KEY, "false"));
            playScourSound = new SimpleBooleanProperty(initValue);
        }
        return playScourSound;
    }

    private static final String SPEAK_SELECTION_KEY = "app.navigation.speakSelection";
    private BooleanProperty speakSelection;

    /**
     * Sets whether the user requires autocomplete while app.typing.
     *
     * @param value true if required, false otherwise
     */
    public void setSpeakSelection(Boolean value) {
        speakSelectionProperty().setValue(value);
        getPreferences().put(SPEAK_SELECTION_KEY, value.toString());
    }

    /**
     * Gets whether the user requires autocomplete while app.typing.
     *
     * @return requires if true, false otherwise
     */
    public Boolean speakSelection() {
        return speakSelectionProperty().getValue();
    }

    /**
     * Represents whether the user requires autocomplete while app.typing.
     *
     * @return autocomplete
     */
    public BooleanProperty speakSelectionProperty() {
        if (speakSelection == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(SPEAK_SELECTION_KEY));
            speakSelection = new SimpleBooleanProperty(initValue);
        }
        return speakSelection;
    }

    private static final String FULL_SCREEN_KEY = "app.navigation.fullScreen";
    private BooleanProperty fullScreen;

    /**
     *
     * @param value
     */
    public void setFullScreen(Boolean value) {
        fullScreenProperty().setValue(value);
        getPreferences().put(FULL_SCREEN_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean fullScreen() {
        return fullScreenProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty fullScreenProperty() {
        if (fullScreen == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(FULL_SCREEN_KEY));
            fullScreen = new SimpleBooleanProperty(initValue);
        }
        return fullScreen;
    }

    private static final String HIGHLIGHT_BORDER_KEY = "app.navigation.highlightBorder";
    private BooleanProperty highlightBorder;

    /**
     *
     * @param value
     */
    public void setHighlightBorder(Boolean value) {
        highlightBorderProperty().setValue(value);
        getPreferences().put(HIGHLIGHT_BORDER_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean highlightBorder() {
        return highlightBorderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty highlightBorderProperty() {
        if (highlightBorder == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(HIGHLIGHT_BORDER_KEY));
            highlightBorder = new SimpleBooleanProperty(initValue);
        }
        return highlightBorder;
    }

    private static final String SCOUR_SOUND_KEY = "app.general.scourSound";
    private StringProperty scourSound;

    /**
     *
     * @param value
     */
    public void setScourSound(String value) {
        scourSoundProperty().setValue(value);
        getPreferences().put(SCOUR_SOUND_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getScourSound() {
        return scourSoundProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty scourSoundProperty() {
        if (scourSound == null) {
            String initValue = getPreferences().get(SCOUR_SOUND_KEY, "C:\\Windows\\media\\Windows Navigation Start.wav");
            scourSound = new SimpleStringProperty(initValue);
        }
        return scourSound;
    }

    private static final String SELECTION_SOUND_KEY = "app.general.selectionSound";
    private StringProperty selectionSound;

    /**
     *
     * @param value
     */
    public void setSelectionSound(String value) {
        selectionSoundProperty().setValue(value);
        getPreferences().put(SELECTION_SOUND_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSelectionSound() {
        return selectionSoundProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty selectionSoundProperty() {
        if (selectionSound == null) {
            String initValue = getPreferences().get(SELECTION_SOUND_KEY, "C:\\Windows\\media\\Windows Default.wav");
            selectionSound = new SimpleStringProperty(initValue);
        }
        return selectionSound;
    }

    private static final String ON_FIRST_CLICK_KEY = "app.navigation.onFirstClick";
    private BooleanProperty onFirstClick;

    /**
     * Sets whether the user requires autocomplete while app.typing.
     *
     * @param value true if required, false otherwise
     */
    public void setOnFirstClick(Boolean value) {
        onFirstClickProperty().setValue(value);
        getPreferences().put(ON_FIRST_CLICK_KEY, value.toString());
    }

    /**
     * Gets whether the user requires autocomplete while app.typing.
     *
     * @return requires if true, false otherwise
     */
    public Boolean onFirstClick() {
        return onFirstClickProperty().getValue();
    }

    /**
     * Represents whether the user requires autocomplete while app.typing.
     *
     * @return autocomplete
     */
    public BooleanProperty onFirstClickProperty() {
        if (onFirstClick == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(ON_FIRST_CLICK_KEY));
            onFirstClick = new SimpleBooleanProperty(initValue);
        }
        return onFirstClick;
    }

    private static final String PREVIOUS_INDEX_KEY = "app.general.previousIndex";
    private StringProperty previousIndex;

    /**
     *
     * @param value
     */
    public void setPreviousIndex(String value) {
        previousIndexProperty().setValue(value);
        getPreferences().put(PREVIOUS_INDEX_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getPreviousIndex() {
        return previousIndexProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty previousIndexProperty() {
        if (previousIndex == null) {
            String initValue = getPreferences().get(PREVIOUS_INDEX_KEY, "home");
            previousIndex = new SimpleStringProperty(initValue);
        }
        return previousIndex;
    }
    private static final String SCROLL_DISTANCE_KEY = "app.navigation.scrollDistance";
    private DoubleProperty scrollDistance;

    /**
     *
     * @param value
     */
    public void setScrollDistance(Double value) {
        scrollDistanceProperty().setValue(value);
        getPreferences().put(SCROLL_DISTANCE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getScrollDistance() {
        return scrollDistanceProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty scrollDistanceProperty() {
        if (scrollDistance == null) {
            Double initValue = Double.valueOf(getPreference(SCROLL_DISTANCE_KEY));
            scrollDistance = new SimpleDoubleProperty(initValue);
        }
        return scrollDistance;
    }

    private static final String CURRENT_INDEX_KEY = "app.general.previousIndex";
    private StringProperty currentIndex;

    /**
     *
     * @param value
     */
    public void setCurrentIndex(String value) {
        currentIndexProperty().setValue(value);
        getPreferences().put(CURRENT_INDEX_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getCurrentIndex() {
        return currentIndexProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty currentIndexProperty() {
        if (currentIndex == null) {
            String initValue = getPreferences().get(CURRENT_INDEX_KEY, "home");
            currentIndex = new SimpleStringProperty(initValue);
        }
        return currentIndex;
    }

    private static final String CURSOR_TYPE_KEY = "app.navigation.cursorType";
    private ObjectProperty<Cursor> cursorType;

    /**
     *
     * @param value
     */
    public void setCursor(Cursor value) {
        cursorTypeProperty().setValue(value);
        getPreferences().put(CURSOR_TYPE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Cursor getCursor() {
        return cursorTypeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Cursor> cursorTypeProperty() {
        if (cursorType == null) {
            Cursor initValue = Cursor.cursor(getPreference(CURSOR_TYPE_KEY));
            cursorType = new SimpleObjectProperty<>(initValue);
        }
        return cursorType;
    }
}
