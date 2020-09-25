/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.mouse;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;
import org.symfound.builder.session.Display;
import org.symfound.device.emulation.input.InputAutomator;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee
 */
public class MouseAutomator extends Robot implements InputAutomator {

    private static final String NAME = MouseAutomator.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final int ACTION_DELAY = 50;

    /**
     *
     */
    public static final int LONG_CLICK_DELAY = 50;

    /**
     *
     */
    public static final int SHORT_CLICK_DELAY = 5;

    /**
     *
     */
    public static final int SCROLL_MAX = 50;

    /**
     *
     */
    public static final int LEFT_BUTTON = InputEvent.BUTTON1_MASK;

    /**
     *
     */
    public static final int RIGHT_BUTTON = InputEvent.BUTTON1_MASK;

    /**
     *
     */
    public ZoomUI zoom;

    /**
     *
     * @throws AWTException
     */
    public MouseAutomator() throws AWTException {
    }

    // NAVIGATION
    /**
     *
     * @param nav
     */
    public void navigate(Point nav) {

        mouseMove((int) nav.getX(), (int) nav.getY());
    }

    /**
     *
     * @param nav
     */
    public void resetMousePos(Point nav) {
        if (nav != null) {
            // Set the mouse to the original click position in case it has moved
            mouseMove((int) nav.getX(), (int) nav.getY());
            // Short delay after moving before clicking
            delay(ACTION_DELAY);
        }

    }

    // SELECTION
    /**
     *
     * @param nav
     * @param scale
     * @param size
     * @throws AWTException
     */
    public void runClickSelect(Point nav, Double scale, Integer size) throws AWTException {
        switch (getSelectionType()) {
            case MouseSelectionType.OFF:
                // Can be used to turn off the device's coordinates.
                break;
            case MouseSelectionType.LEFT:
                // Do nothing as it is assumed Left Click is available on hardware.
                break;
            case MouseSelectionType.RIGHT:
                runRightClick(nav);
                break;
            case MouseSelectionType.DOUBLE:
                runLeftClick(nav);
                break;
            case MouseSelectionType.DRAG:
                runDrag(nav);
                break;
            case MouseSelectionType.SCROLL:
                runScroll(nav);
                break;
            case MouseSelectionType.ZOOM:
                runZoom(nav, scale, size);
                break;
        }
        setNextSelectionType();
    }

    private static final String DEFAULT_CLICK = MouseSelectionType.LEFT;
    private ModeIterator<String> selectionTypeIterator;

    /**
     *
     * @throws AWTException
     */
    public void setNextSelectionType() throws AWTException {
        String currentMode = getSelectionType();
        if (!currentMode.equals(MouseSelectionType.SCROLL) && !currentMode.equals(MouseSelectionType.OFF)) {
            setSelectionType(MouseSelectionType.LEFT);
        }
    }

    /**
     *
     * @param click
     */
    public void setSelectionType(String click) {
        getSelectionTypeIterator().set(click);
    }

    /**
     *
     * @return
     */
    public String getSelectionType() {
        return getSelectionTypeIterator().get();
    }

    /**
     *
     * @return
     */
    public ModeIterator<String> getSelectionTypeIterator() {
        if (selectionTypeIterator == null) {
            selectionTypeIterator = new ModeIterator<>(MouseSelectionType.TYPES);
            selectionTypeIterator.set(DEFAULT_CLICK);
        }
        return selectionTypeIterator;
    }

    // 
    /**
     * Resets the mouse position to the the requested <code>Point</code> and
     * then simulates the press and release of the left mouse button.
     *
     * @param nav Point where the click needs to be simulated
     * @throws AWTException
     */
    public void runLeftClick(Point nav) throws AWTException {
        LOGGER.info("Left Clicked");
        // Set the mouse to the original click position in case it has moved
        resetMousePos(nav);
        // Press and release the left mouse button
        runPressReleaseClick(LEFT_BUTTON, LONG_CLICK_DELAY);
    }

