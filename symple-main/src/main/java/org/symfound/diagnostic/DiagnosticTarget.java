package org.symfound.diagnostic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.test.Result;
import org.symfound.test.Target;
import org.symfound.tools.animation.NodeAnimator;
import org.symfound.tools.timing.DelayedEvent;
import org.symfound.voice.player.AudioPlayer;

// TO DO: Reimplement mutex
/**
 * Tests the user's ability to select buttons. The number of tries are
 * incremented every time the user enters the button. If the number of tries is
 * less than <code>CRITICAL_TRIES</code> the result is either SUCCESS or
 * WARNING. If the tries exceed <code>CRITICAL_TRIES</code> It will be a fail.
 *
 * @author Javed Gangjee
 */
public final class DiagnosticTarget extends Target {

    private static final String NAME = DiagnosticTarget.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String CSS_CLASS = "diagnostic-button";

    // Timing

    /**
     *
     */
    public static final Double TIMEOUT = 30.0;

    /**
     *
     */
    public static final Integer TIMEOUT_PENTALTY = 6;

    /**
     *
     */
    public Duration travelTime;

    /**
     *
     */
    public DelayedEvent travelTimer = new DelayedEvent();

    /**
     *
     */
    public static final String SUCCESS_CONSENSUS = "Perfect!";

    /**
     *
     */
    public static final String WARNING_CONSENSUS = "Nice";

    /**
     *
     */
    public static final String CRITICAL_CONSENSUS = "Fail";

    /**
     *
     */
    public static final String SUCCESS_COLOUR = "27ae60";

    /**
     *
     */
    public static final String WARNING_COLOUR = "e67e22";

    /**
     *
     */
    public static final String CRITICAL_COLOUR = "e74c3c";

    /**
     *
     */
    public static final String SUCCESS_TEXT = "Success";

    /**
     *
     */
    public static final String WARNING_TEXT = "Warning";

    /**
     *
     */
    public static final String CRITICAL_TEXT = "Critical";

    /**
     *
     */
    public String strResult;

