/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import org.apache.log4j.Logger;
import org.symfound.controls.ConfirmableControl;
import org.symfound.controls.ScreenControl.ControlType;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public class EditButton extends ConfirmableControl {

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
    public static final String DEFAULT_TITLE = "Edit Home Grid";

    /**
     *
     */
    public EditButton() {
        super("toolbar-edit");
        initialize();
    }

    private void initialize() {
        setControlType(ControlType.SETTING_CONTROL);
        primary = new AnimatedButton();
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
        initTitleText = "Editing the screen";
        initCaptionText = "Are you sure you want to change this screen?";
        getSession().builtProperty().addListener((observableList1, oldValue1, newValue1) -> {
            if (newValue1) {
                UI parentUI = (UI) getScene().getWindow();
                parentUI.editModeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        setTitleText("Confirm Screen Edits");
                        setCaptionText("Are you done editing this screen?");
                    } else {
                        setTitleText("Editing Screen");
                        setCaptionText("Are you sure you want to edit the screen?");
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
        LOGGER.info("Edit mode for UI is now" + ui.inEditMode());
    }

}
