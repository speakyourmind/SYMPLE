/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.controls.SystemControl;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ButtonGrid;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class ReplaceKeyButton extends SystemControl {

    public static final String NAME = EditGridButton.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);
    public ButtonGrid buttonGrid;

    /**
     *
     */
    public static final String KEY = "Replace Key";

    /**
     *
     * @param buttonGrid
     */
    public ReplaceKeyButton(ButtonGrid buttonGrid) {
        super("toolbar-add", KEY, "", "default");
        this.buttonGrid = buttonGrid;
        initialize();
    }

    private void initialize() {
        setConfirmable(Boolean.TRUE);
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

    public List<SettingsRow> orderSettings = new ArrayList<>();

    /**
     *
     * @return
     */
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog() {
            TextArea buttonOrderField;
            BuildableGrid buttonOrderGrid;
            ConfigurableGrid appGrid;
            DeleteKeyButton deleteKeyButton;

            @Override
            public Node addSettingControls() {

                SettingsRow buttonOrderRow = createSettingRow("Button Order", "Placeholder method to change app order");
                buttonOrderField = new TextArea();
                buttonOrderField.setStyle("-fx-font-size:1.6em;");
                buttonOrderField.setWrapText(true);
                buttonOrderField.setText(KEY + "=" + getIndex().toLowerCase());
                buttonOrderField.maxHeight(80.0);
                buttonOrderField.maxWidth(360.0);
                buttonOrderField.getStyleClass().add("settings-text-area");
                buttonOrderRow.add(buttonOrderField, 1, 0, 2, 1);

                orderSettings.add(buttonOrderRow);
                Tab orderTab = buildTab("ORDER", orderSettings);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(orderTab);


                /*    ParallelList<String, String> list = new ParallelList<>();
                
                ButtonGrid.KEY_CATALOGUE.forEach((key) -> {
                    list.put("Script", "picker/"+key);
                });

                appGrid = new ConfigurableGrid();
                appGrid.getGridManager().setOrder(list);
                appGrid.setOverrideColumn(3.0);
                appGrid.setOverrideRow(10.0);
                
                appGrid.configure();
                
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(Boolean.TRUE);
                scrollPane.setFitToHeight(Boolean.TRUE);
                scrollPane.setContent(appGrid);

                Tab orderTab = new Tab("SELECT APP");
                orderTab.setContent(scrollPane);
                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(orderTab);*/
                return tabPane;
            }

            @Override
            public void setSettings() {
                final ParallelList<String, String> order1 = buttonGrid.getOrder();
                String[] pairs = buttonOrderField.getText().split(BUTTON_DELIMITER);
                for (String pair : pairs) {
                    String[] keyValue = pair.split(KEY_DELIMITER);
                    order1.getFirstList().set(getGridLocation(), keyValue[0]);
                    order1.getSecondList().set(getGridLocation(), keyValue[1]);

                }
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
                buttonOrderField.setText(KEY + "=" + getIndex().toLowerCase());
                SettingsController.setUpdated(false);
            }
        };
        return editDialog;
    }

    @Override
    public void run() {
        LOGGER.info("Add Keys button clicked");
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
