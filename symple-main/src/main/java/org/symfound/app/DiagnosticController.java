
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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Ability;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.comm.file.PathReader;
import org.symfound.controls.user.NavigateButton;
import org.symfound.diagnostic.DiagnosticGrid;
import org.symfound.diagnostic.DiagnosticTarget;
import org.symfound.diagnostic.DiagnosticTest;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.HOME;
import static org.symfound.main.FullSession.getMainUI;
import org.symfound.main.settings.SettingsController;
import org.symfound.test.Result;
import org.symfound.test.ResultMap;
import org.symfound.test.ResultValue;
import static org.symfound.test.Test.COUNTDOWN_TIME;
import org.symfound.test.TestControllerBase;
import org.symfound.tools.selection.ModeIterator;
import org.symfound.tools.timing.DelayedEvent;
import org.symfound.tools.timing.clock.Countdown;
import org.symfound.voice.player.AudioPlayer;

/**
 *
 * @author Javed Gangjee
 */
public class DiagnosticController extends TestControllerBase {

    private static final String NAME = DiagnosticController.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    @FXML
    private Label lblCountdown;
    @FXML
    private Label lblFirstCountdown;
    @FXML
    private AnchorPane apFirstCountdown;
    @FXML
    private AnchorPane apMap;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblNext;
    @FXML
    private Label lblConsensus;
    @FXML
    private Label lblLevel;
    @FXML
    private ResultValue lblAvgTries;
    @FXML
    private ResultValue lblTravelTime;

    private ResultMap gpMap;

    @FXML
    private NavigateButton btnHome;
    @FXML
    private DiagnosticGrid gpDiag;
    @FXML
    private BorderPane bpResults;

    // Target Variables
    private int targetCount = 0;

    /**
     *
     */
    public static final Integer MAX_TARGETS = 10;

    /**
     *
     */
    public static final Integer MAX_LEVEL = 6;

    /**
     *
     */
    public List<Integer> randomIndices = new ArrayList<>();

    /**
     *
     */
    public List<File> targetImages;

    /**
     *
     */
    public static final int TIMEOUT = 60000;
    // Timing Variables

    /**
     *
     */
    public int finishAnimTime;
    private DelayedEvent resetEvent = new DelayedEvent();

    // Tries

    /**
     *
     */
    public Integer totalTries = 0;

    /**
     *
     */
    public Double totalTravelTime = 0.0;

    /**
     *
     */
    public AudioPlayer audioPlayer = new AudioPlayer();

    /**
     *
     */
    public DiagnosticTest test = new DiagnosticTest();

    /**
     * Play the entire diagnostic test again. This includes hiding the replay
     * button and calling the diagnostics intialize method (load) again.
     *
     * @param e event
     */
    @FXML
    @Override
    public void play(MouseEvent e) {
        test.getCountdown().complete();
    }

    /**
     *
     */
    public void playNext() {
        apFirstCountdown.setVisible(false);
        test.incrementAttempts();
        gpDiag.setup(getUser().getAbility().getLevel().intValue());
        bpResults.setVisible(false);
        bpResults.toBack();
        // Create a randomized index arrays for buttons 
        randomIndices = gpDiag.buildIndexList(gpDiag.getNumOfNodes(), true);
        if (gpDiag.getNumOfNodes() > MAX_TARGETS) {
            randomIndices = randomIndices.subList(0, MAX_TARGETS);
        }
        getNextTarget();
    }

    /**
     * Loads the next target in the specified target list. If this is the last
     * target, the test is deemed done.
     *
     *
     */
    public void getNextTarget() {
        if (targetCount == gpDiag.getNumOfNodes() || targetCount >= MAX_TARGETS) {
            // Test is now done. Show the buttons and get results.
            complete();
            targetCount = 0;
        } else if (gpDiag.getNodes().get(targetCount) instanceof DiagnosticTarget) {
            //Get the next button index from shuffled ArrayList
            DiagnosticTarget nextTarget = (DiagnosticTarget) gpDiag.getNodes().get(randomIndices.get(targetCount));
            if (getImageIterator().size() > 0) {
                setNextImage(nextTarget);
            } else {
                nextTarget.setText("Catch me!");
            }
            nextTarget.show();
            nextTarget.completeProperty().addListener((observable, oldValue, newValue) -> {
                nextTarget.hide();
                // Add to total number of tries
                totalTries = totalTries + nextTarget.getTries();
                totalTravelTime = totalTravelTime + nextTarget.getTravelTime().toSeconds();
                getNextTarget();
            });
            gpDiag.toFront();
            // Increment the count for the diagnostic buttons
            targetCount++;
            // reset the travel timeline for the new target
            nextTarget.resetTries();
        }
    }

