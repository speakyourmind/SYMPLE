/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.main.builder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.component.BaseUI;
import org.symfound.builder.loader.UIPath;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee
 */
public abstract class UI extends BaseUI {

    private static final String NAME = UI.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public Scene scene;

    /**
     *
     */
    public UI() {

    }

    /**
     *
     * @param file
     * @param hasMenu
     */
    public UI(String file) {
        super();
        this.file = new UIPath(file);
    }

    /**
     *
     * @param basePane
     * @param setProp
     * @return
     */
    public Scene create(CommonPane basePane, Boolean setProp) {
        scene = new Scene(basePane);
        scene.setCursor(Cursor.DEFAULT);
        scene.setOnKeyReleased((KeyEvent event) -> {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case SHIFT:
                        LOGGER.info("Key Pressed: " + event.getCode().getName() + "Toggling Mouse Control");
                        Hardware currentHardware = Main.getSession().getDeviceManager().getCurrent().getHardware();
                        Movability movability = currentHardware.getMovability();
                        movability.setEnabled(!movability.isEnabled());
                        LOGGER.info("Mouse Control is now " + movability.isEnabled());
                        if(!movability.isEnabled()&& scene.getCursor().equals(Cursor.NONE)){
                            LOGGER.info("Making cursor visible");
                            scene.setCursor(Cursor.DEFAULT);
                        }
                        break;
                    case TAB:
                        LOGGER.info("Key Pressed: " + event.getCode().getName());
                        if (!scene.getCursor().equals(Cursor.NONE)) {
                            scene.setCursor(Cursor.NONE);
                        } else {
                            scene.setCursor(Cursor.DEFAULT);
                        }
                        break;
                    case ALT:
                        AnimatedButton primaryControl = basePane.getMenuLaunchButton().getPrimaryControl();
                        primaryControl.setVisible(!primaryControl.isVisible());
                        break;
                    default:
                        break;
                }
            }

        });

        // Set stage title
        setTitle(APP_TITLE);
        initModality(Modality.APPLICATION_MODAL);
        // Add icon
        getIcons().add(new Image("images/icon.png"));
        supressFullScreenHint();
        // Load scene to stage
        setScene(scene);
        
        if (setProp) {
            // Set the properties of the stage from the config file
            setProperties();
        }
        // Required to exit cleanly when the window is closed
        setOnCloseRequest((WindowEvent t) -> {
            Main.getSession().exit(Boolean.TRUE);
        });
        return scene;
    }

    /**
     * Set the properties of the stage including fullscreen, resizeable,
     * maximized and style
     *
     */
    public void setProperties() {
        scene.setCursor(Cursor.DEFAULT);
        // Get the properties from the config file
        fullscreen = Boolean.parseBoolean(file.getBundle().getString("fullscreen"));
        resizeable = Boolean.parseBoolean(file.getBundle().getString("resizeable"));
        initStyle(StageStyle.valueOf(file.getBundle().getString("style")));
        setCurrentProperties();
    }

    /**
     *
     * @param parent
     */
    public static void openAsPopup(Parent parent) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(new Group(), 1, 1);
        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.getIcons().add(new Image("images/icon.png"));
        stage.show();

        Popup popup = new Popup();
        popup.getContent().add(parent);
        //  popup.setOpacity(0.5);
        popup.show(stage);
    }

    /**
     *
     * @param builder
     */
    public abstract void build(Builder builder);

    private UIPath file;

    /**
     *
     * @return
     */
    public String getPath() {
        return file.get();
    }

    /**
     *
     * @return
     */
    public UIPath getUIPath() {
        return file;
    }

    //=====================USABILITY METHODS====================================

    /**
     *
     * @return
     */
    public Boolean isEnabled() {
        return Boolean.valueOf(file.getBundle().getString(""));
    }

    /**
     *
     * @param difficulty
     * @return
     */
    public Boolean isWithinAbility(Double difficulty) {
        // TO DO: Test difficulty validity
        return getDifficulty() <= difficulty;
    }

    /**
     *
     * @param selectionMode
     * @return
     */
    public Boolean isSelectable(SelectionMethod selectionMode) {
        return Boolean.valueOf(file.getBundle().getString(selectionMode.toString().toLowerCase()));
    }

    /**
     * Determines whether this <code>UI</code> is usable by the
     * <code>User</code> in the parameter. This is determined by 2 criteria
     * Users preferred mode of selection must be applicable to this UI and Users
     * ability must be within the difficulty of this UI
     *
     * @param user user that wants to use this UI
     * @return usable if true, false otherwise
     */
    //TO DO: Create similar methods in User with UI as input
    public Boolean isUsable(User user) {
        SelectionMethod selectionMethod = user.getInteraction().getSelectionMethod();
        // Checks if this UI is selectable by the user
        Boolean isSelectionMode = isSelectable(selectionMethod);
        // Checks if this users ability is within the difficulty of this UI
        Boolean isWithinAbility = isWithinAbility(user.getAbility().getLevel());

        return isSelectionMode && isWithinAbility;
    }

    private IntegerProperty difficulty;

    /**
     *
     * @param value
     */
    public void setDifficulty(Integer value) {
        difficultyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getDifficulty() {
        return difficultyProperty().getValue();
    }

    /**
     * Represents the difficulty of this UI. This is currently defined as the
     * number of maximum number of buttons in each dimension at any given time
     * while using the UI.
     * <b>NOTE:</b> This is currently set manually.
     *
     * @return
     */
    public IntegerProperty difficultyProperty() {
        if (difficulty == null) {
            Integer initValue = Integer.valueOf(file.getBundle().getString("difficulty"));
            difficulty = new SimpleIntegerProperty(initValue);
        }
        return difficulty;
    }

    private IntegerProperty priority;

    /**
     *
     * @param value
     */
    public void setPriority(Integer value) {
        priorityProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getPriority() {
        return priorityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty priorityProperty() {
        if (priority == null) {
            Integer initValue = Integer.valueOf(file.getBundle().getString("priority"));
            priority = new SimpleIntegerProperty(initValue);
        }
        return priority;
    }

    private StringProperty description;

    /**
     * Sets the description of this UI
     *
     * @param value
     */
    public void setDescription(String value) {
        descriptionProperty().setValue(value);
    }

    /**
     * Gets the description of this UI
     *
     * @return description
     */
    public String getDescription() {
        return descriptionProperty().getValue();
    }

    /**
     * Represents the description of this UI as set in the properties file
     *
     * @return description
     */
    public StringProperty descriptionProperty() {
        if (description == null) {
            String initValue = file.getBundle().getString("description");
            description = new SimpleStringProperty(initValue);
        }
        return description;
    }

    private final Boolean DEFAULT_EDIT_MODE = Boolean.FALSE;
    private BooleanProperty editMode;

    /**
     *
     * @param value
     */
    public void setEditMode(Boolean value) {
        editModeProperty().setValue(value);
    }

    /**
     *
     */
    public void toggleEditMode() {
        setEditMode(!inEditMode());
    }

    /**
     *
     * @return
     */
    public Boolean inEditMode() {
        return editModeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty editModeProperty() {
        if (editMode == null) {
            editMode = new SimpleBooleanProperty(DEFAULT_EDIT_MODE);
        }
        return editMode;
    }

}
