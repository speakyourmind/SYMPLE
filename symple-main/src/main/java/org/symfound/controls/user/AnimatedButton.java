/*
 * Copyright (C) 2014 SpeakYourMind Foundation
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
package org.symfound.controls.user;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.symfound.builder.session.Session;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.builder.user.selection.SelectionMethod;
import static org.symfound.controls.ScreenControl.CSS_PATH;

//import org.symfound.main.FullSession;//

import org.symfound.main.Main;//
import org.symfound.main.builder.UI;//

import org.symfound.tools.animation.Animated;
import org.symfound.tools.animation.NodeAnimator;
import org.symfound.tools.timing.DelayedEvent;
import org.symfound.tools.timing.transition.DwellTransition;
import org.symfound.tools.timing.transition.ScanTransition;

/**
 * Includes a timed animation to be implemented at the end of a selection.
 *
 * @author Javed Gangjee
 */
public class AnimatedButton extends Button implements Animated, Usable {

    /**
     *
     */
    public static final String DEFAULT_TEXT = "";

    /**
     *
     */
    public static final String DEFAULT_STYLE = "";

    /**
     *
     */
    public NodeAnimator nodeAnimator;

    private Session session;
    private User user;
/* public AnimatedButton("") {
        this("");
    }*/

    /**
     *
     * @param text
     */
    public AnimatedButton(String text) {
        super(text);
        session = Main.getSession();
        user = session.getUser();
        setRippleAnimation();
        getStylesheets().add(CSS_PATH);
        textProperty().bindBidirectional(accessibleTextProperty());
    }

    /**
     *
     * @return
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     *
     * @return
     */
    public Session getSession() {
        return session;
    }

    private Circle rippleCircle;
    private final Rectangle rippleClip = new Rectangle();
    private final Duration rippleDuration = Duration.seconds(0.5);
    private double lastRippleHeight = 0;
    private double lastRippleWidth = 0;
    private final Color rippleColor = new Color(0, 0, 0, 0.08);
    private BooleanProperty ripple;

