/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

/**
 *
 * @author Javed Gangjee
 */
public class RootPane extends AnimatedPane {

    /**
     *
     */
    public static final Double DEFAULT_PREF_WIDTH = 600.0;

    /**
     *
     */
    public static final Double DEFAULT_PREF_HEIGHT = 400.0;

    // TO DO: This is is to replace the AnchorPane at the base of every fxml

    /**
     *
     */
    public RootPane() {
        setPrefWidth(DEFAULT_PREF_WIDTH);
        setPrefHeight(DEFAULT_PREF_HEIGHT);

    }
}
