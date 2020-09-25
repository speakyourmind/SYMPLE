/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import java.io.File;
import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.HomeController;
import org.symfound.main.Main;
import org.symfound.voice.player.AudioPlayer;

/**
 *
 * @author Javed
 */
public class Scourer {

    private static final String NAME = Scourer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private final SubGrid gridToScour;

    /**
     *
     * @param grid
     */
    public Scourer(SubGrid grid) {
        this.gridToScour = grid;
    }

    /**
     *
     */
    public void clearHighlight() {
        for (int i = 0; i < gridToScour.getConfigurableGrid().getChildren().size(); i++) {
            Node get = gridToScour.getConfigurableGrid().getChildren().get(i);
            if (get instanceof AppableControl) {
                AppableControl node = (AppableControl) get;
                node.getPrimaryControl().getStyleClass().remove("highlight");
                if (node instanceof SubGrid) {
                    get.getStyleClass().remove("highlight");
                }
            }

        }
    }

    /**
     *
     */
    public void resetPosition() {
        final AppableControl control = getCurrentControl();
        if (control != null) {
            if (gridToScour.getConfigurableGrid().isRootGrid()) {
                GridPane.setColumnIndex(control, columnIndex);
                GridPane.setColumnSpan(control, columnSpan);
                GridPane.setRowIndex(control, rowIndex);
                GridPane.setRowSpan(control, rowSpan);
            } else {
                LOGGER.info("Return control to original parent root grid");
                SubGrid subGrid = HomeController.getSubGrid();
                final ConfigurableGrid gridToView = subGrid.getConfigurableGrid();
                gridToView.getChildren().remove(control);
                GridPane.setColumnIndex(control, columnIndex);
                GridPane.setColumnSpan(control, columnSpan);
                GridPane.setRowIndex(control, rowIndex);
                GridPane.setRowSpan(control, rowSpan);
                control.getFontTracker().fontTracking.setValue(Font.font("Roboto",0.0));
                if (!gridToScour.getConfigurableGrid().getChildren().contains(control)) {
                    gridToScour.getConfigurableGrid().add(control, columnIndex, rowIndex, columnSpan, rowSpan);
                }
            }

        }
    }

    /**
     *
     * @param navigation
     */
    public void highlightNext(Navigation navigation) {
        LOGGER.info("Searching for next control in grid " + gridToScour.getConfigurableGrid().getIndex());
        AppableControl next = null;
        switch (getDirection()) {
            case COLUMN_DIRECTION:
                do {
                    next = getNextInColumn();
                } while (next == null);
                break;
            case ROW_DIRECTION:
                do {
                    next = getNextInRow();
                } while (!next.isSelectable());
                break;
        }

        highlightAction(next, navigation);
    }

    Integer columnIndex;
    Integer columnSpan;
    Integer rowIndex;
    Integer rowSpan;

    /**
     *
     * @param next
     * @param navigation
     */
    public void highlightAction(AppableControl next, Navigation navigation) {
        if (next != null) {
            if (next.isSelectable()) {
                LOGGER.info("Next control found in grid." + gridToScour.getIndex()
                        + ". Setting highlight for " + next.getText() + " at "
                        + getCurrentRow() + ", " + getCurrentColumn());

                if (navigation.fullScreen()) {
                    columnIndex = GridPane.getColumnIndex(next);
                    columnSpan = GridPane.getColumnSpan(next);
                    rowIndex = GridPane.getRowIndex(next);
                    rowSpan = GridPane.getRowSpan(next);
                    setCurrentControl(next);
                    if (gridToScour.getConfigurableGrid().isRootGrid()) {
                        GridPane.setColumnIndex(next, 0);
                        GridPane.setColumnSpan(next, gridToScour.getConfigurableGrid().getColumnConstraints().size());
                        GridPane.setRowIndex(next, 0);
                        GridPane.setRowSpan(next, gridToScour.getConfigurableGrid().getRowConstraints().size());
                        next.setMaxHeight(gridToScour.getConfigurableGrid().getHeight());
                    } else {
                        // gridToScour.getChildren().remove(next);
                        next.removeFromParent();
                        LOGGER.info("Requiring full screen scanning while in a nested grid");
                        SubGrid subGrid = HomeController.getSubGrid();
                        final ConfigurableGrid gridToView = subGrid.getConfigurableGrid();
                        GridPane.setColumnIndex(next, 0);
                        GridPane.setColumnSpan(next, gridToView.getColumnConstraints().size());
                        GridPane.setRowIndex(next, 0);
                        GridPane.setRowSpan(next, gridToView.getRowConstraints().size());
                        gridToView.getChildren().add(next);
                        next.setMaxHeight(gridToView.getHeight());
                    }
                    next.toFront();
                } else {

                    if (navigation.highlightBorder()) {
                        next.setCSS("highlight", next.getPrimaryControl());
                    }
                }
                if (navigation.speakSelection()) {
                    if (next.isSpeakable()) {
                        next.speak(next.getText());
                    }
                }
                //  navigation.setPlaySound(true);
                if (navigation.playScourSound()) {
                    try {
                        AudioPlayer audioPlayer = new AudioPlayer();
                        // final File file = new File(getClass().getClassLoader().getResource("sounds/click.wav").getFile());
                        final File file = new File(Main.getSession().getUser().getNavigation().getScourSound());

                        audioPlayer.playAudioFile(file);
                    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                        LOGGER.fatal(null, ex);
                    }
                }

                if (next instanceof SubGrid) {
                    SubGrid nextSubGrid = (SubGrid) next;
                    nextSubGrid.setCSS("highlight", nextSubGrid);

                }
                next.toFront();
            } else {
                highlightNext(navigation);
            }
        }
    }

