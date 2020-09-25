/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class Statistics extends Characteristic {

    private static final String NAME = Statistics.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    public Statistics(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }
    Preferences savedStats;

    public final Preferences getSavedStats() {
        if (savedStats == null) {
            String name = "statistics";
            savedStats = Preferences.userNodeForPackage(this.getClass()).node(name);

        }
        return preferences;
    }
    private static final String TOTAL_SESSIONS_KEY = "statistics.global.sessions";
    private IntegerProperty sessionCount;

    /**
     *
     * @param value
     */
    public void setTotalSessionCount(Integer value) {
        totalSessionCountProperty().set(value);
        updatePreferences(TOTAL_SESSIONS_KEY, value.toString());
    }

    public void incrementTotalSessionCount() {
        setTotalSessionCount(getTotalSessionCount() + 1);
    }

    public void resetTotalSessionCount() {
        setTotalSessionCount(0);
    }

    public Integer getTotalSessionCount() {
        return totalSessionCountProperty().get();
    }

    public IntegerProperty totalSessionCountProperty() {
        if (sessionCount == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_SESSIONS_KEY, "0"));
            sessionCount = new SimpleIntegerProperty(initValue);
        }
        return sessionCount;
    }

    private static final String TOTAL_SELECTIONS_KEY = "statistics.global.selections";
    private IntegerProperty selectionCount;

    /**
     *
     * @param value
     */
    public void setTotalSelectionCount(Integer value) {
        totalSelectionCountProperty().set(value);
        updatePreferences(TOTAL_SELECTIONS_KEY, value.toString());
    }

    public void incrementTotalSelectionCount() {
        setTotalSelectionCount(getTotalSelectionCount() + 1);
    }

    public void resetTotalSelectionCount() {
        setTotalSelectionCount(0);
    }

    public Integer getTotalSelectionCount() {
        return totalSelectionCountProperty().get();
    }

    public IntegerProperty totalSelectionCountProperty() {
        if (selectionCount == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_SELECTIONS_KEY, "0"));
            selectionCount = new SimpleIntegerProperty(initValue);
        }
        return selectionCount;
    }

    private static final String TOTAL_SPOKEN_WORDS_KEY = "statistics.global.wordsSpoken";
    private IntegerProperty spokenWordCount;

    /**
     *
     * @param value
     */
    public void setTotalSpokenWordsCount(Integer value) {
        totalSpokenWordsCountProperty().set(value);
        updatePreferences(TOTAL_SPOKEN_WORDS_KEY, value.toString());
    }

    public void incrementTotalSpokenWordsCount() {
        setTotalSpokenWordsCount(getTotalSpokenWordsCount() + 1);
    }

    public void resetTotalSpokenWordsCount() {
        setTotalSpokenWordsCount(0);
    }

    public Integer getTotalSpokenWordsCount() {
        return totalSpokenWordsCountProperty().get();
    }

    public IntegerProperty totalSpokenWordsCountProperty() {
        if (spokenWordCount == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_SPOKEN_WORDS_KEY, "0"));
            spokenWordCount = new SimpleIntegerProperty(initValue);
        }
        return spokenWordCount;
    }

    private static final String TOTAL_TIME_KEY = "statistics.global.time";
    private IntegerProperty timeInUse;

    /**
     *
     * @param value
     */
    public void setTotalTimeInUse(Integer value) {
        totalTimeInUseProperty().set(value);
        updatePreferences(TOTAL_TIME_KEY, value.toString());
    }

    public void incrementTotalTimeInUse(Integer seconds) {
        setTotalTimeInUse(getTotalTimeInUse() + seconds);
    }

    public void resetTotalTimeInUse() {
        setTotalTimeInUse(0);
    }

    public Integer getTotalTimeInUse() {
        return totalTimeInUseProperty().get();
    }

    public IntegerProperty totalTimeInUseProperty() {
        if (timeInUse == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_TIME_KEY, "0"));
            timeInUse = new SimpleIntegerProperty(initValue);
        }
        return timeInUse;
    }

    private static final String SESSION_START_KEY = "statistics.session.startTime";
    private LongProperty sessionStartTime;

    public void setSessionStartTime(Long value) {
        sessionStartTimeProperty().set(value);
        updatePreferences(SESSION_START_KEY, value.toString());
    }

    public Long getSessionStartTime() {
        return sessionStartTimeProperty().get();
    }

    public LongProperty sessionStartTimeProperty() {
        if (sessionStartTime == null) {
            sessionStartTime = new SimpleLongProperty(Long.valueOf(loadPreference(SESSION_START_KEY, "0")));
        }
        return sessionStartTime;
    }

    private static final String LAST_USED_KEY = "statistics.global.lastUsed";
    private LongProperty lastUsed;

    public void setLastUsed(Long value) {
        lastUsedProperty().set(value);
        updatePreferences(LAST_USED_KEY, value.toString());
    }

    public void resetLastUsed() {
        setLastUsed(0L);
    }

    public Long getLastUsed() {
        return lastUsedProperty().get();
    }

    public LongProperty lastUsedProperty() {
        if (lastUsed == null) {
            lastUsed = new SimpleLongProperty(Long.valueOf(loadPreference(LAST_USED_KEY, String.valueOf(System.currentTimeMillis()))));
        }
        return lastUsed;
    }

    private static final String SESSION_TIME_IN_USE_KEY = "statistics.session.timeInUse";
    private IntegerProperty sessionTimeInUse;

    /**
     *
     * @param value
     */
    public void setSessionTimeInUse(Integer value) {
        sessionTimeInUseProperty().set(value);
        updatePreferences(SESSION_TIME_IN_USE_KEY, value.toString());
    }

    public void incrementSessionTimeInUse(Integer seconds) {
        setSessionTimeInUse(getSessionTimeInUse() + seconds);
    }

    public void resetSessionTimeInUse() {
        setSessionTimeInUse(0);
    }

    public Integer getSessionTimeInUse() {
        return sessionTimeInUseProperty().get();
    }

    public IntegerProperty sessionTimeInUseProperty() {
        if (sessionTimeInUse == null) {
            Integer initValue = Integer.valueOf(loadPreference(SESSION_TIME_IN_USE_KEY, "0"));
            sessionTimeInUse = new SimpleIntegerProperty(initValue);
        }
        return sessionTimeInUse;
    }

    private static final String TOTAL_SESSION_WORDS_KEY = "statistics.session.wordsSpoken";
    private IntegerProperty sessionWordCount;

    /**
     *
     * @param value
     */
    public void setSessionSpokenWordCount(Integer value) {
        sessionSpokenWordCountProperty().set(value);
        updatePreferences(TOTAL_SESSION_WORDS_KEY, value.toString());
    }

    public void incrementSessionSpokenWordCount() {
        setSessionSpokenWordCount(getSessionSpokenWordCount() + 1);
    }

    public void resetSessionSpokenWordCount() {
        setSessionSpokenWordCount(0);
    }

    public Integer getSessionSpokenWordCount() {
        return sessionSpokenWordCountProperty().get();
    }

    public IntegerProperty sessionSpokenWordCountProperty() {
        if (sessionWordCount == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_SESSION_WORDS_KEY, "0"));
            sessionWordCount = new SimpleIntegerProperty(initValue);
        }
        return sessionWordCount;
    }

    private static final String TOTAL_SESSION_SELECTIONS_KEY = "statistics.session.selections";
    private IntegerProperty sessionSelections;

    /**
     *
     * @param value
     */
    public void setSessionSelections(Integer value) {

        sessionSelectionsProperty().set(value);
        updatePreferences(TOTAL_SESSION_WORDS_KEY, value.toString());
    }

    public void incrementSessionSelections() {
        setSessionSelections(getSessionSelections() + 1);
    }

    public void resetSessionSelections() {
        setSessionSelections(0);
    }

    public Integer getSessionSelections() {
        return sessionSelectionsProperty().get();
    }

    public IntegerProperty sessionSelectionsProperty() {
        if (sessionSelections == null) {
            Integer initValue = Integer.valueOf(loadPreference(TOTAL_SESSION_SELECTIONS_KEY, "0"));
            sessionSelections = new SimpleIntegerProperty(initValue);
        }
        return sessionSelections;
    }

    private static final String RECORD_KEY = "statistics.record";
    private BooleanProperty record;

    /**
     *
     * @param value
     */
    public void setRecord(Boolean value) {
        recordProperty().setValue(value);
        updatePreferences(RECORD_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isRecording() {
        return recordProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty recordProperty() {
        if (record == null) {
            Boolean initValue = Boolean.valueOf(loadPreference(RECORD_KEY, "true"));
            record = new SimpleBooleanProperty(initValue);
        }
        return record;
    }

    public void resetAllStats() {
        resetTotalSessionCount();
        resetTotalSpokenWordsCount();
        resetTotalTimeInUse();
        resetTotalSelectionCount();
        resetSessionStats();
    }

    public void resetSessionStats() {
        resetSessionTimeInUse();
        resetSessionSpokenWordCount();
        resetSessionSelections();
    }

    public void updatePreferences(String node, String value) {
        try {
            final Preferences prefs = getPreferences();
            if (prefs.nodeExists(node)) {
                prefs.put(node, value);
            }
        } catch (BackingStoreException ex) {
            LOGGER.warn("Node " + node + " does not exist in registry");
        } catch(IllegalStateException ex){
            LOGGER.warn("Node " + node + " may have been removed.");
        }
    }

    public String loadPreference(String key, String defaultValue){
        String value=defaultValue;
        try {
            final Preferences prefs = getPreferences();
            if (prefs.nodeExists(key)) {
                 value = prefs.get(key, defaultValue);
            }
        } catch (BackingStoreException ex) {
            LOGGER.warn("Node " + key + " does not exist in registry");
        } catch(IllegalStateException ex){
            LOGGER.warn("Node " + key + " may have been removed.");
        }
        return value;
    }
}
