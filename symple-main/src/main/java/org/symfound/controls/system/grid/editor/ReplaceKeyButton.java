/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.symfound.controls.SystemControl;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ButtonGrid;
import static org.symfound.controls.user.ButtonGrid.OTHER_BUTTON;
import static org.symfound.controls.user.ButtonGrid.USABLE_OPTIONS_MAP;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.GenericButton;
import org.symfound.controls.user.ScreenButton;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class ReplaceKeyButton extends SystemControl {

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
    public ButtonGrid buttonGrid;

    /**
     *
     */
    public static final String KEY = "Replace Key";
    public static final String DESCRIPTION = "";

    /**
     *
     * @param buttonGrid
     */
    public ReplaceKeyButton(ButtonGrid buttonGrid) {
        super("toolbar-add", KEY, "", "default");
        this.buttonGrid = buttonGrid;

    }

    @Override
    public void defineButton() {
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        //  setConfirmable(Boolean.FALSE);
        setSelectable(Boolean.FALSE);
        setNavigatePostClick(Boolean.FALSE);
    }

    /**
     *
     * @return
     */
    @Override
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = configureEditDialog();
        }
        return settingsDialog;
    }

    /**
     *
     */
    public List<SettingsRow> orderSettings = new ArrayList<>();

    /**
     *
     * @return
     */
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog("Add Button") {
            TextArea buttonOrderField;
            ChoiceBox<String> buttonTypeChoices;
            ChoiceBox<String> otherTypeChoices;
            TextField buttonTitleField;
            ChoiceBox<String> existingButtonChoiceBox;
            ConfigurableGrid appGrid;
            DeleteKeyButton deleteKeyButton;

            @Override
            public Node addSettingControls() {
                SettingsRow buttonTypeRow = createSettingRow("Type", "Select the type of button you would like to add");
                buttonTypeChoices = new ChoiceBox<>(FXCollections.observableArrayList(new ArrayList<>(USABLE_OPTIONS_MAP.keySet())));
                buttonTypeChoices.setValue(DESCRIPTION);
                buttonTypeChoices.setMaxSize(180.0, 60.0);
                buttonTypeChoices.getStyleClass().add("settings-text-area");
                buttonTypeRow.add(buttonTypeChoices, 1, 0, 1, 1);

                otherTypeChoices = new ChoiceBox<>(FXCollections.observableArrayList(ButtonGrid.USABLE_KEY_CATALOGUE));
                otherTypeChoices.setValue(GenericButton.KEY);
                otherTypeChoices.setMaxSize(180.0, 60.0);
                otherTypeChoices.getStyleClass().add("settings-text-area");
                otherTypeChoices.visibleProperty().bind(Bindings.equal(buttonTypeChoices.valueProperty(), ButtonGrid.OTHER_BUTTON));

                buttonTypeRow.add(otherTypeChoices, 2, 0, 1, 1);

                SettingsRow buttonTitleRow = EditDialog.createSettingRow("Title", "Use an existing button or pick a unique title to create a new button");
                buttonTitleField = new TextField();
                buttonTitleField.setText("");
                buttonTitleField.setPromptText("Enter a name to create a new screen");
                buttonTitleField.setPrefSize(180.0, 60.0);
                buttonTitleField.getStyleClass().add("settings-text-area");
                buttonTitleRow.add(buttonTitleField, 2, 0, 1, 1);

                //  SettingsRow settingsRowA = createSettingRow("Navigate", "Screen to navigate to after click");
                List<String> nodes = new ArrayList<>();
                try {
                    final String buttonType = buttonTypeChoices.getValue();
                    nodes = getButtons(buttonType, buttonType);
                } catch (BackingStoreException ex) {
                    LOGGER.fatal("Unable to load Preferences" + ex.getMessage());
                }
                existingButtonChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(nodes));

                existingButtonChoiceBox.disableProperty().bind(Bindings.notEqual(buttonTitleField.textProperty(), ""));

                buttonTypeChoices.valueProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        String buttonType = (buttonTypeChoices.getValue().equals(OTHER_BUTTON)) ? otherTypeChoices.getValue().toLowerCase() : USABLE_OPTIONS_MAP.get(buttonTypeChoices.getValue()).toLowerCase();
                        if (buttonType.equals(ScreenButton.KEY.toLowerCase())) {
                            buttonType = SubGrid.KEY.toLowerCase();
                        }
                        final List<String> buttons = getButtons(buttonType, buttonType);
                        existingButtonChoiceBox.setItems(FXCollections.observableArrayList(buttons));
                    } catch (BackingStoreException ex) {
                        LOGGER.warn(ex);
                    }
                });

                otherTypeChoices.valueProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        String buttonType = (buttonTypeChoices.getValue().equals(OTHER_BUTTON)) ? otherTypeChoices.getValue().toLowerCase() : USABLE_OPTIONS_MAP.get(buttonTypeChoices.getValue()).toLowerCase();
                        final List<String> buttons = getButtons(buttonType, buttonType);
                        existingButtonChoiceBox.setItems(FXCollections.observableArrayList(buttons));
                    } catch (BackingStoreException ex) {
                        LOGGER.warn(ex);
                    }
                });
                existingButtonChoiceBox.setMaxSize(180.0, 60.0);
                existingButtonChoiceBox.getStyleClass().add("settings-text-area");
                buttonTitleRow.add(existingButtonChoiceBox, 1, 0, 1, 1);

                orderSettings.add(buttonTypeRow);
                orderSettings.add(buttonTitleRow);
                Tab orderTab = buildTab("ADD", orderSettings);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

                tabPane.getTabs().add(orderTab);

                return tabPane;
            }

            @Override
            public void setSettings() {

                final ParallelList<String, String> order1 = buttonGrid.getOrder();

                final String buttonType = (buttonTypeChoices.getValue().equals(OTHER_BUTTON)) ? otherTypeChoices.getValue() : USABLE_OPTIONS_MAP.get(buttonTypeChoices.getValue());
                order1.getFirstList().set(getGridLocation(), buttonType);
                String index;
                if (!buttonTitleField.getText().isEmpty()) {
                    index = buttonTitleField.getText();
                } else {
                    // TO DO - Set to default if choice box is also empty
                    if (existingButtonChoiceBox.getValue() != null) {
                        index = existingButtonChoiceBox.getValue();
                    } else {
                        index = "default";
                    }
                }
                order1.getSecondList().set(getGridLocation(), index);
                final Parent parent = ReplaceKeyButton.this.getParent();
                ConfigurableGrid configurableGrid;
                if (parent instanceof ConfigurableGrid) {
                    configurableGrid = (ConfigurableGrid) parent;
                    configurableGrid.setOrder(order1);
                    configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
                }

                SettingsController.setUpdated(true);

            }

            @Override
            public void resetSettings() {
                buttonTypeChoices.setValue(DESCRIPTION);
                buttonTitleField.setText(getIndex().toLowerCase());
                //  buttonOrderField.setText(KEY + "=" + getIndex().toLowerCase());

                SettingsController.setUpdated(false);
            }
        };
        return editDialog;
    }

    private List<String> getButtons(String node, String alt) throws BackingStoreException {

       // System.out.println("Node:" + node);
        List<String> screenNames = new ArrayList<>();
        final Preferences userNodeForPackage = Preferences.userNodeForPackage(AnimatedButton.class);
        //System.out.println(userNodeForPackage.name());
        List<String> childrenNames = Arrays.asList(userNodeForPackage.node(node).childrenNames());
        //System.out.println(childrenNames);
        if (childrenNames.size() > 0) {
            for (String child : childrenNames) {
                final String name = node + "/" + child;
                screenNames.add(name.replaceAll(alt + "/", ""));
                screenNames.addAll(getButtons(name, alt));
            }
        }

        return screenNames;
    }

    @Override
    public void run() {
        LOGGER.info("Replace Keys button clicked");
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ReplaceKeyButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
