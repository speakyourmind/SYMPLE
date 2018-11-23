/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media;

import org.symfound.controls.user.ScreenStatus;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.symfound.builder.user.User;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.tools.selection.SelectionMethod;
import org.symfound.tools.timing.Hold;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class MediaViewer extends AppableControl implements Reloadable {

    AnimatedLabel label = new AnimatedLabel("Loading...");

    public MediaViewer(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
        initialize();
    }

    public abstract void play();

    public abstract void end();

    private void initialize() {
        this.setSelectable(false);

        label.setStyle("-fx-text-fill:-fx-blue; -fx-font-size:48pt;");
        label.setAlignment(Pos.CENTER);
        statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.equals(ScreenStatus.READY)) {
                label.setDisable(true);

                if (!getChildren().contains(label)) {
                    addToPane(label);
                }
                label.toFront();
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.0), label);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.setCycleCount(1);
                fadeTransition.setInterpolator(Interpolator.EASE_IN);
                fadeTransition.play();
            } else if (newValue1.equals(ScreenStatus.PLAYING)) {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.0), label);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(1);
                fadeTransition.setInterpolator(Interpolator.EASE_IN);
                fadeTransition.play();
                fadeTransition.setOnFinished((ActionEvent e) -> {
                    label.setDisable(false);
                    label.removeFromParent();

                });
            }
        });
    }

    private Hold hold;

    /**
     *
     * @return
     */
    public Hold getHold() {
        if (hold == null) {
            hold = new Hold();
        }
        return hold;
    }

    /**
     *
     */
    public void addHold() {
        FullSession session = Main.getSession();
        User user = session.getUser();
        SelectionMethod selectionMethod = user.getInteraction().getSelectionMethod();
        if (selectionMethod.equals(SelectionMethod.CLICK)
                || selectionMethod.equals(SelectionMethod.SWITCH)
                || selectionMethod.equals(SelectionMethod.SCAN)
                || selectionMethod.equals(SelectionMethod.STEP)) {
            Hardware hardware = session.getDeviceManager().getCurrent().getHardware();
            getHold().add(hardware.getSelectability().getPostSelectTime());
        }
    }

    private ObjectProperty<ScreenStatus> status;

    public void setStatus(ScreenStatus value) {
        statusProperty().setValue(value);
    }

    public ScreenStatus getStatus() {
        return statusProperty().getValue();
    }

    public ObjectProperty<ScreenStatus> statusProperty() {
        if (status == null) {
            status = new SimpleObjectProperty<>(ScreenStatus.CLOSED);
        }
        return status;
    }

}
