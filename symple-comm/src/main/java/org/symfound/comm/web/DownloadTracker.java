/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.web;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Javed Gangjee
 */
public class DownloadTracker {

    private BooleanProperty complete;

    /**
     *
     * @param value
     */
    public void setComplete(Boolean value) {
        completeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isComplete() {
        return completeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty completeProperty() {
        if (complete == null) {
            complete = new SimpleBooleanProperty(false);
        }
        return complete;
    }
    private BooleanProperty begin;

    /**
     *
     * @param value
     */
    public void setBegin(Boolean value) {
        beginProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean hasBegun() {
        return beginProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty beginProperty() {
        if (begin == null) {
            begin = new SimpleBooleanProperty(false);
        }
        return begin;
    }

    private static final Double INCREMENT_PROGRESS = 0.1;
    private static final Double INITIAL_PROGRESS = 0.0;
    private DoubleProperty progress;

    /**
     *
     * @param value
     */
    public void setProgress(Double value) {
        progressProperty().setValue(value);
    }

    /**
     *
     */
    public void incrementProgress() {
        setProgress(getProgress() + INCREMENT_PROGRESS);
    }

    /**
     *
     */
    public void resetProgress() {
        setProgress(INITIAL_PROGRESS);
    }

    /**
     *
     * @return
     */
    public Double getProgress() {
        return progressProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty progressProperty() {
        if (progress == null) {
            progress = new SimpleDoubleProperty(INITIAL_PROGRESS);
        }
        return progress;
    }

}
