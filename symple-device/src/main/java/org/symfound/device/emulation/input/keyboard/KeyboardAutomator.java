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
package org.symfound.device.emulation.input.keyboard;

import java.awt.event.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.comtel2000.keyboard.control.KeyButton;
import org.comtel2000.keyboard.event.KeyButtonEvent;
import org.comtel2000.keyboard.robot.IRobot;
import org.comtel2000.swing.robot.NativeAsciiRobotHandler;
import org.symfound.device.emulation.input.InputAutomator;

/**
 *
 * @author Javed Gangjee
 */
public final class KeyboardAutomator implements InputAutomator {

    private static final String NAME = KeyboardAutomator.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private static final ObservableList<IRobot> robots = FXCollections.observableArrayList();

    /**
     *
     */
    public KeyboardAutomator() {
        addRobotHandler(new NativeAsciiRobotHandler());
    }

    /**
     *
     * @param ch
     * @param ctrl
     */
    public void sendToComponent(char ch, boolean ctrl) {
        if (robots.isEmpty()) {
            LOGGER.fatal("No robot handler available");
            return;
        }
        robots.stream().forEach((robot) -> {
            robot.sendToComponent(this, ch, ctrl);
        });

    }

    /**
     *
     * @param robot
     */
    public void addRobotHandler(IRobot robot) {
        robots.add(robot);
    }

    /**
     *
     * @param robot
     */
    public void removeRobotHandler(IRobot robot) {
        robots.remove(robot);
    }
    
    /**
     *
     * @param event
     */
    public final void handle(KeyButtonEvent event) {
        event.consume();
        KeyButtonEvent kbEvent = event;
        if (!kbEvent.getEventType().equals(KeyButtonEvent.SHORT_PRESSED)) {
            LOGGER.warn("ignore non short pressed events");
            return;
        }
        KeyButton kb = (KeyButton) kbEvent.getSource();
        switch (kb.getKeyCode()) {
            case ActionKeyCode.TAB:
                sendToComponent((char) KeyEvent.VK_TAB, true);
                break;
            case ActionKeyCode.BACK_SPACE:
                sendToComponent((char) KeyEvent.VK_BACK_SPACE, true);
                break;
            case ActionKeyCode.DELETE:
                sendToComponent((char) KeyEvent.VK_DELETE, true);
                break;
            case ActionKeyCode.ENTER:
                sendToComponent((char) KeyEvent.VK_ENTER, true);
                break;
            case ActionKeyCode.ARROW_UP:
                sendToComponent((char) KeyEvent.VK_UP, true);
                break;
            case ActionKeyCode.ARROW_DOWN:
                sendToComponent((char) KeyEvent.VK_DOWN, true);
                break;
            case ActionKeyCode.ARROW_LEFT:
                sendToComponent((char) KeyEvent.VK_LEFT, true);
                break;
            case ActionKeyCode.ARROW_RIGHT:
                sendToComponent((char) KeyEvent.VK_RIGHT, true);
                break;
            case ActionKeyCode.UNDO:
                sendToComponent((char) KeyEvent.VK_Z, true);
                break;
            case ActionKeyCode.REDO:
                sendToComponent((char) KeyEvent.VK_Y, true);
                break;
            case ActionKeyCode.COPY:
                sendToComponent((char) KeyEvent.VK_C, true);
                break;
            case ActionKeyCode.PASTE:
                sendToComponent((char) KeyEvent.VK_V, true);
                break;
            case ActionKeyCode.CUT:
                sendToComponent((char) KeyEvent.VK_X, true);
                break;
            case ActionKeyCode.SAVE:
                sendToComponent((char) KeyEvent.VK_S, true);
                break;
            case ActionKeyCode.SELECT_ALL:
                sendToComponent((char) KeyEvent.VK_A, true);
                break;
            default:
                sendToComponent((char) kb.getKeyCode(), false);
                break;
        }
    }

}
