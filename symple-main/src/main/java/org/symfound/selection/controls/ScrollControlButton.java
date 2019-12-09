/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.controls;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.selection.modes.Scroller;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class ScrollControlButton extends SelectionControl {

    public static final String KEY = "Scroll";
    private final ScrollPane scrollPane;
    private final Scroller scroller;

    public ScrollControlButton(Scroller scroller, ScrollPane scrollPane, ScrollControl control) {
        super("selector", KEY, KEY, control.toString().toLowerCase());
        this.scrollPane = scrollPane;
        this.scroller = scroller;
        initialize(control);

    }


    private void initialize(ScrollControl control) {

        setCSS("selector-" + getScrollControl().toString().toLowerCase(), getPrimaryControl());
        scrollControlProperty().addListener((observeableValue, oldValue, newValue) -> {
            setCSS("selector-" + getScrollControl().toString().toLowerCase(), getPrimaryControl());

        });
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
        final Double scrollDistance = getUser().getNavigation().getScrollDistance();
        switch (getScrollControl()) {
            case UP:
                if (scrollPane.getVvalue() != 0.0) {
                    //   setVisible(Boolean.TRUE);
                    scrollPane.setVvalue(scrollPane.getVvalue() - scrollDistance);
                    LOGGER.info("Scrolling up");
                } else {
                    LOGGER.info("Reached the end of the vertical scroll bar.");
                    // setVisible(Boolean.FALSE);
                }
                break;
            case DOWN:
                if (scrollPane.getVvalue() != 1.0) {
                    // setVisible(Boolean.TRUE);
                    scrollPane.setVvalue(scrollPane.getVvalue() + scrollDistance);
                    LOGGER.info("Scrolling down");
                } else {
                    LOGGER.info("Reached the end of the vertical scroll bar.");
                    //setVisible(Boolean.FALSE);
                }
                break;
            case LEFT:
                if (scrollPane.getHvalue() != 0.0) {
                    //setVisible(Boolean.TRUE);
                    scrollPane.setHvalue(scrollPane.getHvalue() - scrollDistance);
                    LOGGER.info("Scrolling left");
                } else {
                    LOGGER.info("Reached the end of the horizontal scroll bar.");
                    scroller.reset();
                    //setVisible(Boolean.FALSE);
                }
                break;
            case RIGHT:
                if (scrollPane.getHvalue() != 1.0) {
                    //setVisible(Boolean.TRUE);
                    scrollPane.setHvalue(scrollPane.getHvalue() + scrollDistance);
                    LOGGER.info("Scrolling right");
                } else {
                    LOGGER.info("Reached the end of the horizontal scroll bar.");
                    //setVisible(Boolean.FALSE);
                    scroller.reset();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEnter(MouseEvent e) {
        scroller.reset();
        Double scanTime = getUser().getTiming().getScrollTime();
        scroller.getLoopedEvent().setup(scanTime, (ActionEvent f) -> {
            run();
        });
        scroller.getLoopedEvent().play();

    }

    @Override
    public void mouseExit(MouseEvent e) {
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
