/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.SystemControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class DeleteKeyButton extends SystemControl {

    /**
     *
     */
    public static final String NAME = EditGridButton.class.getName();

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
    public AppableControl control;

    /**
     *
     */
    public static final String KEY = "Delete Key";

    /**
     *
     * @param control
     * @param configurableGrid
     */
    public DeleteKeyButton(AppableControl control, ConfigurableGrid configurableGrid) {
        super("word-2", KEY, "Delete", "default");
        this.setText("Delete");
        this.configurableGrid = configurableGrid;
        this.control = control;
        initialize();
    }

    private void initialize() {
        
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        setConfirmable(Boolean.FALSE);
    }

    @Override
    public void run() {
        LOGGER.info("Delete Keys button clicked");
        final ParallelList<String, String> order1 = configurableGrid.getOrder();
        order1.remove(control.getGridLocation());
        configurableGrid.setOrder(order1);
        configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
        control.getEditAppButton().getDialog().onCancel();
        control.getEditAppButton().getDialog().setDone(true);
       // ConfigurableGrid.setEditMode(false);
        SettingsController.setUpdated(true);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends DeleteKeyButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
