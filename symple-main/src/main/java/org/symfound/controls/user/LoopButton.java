/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import org.symfound.tools.iteration.ModeIterator;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * TO DO: Not Tested. Incomplete. Do not use.
 */
public class LoopButton extends RunnableControl {

    private static final String NAME = LoopButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private StringProperty textConfig;
    private StringProperty styleConfig;
    private List<String> textList = new ArrayList<>();

    /**
     *
     */
    public LoopButton() {
        super();
    }

    /**
     *
     */
    public final void initialize() {
        textConfigProperty().addListener((observable, oldValue, newValue) -> {
            textList = Arrays.asList(newValue.split(","));
            ModeIterator<String> textIterator = new ModeIterator<>(textList);
            getPrimaryControl().textProperty().bindBidirectional(textIterator.modeProperty());

            if (textList.size() > 1) {
                LoopedEvent loopedEvent = new LoopedEvent();
                loopedEvent.setup(getUser().getTiming().getScanTime(), (ActionEvent e) -> {
                    textIterator.next();
                });

                loopedEvent.play();
            } else {
                LOGGER.fatal("ERROR");
            }
        });

    }

    @Override

    public void run() {

    }

    /**
     *
     * @param value
     */
    public void setTextConfig(String value) {
        textConfigProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getTextConfig() {
        return textConfigProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty textConfigProperty() {
        if (textConfig == null) {
            textConfig = new SimpleStringProperty();
        }
        return textConfig;
    }

    /**
     *
     * @param value
     */
    public void setStyleConfig(String value) {
        styleConfigProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getStyleConfig() {
        return styleConfigProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty styleConfigProperty() {
        if (styleConfig == null) {
            styleConfig = new SimpleStringProperty();
        }
        return styleConfig;
    }

}