    /**
     *
     * @return
     */
    public Boolean shouldRipple() {
        return rippleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty rippleProperty() {
        if (ripple == null) {
            ripple = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return ripple;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        final ButtonSkin buttonSkin = new ButtonSkin(this);
        // Adding circleRipple as fist node of button nodes to be on the bottom
        getChildren().add(0, rippleCircle);

        return buttonSkin;
    }

    private void setRippleAnimation() {
        rippleCircle = new Circle(0.1, rippleColor);
        rippleCircle.setOpacity(0.0);
        // Optional box blur on ripple - smoother ripple effect
        rippleCircle.setEffect(new BoxBlur(3, 3, 2));
        // Fade effect bit longer to show edges on the end of animation
        final FadeTransition fadeTransition = new FadeTransition(rippleDuration, rippleCircle);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        final Timeline scaleRippleTimeline = new Timeline();
        final SequentialTransition sequentialTransition = new SequentialTransition();

        sequentialTransition.getChildren().addAll(scaleRippleTimeline, fadeTransition);
        // When ripple transition is finished then reset circleRipple to starting point  
        sequentialTransition.setOnFinished(event -> {
            rippleCircle.setOpacity(0.0);
            rippleCircle.setRadius(0.1);
        });
        setOnMousePressed((MouseEvent event) -> {
            sequentialTransition.stop();
            // Manually fire finish event
            sequentialTransition.getOnFinished().handle(null);
            rippleCircle.setCenterX(event.getX());
            rippleCircle.setCenterY(event.getY());
            // Recalculate ripple size if size of button from last time was changed
            if (getWidth() != lastRippleWidth || getHeight() != lastRippleHeight) {
                lastRippleWidth = getWidth();
                lastRippleHeight = getHeight();
                rippleClip.setWidth(lastRippleWidth);
                rippleClip.setHeight(lastRippleHeight);
                // try block because of possible null of Background, fills ...
                try {
                    CornerRadii radii = this.getBackground().getFills().get(0).getRadii();
                    rippleClip.setArcHeight(radii.getTopLeftHorizontalRadius());
                    rippleClip.setArcWidth(radii.getTopLeftHorizontalRadius());
                    rippleCircle.setClip(rippleClip);
                } catch (Exception e) {
                }
                // Getting 45% of longest button's length, because we want edge of ripple effect always visible
                double circleRippleRadius = Math.max(getHeight(), getWidth()) * 0.8;
                final KeyValue keyValue = new KeyValue(rippleCircle.radiusProperty(), circleRippleRadius, Interpolator.EASE_OUT);
                final KeyFrame keyFrame = new KeyFrame(rippleDuration, keyValue);
                ObservableList<KeyFrame> keyFrames = scaleRippleTimeline.getKeyFrames();
                keyFrames.clear();
                keyFrames.add(keyFrame);
            }
            if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
                sequentialTransition.playFromStart();
            }
        });
    }

    /**
     *
     * @param color
     */
    public void setRippleColor(Color color) {
        rippleCircle.setFill(color);
    }

    /**
     *
     */
    public DwellTransition growTransition = new DwellTransition();

    /**
     *
     */
    public void startGrow() {
        textFillProperty().bindBidirectional(growTransition.colorProperty());
        scaleXProperty().bindBidirectional(growTransition.scaleProperty());
        scaleYProperty().bindBidirectional(growTransition.scaleProperty());

        Double time = getUser().getTiming().getDwellTime();
        Color color = Color.valueOf(getUser().getInteraction().getSelectionColour());
        Double scale = NodeAnimator.DWELL_SCALE;

        growTransition.setupGrow(time, color, scale);
        growTransition.play();

    }

    /**
     *
     */
    public void stopGrow() {
        growTransition.end();

    }

    /**
     *
     * @param e
     */
    public void setOnGrowFinished(EventHandler<ActionEvent> e) {
        growTransition.get().setOnFinished(e);
    }

    /**
     *
     */
    public ScanTransition scanTransition = new ScanTransition();

    /**
     *
     * @param list
     */
    public void startScan(List<String> list) {
        textFillProperty().bindBidirectional(scanTransition.colorProperty());
        textProperty().bindBidirectional(scanTransition.textProperty());
        scaleXProperty().bindBidirectional(scanTransition.scaleProperty());
        scaleYProperty().bindBidirectional(scanTransition.scaleProperty());

        Double time = getUser().getTiming().getDwellTime();
        Color color = Color.valueOf(getUser().getInteraction().getSelectionColour());
        Color background = (Color) backgroundProperty().getValue().getFills().get(0).getFill();
        Double scale = NodeAnimator.DWELL_SCALE;

        scanTransition.setupGrow(time, color, scale);
        scanTransition.setupScan(list, color, background, time);
        scanTransition.play();

    }

    /**
     *
     */
    public void stopScan() {
        scanTransition.end();

    }

    /**
     *
     * @param e
     */
    public void setOnScanFinished(EventHandler<ActionEvent> e) {
        scanTransition.get().setOnFinished(e);
    }

    /**
     *
     * @return
     */
    @Override
    public NodeAnimator animate() {
        if (nodeAnimator == null) {
            nodeAnimator = new NodeAnimator(this);
            nodeAnimator.playingProperty().addListener((observable, oldValue, newValue) -> {
                getSession().setMutex(newValue);
            });

        }
        return nodeAnimator;
    }

    /**
     * Set button to text, style and scale.
     *
     * @param originalText Button text to set it to
     * @param originalStyle Button style to set it to
     */
    public void revert(String originalText, String originalStyle) {

        //Original Size X
        setScaleX(1);
        //Original Size Y
        setScaleY(1);
        if (originalText != null) {
            //Original Text
            setText(originalText);
        }

      /*  if (originalStyle != null) {
            //Original Style
            setStyle(originalStyle);
        } else {
            setStyle("");
        }*/
        getSession().setMutex(false);
    }

    /**
     * Animate the button when a <code>Timeline</code> is completed and reset to
     * original style, text and size.
     *
     * @param originalText
     * @param originalStyle
     * @return
     */
    public DelayedEvent setOnFinished(String originalText, String originalStyle) {

        getSession().setMutex(true);
        Interaction interaction = getUser().getInteraction();
        //String style=this.getStyle();
        // Set button selection animation
       // setStyle(originalStyle+"-fx-background-color:" + interaction.getSelectionColour() + ";"); // TO DO: Replace with setCSS

        // Create a timeline that resets after the specified amount of time.
        //  ResetTransition resetTransition = new ResetTransition();
        DelayedEvent delayedEvent = new DelayedEvent();
        delayedEvent.setup(interaction.getSelectionTime(), (ActionEvent u) -> {
            // Set button to original style, size and text
            revert(originalText, originalStyle);
        });
        delayedEvent.play();
        return delayedEvent;
    }

    /**
     * Get the stage that the provided node belongs to.
     *
     * @return
     */
    public UI getParentUI() {
        Scene scene = getScene();
        Window window = scene.getWindow();
        return (UI) window;
    }

    /**
     *
     */
    public void setHighlight() {
        setStyle("-fx-background-insets:  0,40 40 40 40,10,10,10;");
    }

}
