/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class TabTitle extends StackPane {

    private final String title;
    public TabTitle(String title) {
        this.title=title;
        initialize();
    }

    private void initialize() {
        Label tabLabel = getLabel();
        this.getChildren().add(new Group(tabLabel));

    }
    private Label label;

    public Label getLabel() {
        if (label == null) {
            label = new Label(title);
            label.getStyleClass().add("tab-title");
        }
        return label;
    }

}
