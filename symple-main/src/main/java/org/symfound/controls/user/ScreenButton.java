/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.main.HomeController;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class ScreenButton extends GenericButton {

    public static final String KEY = "Screen";
    public static final String DESCRIPTION = "Board";

    public ScreenButton(String index) {
        super(index);
        initialize(index);
    }

    private void initialize(String index) {
        defaultTitle = index;
        setNavigatePostClick(Boolean.FALSE);
        configureTitle();
    }

    @Override
    public void run() {
        ConfigurableGrid homeGrid = HomeController.getSubGrid().getConfigurableGrid();
        ActiveTextArea.get().replaceSelection("");

        LOGGER.info("Setting index to " + initIndex);
        homeGrid.setIndex(initIndex);
        super.run();
    }

}
