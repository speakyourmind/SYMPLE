/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.symfound.builder.loader.Loader;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class Builder {

    private static final String NAME = Builder.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private final DelayedEvent startTimeline = new DelayedEvent();
    private final ThreadPoolExecutor executor;

    /**
     *
     */
    public Builder() {
        executor = new ThreadPoolExecutor(3, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    /**
     *
     * @param timeout
     */
    public void start(Double timeout) {
        startTimeline.setup(timeout, (ActionEvent e) -> {
            LOGGER.fatal("Build timed out");
            LOGGER.fatal("Jobs remaining: " + getExecutor().getPoolSize());
        });
        startTimeline.play();
    }

    /**
     *
     * @return
     */
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /**
     *
     * @return
     */
    public Duration getTimeElapsed() {
        return startTimeline.get().getCurrentTime();
    }

    /**
     *
     * @param loader
     */
    public void addLoader(Loader loader) {
        executor.execute(loader);
    }

    /**
     *
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException {
        executor.shutdown();
    }

    /**
     *
     */
    public void end() {
        startTimeline.end();
    }

    // Build Constants

    /**
     *
     */
    public static final Double INCREMENT_PROGRESS = 0.02;

    /**
     *
     */
    public static final Double INITIAL_PROGRESS = 0.4;

    /**
     *
     */
    public DoubleProperty progress;

    /**
     *
     * @return
     */
    public Double getProgress() {
        return progressProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setProgress(Double value) {
        progressProperty().setValue(value);
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

    /**
     *
     */
    public void incrementProgress() {
        setProgress(getProgress() + INCREMENT_PROGRESS);
    }

}
