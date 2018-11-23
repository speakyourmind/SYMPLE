/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.log4j.Logger;
import org.symfound.tools.selection.ModeIterator;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class MediaManager<K> implements Runnable {

    private static final String NAME = MediaManager.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private List<K> defaultValues;

    public MediaManager(List<K> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public void setItems(List<K> files, Integer max) {
        List<K> iteratableFiles;

        LOGGER.info(files.size() + " items found");
        if (files.size() > max) {
            LOGGER.info("More than a 100 items found.");
            iteratableFiles = new ArrayList<>(files.subList(0, 100));
        } else {
            iteratableFiles = new ArrayList<>(files);
        }
        if (toShuffle()) {
            LOGGER.info("Shuffling files");
            Collections.shuffle(iteratableFiles);
        }
        getIterator().setTypes(iteratableFiles);
    }

    private BooleanProperty shuffle;

    /**
     *
     * @param value
     */
    public void setShuffle(Boolean value) {
        shuffleProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final Boolean toShuffle() {
        return shuffleProperty().get();
    }

    public BooleanProperty shuffleProperty() {
        if (shuffle == null) {
            shuffle = new SimpleBooleanProperty();
        }
        return shuffle;
    }

    private ModeIterator<K> iterator;

    public ModeIterator<K> getIterator() {
        if (iterator == null) {
            iterator = new ModeIterator<>(defaultValues);
        }
        return iterator;
    }
}
