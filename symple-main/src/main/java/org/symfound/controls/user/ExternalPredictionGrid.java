/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.List;
import javafx.application.Platform;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class ExternalPredictionGrid extends PredictionGrid {

    private static final String NAME = ExternalPredictionGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public ExternalPredictionGrid() {
        getUser().getTyping().activeTextProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                reload();
            });
        });
    }

    /**
     *
     */
    @Override
    public void configureReload() {
        getSession().builtProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1) {
                reload();
                numOfWordsProperty().addListener((observable, oldValue, newValue) -> {
                    reload();
                });
            }
        });
    }

    /**
     *
     */
    @Override
    public void fill() {
        List<String> predictions = getWords();
        resetControlsQueue();
        for (String prediction : predictions) {
            ExternalPredictionButton wordButton = new ExternalPredictionButton(prediction, "type-button-small");
            getControlsQueue().add(wordButton);
        }
    }

}
