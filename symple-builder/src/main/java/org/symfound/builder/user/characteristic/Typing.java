/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;
import org.symfound.text.prediction.local.PredictionGenerator;

/**
 *
 * @author Javed Gangjee
 */
public class Typing extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Typing(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }
    private StringProperty activeText;

    /**
     *
     * @param value
     */
    public void setActiveText(String value) {
        activeTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getActiveText() {
        return activeTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty activeTextProperty() {
        if (activeText == null) {
            activeText = new SimpleStringProperty("");
        }
        return activeText;
    }
    
       private StringProperty pictoText;

    /**
     *
     * @param value
     */
    public void setPictoText(String value) {
        pictoTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getPictoText() {
        return pictoTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoTextProperty() {
        if (pictoText == null) {
            pictoText = new SimpleStringProperty("");
        }
        return pictoText;
    }

    private static final String AUTOCOMPLETE_KEY = "app.typing.autocomplete.enabled";
    private BooleanProperty autoComplete;

    /**
     * Sets whether the user requires autocomplete while app.typing.
     *
     * @param value true if required, false otherwise
     */
    public void setAutoComplete(Boolean value) {
        autoCompleteProperty().setValue(value);
        getPreferences().put(AUTOCOMPLETE_KEY, value.toString());
    }

    /**
     * Gets whether the user requires autocomplete while app.typing.
     *
     * @return requires if true, false otherwise
     */
    public Boolean needsAutoComplete() {
        return autoCompleteProperty().getValue();
    }

    /**
     * Represents whether the user requires autocomplete while app.typing.
     *
     * @return autocomplete
     */
    public BooleanProperty autoCompleteProperty() {
        if (autoComplete == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(AUTOCOMPLETE_KEY));
            autoComplete = new SimpleBooleanProperty(initValue);
        }
        return autoComplete;
    }

    private static final String AUTOCOMPLETE_TIME_KEY = "app.typing.autocomplete.time";
    private DoubleProperty autoCompleteTime;

    /**
     *
     * @param value
     */
    public void setAutoCompleteTime(Double value) {
        autoCompleteTimeProperty().setValue(value);
        getPreferences().put(AUTOCOMPLETE_TIME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getAutoCompleteTime() {
        return autoCompleteTimeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty autoCompleteTimeProperty() {
        if (autoCompleteTime == null) {
            Double initValue = Double.valueOf(getPreference(AUTOCOMPLETE_TIME_KEY));
            autoCompleteTime = new SimpleDoubleProperty(initValue);
        }
        return autoCompleteTime;
    }

    private static final String USER_DICT_WEIGHT_KEY = "app.typing.userDictWeight";
    private IntegerProperty dictionaryWeight;

    /**
     *
     * @param value
     */
    public void setDictionaryWeight(Integer value) {
        dictionaryWeightProperty().setValue(value);
        getPreferences().put(USER_DICT_WEIGHT_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getDictionaryWeight() {
        return dictionaryWeightProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty dictionaryWeightProperty() {
        if (dictionaryWeight == null) {
            Integer initValue = Integer.parseInt(getPreference(USER_DICT_WEIGHT_KEY).trim());
            dictionaryWeight = new SimpleIntegerProperty(initValue);
        }
        return dictionaryWeight;
    }

    private static final String UPPER_CASE_KEY = "app.typing.isUpperCase";
    private BooleanProperty upperCase;

    /**
     *
     * @param value
     */
    public void setUpperCase(Boolean value) {
        upperCaseProperty().setValue(value);
        getPreferences().put(UPPER_CASE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isUpperCase() {
        return upperCaseProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty upperCaseProperty() {
        if (upperCase == null) {
            Boolean initValue = Boolean.parseBoolean(getPreference(UPPER_CASE_KEY));
            upperCase = new SimpleBooleanProperty(initValue);
        }
        return upperCase;
    }

    /**
     *
     */
    public static final String PHRASE_DELIMITER = ",";
    private ObjectProperty<PredictionGenerator> predictionGenerator;

    /**
     *
     * @param value
     */
    public void setPredictionGenerator(PredictionGenerator value){
        predictionGeneratorProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public PredictionGenerator getPredictionGenerator(){
        return predictionGeneratorProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<PredictionGenerator> predictionGeneratorProperty() {
        if (predictionGenerator == null) {
            PredictionGenerator initValue;
            Properties configuration = getDefaultConfiguration();
            String[] dictsArr = configuration.getProperty("app.typing.dicts").split(PHRASE_DELIMITER);
            // User dictionary for custom words/frequency, updated automatically
            String userDict = configuration.getProperty("app.typing.userDict");
            final Integer userWeight = getDictionaryWeight();
            // Set default suggestions (when text area has trailing space)
            String[] defaultsArr = configuration.getProperty("app.typing.defaultSuggestions").split(PHRASE_DELIMITER);
            initValue = new PredictionGenerator(dictsArr, false, false, true, 0, userDict, userWeight);
            initValue.setDefaultSuggestions(defaultsArr);
            predictionGenerator = new SimpleObjectProperty<>(initValue);
        }
        return predictionGenerator;
    }

}