    /**
     * When the test has been completed, show and disable all targets, reset the
     * counter and get the results of the test.
     *
     *
     */
    @Override
    public void complete() {
        test.getCountdown().play();
        gpDiag.done();
        Integer level = getUser().getAbility().getLevel().intValue();
        // Get the getAverage number of tries
        Double tryAverage = getAverage(totalTries.doubleValue(), Math.pow(level, 2));
        Double travelAverage = getAverage(totalTravelTime, Math.pow(level, 2));
        test.rating(tryAverage);

        LOGGER.info("Result = " + test.getResult().get());
        LOGGER.info("Result Consensus = " + test.getResult().getConsensus());
        LOGGER.info("Average Tries = " + tryAverage);
        LOGGER.info("Travel Time = " + travelAverage);
        LOGGER.info("Level = " + level);

        this.totalTries = 0;
        totalTravelTime = 0.0;

        setResults(test.getResult(), level, tryAverage, travelAverage);
        evaluateAbility();

        bpResults.setVisible(true);
        bpResults.toFront();
        
        SettingsController.setUpdated(Boolean.TRUE);

    }

    private static final int MAX_ATTEMPTS = 3;

    /**
     *
     */
    public void evaluateAbility() {
        final Ability ability = getUser().getAbility();
        switch (test.getResult().get()) {
            case Result.SUCCESS_TITLE:
                if (ability.getLevel() >= MAX_LEVEL) {
                    done();
                } else {
                    ability.incrementLevel();
                }
                break;
            case Result.WARNING_TITLE:
                if (test.getAttempts() >= MAX_ATTEMPTS) {
                    ability.decrementLevel();
                    done();
                }
                break;
            case Result.CRITICAL_TITLE:
                ability.decrementLevel();
                done();
                break;
        }
    }

    /**
     *
     */
    @Override
    public void done() {
        test.resetAttempts();
        test.getCountdown().end();
        lblCountdown.setText("Done");
        DelayedEvent delayedEvent = new DelayedEvent();
        delayedEvent.setup(5.0, (ActionEvent e) -> {
            getMainUI().getStack().load(HOME);
            getMainUI().open();
        });
        delayedEvent.play();
    }

    /**
     *
     * @param result
     * @param level
     * @param avgTries
     * @param travelTime
     */
    public void setResults(Result result, Integer level, Double avgTries,
            Double travelTime) {

        lblTitle.setStyle("-fx-background-color:#" + result.getColour() + ";");
        lblNext.setStyle("-fx-background-color:#" + result.getColour() + ";");
        if (gpMap != null) {
            apMap.getChildren().remove(gpMap);
        }
        gpMap = new ResultMap();
        gpMap.build(gpDiag.getSize(), gpDiag.getChildren());
        apMap.getChildren().add(gpMap);

        lblLevel.setText(level.toString());

        if (avgTries < DiagnosticTarget.TIMEOUT_PENTALTY) {
            lblAvgTries.set(avgTries, 1, "");
        } else {
            lblAvgTries.setText("N/A");
        }

        if (travelTime < DiagnosticTarget.TIMEOUT) {
            lblTravelTime.set(travelTime, 1, "s");
        } else {
            lblTravelTime.setText("N/A");
        }
    }

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test.getResult().modeProperty().bindBidirectional(lblTitle.textProperty());
        test.getResult().consensusProperty().bindBidirectional(lblConsensus.textProperty());
        test.setCountdown(new Countdown(COUNTDOWN_TIME) {
            @Override
            public void run() {
                LOGGER.info("Countdown has ended");
                playNext();
            }
        });
        test.getCountdown().textProperty().bindBidirectional(lblCountdown.textProperty());
        test.getCountdown().textProperty().bindBidirectional(lblFirstCountdown.textProperty());

        getMainUI().getStack().currentProperty().addListener((observeableValue, oldValue, newValue) -> {
            if (newValue.get().equals(FullSession.DIAGNOSTIC_APP)) {
                test.getCountdown().play();
                if (getUser().getProfile().isFirstUse()) {
                    apFirstCountdown.setVisible(true);
                }
            } else {
                test.getCountdown().end();
            }
        });

    }

    private ModeIterator<String> imageIterator;

    /**
     *
     * @return
     */
    public ModeIterator<String> getImageIterator() {
        if (imageIterator == null) {
            PathReader pathReader = new PathReader(getUser().getContent().getHomeFolder() + "/pictures/");
            List<String> folderFilePaths = pathReader.getFolderFilePaths();
            List<String> imagePaths = new ArrayList<>();
            folderFilePaths.stream().forEach((path) -> {
                ExtensionAnalyzer pictureChecker = new ExtensionAnalyzer(path);
                if (pictureChecker.isPictureFile()) {
                    imagePaths.add(path);
                }
            });
            imageIterator = new ModeIterator<>(imagePaths);
            imageIterator.shuffle();
        }
        return imageIterator;
    }

    /**
     *
     * @param next
     */
    public void setNextImage(DiagnosticTarget next) {
        getImageIterator().next();
        String image = getImageIterator().get();
        ExtensionAnalyzer pictureChecker = new ExtensionAnalyzer(image);
        if (pictureChecker.isPictureFile()) {
            next.setImage(image);
            LOGGER.info("Setting image " + getImageIterator().get() + " to next target");
        }
    }

}
