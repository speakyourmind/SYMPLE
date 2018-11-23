/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.tools.animation;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 * @author Javed Gangjee
 */
public class NodeAnimator {

    /**
     *
     */
    public static final Double DWELL_SCALE = 1.18;

    /**
     *
     */
    public static final Double FADE_IN_TIME = 0.5;

    /**
     *
     */
    public static final Double FADE_OUT_TIME = 0.5;

    /**
     *
     */
    public static final Double MAX_OPACITY = 1.0;

    /**
     *
     */
    public static final Double MIN_OPACITY = 0.0;
    private final Node node;


    /**
     *
     * @param node
     */
    public NodeAnimator(Node node) {
        this.node = node;
    }

    private BooleanProperty playing;

    /**
     *
     * @param value
     */
    public void setPlaying(Boolean value) {
        playingProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isPlaying() {
        return playingProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty playingProperty() {
        if (playing == null) {
            playing = new SimpleBooleanProperty(false);
        }
        return playing;
    }

    private class RotateAnimation{
            private RotateTransition rotateTransition;
    /**
     *
     * @param time
     * @param fromValue
     * @param toValue
     */
    public void startRotation(Double time, Double fromValue, Double toValue) {
        rotateTransition = new RotateTransition(Duration.seconds(time), node);
        rotateTransition.setFromAngle(fromValue);
        rotateTransition.setToAngle(toValue);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition.play();
        setPlaying(true);
        setOnRotationFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopRotation() {
        if (rotateTransition != null) {
            rotateTransition.stop();
            setPlaying(false);
        }
    }

    /**
     *
     * @param e
     */
    public void setOnRotationFinished(EventHandler<ActionEvent> e) {
        rotateTransition.setOnFinished(e);
    }
    }
    private RotateTransition rotateTransition;
    /**
     *
     * @param time
     * @param fromValue
     * @param toValue
     */
    public void startRotation(Double time, Double fromValue, Double toValue) {
        rotateTransition = new RotateTransition(Duration.seconds(time), node);
        rotateTransition.setFromAngle(fromValue);
        rotateTransition.setToAngle(toValue);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);
        rotateTransition.play();
        setPlaying(true);
        setOnRotationFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopRotation() {
        if (rotateTransition != null) {
            rotateTransition.stop();
            setPlaying(false);
        }
    }

    /**
     *
     * @param e
     */
    public void setOnRotationFinished(EventHandler<ActionEvent> e) {
        rotateTransition.setOnFinished(e);
    }

    private FadeTransition fadeTransition;
    /**
     *
     * @param time
     * @param fromValue
     * @param toValue
     */
    public void startFade(Double time, Double fromValue, Double toValue) {
        fadeTransition = new FadeTransition(Duration.seconds(time), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setCycleCount(1);
        fadeTransition.setInterpolator(Interpolator.EASE_IN);
        fadeTransition.play();
        setPlaying(true);
        setOnFadeFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopFade() {
        if (fadeTransition != null) {
            fadeTransition.stop();
            setPlaying(false);
        }
    }

    /**
     *
     * @param e
     */
    public void setOnFadeFinished(EventHandler<ActionEvent> e) {
        fadeTransition.setOnFinished(e);
    }

    /**
     *
     */
    public ScaleTransition scaleTransition;
    /**
     *
     * @param time
     * @param fromValue
     * @param toValue
     */
    public void startScale(Double time, Double fromValue, Double toValue) {
        scaleTransition = new ScaleTransition(Duration.seconds(time), node);
        scaleTransition.setFromX(fromValue);
        scaleTransition.setFromY(fromValue);
        scaleTransition.setToX(toValue);
        scaleTransition.setToY(toValue);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_IN);
        scaleTransition.play();
        setPlaying(true);
        setOnScaleFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopScale() {
        if (scaleTransition != null) {
            scaleTransition.stop();
            setPlaying(false);
        }
    }

    /**
     *
     * @param e
     */
    public void setOnScaleFinished(EventHandler<ActionEvent> e) {
        scaleTransition.setOnFinished(e);
    }

    private TranslateTransition translationX;
    private TranslateTransition translationY;
    /**
     *
     * @param time
     * @param fromValue
     */
    public void startTranslateInX(Double time, Double fromValue) {
        translationX = new TranslateTransition(Duration.seconds(time), node);
        translationX.setFromX(fromValue); // Calculate based on Screen size
        translationX.setToX(0.0);
        translationX.setCycleCount(1);
        translationX.setInterpolator(Interpolator.EASE_IN);
        translationX.play();
        setPlaying(true);
        setOnTranslateInXFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopTranslateInX() {
        translationX.stop();
        setPlaying(false);
    }

    /**
     *
     * @param e
     */
    public void setOnTranslateInXFinished(EventHandler<ActionEvent> e) {
        translationX.setOnFinished(e);
    }

    /**
     *
     * @param time
     * @param fromValue
     */
    public void startTranslateInY(Double time, Double fromValue) {
        translationY = new TranslateTransition(Duration.seconds(time), node);
        translationY.setFromY(fromValue);
        translationY.setToY(0.0);
        translationY.setCycleCount(1);
        translationY.setInterpolator(Interpolator.EASE_IN);
        translationY.play();
        setPlaying(true);
    }

    /**
     *
     */
    public void stopTranslateInY() {
        translationY.stop();
        setPlaying(false);
    }

    /**
     *
     * @param e
     */
    public void setOnTranslateInYFinished(EventHandler<ActionEvent> e) {
        translationX.setOnFinished(e);
    }

    /**
     *
     * @param time
     * @param toValue
     */
    public void startTranslateOutX(Double time, Double toValue) {
        translationX = new TranslateTransition(Duration.seconds(time), node);
        translationX.setFromX(0.0);
        translationX.setToX(toValue);
        translationX.setCycleCount(1);
        translationX.setInterpolator(Interpolator.EASE_OUT);
        translationX.play();
        setPlaying(true);
    }

    /**
     *
     */
    public void stopTranslateOutX() {
        translationX.stop();
        setPlaying(false);
    }

    /**
     *
     * @param e
     */
    public void setOnTranslateOutXFinished(EventHandler<ActionEvent> e) {
        translationX.setOnFinished(e);
    }

    /**
     *
     * @param time
     */
    public void startTranslateOutY(Double time) {
        translationY = new TranslateTransition(Duration.seconds(time), node);
        translationY.setFromY(0.0);
        translationY.setToY(-3000.0);
        translationY.setCycleCount(1);
        translationY.setInterpolator(Interpolator.EASE_OUT);
        translationY.play();
        setPlaying(true);
        setOnTranslateOutYFinished((ActionEvent e) -> {
            setPlaying(false);
        });
    }

    /**
     *
     */
    public void stopTranslateOutY() {
        translationY.stop();
        setPlaying(false);
    }

    /**
     *
     * @param e
     */
    public void setOnTranslateOutYFinished(EventHandler<ActionEvent> e) {
        translationY.setOnFinished(e);
    }

}
