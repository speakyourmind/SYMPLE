/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.mouse;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Javed Gangjee
 */
public class GlassButton extends Button {

    /**
     *
     */
    public GlassButton() {
        initialize();
    }

    private void initialize() {
        setStyle("-fx-background-color:#0000;-fx-opacity:0.1;");
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
    }
}
