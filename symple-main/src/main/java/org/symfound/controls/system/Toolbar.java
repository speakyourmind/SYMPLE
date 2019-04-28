/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.FillableGrid.FillDirection;
import org.symfound.controls.user.FillableGrid.FillMethod;
import org.symfound.controls.user.MoveWindowButton;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class Toolbar extends ScreenControl<AnimatedButton> {

    /**
     *
     */
    public static final String TITLE_STYLE = "settings-title";
    private static final Boolean DEFAULT_WRAP_TEXT = Boolean.TRUE;
    private ToolbarGrid grid;
    private MoveWindowButton moveButton;

    /**
     *
     */
    public AnimatedLabel titleLabel;

    /**
     *
     */
    public Toolbar() {
        getStylesheets().add(CSS_PATH);
        getStyleClass().add("toolbar");
        initialize();
    }

    private void initialize() {
        addTitleLabel();
        //  addMovableButton();
        addGrid();
    }

    /*
     private void addMovableButton() {
     moveButton = new MoveWindowButton();
     moveButton.setSymStyle("toolbar");
     if (isMovable()) {
     addToPane(moveButton, 0.0, 0.0, 0.0, 0.0);
     } else {
     getChildren().remove(moveButton);
     }
     movableProperty().addListener((observable, oldValue, newValue) -> {
     if (isMovable()) {
     addToPane(moveButton, 0.0, 0.0, 0.0, 0.0);
     moveButton.toBack();
     } else {
     getChildren().remove(moveButton);
     }
     });
     }*/
    private void addGrid() {
        grid = new ToolbarGrid();
        Double size = getUser().getAbility().getLevel();
        grid.reload(getParallelList(), FillMethod.ROW_WISE, FillDirection.FORWARD, size);
        buttonOrderProperty().addListener((observable, oldValue, newValue) -> {
            grid.reload(getParallelList(), FillMethod.ROW_WISE, FillDirection.FORWARD, size);
        });
        addToPane(grid, 0.0, null, 0.0, 0.0);
    }

    /**
     *
     * @return
     */
    public ParallelList<String, String> getParallelList() {
        ParallelList<String, String> parallelList = new ParallelList<>();
        String[] pairs = getButtonOrder().split(BUTTON_DELIMITER);
        for (String pair : pairs) {
            String[] keyValue = pair.split(KEY_DELIMITER);
            parallelList.add(keyValue[0], keyValue[1]);
        }
        return parallelList;
    }

    private void addTitleLabel() {
        titleLabel = new AnimatedLabel();
        
        setCSS("settings-title", titleLabel);
        titleLabel.setWrapText(DEFAULT_WRAP_TEXT);
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        setSizeMax(titleLabel);
        titleTextProperty().bindBidirectional(titleLabel.textProperty());
        if (!getTitleText().isEmpty()) {
            addToPane(titleLabel);
        }
        titleTextProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                addToPane(titleLabel);
                titleLabel.toBack();
            }
        });
    }

    /**
     *
     */
    public StringProperty titleText;

    /**
     *
     * @return
     */
    public String getTitleText() {
        return titleTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setTitleText(String value) {
        titleTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty titleTextProperty() {
        if (titleText == null) {
            titleText = new SimpleStringProperty("");
        }
        return titleText;
    }

    private static final String DEFAULT_BUTTON_ORDER = "Minimize=default, Exit=default";

    /**
     *
     */
    public StringProperty buttonOrder;

    /**
     *
     * @return
     */
    public String getButtonOrder() {
        return buttonOrderProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setButtonOrder(String value) {
        buttonOrderProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty buttonOrderProperty() {
        if (buttonOrder == null) {

            buttonOrder = new SimpleStringProperty(DEFAULT_BUTTON_ORDER);
        }
        return buttonOrder;
    }

    /**
     *
     */
    public BooleanProperty movable;

    /**
     *
     * @param value
     */
    public void setMovable(Boolean value) {
        movableProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isMovable() {
        return movableProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty movableProperty() {
        if (movable == null) {
            movable = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return movable;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        //UNUSED
    }

}
