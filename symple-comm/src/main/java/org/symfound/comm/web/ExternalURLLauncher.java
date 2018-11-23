/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.web;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Javed Gangjee
 */
public class ExternalURLLauncher {

    /**
     *
     * @param URL
     */
    public void launchWebPage(String URL) {
        Application application = new Application() {
            @Override
            public void start(Stage primaryStage) throws Exception {
            }
        };
        application.getHostServices().showDocument(URL);
    }
}
