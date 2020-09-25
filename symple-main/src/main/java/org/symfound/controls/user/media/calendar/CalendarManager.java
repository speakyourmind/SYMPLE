/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.symfound.media.MediaManager;
import org.symfound.social.google.calendar.GoogleCalendar;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class CalendarManager extends MediaManager<Event> {

    private static final String NAME = CalendarManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public CalendarManager() {
        super(Arrays.asList(new Event()));
    }

    @Override
    public void run() {
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime to = new DateTime(System.currentTimeMillis()+86400000);
        try {
            setEvents(GoogleCalendar.load(now));
        } catch (IOException | GeneralSecurityException ex) {
            LOGGER.fatal(ex);
        }
        setItems(getEvents(), 100);
    }

    private ListProperty<Event> events;

    /**
     *
     * @param value
     */
    public void setEvents(ObservableList<Event> value) {
        eventsProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final List<Event> getEvents() {
        return eventsProperty().get();
    }

    /**
     *
     * @return
     */
    public ListProperty<Event> eventsProperty() {
        if (events == null) {
            events = new SimpleListProperty<>();
        }
        return events;
    }

}
