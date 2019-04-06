/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.system.grid.editor.AddKeyButton;
import org.symfound.controls.system.grid.editor.EditGridButton;
import org.symfound.controls.system.grid.editor.KeyRemoveButton;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class SubGrid extends AppableControl {

    private static final String NAME = SubGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    /**
     *
     */
    public static final String KEY = "SubGrid";
    public static final String DESCRIPTION = "Button Group";

    /**
     *
     * @param key
     * @param index
     */
    public SubGrid(String key, String index) {
        super("", key, "", index);
        initialize();
    }

    /**
     *
     * @param index
     */
    public SubGrid(String index) {
        super("", KEY, "", index);
        initialize();
    }

    private void initialize() {
        // TO DO: Test with loadPrimaryControl Instead
        addToPane(getConfigurableGrid());
    }

    private ConfigurableGrid grid;

    /**
     *
     * @return
     */
    public ConfigurableGrid getConfigurableGrid() {
        if (grid == null) {
            grid = new ConfigurableGrid();
            grid.setIndex(getIndex());
            grid.configure();

            setCSS("subgrid", this);
        }
        return grid;
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
    }

    List<KeyRemoveButton> removeButtons;

    /**
     *
     */
    @Override
    public void configButtons() {
        boolean isSettingsControl = getControlType().equals(ControlType.SETTING_CONTROL);

        getConfigurableGrid().statusProperty().addListener((ob, o, n) -> {
            if (n.equals(ScreenStatus.PLAYING)) {
                if (ConfigurableGrid.inEditMode() && !isSettingsControl && isEditable()) {
                    final ObservableList<Node> children = this.getConfigurableGrid().getChildren();
                    addKeyRemoveButtons();

                    if (!this.getChildren().contains(getAddKeyButton())) {
                        addToPane(getAddKeyButton(), null, 0.0, 80.0, null);
                    }
                    if (!this.getChildren().contains(getEditGridButton())) {
                        addToPane(getEditGridButton(), null, 0.0, 0.0, null);
                    }
                }
            }
        });

        ConfigurableGrid.editModeProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1 && !isSettingsControl && isEditable()) {
                addConfigButtons();
            } else {
                removeConfigButtons();
            }
        });

    }

    /**
     *
     */
    @Override
    public void addConfigButtons() {
        addKeyRemoveButtons();
        addToPane(getAddKeyButton(), null, 0.0, 80.0, null);
        addToPane(getEditGridButton(), null, 0.0, 0.0, null);
    }

    /**
     *
     */
    @Override
    public void removeConfigButtons() {
        LOGGER.debug("Removing Key Remove & Add Keys from SubGrid " + getIndex());
//        getKeyRemoveButton().removeFromParent();
        if (!getRemoveButtons().isEmpty()) {
            getRemoveButtons().forEach((removeButton) -> {
                removeButton.removeFromParent();
            });
            getRemoveButtons().clear();
        }
        if (editGridButton != null) {
            getEditGridButton().removeFromParent();
            editGridButton = null;
        }
        if (addKeyButton != null) {
            getAddKeyButton().removeFromParent();
            addKeyButton = null;
        }
    }

    /**
     *
     */
    public void addKeyRemoveButtons() {
        final ObservableList<Node> children = this.getConfigurableGrid().getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof RunnableControl) {
                RunnableControl control = (RunnableControl) node;
                final KeyRemoveButton keyRemoveButton = new KeyRemoveButton(control.getGridLocation(), getConfigurableGrid());
                keyRemoveButton.gridLocationProperty().bindBidirectional(control.gridLocationProperty());
                keyRemoveButton.setPane("apMain");
                //  keyRemoveButton.setConfirmable(false);
                getRemoveButtons().add(keyRemoveButton);
                control.addToPane(keyRemoveButton, 0.0, null, null, 0.0);
                keyRemoveButton.toFront();
                LOGGER.info("Key Remove Button added to control " + control.getText() + " with index " + control.getGridLocation() + ". Order is " + getConfigurableGrid().getOrder().asString());
            }
        }
    }
    EditGridButton editGridButton;

    /**
     *
     * @return
     */
    public EditGridButton getEditGridButton() {
        if (editGridButton == null) {
            editGridButton = new EditGridButton(getConfigurableGrid());
            editGridButton.setMinHeight(80.0);
            editGridButton.setPane("apMain");
            editGridButton.toFront();
        }
        return editGridButton;
    }

    AddKeyButton addKeyButton;

    /**
     *
     * @return
     */
    public AddKeyButton getAddKeyButton() {
        if (addKeyButton == null) {
            addKeyButton = new AddKeyButton(getConfigurableGrid());
            addKeyButton.setMinHeight(80.0);
            addKeyButton.setPane("apMain");
            addKeyButton.toFront();
        }
        return addKeyButton;
    }

    /**
     *
     * @return
     */
    public List<KeyRemoveButton> getRemoveButtons() {
        if (removeButtons == null) {
            removeButtons = new ArrayList<>(Arrays.asList(new KeyRemoveButton(null, getConfigurableGrid())));// TO DO: Make cleaner
        }
        return removeButtons;
    }

    /**
     *
     */
    public static final Boolean DEFAULT_INERROR_VALUE = Boolean.FALSE;
    private BooleanProperty inError;

    /**
     *
     * @return
     */
    public Boolean isInError() {
        return inErrorProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setInError(Boolean value) {
        inErrorProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public BooleanProperty inErrorProperty() {
        if (inError == null) {
            inError = new SimpleBooleanProperty(DEFAULT_INERROR_VALUE);
        }
        return inError;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends SubGrid> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
