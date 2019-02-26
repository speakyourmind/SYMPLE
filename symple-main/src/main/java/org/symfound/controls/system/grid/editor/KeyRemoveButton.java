/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.prefs.Preferences;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.SystemControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed
 */
public class KeyRemoveButton extends SystemControl {

    /**
     *
     */
    public static final String NAME = KeyRemoveButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private final Integer orderIndexInit;
    private final ConfigurableGrid configurableGrid;

    /**
     *
     */
    public static final String KEY = "Remove Key";

    /**
     *
     * @param orderIndex
     * @param configurableGrid
     */
    public KeyRemoveButton(Integer orderIndex, ConfigurableGrid configurableGrid) {
        super("toolbar-exit", KEY, "", "default");
        this.orderIndexInit = orderIndex;
        this.configurableGrid = configurableGrid;
        initialize();
    }

    private void initialize() {

        setOrder(configurableGrid.getOrder());

        setConfirmable(Boolean.TRUE);
      //  setPane("apMain");
//        initTitleText = "Removing Button";
  //      initCaptionText = "Are you sure you want to permanently remove this button?";
       setTitleText("Removing Button");
       setCaptionText("Are you sure you want to permanently remove this button?");
        setOkText("CONFIRM");
        setCancelText("CANCEL");

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

    /**
     *
     */
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

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends KeyRemoveButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
