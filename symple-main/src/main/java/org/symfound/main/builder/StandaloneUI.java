/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.builder;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.component.StandalonePane;

/**
 *
 * @author Javed Gangjee
 */
public class StandaloneUI extends UI {

    private static final String NAME = StandaloneUI.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param file
     * @param hasMenu
     */
    public StandaloneUI(String file, Boolean hasMenu) {
        super(file, hasMenu);
    }

    /**
     *
     * @param builder
     */
    @Override
    public void build(Builder builder) {
        StandalonePane standalone = new StandalonePane();
        standalone.build(getUIPath(), builder);
        ResourceBundle bundle = getUIPath().getBundle();
        Boolean hasMenu = Boolean.valueOf(bundle.getString("hasMenu"));
        Boolean hasToolbar = Boolean.valueOf(bundle.getString("hasToolbar"));
        String gridOrder = bundle.getString("gridOrder");

        CommonPane commonPane = new CommonPane(standalone, hasMenu, hasToolbar, gridOrder);
        scene = create(commonPane, true);
        standalone.builtProperty().bindBidirectional(builtProperty());
        LOGGER.info("Screen " + getUIPath().get() + " built");
    }

}
