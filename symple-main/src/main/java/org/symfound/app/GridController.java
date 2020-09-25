/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.app;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public abstract class GridController extends CommonController {

    private static final String NAME = GridController.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    @FXML
    public AnchorPane apMain;

}
