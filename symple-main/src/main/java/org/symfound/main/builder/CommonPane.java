/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.builder;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.symfound.controls.system.MenuLaunchButton;
import org.symfound.controls.system.Toolbar;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public class CommonPane extends AnimatedPane {

    /**
     *
     */
    public static final double MENU_OFFSET = 120.0;
    private final Parent content;
    private BuildableGrid baseGrid;
    private Toolbar toolbar;
    private final Boolean hasMenu;
    private final Boolean hasToolbar;
    private final String buttonOrder;

    /**
     *
     * @param parent
     * @param hasMenu
     */
    public CommonPane(Parent parent, Boolean hasMenu) {
        this(parent, hasMenu, false, "");
    }

    /**
     *
     * @param parent
     * @param hasMenu
     * @param hasToolbar
     */
    public CommonPane(Parent parent, Boolean hasMenu, Boolean hasToolbar) {
        this(parent, hasMenu, hasToolbar, "Navigate, Settings");
    }

    /**
     *
     * @param parent
     * @param hasMenu
     * @param hasToolbar
     * @param buttonOrder
     */
    public CommonPane(Parent parent, Boolean hasMenu, Boolean hasToolbar, String buttonOrder) {
        this.content = parent;
        this.hasMenu = hasMenu;
        this.hasToolbar = hasToolbar;
        this.buttonOrder = buttonOrder;
        addBaseGrid();
        addContent(parent);
    }

    /**
     *
     */
    private void addBaseGrid() {
        baseGrid = new BuildableGrid();
        baseGrid.setSpecRows(2);
        List<Double> rowPercentages = Arrays.asList(4.0, 96.0);
        baseGrid.buildRowByPerc(rowPercentages);
        baseGrid.setSpecColumns(1);
        baseGrid.buildColumns();
        getChildren().add(baseGrid);
    }

    /**
     *
     * @param parent
     */
    private void addContent(Parent parent) {
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
        setStyle("-fx-background-color: transparent;");
        AnchorPane.setTopAnchor(baseGrid, 0.0);
        AnchorPane.setBottomAnchor(baseGrid, 0.0);
        AnchorPane.setLeftAnchor(baseGrid, 0.0);
        AnchorPane.setRightAnchor(baseGrid, 0.0);

        if (hasToolbar) {
            toolbar = new Toolbar();
            toolbar.setTitleText("");
            toolbar.setButtonOrder(buttonOrder);
            baseGrid.add(parent, 0, 1);
            baseGrid.add(toolbar, 0, 0);
        } else {
            baseGrid.add(parent, 0, 0, 1, 2);
        }

        if (hasMenu) {
            addUserMenu();
        }
    }

    /**
     *
     */
    public MenuLaunchButton menuLaunchButton;

    /**
     *
     * @return
     */
    public MenuLaunchButton getMenuLaunchButton() {
        if (menuLaunchButton == null) {
            menuLaunchButton = new MenuLaunchButton();
        }
        return menuLaunchButton;
    }

    private void addUserMenu() {
        Dimension dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Double menuOffset = dimScreenSize.getWidth() * 0.3;
        AnchorPane.setTopAnchor(getMenuLaunchButton(), 0.0);
        menuUp(getMenuLaunchButton(), menuOffset);
        getMenuLaunchButton().menuVisibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                menuDown(menuLaunchButton);
            } else {
                menuUp(menuLaunchButton, menuOffset);
            }
        });

        getChildren().add(menuLaunchButton);
        content.disableProperty().bindBidirectional(menuLaunchButton.menuVisibleProperty());

    }

    /**
     *
     * @param menuLauncher
     * @param left
     * @param right
     * @param bottom
     */
    public void menuToggle(MenuLaunchButton menuLauncher, Double left, Double right, Double bottom) {
        AnchorPane.setLeftAnchor(menuLauncher, left);
        AnchorPane.setRightAnchor(menuLauncher, right);
        AnchorPane.setBottomAnchor(menuLauncher, bottom);
    }

    /**
     *
     * @param menuLauncher
     */
    public void menuDown(MenuLaunchButton menuLauncher) {
        menuToggle(menuLauncher, MENU_OFFSET, MENU_OFFSET, MENU_OFFSET);
    }

    /**
     *
     * @param menuLauncher
     * @param dblMenuOffset
     */
    public void menuUp(MenuLaunchButton menuLauncher, Double dblMenuOffset) {
        menuToggle(menuLauncher, dblMenuOffset, dblMenuOffset, null);
    }

}
