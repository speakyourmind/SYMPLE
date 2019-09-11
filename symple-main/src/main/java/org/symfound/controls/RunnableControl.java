/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
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
package org.symfound.controls;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.selection.SelectionMethod;
import static org.symfound.controls.AppableControl.LOGGER;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.input.switcher.SwitchDirection;
import org.symfound.device.emulation.input.switcher.SwitchDirector;
import static org.symfound.device.emulation.input.switcher.SwitchDirector.*;
import org.symfound.device.emulation.input.switcher.SwitchListener;
import org.symfound.device.emulation.reaction.EmulatedReaction;
import org.symfound.device.emulation.reaction.PhysicalReaction;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.processing.Processor;
import org.symfound.device.selection.SelectionEventType;
import org.symfound.main.HomeController;
import org.symfound.main.Main;
import org.symfound.controls.user.voice.TTSManager;
import org.symfound.device.emulation.EmulationRequest;
import org.symfound.main.builder.UI;
import org.symfound.tools.animation.NodeAnimator;
import org.symfound.tools.timing.transition.ScanTransition;
import org.symfound.voice.builder.TTSPlayer;
import org.symfound.voice.main.TTSLauncher;
import org.symfound.voice.player.AudioPlayer;

/**
 * Defines the base pane of every custom control created.
 * <p>
 * There is one {@link AnimatedButton} available. Since the use of this button
 * is entirely optional it needs to be added to each newly created control
 * manually in the constructor or elsewhere.
 * <p>
 *
 * @author Javed Gangjee
 */
// TO DO: Moving towards Reactions
public class RunnableControl extends ScreenControl<AnimatedButton> implements Runnable {

    // Common Timing variables
    /**
     *
     */
    public ScanTransition scanTimeline = new ScanTransition();

    /**
     *
     */
    public RunnableControl() {
        this("");
    }

    /**
     *
     * @param cssClass
     */
    public RunnableControl(String cssClass) {
        super(cssClass);
    }

    // Common Button Variables
    /**
     *
     */
    public String id;

    /**
     *
     */
    public String srcText;

    /**
     *
     */
    public String srcStyle;

    /**
     * Initialize variables that are common to all MouseEvent handle methods.
     * This includes: -button source of the click (could be expanded for other
     * controls), -original text of the button at the time of the event,
     * -original style of the button at the time of the event.
     *
     */
    public void buttonHandler() {
        // Save the original text
        id = getPrimaryControl().getId();
        // Save the original text
        srcText = getPrimaryControl().getText();
        // Save the original style
        srcStyle = getPrimaryControl().getStyle();
        // Bring button to front
        toFront();
    }

    /**
     *
     * @param node
     */
    public final void setSelection(Node node) {
        getPhysicalDwellReaction().configure(node);
        controlTypeProperty().addListener((observable, oldValue, newValue) -> {
            getPhysicalDwellReaction().configure(node);
        });

        getPhysicalClickReaction().configure(node);
        final Device currentDevice = getSession().getDeviceManager().getCurrent();
        Hardware hardware = currentDevice.getHardware();
        hardware.getSelectability().getClickability().eventTypeProperty().addListener((observable, oldValue, newValue) -> {
            getPhysicalClickReaction().configure(node);
        });

        //  getEmulatedSwitchReaction().configure();
    }

    /**
     *
     */
    public void click() {
        buttonHandler();
        if (!getSession().isMutex()) {
            getSession().setMutex(true);
            execute();
        }
    }

    //DWELL METHODS
    /**
     *
     * @param e
     */
    public void mouseEnter(MouseEvent e) {
        SelectionMethod deducedMethod = getDeducedSelectionMethod();

        if (deducedMethod.equals(SelectionMethod.DWELL)) {
            buttonHandler();
            enter();
        }
    }

    /**
     *
     */
    public void enter() {
        if (!getSession().isMutex()) {
            getSession().setMutex(true);
            final NodeAnimator animate = getPrimaryControl().animate();
            animate.startScale(getUser().getTiming().getDwellTime(), 1.0, NodeAnimator.DWELL_SCALE);
            animate.setOnScaleFinished((ActionEvent t) -> {
                execute();
            });
        }
    }

