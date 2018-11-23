/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.ConfirmableControl;
import org.symfound.controls.system.grid.editor.ReplaceKeyButton;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.selection.ParallelList;

/**
 *
 * @author Javed
 */
public class KeyRemoveButton extends ConfirmableControl {

    public static final String NAME = KeyRemoveButton.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private Integer orderIndexInit;
    private ConfigurableGrid configurableGrid;

    public KeyRemoveButton(Integer orderIndex, ConfigurableGrid configurableGrid) {
        super("toolbar-exit");
        this.orderIndexInit = orderIndex;
        this.configurableGrid = configurableGrid;
        initialize();
    }

    private void initialize() {
        setControlType(ControlType.SETTING_CONTROL);
        primary = new AnimatedButton();
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
        setOrder(configurableGrid.getOrder());

    }

    @Override
    public void run() {
        SettingsController.setUpdated(Boolean.FALSE);
        final ParallelList<String, String> order1 = getOrder();
        if (order1.getFirstList().get(getOrderIndex()).equals(ReplaceKeyButton.KEY)) {
            order1.remove(getOrderIndex());
        } else {
            order1.getFirstList().set(getOrderIndex(), ReplaceKeyButton.KEY);
            order1.getSecondList().set(getOrderIndex(), "default");
        }
        setOrder(order1);
        configurableGrid.setOrder(getOrder());
        configurableGrid.getGridManager().setOrder(getOrder());

        ConfigurableGrid.setEditMode(false);
        SettingsController.setUpdated(Boolean.TRUE);
    }
    private ObjectProperty<ParallelList<String, String>> order;

    /**
     *
     * @param value
     */
    public void setOrder(ParallelList<String, String> value) {
        orderProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public ParallelList<String, String> getOrder() {
        return orderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ParallelList<String, String>> orderProperty() {
        if (order == null) {
            order = new SimpleObjectProperty<>();
        }
        return order;
    }

    public IntegerProperty orderIndex;

    /**
     *
     * @param value
     */
    public void setOrderIndex(Integer value) {
        orderIndexProperty().set(value);
    }

    /**
     *
     * @return
     */
    public Integer getOrderIndex() {
        return orderIndexProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty orderIndexProperty() {
        if (orderIndex == null) {
            orderIndex = new SimpleIntegerProperty(orderIndexInit);
        }
        return orderIndex;
    }

}
