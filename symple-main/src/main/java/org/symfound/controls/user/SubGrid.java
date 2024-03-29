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
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.grid.editor.AddKeyButton;
import org.symfound.controls.system.grid.editor.EditGridButton;
import org.symfound.controls.system.grid.editor.KeyRemoveButton;
import org.symfound.controls.system.grid.editor.ReplaceKeyButton;
import org.symfound.main.Main;
import org.symfound.selection.modes.Scanner;
import org.symfound.selection.modes.Scroller;
import org.symfound.selection.modes.Stepper;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class SubGrid extends AppableControl {

    private static final String NAME = SubGrid.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);
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

    public SubGrid(String index, Boolean rootGrid) {
        super("", KEY, "", index);
        initialize();
    }

    private void initialize() {
        final Double customMargin = getConfigurableGrid().getGridManager().getCustomMargin();
        addToPane(getConfigurableGrid(), customMargin, customMargin, customMargin, customMargin);
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

    private ChangeListener<Boolean> addRemoveListener;

    /**
     *
     */
    @Override
    public void configButtons() {
        boolean isSettingsControl = getControlType().equals(ControlType.SETTING_CONTROL);

        getConfigurableGrid().statusProperty().addListener((ob, o, n) -> {
            if (n.equals(ScreenStatus.PLAYING)) {
                getScanner().configure();
                getStepper().configure();
                getScroller().configure();
                
                setDisable(false);
                if (ConfigurableGrid.inEditMode() && !isSettingsControl && isEditable()) {
                    addConfigButtons();
                } else {
                    removeConfigButtons();
                }
            } else {
                setDisable(true);
            }
        });
        final BooleanProperty editModeProperty = ConfigurableGrid.editModeProperty();
        editModeProperty.removeListener(getConfigButtonListener());
        editModeProperty.addListener(getConfigButtonListener());

    }

    public ChangeListener<Boolean> getConfigButtonListener() {
        if (addRemoveListener == null) {
            addRemoveListener = (observable1, oldValue1, newValue1) -> {

                if (ConfigurableGrid.inEditMode() && isEditable()) {
                    addConfigButtons();
                } else {
                    removeConfigButtons();
                }
            };
        }
        return addRemoveListener;
    }

    /**
     *
     */
    @Override
    public void addConfigButtons() {
        addKeyRemoveButtons();
        addEditAppButtons();
        if (!getChildren().contains(getMenu())) {
            gridMenu = null;
            if (this.getConfigurableGrid().isRootGrid()) {
                addToPane(getMenu(), 10.0, 10.0, null, null);
            } else {
                addToPane(getMenu(), 10.0, 90.0, null, null);
            }
        }

    }

    private BuildableGrid gridMenu;

    public BuildableGrid getMenu() {
        if (gridMenu == null) {
            gridMenu = new BuildableGrid();
            gridMenu.setSpecRows(2);
            gridMenu.setSpecColumns(1);
            gridMenu.build();
            gridMenu.add(getAddKeyButton(), 0, 0);
            gridMenu.add(getEditGridButton(), 0, 1);
            gridMenu.setMaxHeight(110.0);
            gridMenu.setMaxWidth(20.0);
            gridMenu.setVgap(10.0);
        }
        return gridMenu;
    }

    /**
     *
     */
    @Override
    public void removeConfigButtons() {
        LOGGER.debug("Removing Key Remove & Add Keys from SubGrid " + getIndex());
        if (!getRemoveButtons().isEmpty()) {
            getRemoveButtons().forEach((removeButton) -> {
                removeButton.removeFromParent();
            });
            getRemoveButtons().clear();
        }
        if (!getEditButtons().isEmpty()) {
            getEditButtons().forEach((editButton) -> {
                editButton.removeFromParent();
            });
            getEditButtons().clear();
        }

        final ObservableList<Node> children = this.getConfigurableGrid().getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof AppableControl) {
                AppableControl control = (AppableControl) node;
                control.getPrimaryControl().setDisable(control.isPrimaryDisabled());

            }
        }
        getChildren().remove(getMenu());

    }

    /**
     *
     */
    public void addKeyRemoveButtons() {
        final ObservableList<Node> children = this.getConfigurableGrid().getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof AppableControl) {
                AppableControl control = (AppableControl) node;
                if (control.isRemovable()) {

                    final KeyRemoveButton keyRemoveButton = new KeyRemoveButton(control.getGridLocation(), getConfigurableGrid());
                    keyRemoveButton.gridLocationProperty().bindBidirectional(control.gridLocationProperty());
                    keyRemoveButton.setPane("apMain");
                    //  keyRemoveButton.setConfirmable(false);
                    getRemoveButtons().add(keyRemoveButton);
                    control.addToPane(keyRemoveButton, 0.0, null, null, 0.0);

                    keyRemoveButton.setVisible(!(control.getGridLocation() == 0 && control.getKey().equals((ReplaceKeyButton.KEY))));
                    keyRemoveButton.toFront();

                }
            }
        }
    }

    public void addEditAppButtons() {
        final ObservableList<Node> children = this.getConfigurableGrid().getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof AppableControl) {
                AppableControl control = (AppableControl) node;
                boolean isSettingsControl = control.getControlType().equals(ControlType.SETTING_CONTROL);
                if (ConfigurableGrid.inEditMode() && !isSettingsControl && isEditable()) {

                    final EditAppButton button = control.getEditAppButton();
                    getEditButtons().add(button);
                    control.addToPane(button, null, null, 0.0, 0.0);
                    button.toFront();
                    control.getPrimaryControl().setDisable(true);

                }
            }
        }
    }
    EditGridButton editGridButton;

    /**
     *
     * @return
     */
    public EditGridButton getEditGridButton() {
        // if (editGridButton == null) {
        editGridButton = new EditGridButton(this.getConfigurableGrid());
        editGridButton.setMinHeight(5.0);
        editGridButton.setPrefHeight(60.0);
        editGridButton.setMaxHeight(120.0);
        editGridButton.setPane("apMain");
        editGridButton.toFront();
        //}
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
            addKeyButton.setMinHeight(5.0);
            addKeyButton.setPrefHeight(60.0);
            addKeyButton.setMaxHeight(120.0);
            addKeyButton.setPane("apMain");
            addKeyButton.toFront();
        }
        return addKeyButton;
    }

    List<EditAppButton> editButtons;

    /**
     *
     * @return
     */
    public List<EditAppButton> getEditButtons() {
        if (editButtons == null) {
            editButtons = new ArrayList<>();// TO DO: Make cleaner
        }
        return editButtons;
    }

    List<KeyRemoveButton> removeButtons;

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

    /**
     *
     */
    public Scanner scanner;

    /**
     *
     * @return
     */
    public Scanner getScanner() {
        // if (scanner == null) {
        scanner = new Scanner(this, Main.getSession().getUser());
        //}
        return scanner;
    }

    /**
     *
     */
    public Stepper stepper;

    /**
     *
     * @return
     */
    public Stepper getStepper() {
        if (stepper == null) {
            stepper = new Stepper(this, Main.getSession().getUser());
        }
        return stepper;
    }
    public Scroller scroller;

    /**
     *
     * @return
     */
    public Scroller getScroller() {
        if (scroller == null) {
            scroller = new Scroller(this, Main.getSession().getUser());
        }
        return scroller;
    }
}
