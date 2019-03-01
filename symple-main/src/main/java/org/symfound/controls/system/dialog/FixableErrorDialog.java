/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import org.symfound.controls.system.TitledLabel;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.HomeController;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public abstract class FixableErrorDialog extends OKCancelDialog {

    public FixableErrorDialog(String titleText, String captionText, String okText, String cancelText) {
        super(titleText, captionText, okText, cancelText);
       
    }

    @Override
    public TitledLabel buildTitledLabel() {
        TitledLabel label = new TitledLabel();
        label.titleTextProperty().bindBidirectional(titleTextProperty());
        label.captionTextProperty().bindBidirectional(captionTextProperty());
        label.vBox.setSpacing(20.0);
        label.vBox.setAlignment(Pos.CENTER);
        label.vBox.setStyle("-fx-background-color: transparent, -fx-red;-fx-background-insets: 0,5;");
        label.getPrimaryControl().setAlignment(Pos.CENTER);
        label.getPrimaryControl().setStyle("-fx-text-fill:-fx-light;-fx-font-size:3.2em;");
        label.captionLabel.setAlignment(Pos.CENTER);
        label.captionLabel.setStyle("-fx-text-alignment:center;-fx-font-size:1.6em;-fx-text-fill:-fx-light");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
        return label;
    }
}
