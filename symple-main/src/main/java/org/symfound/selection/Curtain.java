/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.FillableGrid;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class Curtain extends AppableControl {

    public static final String KEY = "Curtain";
    Selector selector;

    public Curtain(Selector selector, String buttonOrder) {
        super("", KEY, "", "default");
        this.selector = selector;
        initialize(buttonOrder);
    }

    /**
     *
     */

    private void initialize(String buttonOrder) {
        grid = new CurtainGrid(selector);
        this.setButtonOrder(buttonOrder);
        grid.reload(getParallelList(), FillableGrid.FillMethod.ROW_WISE, FillableGrid.FillDirection.FORWARD, 1.0);
        buttonOrderProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Grid has reloaded" + newValue);
            grid.reload(getParallelList(), FillableGrid.FillMethod.ROW_WISE, FillableGrid.FillDirection.FORWARD, 1.0);
        });
        addToPane(grid);
    }

    private CurtainGrid grid;

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

    private static final String DEFAULT_BUTTON_ORDER = "Selector=default";

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

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
    }

    @Override
    public void configureFeatures() {
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            Class<? extends Curtain> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(KEY);
        }
        return preferences;
    }

}
