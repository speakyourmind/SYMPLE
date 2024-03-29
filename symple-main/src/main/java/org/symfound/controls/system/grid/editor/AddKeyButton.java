/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.SystemControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.main.HomeController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class AddKeyButton extends SystemControl {

    /**
     *
     */
    public static final String NAME = AddKeyButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public ConfigurableGrid configurableGrid;

    /**
     *
     */
    public static final String KEY = "Add Key";

    /**
     *
     * @param configurableGrid
     */
    public AddKeyButton(ConfigurableGrid configurableGrid) {
        super("toolbar-add", KEY, "", "default");
        this.configurableGrid = configurableGrid;
      
    }

    @Override
    public void defineButton() {
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        setConfirmable(Boolean.FALSE);
        setNavigatePostClick(Boolean.FALSE);
    }

    @Override
    public void run() {
        HomeController.setUpdated(Boolean.FALSE);
        LOGGER.info("Add Key button clicked");
        final ParallelList<String, String> order1 = configurableGrid.getOrder();
        order1.add(ReplaceKeyButton.KEY, getIndex().toLowerCase());
        configurableGrid.setOrder(order1);
        configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
        HomeController.setUpdated(true);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends AddKeyButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
