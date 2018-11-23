/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import static org.symfound.device.emulation.input.InputListener.LOGGER;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchDirector {

    private static final String NEUTRAL_NAME = "-";
    private static final String OTHER_NAME = "Out of Bounds";
    private static final String UP_NAME = "Up";
    private static final String DOWN_NAME = "Down";
    private static final String LEFT_NAME = "Left";
    private static final String RIGHT_NAME = "Right";

    private static final Double NEUTRAL_ANGLE = 0.0;
    private static final Double OTHER_ANGLE = 0.0;
    private static final Double UP_ANGLE = 3 * Math.PI / 2;
    private static final Double LEFT_ANGLE = Math.PI;
    private static final Double DOWN_ANGLE = Math.PI / 2;
    private static final Double RIGHT_ANGLE = 2 * Math.PI;

    /**
     *
     */
    public static final SwitchDirection NEUTRAL = new SwitchDirection(NEUTRAL_NAME, NEUTRAL_ANGLE);

    /**
     *
     */
    public static final SwitchDirection OTHER = new SwitchDirection(OTHER_NAME, OTHER_ANGLE);

    /**
     *
     */
    public static final SwitchDirection UP = new SwitchDirection(UP_NAME, UP_ANGLE);

    /**
     *
     */
    public static final SwitchDirection DOWN = new SwitchDirection(DOWN_NAME, DOWN_ANGLE);

    /**
     *
     */
    public static final SwitchDirection LEFT = new SwitchDirection(LEFT_NAME, LEFT_ANGLE);

    /**
     *
     */
    public static final SwitchDirection RIGHT = new SwitchDirection(RIGHT_NAME, RIGHT_ANGLE);

    /**
     *
     */
    public static final List<SwitchDirection> directions = Arrays.asList(UP, DOWN, LEFT, RIGHT);

    private final SwitchListener listener;

    /**
     *
     * @return
     */
    public SwitchListener getListener() {
        return listener;
    }

    /**
     *
     * @param listener
     */
    public SwitchDirector(SwitchListener listener) {
        this.listener = listener;
        initialize();
    }

    private void initialize() {
        listener.switchedProperty().addListener((observable, oldValue, newValue) -> {
            ZScore zScore = listener.getZScore();
            final Double magnitude = zScore.getMagnitude();
            final Double theta = zScore.getTheta();//0 to 360 ex 310
            final Double thetaDegrees = Math.toDegrees(theta);
            if (newValue) {
                LOGGER.info("Switch on: Direction:" + thetaDegrees + "; zScore:" + magnitude);
                setCurrentDirection(OTHER);
                directions.stream().forEach((direction) -> {
                    checkDirection(zScore, direction,listener.hardware.getSelectability().getSwitchability().getArcSize());
                });
            } else {
                LOGGER.info("Switch off : Direction:" + thetaDegrees + "; zScore:" + magnitude);
                setCurrentDirection(NEUTRAL);
            }
        });
    }

    private void checkDirection(ZScore zScore, SwitchDirection direction, Double sensitivity) {
        Double theta = zScore.getTheta();
        if (zScore.isTheta(theta, direction.getAngle(), sensitivity)) {
            Double magnitude = zScore.getMagnitude();
            LOGGER.info(direction.getName() + " direction = " + Math.toDegrees(theta) + "; zScore=" + magnitude);
            setCurrentDirection(direction);
        }
    }

    /*   public BooleanBinding upBinding(){
     return Bindings.equal(currentDirectionProperty(), UP);
     }
     */
    private ObjectProperty<SwitchDirection> currentDirection;

    /**
     *
     * @param value
     */
    public void setCurrentDirection(SwitchDirection value) {
        currentDirectionProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public SwitchDirection getCurrentDirection() {
        return currentDirectionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<SwitchDirection> currentDirectionProperty() {
        if (currentDirection == null) {
            currentDirection = new SimpleObjectProperty<>();
        }
        return currentDirection;
    }
    
    
}
