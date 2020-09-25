package org.symfound.device.emulation.input.mouse;

import java.awt.Point;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.symfound.device.emulation.input.InputListener;

/**
 *
 * @author Javed Gangjee
 */
public abstract class NativeMouseListener extends InputListener implements NativeMouseInputListener {

    private static final String NAME = NativeMouseListener.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param e
     */
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
      //  System.out.println("Mouse Clicked: " + e.getClickCount());
    }

    /**
     *
     * @param e
     */
    @Override
    public abstract void nativeMousePressed(NativeMouseEvent e);{
      //  System.out.println("Mouse Pressed: " + e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        //System.out.println("Mouse Released: " + e.getButton());
    }

    /**
     *
     * @param e
     */
    @Override
    public abstract void nativeMouseMoved(NativeMouseEvent e);

    /**
     *
     * @param e
     */
    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        //System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
    }

    /**
     *
     */
    @Override
    public void start() {
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
    }

    /**
     *
     */
    public MousePositionLog positionLog = new MousePositionLog();

    /**
     *
     * @param point
     * @return
     */
    public double getStdDevX(Point point) {
        double totStdX = 0;
        int numSamples = positionLog.size();
        for (Point p : positionLog) {
            double x = point.getX();
            totStdX += Math.pow((p.getX() - x), 2);
        }
        double stdX = Math.sqrt(totStdX / (numSamples - 1));
        return stdX;
    }

    /**
     *
     * @param point
     * @return
     */
    public double getStdDevY(Point point) {
        double totStdX = 0;
        int numSamples = positionLog.size();
        for (Point p : positionLog) {
            totStdX += Math.pow((p.getY() - point.getY()), 2);
        }
        double stdX = Math.sqrt(totStdX / (numSamples - 1));
        return stdX;
    }

    /**
     * Calculate the Simple Moving Average SMA of the X and Y coordinates of a
     * list of points and return the result as a point.
     *
     * @return
     */
    public Point getSMAPoint() {

        int totalX = 0;
        int totalY = 0;
        int numSamples = positionLog.size();

        for (Point p : positionLog) {
            totalX += (int) p.getX();
            totalY += (int) p.getY();
        }

        int setX = totalX / numSamples;
        int setY = totalY / numSamples;

        Point setPoint = new Point(setX, setY);

        return setPoint;
    }

    /**
     * Calculate the Exponential Moving Average EMA of the X and Y coordinates
     * of a list of points and return the result as a point.
     *
     * @param s
     * @return
     */
    public Point getEMAPoint(double s) {

        double totalX = 0, totalY = 0, totalExp = 0;

        for (int i = 1; i < positionLog.getSampleSize(); i++) {
            Point get = positionLog.get(i);
            double exp = Math.exp(s * i);
            totalX += exp * get.getX();
            totalY += exp * get.getY();
            totalExp += exp;
        }

        int setX = (int) (totalX / totalExp);
        int setY = (int) (totalY / totalExp);
        Point setPoint = new Point(setX, setY);

        return setPoint;
    }

    /**
     * Calculate whether a click has been requested by comparing an integer
     * sensitivity value to the standard deviation of the Exponential Moving
     * Average of the X and Y coordinates of the given point list.
     *
     * @param sensitivity
     * @return
     */
    public Boolean isDwellSelect(Integer sensitivity) {
        Point point = getActualPosition();
        Boolean isClick = false;
        Double stdX = getStdDevX(point);
        Double stdY = getStdDevY(point);
        if (stdX < sensitivity && stdY < sensitivity) {
            LOGGER.info("Filter: Deviation is Click");
            isClick = true;
        }
        return isClick;
    }

    private static final Point DEFAULT_ACTUAL_POSITION = new Point(0, 0);

    /**
     *
     */
    public ObjectProperty<Point> actualPosition;

    /**
     *
     * @param value
     */
    public void setActualPosition(Point value) {
        actualPositionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Point getActualPosition() {
        return actualPositionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Point> actualPositionProperty() {
        if (actualPosition == null) {
            actualPosition = new SimpleObjectProperty(DEFAULT_ACTUAL_POSITION);
        }
        return actualPosition;
    }
}
