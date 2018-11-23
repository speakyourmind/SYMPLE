/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.preloader;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee
 */
public class MainPreloader extends Preloader {

    private Scene createPreloaderScene() {
        PreloaderPane ap = new PreloaderPane(Main.VERSION);
        Scene scene = new Scene(ap, 600, 300);

        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(createPreloaderScene());
        stage.getScene().setFill(null);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Main.readyProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        stage.hide();
                    }
                });
            }
        });
        stage.show();
    }

}
