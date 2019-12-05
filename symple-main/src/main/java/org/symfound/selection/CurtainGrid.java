/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import org.symfound.selection.controls.SelectorButton;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import org.symfound.controls.AppableControl;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.user.FillableGrid.FillDirection;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.controls.user.ButtonGrid;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.ScriptButton;
import org.symfound.main.HomeController;
import org.symfound.selection.controls.ScrollControl;
import org.symfound.selection.controls.ScrollControlButton;
import org.symfound.selection.modes.Scroller;
import org.symfound.tools.ui.ColourChoices;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class CurtainGrid extends ButtonGrid {

    Selector selector;

    public CurtainGrid(Selector selector) {
        super();
        this.selector = selector;

    }

    /**
     *
     * @param buildOrder
     * @param method
     * @param direction
     * @param size
     */
    @Override

    public void build(ParallelList<String, String> buildOrder,
            FillMethod method, FillDirection direction, Double size) {
        setStatus(ScreenStatus.ENDING);
        clear();
        setStatus(ScreenStatus.READY);
        int rowSize = getRowSize(size);
        setSpecRows(1);
        int columnSize = getColumnSize(size);
        setSpecColumns(3);
        setStatus(ScreenStatus.REQUESTED);
        fill(buildOrder, rowSize * columnSize);
        setStatus(ScreenStatus.LOADING);
        build();
        configure(getControlsQueue(), method, direction);

        Insets insets = new Insets(DEFAULT_GRID_GAP);
        setPadding(insets);
        /*        if (!AppGrid.inEditMode()) {
        launchAnimation();
        }*/
        toBack();

    }

    @Override
    public void fill(ParallelList<String, String> buildOrder, Integer size) {
        List<AppableControl> requested = buildRequestedList(buildOrder);
        resetControlsQueue();
        if (requested.size() > 0) {
            getControlsQueue().addAll(requested);
            System.out.println(getControlsQueue().get(0).getKey());
        } else {
            LOGGER.warn("No controls available!");
        }
    }

    public List<AppableControl> buildRequestedList(ParallelList<String, String> buildOrder) {
        List<AppableControl> requested = new ArrayList<>();
        for (int i = 0; i < buildOrder.getFirstList().size(); i++) {
            String toBuild = buildOrder.getFirstList().get(i);
            String index = buildOrder.getSecondList().get(i);
            switch (toBuild.trim()) {
                case SelectorButton.KEY:
                    SelectorButton selectorButton = new SelectorButton(selector);
                    requested.add(selectorButton);
                    break;
                case ScrollControlButton.KEY:
                    ScrollControlButton scrollButton = new ScrollControlButton((Scroller)selector,
                            HomeController.getScrollPane(), ScrollControl.valueOf(index.toUpperCase()));
                    scrollButton.setText(index.toUpperCase());
                    System.out.println("------------->"+ScrollControl.valueOf(index.toUpperCase()).toString());
                    requested.add(scrollButton);
                    break;
                case "Blank":
                    ScriptButton blankButton = new ScriptButton("blank");
                    blankButton.setControlType(ScreenControl.ControlType.SETTING_CONTROL);
                    blankButton.setEditable(Boolean.FALSE);
                    blankButton.getStyleClass().clear();
                    blankButton.getStyleClass().add("transparent");
                    blankButton.setSymStyle("transparent");// TODO: Fix;
                    blankButton.setBackgroundColour(ColourChoices.TRANSPARENT);
                    requested.add(blankButton);
                    break;

            }
        }

        return requested;
    }

}
