/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.gmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.media.MediaManager;
import org.symfound.social.google.gmail.GmailReader;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class GMailManager extends MediaManager<String> {

    private static final String NAME = GMailManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public GMailManager() {
        super(Arrays.asList(""));
    }

    @Override
    public void run() {
        List<String> messages = retrieveMessages(getFilter(),10);
        LOGGER.info(messages.size() + " messages retrieved");
        getIterator().setTypes(messages);
    }

    /**
     *
     * @param filter
     * @param max
     * @return
     */
    public List<String> retrieveMessages(String filter, Integer max) {
        List<String> messages = new ArrayList<>();
        try {
            messages = GmailReader.getMessages(filter,max);
        } catch (IOException ex) {
            LOGGER.fatal(ex);
        }
        return messages;
    }

    private StringProperty filter;

    /**
     *
     * @param value
     */
    public void setFilter(String value) {
        filterProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getFilter() {
        return filterProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty filterProperty() {
        if (filter == null) {
            filter = new SimpleStringProperty();
        }
        return filter;
    }
}
