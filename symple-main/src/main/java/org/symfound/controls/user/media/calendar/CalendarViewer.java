/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.FillableGrid;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.web.WebViewer;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class CalendarViewer extends WebViewer {

    private static final String NAME = CalendarViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Calendar Viewer";

    /**
     *
     * @param index
     */
    public CalendarViewer(String index) {
        super(KEY, index);
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");

        // addToPane(getWebView());
        addToPane(getCalendarGrid());
    }

    private FillableGrid calendarGrid;

    private FillableGrid getCalendarGrid() {
        if (calendarGrid == null) {
            calendarGrid = new FillableGrid();
        }
        return calendarGrid;
    }

    /**
     *
     */
    @Override
    public void configure() {
        getCalendarManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

        if (getIndex().contains("next")) {
            reload();
        }
        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.contains("next")) {
                reload();
            }
        });
    }
    List<CalendarEntry> entries = new ArrayList<>();

    /**
     *
     */
    @Override
    public void play() {
        Platform.runLater(() -> {
            entries = new ArrayList<>();
            setStatus(ScreenStatus.LOADING);
            // Event event = getCalendarManager().getIterator().get();
            List<Event> events = getCalendarManager().getEvents();
            if (events.isEmpty()) {
            } else {
                events.forEach((event) -> {
                    DateTime start = event.getStart().getDateTime();
                    DateTime end = event.getEnd().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    if (end == null) {
                        end = event.getEnd().getDate();
                    }
                    CalendarEntry entry = new CalendarEntry("", start,
                            end, event.getSummary());
                    entries.add(entry);
                    addToPane(entry);
                });
            }
            setStatus(ScreenStatus.PLAYING);
            addHold();
            this.toBack();
        });
    }

    /**
     *
     */
    @Override
    public void end() {
        setStatus(ScreenStatus.ENDING);
        //   getWebView().getEngine().load(null);
        removeFromParent();
        setStatus(ScreenStatus.CLOSED);

    }

    /**
     *
     */
    @Override
    public void reload() {
        setStatus(ScreenStatus.READY);
        setStatus(ScreenStatus.REQUESTED);
        Thread thread = new Thread(getCalendarManager());
        thread.start();
    }

    private CalendarManager manager;

    /**
     *
     * @return
     */
    public CalendarManager getCalendarManager() {
        if (manager == null) {
            manager = new CalendarManager();
        }

        return manager;
    }

    @Override
    public Preferences getPreferences() {
        // if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends CalendarViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        //}
        return preferences;
    }

}
