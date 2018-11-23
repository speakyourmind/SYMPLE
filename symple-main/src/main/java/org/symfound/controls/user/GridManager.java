/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import static org.symfound.app.GridController.LOGGER;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.builder.controller.Editable;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.tools.selection.ParallelList;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class GridManager implements Editable {

    public Preferences getPrefs(String index) {
        String name = "subgrid/" + index.toLowerCase();
        Class<? extends GridManager> aClass = this.getClass();
        return Preferences.userNodeForPackage(aClass).node(name);
    }
    /**
     *
     */
    public ObjectProperty<ParallelList<String, String>> order;

    /**
     *
     * @param value
     */
    public void setOrder(ParallelList<String, String> value) {
        orderProperty().setValue(value);
        getPreferences().put("order", value.asString());
        LOGGER.info("Order set to: " + value.asString());
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
        final String initValue = "Replace Key=default";
        String value = getPreferences().get("order", initValue);
        ParallelList<String, String> parallelList = new ParallelList<>();
        String[] pairs = value.split(BUTTON_DELIMITER);
        for (String pair : pairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            parallelList.put(keyValue[0], keyValue[1]);
        }
        order = new SimpleObjectProperty<>(parallelList);
        return order;
    }

    private DoubleProperty gap;

    /**
     *
     * @param value
     */
    public void setGap(Double value) {
        gapProperty().set(value);
        getPreferences().put("gap", value.toString());
        LOGGER.info("Grid gap set to " + value.toString());
    }

    /**
     *
     * @return
     */
    public final Double getGap() {
        return gapProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty gapProperty() {
        if (gap == null) {
            Double value = Double.valueOf(getPreferences().get("gap", DEFAULT_GRID_GAP.toString()));
            gap = new SimpleDoubleProperty(value);
        }
        return gap;
    }

    /**
     *
     */
    public ObjectProperty<FillableGrid.FillMethod> fillMethod;

    /**
     *
     * @param value
     */
    public void setFillMethod(FillableGrid.FillMethod value) {
        fillMethodProperty().setValue(value);
        getPreferences().put("fillMethod", value.toString());
        LOGGER.info("FillMethod set to: " + value);

    }

    /**
     *
     * @return
     */
    public FillableGrid.FillMethod getFillMethod() {
        return fillMethodProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<FillableGrid.FillMethod> fillMethodProperty() {
        //  if (fillMethod == null) {
        String value = getPreferences().get("fillMethod", FillableGrid.FillMethod.COLUMN_WISE.toString());
        fillMethod = new SimpleObjectProperty<>(FillableGrid.FillMethod.valueOf(value));
        //}
        return fillMethod;
    }

    /**
     *
     */
    public ObjectProperty<FillableGrid.FillDirection> fillDirection;

    /**
     *
     * @param value
     */
    public void setFillDirection(FillableGrid.FillDirection value) {
        fillDirectionProperty().setValue(value);
        getPreferences().put("fillDirection", value.toString());
        LOGGER.info("FillDirection set to: " + value);

    }

    /**
     *
     * @return
     */
    public FillableGrid.FillDirection getFillDirection() {
        return fillDirectionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<FillableGrid.FillDirection> fillDirectionProperty() {
        //if (fillDirection == null) {
        String value = getPreferences().get("fillDirection", FillableGrid.FillDirection.FORWARD.toString());
        fillDirection = new SimpleObjectProperty<>(FillableGrid.FillDirection.valueOf(value));
        //}
        return fillDirection;
    }

    private DoubleProperty maxDifficulty;

    public void setMaxDifficulty(Double value) {
        maxDifficultyProperty().setValue(value);
        getPreferences().put("maxDifficulty", value.toString());
        LOGGER.info("Max Difficulty set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getMaxDifficulty() {
        return maxDifficultyProperty().getValue();
    }

    public DoubleProperty maxDifficultyProperty() {
        String value = getPreferences().get("maxDifficulty", "10.0");
        maxDifficulty = new SimpleDoubleProperty(Double.valueOf(value));
        return maxDifficulty;
    }

    private DoubleProperty minDifficulty;

    public void setMinDifficulty(Double value) {
        minDifficultyProperty().setValue(value);
        getPreferences().put("minDifficulty", value.toString());
        LOGGER.info("Min Difficulty set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getMinDifficulty() {
        return minDifficultyProperty().getValue();
    }

    public DoubleProperty minDifficultyProperty() {
        String value = getPreferences().get("minDifficulty", "0.0");
        minDifficulty = new SimpleDoubleProperty(Double.valueOf(value));
        return minDifficulty;
    }

    private DoubleProperty overrideRow;

    public void setOverrideRow(Double value) {
        overrideRowProperty().set(value);
        getPreferences().put("overrideRow", value.toString());
        LOGGER.info("Override Row set to: " + value);
    }

    public Double getOverrideRow() {
        return overrideRowProperty().getValue();
    }

    public DoubleProperty overrideRowProperty() {
        // if (overrideRow == null) {
        String value = getPreferences().get("overrideRow", "0.0");
        overrideRow = new SimpleDoubleProperty(Double.valueOf(value));
        //}
        return overrideRow;
    }

    private DoubleProperty overrideColumn;

    public void setOverrideColumn(Double value) {
        overrideColumnProperty().set(value);
        getPreferences().put("overrideColumn", value.toString());
        LOGGER.info("Override Column set to: " + value);
    }

    public Double getOverrideColumn() {
        return overrideColumnProperty().getValue();
    }

    public DoubleProperty overrideColumnProperty() {
        //  if (overrideColumn == null) {
        overrideColumn = new SimpleDoubleProperty(Double.valueOf(getPreferences().get("overrideColumn", "0.0")));
        //}
        return overrideColumn;
    }

    private StringProperty overrideStyle;

    /**
     *
     * @param value
     */
    public void setOverrideStyle(String value) {
        overrideStyleProperty().set(value);
        getPreferences().put("overrideStyle", value);
    }

    /**
     *
     * @return
     */
    public String getOverrideStyle() {
        return overrideStyleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty overrideStyleProperty() {
        if (overrideStyle == null) {
            overrideStyle = new SimpleStringProperty(getPreferences().get("overrideStyle", ""));
        }
        return overrideStyle;
    }
}
