/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.preloader;

import images.Images;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Javed Gangjee
 */
public class PreloaderPane extends AnchorPane {

    /**
     *
     * @param version
     */
    public PreloaderPane(String version) {
        initialize(version);
    }

    private void initialize(String version) {
        String image = Images.class.getResource("blue.png").toExternalForm();
        this.setStyle("-fx-background-image:url('" + image + "'); "
                + "-fx-background-color: #3094bf; "
                + "-fx-background-size: contain; \n"
                + "-fx-background-repeat: no-repeat; \n"
                + "-fx-background-position: center;");
        Label label = new Label();
        label.setText(version);
        label.setStyle("-fx-text-fill: #f3f3f3; -fx-font-size:16pt;-fx-font-weight:bold;");
        AnchorPane.setTopAnchor(label, null);
        AnchorPane.setBottomAnchor(label, 10.0);
        AnchorPane.setLeftAnchor(label, 20.0);
        AnchorPane.setRightAnchor(label, null);
        getChildren().add(label);

        Label label2 = new Label("Loading... Please Wait...");
        label2.setStyle("-fx-text-fill: #f3f3f3; -fx-font-size:16pt; -fx-font-weight:bold;");
        AnchorPane.setTopAnchor(label2, null);
        AnchorPane.setBottomAnchor(label2, 10.0);
        AnchorPane.setRightAnchor(label2, 20.0);
        AnchorPane.setLeftAnchor(label2, null);
        getChildren().add(label2);
    }
}
