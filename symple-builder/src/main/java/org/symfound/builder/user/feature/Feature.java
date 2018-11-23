/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.feature;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Feature {

    /**
     *
     */
    public static final String EYEBROW_FEATURE = "Eyebrow";

    /**
     *
     */
    public static final String EYE_FEATURE = "Eye";

    /**
     *
     */
    public static final String FACE_FEATURE = "Face";

    /**
     *
     */
    public static final String HEAD_FEATURE = "Head";

    /**
     *
     */
    public static final String HAND_FEATURE = "Hand";

    /**
     *
     */
    public static final String FOOT_FEATURE = "Foot";

    /**
     *
     */
    public static final String UNKNOWN_FEATURE = "Unknown";

    /**
     *
     */
    public static final List<String> FEATURE_TYPES = Arrays.asList(
            EYE_FEATURE,
            EYEBROW_FEATURE,
            FACE_FEATURE,
            HEAD_FEATURE,
            HAND_FEATURE,
            FOOT_FEATURE,
            UNKNOWN_FEATURE);

    /**
     *
     * @param name
     */
    public Feature(String name) {
        this.initName = name;
    }

    private final String initName;
    private StringProperty name;

    /**
     *
     * @param value
     */
    public void setName(String value) {
        // Validate value. Throw exception.
        nameProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return nameProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(initName);
        }
        return name;
    }

    private static final Integer DEFAULT_ABILITY = 3;
    private IntegerProperty ability;

    /**
     *
     * @param value
     */
    public void setAble(Integer value) {
        abilityProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getAbility() {
        return abilityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty abilityProperty() {
        if (ability == null) {
            ability = new SimpleIntegerProperty(DEFAULT_ABILITY);
        }
        return ability;
    }

    private static final Double DEFAULT_POS = 0.0;
    private DoubleProperty posX;
    private DoubleProperty posY;
    private DoubleProperty posZ;

    /**
     *
     * @param value
     */
    public void setPosX(Double value) {
        posXProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getPosX() {
        return posXProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty posXProperty() {
        if (posX == null) {
            posX = new SimpleDoubleProperty(DEFAULT_POS);
        }
        return posX;
    }

    /**
     *
     * @param value
     */
    public void setPosY(Double value) {
        posYProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getPosY() {
        return posYProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty posYProperty() {
        if (posY == null) {
            posY = new SimpleDoubleProperty(DEFAULT_POS);
        }
        return posY;
    }

    /**
     *
     * @param value
     */
    public void setPosZ(Double value) {
        posZProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getPosZ() {
        return posZProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty posZProperty() {
        if (posZ == null) {
            posZ = new SimpleDoubleProperty(DEFAULT_POS);
        }
        return posZ;
    }

    private BooleanProperty tracked;

    /**
     *
     * @return
     */
    public Boolean isTracked() {
        return trackedProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setTracked(Boolean value) {
        trackedProperty().setValue(value);
    }

    /**
     *
     * @return tracked
     */
    public BooleanProperty trackedProperty() {
        if (tracked == null) {
            tracked = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return tracked;
    }

}
