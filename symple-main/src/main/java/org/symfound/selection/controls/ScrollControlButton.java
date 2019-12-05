/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.controls;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.selection.modes.Scroller;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class ScrollControlButton extends SelectionControl {

    public static final String KEY = "Scroll";
    ScrollPane scrollPane;
    Scroller scroller;

    public ScrollControlButton(Scroller scroller, ScrollPane scrollPane, ScrollControl control) {
        super("settings-button", KEY, KEY, control.toString().toLowerCase());
        this.scrollPane = scrollPane;
        this.scroller = scroller;
        initialize(control);

    }

    private void initialize(ScrollControl control) {
        setScrollControl(control);
        scroller.runScrollProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                run();
                scroller.setRunScroll(Boolean.FALSE);
            }
        });

    }

    @Override
    public void run() {
        switch (getScrollControl()) {
            case UP:
                LOGGER.info("Scrolling up");
                scrollPane.setVvalue(scrollPane.getVvalue() - 0.1);
                break;
            case DOWN:
                LOGGER.info("Scrolling down");
                scrollPane.setVvalue(scrollPane.getVvalue() + 0.1);
                break;
            case LEFT:
                LOGGER.info("Scrolling left");
                scrollPane.setHvalue(scrollPane.getHvalue() - 0.1);
                break;
            case RIGHT:
                LOGGER.info("Scrolling right");
                scrollPane.setHvalue(scrollPane.getHvalue() + 0.1);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        System.out.println("entered");
     //   scroller.start();
        
         scroller.reset();
        Double scanTime = getUser().getTiming().getScanTime();
        scroller.getLoopedEvent().setup(scanTime, (ActionEvent f) -> {
            run();
        });
        scroller.getLoopedEvent().play();

    }

    @Override
    public void mouseExit(MouseEvent e) {
        System.out.println("exited");
        scroller.reset();
    }

    @Override
    public void click() {

    }

    private ChoiceBox<ScrollControl> scrollControlType;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setScrollControl(scrollControlType.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        scrollControlType.setValue(getScrollControl());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow scrollControlRow = createSettingRow("Scroll Type", "Up, Down, Left or Right");
        scrollControlType = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                ScrollControl.UP,
                ScrollControl.DOWN,
                ScrollControl.LEFT,
                ScrollControl.RIGHT
        )));
        scrollControlType.setValue(getScrollControl());
        scrollControlType.maxHeight(80.0);
        scrollControlType.maxWidth(360.0);
        scrollControlType.getStyleClass().add("settings-text-area");
        scrollControlRow.add(scrollControlType, 1, 0, 2, 1);

        generalSettings.add(scrollControlRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    /**
     *
     */
    public static final ScrollControl DEFAULT_SCROLL_CONTROL = ScrollControl.DOWN;
    private ObjectProperty<ScrollControl> scrollControl;

    /**
     *
     * @param value
     */
    public void setScrollControl(ScrollControl value) {
        scrollControlProperty().setValue(value);
        getPreferences().put("scrollControl", value.toString());
    }

    /**
     *
     * @return
     */
    public ScrollControl getScrollControl() {
        return scrollControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ScrollControl> scrollControlProperty() {
        if (scrollControl == null) {
            scrollControl = new SimpleObjectProperty(ScrollControl.valueOf(getPreferences().get("scrollControl", DEFAULT_SCROLL_CONTROL.toString())));
        }
        return scrollControl;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ScrollControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
