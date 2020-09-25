/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import javafx.event.ActionEvent;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class MessageDialog extends ScreenDialog {

    private final DelayedEvent removeEvent;

    /**
     *
     * @param titleText
     * @param captionText
     * @param resetTime
     */
    public MessageDialog(String titleText, String captionText, Double resetTime) {
        super(titleText, captionText);

        baseGrid.setSpecRows(1);
        baseGrid.setSpecColumns(1);
        baseGrid.build();
        baseGrid.add(titledLabel, 0, 0);

        removeEvent = new DelayedEvent();
        removeEvent.setup(resetTime, (ActionEvent e) -> {
            setDone(true);
        });
        removeEvent.play();
        addToStackPane(baseGrid);
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        //UNUSED
    }

}
