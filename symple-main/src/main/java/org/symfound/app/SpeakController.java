/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.app;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javax.sound.sampled.*;
import org.apache.log4j.Logger;
import org.symfound.controls.user.ActiveTextArea;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;
import org.symfound.tools.animation.NodeAnimator;
import org.symfound.tools.selection.SelectionMethod;

/**
 * TO DO: Move all methods.
 *
 * @author Javed Gangjee
 */
public class SpeakController extends CommonController {

    private static final String NAME = SpeakController.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Speak";
    
    // Common FXML components
    @FXML
    private ActiveTextArea txtMain;

    // Common FXML Containers
    @FXML
    private GridPane gpMain;
    @FXML
    private BuildableGrid gpTier;      //Iteratively build child nodes

    // Component variables

    /**
     *
     */
    public static AnimatedButton button;

    /**
     *
     */
    public String srcID;

    /**
     *
     */
    public String srcText;

    /**
     *
     */
    public String srcStyle;

    @FXML
    private void MClckClckStatType(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                initHandler(e);
                typeButtonRun();
                // Set button selection animation
                button.setOnFinished(srcText, srcStyle);
                show2Tier(false);
                
            }
        }
    }

    /**
     *
     */
    public void typeButtonRun() {
        if (getUser().getTyping().needsAutoComplete()) {
            if (srcText.length() > 1) {
                //Select the previous word in order to read the length
                txtMain.deletePreviousWord();
            }
        }

        int keyCode = ActionKeyCode.UNASSIGNED;

        if (srcID != null) {
            if (srcID.contains("Back")) {
                keyCode = ActionKeyCode.BLANK;
            }
        }
        txtMain.handle(keyCode, srcText);
    }

    @FXML
    private void MEntrDwellGrowType(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                initHandler(e);
                // Setup and play tlDwell
                button.animate().startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
                // When timeline is completed
                button.animate().setOnScaleFinished((ActionEvent t) -> {
                    button.animate().stopScale();
                    typeButtonRun();
                    // Set button selection animation
                    button.setOnFinished(srcText, srcStyle);
                    show2Tier(false);
                });
            }
        }
    }

    @FXML
    private void MEntrDwellScanType(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                initHandler(e);
                List<String> list = txtMain.getTextOperator().splitAsList(srcText.replaceAll("\\s+", ""), "");
                button.startScan(list);
                getSession().setMutex(true);
            }
        }
    }

    @FXML
    private void MExitDwellScanType(MouseEvent e)
            throws IOException, LineUnavailableException {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            String addTxt = button.getText();
            //Write to Main Text Area if current time is past 
            Duration currTime = button.scanTransition.get().getCurrentTime();
            if ((int) currTime.toSeconds() > (getUser().getTiming().getDwellTime() * 2)) {
                txtMain.type(addTxt);
            }
            // Set button selection animation
            button.revert(srcText, srcStyle);
            button.stopScan();
        }
    }

    @FXML
    private void MClckClckStatNumPad(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
            if (!getSession().isMutex()) {
                initHandler(e);
                show2Tier(true);
                // Set button selection animation
                button.setOnFinished(srcText, srcStyle);
            }
        }
    }

    @FXML
    private void MEntrDwellGrowNumPad(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                initHandler(e);
                button.animate().startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
                // When timeline is completed
                button.animate().setOnScaleFinished((ActionEvent t) -> {
                    show2Tier(true);
                });
            }
        }
    }

    @FXML
    private void MClckClckStatNumPadClose(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
            if (!getSession().isMutex()) {
                initHandler(e);
                show2Tier(false);
                // Set button selection animation
                button.setOnFinished(srcText, srcStyle);
            }
        }
    }

    @FXML
    private void MEntrDwellGrowNumPadClose(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                initHandler(e);
                button.animate().startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
                // When timeline is completed
                button.animate().setOnScaleFinished((ActionEvent t) -> {
                    show2Tier(false);
                });
            }
        }
    }

    @FXML
    private void MClckClckStat2TLaunch(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
            if (!getSession().isMutex()) {
                initHandler(e);
                List<String> txtList = buildTier();
                gpTier.setTextAll(txtList);

                show2Tier(true);
                // Set button selection animation
                button.setOnFinished(srcText, srcStyle);
                getSession().setMutex(true);
            }
        }
    }

    @FXML
    private void MEntrDwellGrow2TLaunch(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            initHandler(e);
            if (!getSession().isMutex()) {
                // Get details of button that called the handle
                button.animate().startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
                // When timeline is completed
                button.animate().setOnScaleFinished((ActionEvent t) -> {
                    List<String> txtList = buildTier();
                    gpTier.setTextAll(txtList);
                    show2Tier(true);
                });
            }
        }
    }

    @FXML
    private void MExitDwellStatGener(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            //Set button back to original size and text.
            button.revert(srcText, srcStyle);
            //Stop the tlDwell. Save text in mainText.
            button.animate().stopScale();
        }
    }

    /**
     * Initialize variables that are common to all MouseEvent handle methods.
     * This includes: -button source of the click (could be expanded for other
     * controls), -original text of the button at the time of the event,
     * -original style of the button at the time of the event.
     *
     * @param e MouseEvent that calls the method
     */
    private void initHandler(InputEvent e) {
        if (e.getSource() instanceof AnimatedButton) {
            // Get button source of handle
            button = (AnimatedButton) e.getSource();
            // Save the original text
            srcID = button.getId();
            // Save the original text
            srcText = button.getText();
            // Save the original style
            srcStyle = button.getStyle();
            // Bring button to front
            button.toFront();

            if (txtMain != null) {
                txtMain.toFront();
            }
        }

    }

    /**
     *
     * @param isShown
     */
    public void show2Tier(Boolean isShown) {
        if (gpTier != null) {
            // Hide the top tier
            gpTier.setVisible(isShown);
        }

        if (gpMain != null) {
            // Show the main screen
            gpMain.setDisable(isShown);
        }
    }

    /**
     *
     * @return
     */
    public List<String> buildTier() {
        // Build the list of strings to setMouse to buttons
        List<String> txtList;
        txtList = txtMain.getTextOperator().splitAsList(srcText.replaceAll("\\s+", ""), "");
        txtList.add("");
        // Fill list with empties so it is the same size as tierNode list
        while (txtList.size() < gpTier.getNumOfNodes()) {
            txtList.add("");
        }
        // Set the last element of the list to "Back"
        txtList.set(gpTier.getNumOfNodes() - 1, "Back");
        return txtList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
