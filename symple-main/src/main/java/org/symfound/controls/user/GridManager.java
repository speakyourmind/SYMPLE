/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.controller.Editable;
import static org.symfound.builder.user.characteristic.Ability.MAX_LEVEL;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.selection.SelectionMethod;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import static org.symfound.controls.user.CommonGrid.DEFAULT_MARGIN;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class GridManager implements Editable {

    private static final String NAME = GridManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    /**
     *
     * @param index
     * @return
     */
    public Preferences getPrefs(String index) {
        String name = "subgrid/" + index.toLowerCase();
        Class<? extends GridManager> aClass = this.getClass();
        final Preferences node = Preferences.userNodeForPackage(aClass).node(name);
        return node;
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
            parallelList.add(keyValue[0], keyValue[1]);
        }
        order = new SimpleObjectProperty<>(parallelList);
        return order;
    }
    
    private StringProperty description;

    /**
     *
     * @param value
     */
    public void setDescription(String value) {
        descriptionProperty().set(value);
        getPreferences().put("description", value);
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return descriptionProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty descriptionProperty() {
        //    if (description == null) {
        description = new SimpleStringProperty(getPreferences().get("description", ""));
        //  }
        return description;
    }

    private DoubleProperty customHGap;

    /**
     *
     * @param value
     */
    public void setCustomHGap(Double value) {
        customHGapProperty().set(value);
        getPreferences().put("gap", value.toString());
        LOGGER.info("Grid H gap set to " + value.toString());
    }

    /**
     *
     * @return
     */
    public final Double getCustomHGap() {
        return customHGapProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty customHGapProperty() {
        Double value = Double.valueOf(getPreferences().get("gap", DEFAULT_GRID_GAP.toString()));
        customHGap = new SimpleDoubleProperty(value);
        return customHGap;
    }
    
     private DoubleProperty customVGap;

    /**
     *
     * @param value
     */
    public void setCustomVGap(Double value) {
        customVGapProperty().set(value);
        getPreferences().put("vGap", value.toString());
        LOGGER.info("Grid V gap set to " + value.toString());
    }

    /**
     *
     * @return
     */
    public final Double getCustomVGap() {
        return customVGapProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty customVGapProperty() {
        Double value = Double.valueOf(getPreferences().get("vGap", getCustomHGap().toString()));
        customVGap = new SimpleDoubleProperty(value);
        return customVGap;
    }

    
     private DoubleProperty customMargin;

    /**
     *
     * @param value
     */
    public void setCustomMargin(Double value) {
        customMarginProperty().set(value);
        getPreferences().put("margin", value.toString());
        LOGGER.info("Grid Margin set to " + value.toString());
    }

    /**
     *
     * @return
     */
    public final Double getCustomMargin() {
        return customMarginProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty customMarginProperty() {
        Double value = Double.valueOf(getPreferences().get("margin",DEFAULT_MARGIN.toString()));
        customMargin = new SimpleDoubleProperty(value);
        return customMargin;
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
        String value = getPreferences().get("fillMethod", FillableGrid.FillMethod.COLUMN_WISE.toString());
        fillMethod = new SimpleObjectProperty<>(FillableGrid.FillMethod.valueOf(value));
        
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

        String value = getPreferences().get("fillDirection", FillableGrid.FillDirection.FORWARD.toString());
        fillDirection = new SimpleObjectProperty<>(FillableGrid.FillDirection.valueOf(value));
        return fillDirection;
    }

    private DoubleProperty maxDifficulty;

    /**
     *
     * @param value
     */
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

    /**
     *
     * @return
     */
    public DoubleProperty maxDifficultyProperty() {
        String value = getPreferences().get("maxDifficulty", MAX_LEVEL.toString());
        maxDifficulty = new SimpleDoubleProperty(Double.valueOf(value));
        return maxDifficulty;
    }

    private DoubleProperty minDifficulty;

    /**
     *
     * @param value
     */
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

    /**
     *
     * @return
     */
    public DoubleProperty minDifficultyProperty() {
        String value = getPreferences().get("minDifficulty", "0.0");
        minDifficulty = new SimpleDoubleProperty(Double.valueOf(value));
        return minDifficulty;
    }

    private DoubleProperty overrideRow;

    /**
     *
     * @param value
     */
    public void setOverrideRow(Double value) {
        overrideRowProperty().set(value);
        getPreferences().put("overrideRow", value.toString());
        LOGGER.info("Override Row set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideRow() {
        return overrideRowProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty overrideRowProperty() {
        // if (overrideRow == null) {
        String value = getPreferences().get("overrideRow", "0.0");
        overrideRow = new SimpleDoubleProperty(Double.valueOf(value));
        //}
        return overrideRow;
    }

    private DoubleProperty overrideColumn;

    /**
     *
     * @param value
     */
    public void setOverrideColumn(Double value) {
        overrideColumnProperty().set(value);
        getPreferences().put("overrideColumn", value.toString());
        LOGGER.info("Override Column set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideColumn() {
        return overrideColumnProperty().getValue();
    }

    /**
     *
     * @return
     */
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
        //    if (overrideStyle == null) {
        overrideStyle = new SimpleStringProperty(getPreferences().get("overrideStyle", ""));
        //  }
        return overrideStyle;
    }

    private static final String SELECTION_METHOD_KEY = "selectionMethod";
    private ObjectProperty<SelectionMethod> selectionMethod;

    /**
     * Sets the user's selection preference
     *
     * @param value
     */
    public void setSelectionMethod(SelectionMethod value) {
        selectionMethodProperty().setValue(value);
        getPreferences().put(SELECTION_METHOD_KEY, value.toString());

    }

    /**
     * Get how the user wants to interact with the program
     *
     * @return selection mode
     */
    public SelectionMethod getSelectionMethod() {
        return selectionMethodProperty().getValue();
    }

    /**
     * Defines the way in which the user interacts with the program, that is,
     * the selection mode.
     *
     * @return selection
     */
    public ObjectProperty<SelectionMethod> selectionMethodProperty() {
        // if (selectionMethod == null) {
        selectionMethod = new SimpleObjectProperty<>(SelectionMethod.valueOf(getPreferences().get(SELECTION_METHOD_KEY, SelectionMethod.CLICK.toString())));
        //}
        return selectionMethod;
    }

    private BooleanProperty paused;

    /**
     *
     * @param value
     */
    public void setPaused(Boolean value) {
        pausedProperty().set(value);
        getPreferences().put("paused", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isPaused() {
        return pausedProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty pausedProperty() {
        //  if (paused == null) {
        paused = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("paused", "false")));
        //}
        return paused;
    }

    
      private BooleanProperty enablePagination;

    /**
     *
     * @param value
     */
    public void enablePagination(Boolean value) {
        enablePaginationProperty().set(value);
        getPreferences().put("enablePagination", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isPaginationEnabled() {
        return enablePaginationProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty enablePaginationProperty() {
        //  if (enablePagination == null) {
        enablePagination = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("enablePagination", "false")));
        //}
        return enablePagination;
    }
    
      private BooleanProperty fitToWidth;

    /**
     *
     * @param value
     */
    public void setFitToWidth(Boolean value) {
        fitToWidthProperty().set(value);
        getPreferences().put("fitToWidth", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isFitToWidth() {
        return fitToWidthProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty fitToWidthProperty() {
        //  if (fitToWidth == null) {
        fitToWidth = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("fitToWidth", "true")));
        //}
        return fitToWidth;
    }
    
    
    private DoubleProperty overrideWidth;

    /**
     *
     * @param value
     */
    public void setOverrideWidth(Double value) {
        overrideWidthProperty().setValue(value);
        getPreferences().put("overrideWidth", value.toString());
        LOGGER.info("Width set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideWidth() {
        return overrideWidthProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty overrideWidthProperty() {
        String value = getPreferences().get("overrideWidth", MAX_LEVEL.toString());
        overrideWidth = new SimpleDoubleProperty(Double.valueOf(value));
        return overrideWidth;
    }
    
       private BooleanProperty fitToHeight;

    /**
     *
     * @param value
     */
    public void setFitToHeight(Boolean value) {
        fitToHeightProperty().set(value);
        getPreferences().put("fitToHeight", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isFitToHeight() {
        return fitToHeightProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty fitToHeightProperty() {
        //  if (fitToHeight == null) {
        fitToHeight = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("fitToHeight", "true")));
        //}
        return fitToHeight;
    }
    
    
    
    private DoubleProperty overrideHeight;

    /**
     *
     * @param value
     */
    public void setOverrideHeight(Double value) {
        overrideHeightProperty().setValue(value);
        getPreferences().put("overrideHeight", value.toString());
        LOGGER.info("Height set to: " + value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideHeight() {
        return overrideHeightProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty overrideHeightProperty() {
        String value = getPreferences().get("overrideHeight", MAX_LEVEL.toString());
        overrideHeight = new SimpleDoubleProperty(Double.valueOf(value));
        return overrideHeight;
    }
    
}