    /**
     *
     * @param currentRow
     * @param currentColumn
     */
    public void set(Integer currentRow, Integer currentColumn) {
        setCurrentRow(currentRow);
        setCurrentColumn(currentColumn);
    }

    /**
     *
     * @return
     */
    public AppableControl getNextInRow() {
        incrementCurrentRow();
        if (getCurrentRow() >= gridToScour.getConfigurableGrid().getRowConstraints().size()) {
            setCurrentRow(0);
            incrementCurrentColumn();
            if (getCurrentColumn() >= gridToScour.getConfigurableGrid().getColumnConstraints().size()) {
                setCurrentColumn(0);
            }
        }
        AppableControl control = (AppableControl) gridToScour.getConfigurableGrid().get(getCurrentRow(), getCurrentColumn());

        return control;
    }

    /**
     *
     * @return
     */
    public AppableControl getNextInColumn() {
        incrementCurrentColumn();
        if (getCurrentColumn() >= gridToScour.getConfigurableGrid().getColumnConstraints().size()) {
            setCurrentColumn(0);
            incrementCurrentRow();
            if (getCurrentRow() >= gridToScour.getConfigurableGrid().getRowConstraints().size()) {
                setCurrentRow(0);
            }
        }
        AppableControl control = (AppableControl) gridToScour.getConfigurableGrid().get(getCurrentRow(), getCurrentColumn());

        return control;
    }

    /**
     *
     * @return
     */
    public AppableControl getCurrent() {
        return (AppableControl) gridToScour.getConfigurableGrid().get(getCurrentRow(), getCurrentColumn());
    }

    /**
     *
     */
    public ObjectProperty<AppableControl> currentControl;

    /**
     *
     * @param value
     */
    public void setCurrentControl(AppableControl value) {
        currentControlProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public AppableControl getCurrentControl() {
        return currentControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<AppableControl> currentControlProperty() {
        if (currentControl == null) {
            currentControl = new SimpleObjectProperty<>();
        }
        return currentControl;
    }

    /**
     *
     */
    public static final int DEFAULT_DIMENSION = -1;
    /**
     *
     */
    public IntegerProperty currentRow;

    /**
     *
     * @param value
     */
    public void setCurrentRow(Integer value) {
        currentRowProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getCurrentRow() {
        return currentRowProperty().getValue();
    }

    /**
     *
     */
    public void incrementCurrentRow() {
        setCurrentRow(getCurrentRow() + 1);
    }

    /**
     *
     * @return
     */
    public IntegerProperty currentRowProperty() {
        if (currentRow == null) {
            currentRow = new SimpleIntegerProperty(DEFAULT_DIMENSION);
        }
        return currentRow;
    }

    /**
     *
     */
    public IntegerProperty currentColumn;

    /**
     *
     * @param value
     */
    public void setCurrentColumn(Integer value) {
        currentColumnProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getCurrentColumn() {
        return currentColumnProperty().getValue();
    }

    /**
     *
     */
    public void incrementCurrentColumn() {
        setCurrentColumn(getCurrentColumn() + 1);
    }

    /**
     *
     * @return
     */
    public IntegerProperty currentColumnProperty() {
        if (currentColumn == null) {
            currentColumn = new SimpleIntegerProperty(DEFAULT_DIMENSION);
        }
        return currentColumn;
    }
    /**
     *
     */
    public static final String ROW_DIRECTION = "Row";

    /**
     *
     */
    public static final String COLUMN_DIRECTION = "Column";
    private StringProperty direction;

    /**
     *
     * @param value
     */
    public void setDirection(String value) {
        directionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getDirection() {
        return directionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty directionProperty() {
        if (direction == null) {
            direction = new SimpleStringProperty(COLUMN_DIRECTION);
        }
        return direction;
    }
}
