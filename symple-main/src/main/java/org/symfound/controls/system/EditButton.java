/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.ScreenControl.ControlType;
import org.symfound.controls.SystemControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public class EditButton extends SystemControl {

    private static final String NAME = EditButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Edit";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Edit Grid";

    /**
     *
     */
    public EditButton() {
        super("toolbar-edit","Edit","","default");
        initialize();
    }
    
    @Override
    public void defineButton() {
        setConfirmable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        setEditable(Boolean.FALSE);
    }

    private void initialize() {
        initTitleText = "Editing the screen";
        initCaptionText = "Are you sure you want to make changes to this screen?";
        getSession().builtProperty().addListener((observableList1, oldValue1, newValue1) -> {
            if (newValue1) {
                UI parentUI = (UI) getScene().getWindow();
                parentUI.editModeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        setTitleText("Confirm Changes");
                        setCaptionText("Are you done making changes to this screen?");
                    } else {
                        setTitleText("Editing Screen");
                        setCaptionText("Are you sure you want to make changes to this screen?");
                    }
                });
            }
        });
    }

    @Override
    public void run() {
        LOGGER.info("Edit button clicked. Toggling parent edit mode");
        UI ui = (UI) getScene().getWindow();
        // ui.setEditMode(Boolean.TRUE);
        ui.toggleEditMode();
        LOGGER.info("Edit mode for UI is now " + ui.inEditMode());
        
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends EditButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
