/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.session;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Javed Gangjee
 */
public class Display {

    /**
     *
     */
    public IntegerProperty screenWidth;

    /**
     *
     * @return
     */
    public Integer getScreenWidth() {
        return screenWidthProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty screenWidthProperty() {
        if (screenWidth == null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Integer initValue = (int) screenSize.getWidth();
            screenWidth = new SimpleIntegerProperty(initValue);
        }
        return screenWidth;
    }

    /**
     *
     */
    public IntegerProperty screenHeight;

    /**
     *
     * @return
     */
    public Integer getScreenHeight() {
        return screenHeightProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty screenHeightProperty() {
        if (screenHeight == null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Integer initValue = (int) screenSize.getHeight();
            screenHeight = new SimpleIntegerProperty(initValue);
        }
        return screenHeight;
    }
}
