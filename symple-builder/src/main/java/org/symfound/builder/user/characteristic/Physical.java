/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.builder.characteristic.Characteristic;
import org.symfound.builder.user.feature.Eye;
import org.symfound.builder.user.feature.Eyebrow;
import org.symfound.builder.user.feature.Head;

/**
 *
 * @author Javed Gangjee
 */
public class Physical extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Physical(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     */
    public ObjectProperty<Eye> leftEye;

    /**
     *
     * @param value
     */
    public void setLeftEye(Eye value) {
        leftEyeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Eye getLeftEye() {
        return leftEyeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Eye> leftEyeProperty() {
        if (leftEye == null) {
            final Eye initValue = new Eye();
            leftEye = new SimpleObjectProperty<>(initValue);
        }
        return leftEye;
    }

    /**
     *
     */
    public ObjectProperty<Eye> rightEye;

    /**
     *
     * @param value
     */
    public void setRightEye(Eye value) {
        rightEyeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Eye getRightEye() {
        return rightEyeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Eye> rightEyeProperty() {
        if (rightEye == null) {
            final Eye initValue = new Eye();
            rightEye = new SimpleObjectProperty<>(initValue);
        }
        return rightEye;
    }

    /**
     *
     */
    public ObjectProperty<Eyebrow> leftEyebrow;

    /**
     *
     * @param value
     */
    public void setLeftEyebrow(Eyebrow value) {
        leftEyebrowProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Eyebrow getLeftEyebrow() {
        return leftEyebrowProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Eyebrow> leftEyebrowProperty() {
        if (leftEyebrow == null) {
            final Eyebrow initValue = new Eyebrow();
            leftEyebrow = new SimpleObjectProperty<>(initValue);
        }
        return leftEyebrow;
    }

    /**
     *
     */
    public ObjectProperty<Eyebrow> rightEyebrow;

    /**
     *
     * @param value
     */
    public void setRightEyebrow(Eyebrow value) {
        rightEyebrowProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Eyebrow getRightEyebrow() {
        return rightEyebrowProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Eyebrow> rightEyebrowProperty() {
        if (rightEyebrow == null) {
            final Eyebrow initValue = new Eyebrow();
            rightEyebrow = new SimpleObjectProperty<>(initValue);
        }
        return rightEyebrow;
    }

    /**
     *
     */
    public ObjectProperty<Head> head;

    /**
     *
     * @param value
     */
    public void setHead(Head value) {
        headProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Head getHead() {
        return headProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Head> headProperty() {
        if (head == null) {
            final Head initValue = new Head();
            head = new SimpleObjectProperty<>(initValue);
        }
        return head;
    }
}
