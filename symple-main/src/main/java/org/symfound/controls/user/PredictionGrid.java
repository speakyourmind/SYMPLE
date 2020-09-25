/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.controls.AppableControl;
import org.symfound.text.prediction.local.PredictionGenerator;

/**
 *
 * @author Javed Gangjee
 */
public class PredictionGrid extends FillableGrid {

    private static final String NAME = PredictionGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static final int DEFAULT_ROWS = 1;

    /**
     *
     */
    public static final FillMethod DEFAULT_FILL_METHOD = FillMethod.ROW_WISE;

    /**
     *
     */
    public PredictionGrid() {
        initialize();
    }

    private void initialize() {
        configureReload();
    }

    /**
     *
     */
    public void configureReload() {
        getSession().builtProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1) {
                reload();
                getTextArea().textProperty().addListener((observable, oldValue, newValue) -> {
                    reload();
                });

                numOfWordsProperty().addListener((observable, oldValue, newValue) -> {
                    reload();
                });
            }
        });
    }

    /**
     *
     */
    public void reload() {
        clear();
        fill();
        setSpecRows(DEFAULT_ROWS);
        setSpecColumns(getControlsQueue().size());
        configure(getControlsQueue(), FillMethod.ROW_WISE, FillDirection.FORWARD);
        toBack();
    }

    /**
     *
     */
    public void fill() {
        List<String> predictions = getWords();
        resetControlsQueue();
        for (String prediction : predictions) {
            PredictionButton wordButton = new PredictionButton(prediction);
            wordButton.setTextAreaID(getTextAreaID());
            getControlsQueue().add(wordButton);
        }
    }

    /**
     *
     * @param controls
     * @param method
     * @param direction
     */
    @Override
    public void configure(List<AppableControl> controls, FillMethod method, FillDirection direction) {
        build();
        setHgap(5.0);
        setVgap(5.0);
        populate(method);
    }

    /**
     *
     * @return
     */
    public List<String> getWords() {
        final Typing typing = getUser().getTyping();
        final PredictionGenerator predictionGenerator = typing.getPredictionGenerator();
        Boolean upperCase = typing.isUpperCase();
        Integer size = getNumOfWords();
        List<String> predictions = predictionGenerator.generate(typing.getActiveText(), size, upperCase);
        return predictions;
    }

    /**
     *
     */
    public ActiveTextArea textArea;

    /**
     *
     * @return
     */
    public final ActiveTextArea getTextArea() {
        if (textArea == null) {
            try {
                if (!getTextAreaID().isEmpty()) {
                    final String textAreaHash = "#" + getTextAreaID();
                    Scene scene = getScene();
                    textArea = (ActiveTextArea) scene.lookup(textAreaHash);
                }
            } catch (NullPointerException ex) {
                LOGGER.fatal("Unable to find text area with ID " + getTextAreaID(), ex);
            }

        }
        return textArea;
    }

    /**
     *
     */
    public IntegerProperty numOfWords;

    /**
     *
     * @param value
     */
    public void setNumOfWords(Integer value) {
        numOfWordsProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getNumOfWords() {
        return numOfWordsProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty numOfWordsProperty() {
        if (numOfWords == null) {
            numOfWords = new SimpleIntegerProperty();
        }
        return numOfWords;
    }

    private StringProperty textAreaID;

    /**
     *
     * @param value
     */
    public void setTextAreaID(String value) {
        textAreaIDProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getTextAreaID() {
        return textAreaIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty textAreaIDProperty() {
        if (textAreaID == null) {
            textAreaID = new SimpleStringProperty("txtMain");
        }
        return textAreaID;
    }

}