    /**
     *
     */
    public static final Map<String, String> CONSENSUS_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put(SUCCESS_TEXT, SUCCESS_CONSENSUS);
        map.put(WARNING_TEXT, WARNING_CONSENSUS);
        map.put(CRITICAL_TEXT, CRITICAL_CONSENSUS);
        CONSENSUS_MAP = Collections.unmodifiableMap(map);
    }

    /**
     *
     */
    public static final Map<String, String> COLOUR_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put(SUCCESS_TEXT, SUCCESS_COLOUR);
        map.put(WARNING_TEXT, WARNING_COLOUR);
        map.put(CRITICAL_TEXT, CRITICAL_COLOUR);
        COLOUR_MAP = Collections.unmodifiableMap(map);
    }

    /**
     *
     */
    public static final List<String> DEFAULT_RESULTS = Arrays.asList(
            SUCCESS_TEXT, WARNING_TEXT, CRITICAL_TEXT);

    /**
     *
     */
    public AudioPlayer audioPlayer = new AudioPlayer();

    /**
     *
     */
    public DelayedEvent delayedEvent = new DelayedEvent();

    /**
     *
     */
    public DiagnosticTarget() {
        super();
        result = new Result(DEFAULT_RESULTS, CONSENSUS_MAP, COLOUR_MAP);
        visibleProperty().addListener((observable, oldValue, newValue) -> {
            // If the button is visible and the test is not complete...
            if (newValue && !isComplete()) {
                // Initiate the travel timer
                travelTimer.setup(getUser().getNavigation().getTimeout() * 60.0, (ActionEvent e) -> {
                    setResult(CRITICAL_TEXT);
                    setTries(getTries() + TIMEOUT_PENTALTY);
                    LOGGER.info("Target timed out");
                });
                travelTimer.play();
            }

        });

    }

    /**
     *
     */
    @Override
    public void click() {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.CLICK)) {
            // Get button that called the handle
            buttonHandler();
            travelTimer.end();
            setResult(SUCCESS_TEXT);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseEnter(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            // Get button that called the handle
            buttonHandler();
            // Setup and play the timeline using existing UI variables
            getPrimaryControl().animate().startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
            // If user is able to complete the tlDwell
            getPrimaryControl().animate().setOnScaleFinished((ActionEvent t) -> {
                if (getTries() <= SUCCESS_TRIES) {
                    // Set text and background to Good
                    setResult(SUCCESS_TEXT);
                } else if (getTries() >= WARNING_TRIES && getTries() < CRITICAL_TRIES) {
                    // Set text and background to Okay
                    setResult(WARNING_TEXT);
                } else if (getTries() >= CRITICAL_TRIES) {
                    setResult(CRITICAL_TEXT);
                }
                travelTime = travelTimer.get().currentTimeProperty().getValue();
                travelTimer.end();
            });

        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExit(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            getPrimaryControl().animate().stopScale();
            // Increment the number of tries
            incrementTries();
            if (getTries() >= WARNING_TRIES && getTries() < CRITICAL_TRIES) {
                // Set the target to "Try: 1" and so on
                strResult = "Try:" + getTries();
                //   button.setText(strResult);
            }
            if (getTries() >= CRITICAL_TRIES) {
                setResult(CRITICAL_TEXT);
            }

            //Original Size X
            getPrimaryControl().setScaleX(1);
            //Original Size Y
            getPrimaryControl().setScaleY(1);

        }
    }

    /**
     * Sets the results of the current target. Invoke a pauseUI to view the
     * result for a few seconds and play a sound if the audio file is available.
     * When completed load the next target.
     *
     * @param text text to set the button to upon completion of the test
     */
    public void setResult(String text) {
        result.set(text);
        getPrimaryControl().setText(result.get());
        getPrimaryControl().setStyle("-fx-background-color:#" + result.getColour()
                + ";-fx-text-fill: #ecf0f1;"
                + " -fx-font-weight: bold;");
        // Disable current target
        setDisable(true);
        delayedEvent.setup(getUser().getInteraction().getSelectionTime() * 5, (ActionEvent u) -> {
            setComplete(true);
        });
        delayedEvent.play();
    }

    // Tries
    private static final int DEFAULT_TRIES = 0;

    /**
     *
     */
    public static final Integer SUCCESS_TRIES = 1;

    /**
     *
     */
    public static final Integer WARNING_TRIES = 2;

    /**
     *
     */
    public static final Integer CRITICAL_TRIES = 5;
    private IntegerProperty tries;

    /**
     * Sets the number of attempts made to select this button
     *
     * @param value number of tries
     */
    public void setTries(Integer value) {
        triesProperty().setValue(value);
    }

    /**
     * Adds one to the number of attempts made to select this button
     */
    public void incrementTries() {
        setTries(getTries() + 1);
    }

    /**
     * Resets the number of attempts made to select this button to the
     * <code>SUCCESS_TRIES</code> constant.
     */
    public void resetTries() {
        setTries(SUCCESS_TRIES);
    }

    /**
     * Gets the number of attempts made to select this button
     *
     * @return tries
     */
    public Integer getTries() {
        return triesProperty().getValue();
    }

    /**
     * Represents the number of attempts to select this button
     *
     * @return tries
     */
    public IntegerProperty triesProperty() {
        if (tries == null) {
            tries = new SimpleIntegerProperty(DEFAULT_TRIES);
        }
        return tries;
    }

    /**
     * Gets the value of the current time in the travel
     * <code>TimelineBase</code>.
     *
     * @return travel time
     */
    public Duration getTravelTime() {
        if (travelTime == null) {
            travelTime = Duration.seconds(TIMEOUT);
        }
        return travelTime;
    }

    @Override
    public void run() {
        // Example: Play sound or animation.
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {

        primary = new AnimatedButton();
        load(primary);
        setCSS(CSS_CLASS, primary);
        setSelection(primary);
    }
}