    /**
     *
     * @param e
     */
    public void mouseExit(MouseEvent e) {
        SelectionMethod deducedMethod = getDeducedSelectionMethod();
        if (deducedMethod.equals(SelectionMethod.DWELL)) {
            exit();
        }
    }

    private SelectionMethod getDeducedSelectionMethod() {
        SelectionMethod deducedMethod;
        Boolean overrideSelectionMethod = getUser().getInteraction().isInAssistedMode();
        SelectionMethod userMethod = getUser().getInteraction().getSelectionMethod();
        if (this.getParent() instanceof ConfigurableGrid) {
            ConfigurableGrid grid = (ConfigurableGrid) this.getParent();
            SelectionMethod gridMethod = grid.getSelectionMethod();
            deducedMethod = (overrideSelectionMethod) ? gridMethod : userMethod;
        } else {
            deducedMethod = userMethod;
        }
        return deducedMethod;
    }

    /**
     *
     */
    public void exit() {
        getPrimaryControl().animate().stopScale();
        getPrimaryControl().revert(srcText, srcStyle);
    }

    //EXECUTION METHODS
    /**
     *
     */
    public void execute() {
        final Navigation navigation = Main.getSession().getUser().getNavigation();
        if (this instanceof AppableControl) {
            AppableControl appable = (AppableControl) this;
            /* if (navigation.speakSelection()) {
                if (appable.isSpeakable()) {
                    appable.speak(getSpeakText());
                }
            }*/
            Platform.runLater(this);

            if (appable.navigatePostClick()) {
                if (!appable.getNavigateIndex().isEmpty()) {
                    UI ui = (UI) getScene().getWindow();
                    if (ui.inEditMode()) {
                        LOGGER.info("Exiting edit mode before navigating");
                        ui.setEditMode(Boolean.FALSE);
                    }
                    LOGGER.info("Post click navigation requested by " + this.getText());
                    appable.openHomeScreen();

                    ConfigurableGrid configurableGrid = HomeController.getGrid().getConfigurableGrid();

                    if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.SCAN) || getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.STEP)) {
                        Device current = getSession().getDeviceManager().getCurrent();
                        EmulationManager em = current.getProcessor().getEmulationManager();
                        final EmulationRequest emulationRequest = new EmulationRequest();
                        emulationRequest.setPosition(new Point(0, 0));
                        em.getMouse().getAutomator().navigate(new Point((int) (getParentUI().getWidth() / 2), (int) (getParentUI().getHeight() / 2)));

                    }
                    configurableGrid.setIndex(appable.getNavigateIndex());
                    getSession().setPlaying(false);
                }
            }
        }

        if (navigation.playSelectSound()) {
            try {
                AudioPlayer audioPlayer = new AudioPlayer();
                final File file = new File(Main.getSession().getUser().getNavigation().getSelectionSound());
                audioPlayer.playAudioFile(file);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                LOGGER.fatal(null, ex);
            }
        }
        setExecuted(true);
        getPrimaryControl().setOnFinished(srcText, srcStyle);
        setExecuted(false);

    }

    //SWITCH METHODS
    /**
     *
     */
    public Map<SwitchDirection, BooleanProperty> directionMap;

    /**
     *
     * @return
     */
    public Map<SwitchDirection, BooleanProperty> getDirectionMap() {
        if (directionMap == null) {
            HashMap<SwitchDirection, BooleanProperty> map = new HashMap<>();
            map.put(UP, upProperty());
            map.put(DOWN, downProperty());
            map.put(LEFT, leftProperty());
            map.put(RIGHT, rightProperty());
            directionMap = Collections.unmodifiableMap(map);

        }
        return directionMap;
    }

    private BooleanProperty up;

    /**
     *
     * @param value
     */
    public void setUp(Boolean value) {
        upProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isUp() {
        return upProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty upProperty() {
        if (up == null) {
            up = new SimpleBooleanProperty(true);
        }
        return up;
    }
    private BooleanProperty down;

    /**
     *
     * @param value
     */
    public void setDown(Boolean value) {
        downProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isDown() {
        return downProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty downProperty() {
        if (down == null) {
            down = new SimpleBooleanProperty(true);
        }
        return down;
    }
    private BooleanProperty left;

    /**
     *
     * @param value
     */
    public void setLeft(Boolean value) {
        leftProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isLeft() {
        return leftProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty leftProperty() {
        if (left == null) {
            left = new SimpleBooleanProperty(true);
        }
        return left;
    }
    private BooleanProperty right;

    /**
     *
     * @param value
     */
    public void setRight(Boolean value) {
        rightProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isRight() {
        return rightProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty rightProperty() {
        if (right == null) {
            right = new SimpleBooleanProperty(true);
        }
        return right;
    }

    //PROPERTIES
    /**
     * Sets the alignment of the root <code>AnimatedButton</code>
     *
     * @param value alignment position
     */
    public void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    /**
     * F
     * Gets the alignment of the root <code>AnimatedButton</code>
     *
     * @return alignment
     */
    public Pos getAlignment() {
        return alignmentProperty().get();
    }

    /**
     * Represents the alignment of the root <code>AnimatedButton</code>
     *
     * @return alignment
     */
    public ObjectProperty<Pos> alignmentProperty() {
        return getPrimaryControl().alignmentProperty();
    }

    /**
     *
     */
    public String initType;
    private StringProperty type;

    /**
     *
     * @param value
     */
    public void setType(String value) {
        typeProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getType() {
        return typeProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty typeProperty() {
        if (type == null) {
            type = new SimpleStringProperty(initType);
        }
        return type;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        primary.setWrapText(Boolean.FALSE);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
    }

    private EmulatedReaction emulatedSwitch;

    /**
     *
     * @return
     */
    public EmulatedReaction getEmulatedSwitchReaction() {
        if (emulatedSwitch == null) {
            emulatedSwitch = new EmulatedReaction(SelectionMethod.SWITCH) {

                @Override
                public void configure() {
                    getSession().getDeviceManager().currentProperty().addListener((observable, oldValue, newValue) -> {
                        final Processor processor = newValue.getProcessor();
                        EmulationManager emulationManager = processor.getEmulationManager();
                        SwitchListener listener = emulationManager.getSwitch().getListener();
                        SwitchDirector director = new SwitchDirector(listener);

                        final ChangeListener<? super Boolean> executionListener = (observableValue2, oldValue2, newValue2) -> {
                            if (newValue2) {
                                if (getScene().getWindow().isShowing() && getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.SWITCH)) {
                                    execute();
                                }
                            }
                        };
                        final ObjectProperty<SwitchDirection> currentDirectionProperty = director.currentDirectionProperty();
                        for (SwitchDirection direction : SwitchDirector.directions) {
                            final Boolean isEnabled = getDirectionMap().get(direction).getValue();
                            if (isEnabled) {
                                Bindings.equal(direction, currentDirectionProperty).addListener(executionListener);
                            }
                        }
                    });
                }
            };
        }

        return emulatedSwitch;
    }

    private PhysicalReaction physicalClick;

    /**
     *
     * @return
     */
    public PhysicalReaction getPhysicalClickReaction() {
        if (physicalClick == null) {
            physicalClick = new PhysicalReaction(SelectionMethod.CLICK) {

                @Override
                public void configure(Node node) {
                    final EventHandler<MouseEvent> execute = (MouseEvent e) -> {
                        click();
                    };
                    if (getCurrentHardware() != null) {
                        Hardware hardware = getCurrentHardware();
                        switch (hardware.getSelectability().getClickability().getEventType().getValue()) {
                            case SelectionEventType.RELEASE_SELECT:
                                node.setOnMousePressed(null);
                                node.setOnMouseReleased(execute);
                                break;
                            case SelectionEventType.PRESS_SELECT:
                                node.setOnMousePressed(execute);
                                node.setOnMouseReleased(null);
                                break;
                        }
                    }
                }

            };
        }
        return physicalClick;
    }

    private PhysicalReaction physicalDwell;

    /**
     *
     * @return
     */
    public PhysicalReaction getPhysicalDwellReaction() {
        if (physicalDwell == null) {
            physicalDwell = new PhysicalReaction(SelectionMethod.DWELL) {

                @Override
                public void configure(Node node) {
                    if (!getControlType().equals(ControlType.SETTING_CONTROL)) {
                        set(node);
                    } else {
                        clear(node);
                    }
                }

                public void set(Node node) {
                    node.setOnMouseEntered((MouseEvent f) -> {
                        mouseEnter(f);
                    });
                    node.setOnMouseExited((MouseEvent f) -> {
                        mouseExit(f);
                    });
                }

                public void clear(Node node) {
                    node.setOnMouseEntered(null);
                    node.setOnMouseExited(null);
                }

            };
        }
        return physicalDwell;
    }

    @Override
    public void run() {
        // Not Implemented
    }
    /**
     *
     */
    public IntegerProperty gridLocation;

    /**
     *
     * @param value
     */
    public void setGridLocation(Integer value) {
        gridLocationProperty().set(value);
    }

    /**
     *
     * @return
     */
    public Integer getGridLocation() {
        return gridLocationProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty gridLocationProperty() {
        if (gridLocation == null) {
            gridLocation = new SimpleIntegerProperty(0);
        }
        return gridLocation;
    }

    private BooleanProperty execute;

    /**
     *
     * @param value
     */
    public void setExecuted(Boolean value) {
        executeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public BooleanProperty executeProperty() {
        if (execute == null) {
            execute = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return execute;
    }

    /*public void speak(String text) {
    TTSManager ttsManager = getSession().getTTSManager();
    String currentVoiceName = ttsManager.getIterator().get();
    
    TTSLauncher launcher = ttsManager.getLauncher();
    String currentEngine = TTSLauncher.getVoiceMap().get(currentVoiceName);
    TTSPlayer engine = launcher.getPlayerMap().get(currentEngine);
    
    String message = "Speak Button Clicked. Playing the text: " + getText()
    + " with voice:" + currentVoiceName
    + " from engine " + currentEngine;
    LOGGER.info(message);
    engine.play(text, true, currentVoiceName);
    }*/
    /**
     *
     */
    public List<String> DEFAULT_SKIP_CHARS = Arrays.asList("https://");

    /**
     *
     * @param text
     */
    public void speak(String text) {
        speak(text, DEFAULT_SKIP_CHARS);
    }

    /**
     *
     * @param text
     * @param skipChars
     */
    public void speak(String text, List<String> skipChars) {
        TTSManager ttsManager = getSession().getTTSManager();
        String currentVoiceName = ttsManager.getIterator().get();
        TTSLauncher launcher = ttsManager.getLauncher();
        String currentEngine = TTSLauncher.getVoiceMap().get(currentVoiceName);
        TTSPlayer player = launcher.getPlayerMap().get(currentEngine);

        if (text != null) {
            if (!text.trim().isEmpty()) {
                for (String skipChar : skipChars) {
                    text = text.replaceAll(skipChar, "");
                }
                String message = "Playing the text: " + text + "; using voice:" + currentVoiceName;
                LOGGER.info(message);
                player.play(text, true, currentVoiceName);
            } else {
                LOGGER.warn("There is no text available to play");
            }

        } else {
            LOGGER.warn("Attempting to speak null text");
        }
    }

    /**
     *
     */
    public void stopSpeak() {
        TTSManager ttsManager = getSession().getTTSManager();
        String currentVoiceName = ttsManager.getIterator().get();
        TTSLauncher launcher = ttsManager.getLauncher();
        String currentEngine = TTSLauncher.getVoiceMap().get(currentVoiceName);
        TTSPlayer player = launcher.getPlayerMap().get(currentEngine);
        String message = "Stop Button Clicked. Stopping voice:" + currentVoiceName;
        LOGGER.info(message);
        player.getEngine().setStopped(true);
    }

}
