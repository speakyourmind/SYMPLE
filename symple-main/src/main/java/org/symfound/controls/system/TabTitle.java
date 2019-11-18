/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class TabTitle extends StackPane {

    public TabTitle(String title) {
        initialize(title);
    }

    private void initialize(String title) {
        Label l = new Label(title);
        l.getStyleClass().add("tab-title");
        this.getChildren().add(new Group(l));
    }
    
    
}