    /**
     *
     * Resets the mouse position to the the requested <code>Point</code> and
     * then simulates the press and release of the right mouse button
     *
     * @param nav Point where the click needs to be simulated
     * @throws AWTException
     */
    public void runRightClick(Point nav) throws AWTException {
        LOGGER.info("Right Clicked");
        // Set the mouse to the original click position in case it has moved
        resetMousePos(nav);
        // Press and release the right mouse button
        runPressReleaseClick(RIGHT_BUTTON, LONG_CLICK_DELAY);
    }

    /**
     * Resets the mouse position to the the requested <code>Point</code> and
     * then performs back to back press and release of the left mouse button to
     * simulate a double click
     *
     * @param nav Point where the left click needs to be simulated
     * @throws AWTException
     */
    public void runDoubleClick(Point nav) throws AWTException {
        LOGGER.info("Double Clicked");

        // Set the mouse to the original click position in case it has moved
        resetMousePos(nav);
        // Simulate a press and release of the left mouse button back to back
        runPressReleaseClick(LEFT_BUTTON, LONG_CLICK_DELAY);
        runPressReleaseClick(LEFT_BUTTON, SHORT_CLICK_DELAY);
    }

    /**
     *
     * @param nav
     * @throws AWTException
     */
    public void runDrag(Point nav) throws AWTException {
        LOGGER.info("Drag Clicked");

        // Set the mouse to the original click position in case it has moved
        resetMousePos(nav);
        delay(LONG_CLICK_DELAY * 10);
        // Simulate a single press of the left button
        mousePress(LEFT_BUTTON);
    }

    /**
     *
     * @param nav
     * @throws AWTException
     */
    public void runScroll(Point nav) throws AWTException {
        Display display = new Display();
        int height = display.getScreenHeight();
        LOGGER.info("Scroll Clicked");
        LOGGER.info("Screen Height:" + height);

        // Calculate raw scroll value at the bottom of the screen 
        // with the X axis across the middle of the screen
        double scrollRawMax = -1 * height / 2;
        LOGGER.info("Max Scroll: " + scrollRawMax);
        // Offset the maximum scroll value by the y axis of the mouse
        double scrollRaw = scrollRawMax + (int) nav.getY();
        LOGGER.info("Raw Scroll: " + scrollRaw);
        // Tranpose the scroll amount to a predefined range
        int scrollTransposed = (int) (SCROLL_MAX * (scrollRaw) / (height / 2));
        LOGGER.info("Transposed Scroll:" + scrollTransposed);
        // Bring the window into focus NOTE: Could be made an option
        // runLeftClick(nav);
        // Simulate a single press of the left button
        delay(LONG_CLICK_DELAY * 10);
        mouseWheel(scrollTransposed);
    }

    /**
     *
     * @param nav
     * @param scale
     * @param size
     * @return
     * @throws AWTException
     */
    public ZoomUI runZoom(Point nav, Double scale, Integer size)
            throws AWTException {
        LOGGER.info("Zoom Clicked");

        // Set the mouse to the original click position in case it has moved
        resetMousePos(nav);
        // Implement a short delay in case another click is started
        delay(LONG_CLICK_DELAY);
        // Create a new zoom window 
        zoom = new ZoomUI(nav, scale, size, size);
        zoom.showWindow();
        // When the zoom window is clicked
        zoom.getImageView().setOnMouseReleased((MouseEvent e) -> {
            try {
                // Get the transposed x and y coordinates of the click
                Point zoomPoint = zoom.getClickPoint(e);
                zoom.close();
                // Run a left click at the calculated x & y values
                runLeftClick(zoomPoint);
                LOGGER.info("Zoomed target clicked");
            } catch (AWTException ex) {
                LOGGER.fatal("Unable to execute zoom selection", ex);
            }
        });
        return zoom;
    }

    /**
     *
     * @param mouseBtn
     * @param delay
     */
    public void runPressReleaseClick(int mouseBtn, int delay) {
        // Simulate mouse press for the given button type
        mousePress(mouseBtn);
        // Simulate a short pause
        delay(delay);
        // Simulate release of the mouse button type
        mouseRelease(mouseBtn);
        // Implement a short delay in case another click is started
        delay(SHORT_CLICK_DELAY);

    }

    /**
     *
     * @return
     */
    public ZoomUI getZoomView() {
        return zoom;
    }
}
