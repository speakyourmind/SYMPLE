/*
 * Copyright (C) 2015 SpeakYourMind Foundation
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
package org.symfound.test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.symfound.controls.RunnableControl;
import static org.symfound.tools.animation.NodeAnimator.MAX_OPACITY;
import static org.symfound.tools.animation.NodeAnimator.MIN_OPACITY;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Target extends RunnableControl {

    private BooleanProperty complete;

    /**
     *
     */
    public Integer rowIndex;

    /**
     *
     */
    public Integer columnIndex;

    /**
     *
     */
    public Result result;

    /**
     *
     * @return
     */
    public Result getResult() {
        return result;
    }

    /**
     * Determines that the button has been selected and the test is complete
     *
     * @param value complete if true, in progress otherwise
     */
    public void setComplete(Boolean value) {
        completeProperty().setValue(value);
    }

    /**
     * Checks whether the button has been successfully selected yet.
     *
     * @return true if complete, otherwise in progress
     */
    public Boolean isComplete() {
        return completeProperty().getValue();
    }

    /**
     * Represents the completion of the test on this button.
     *
     * @return complete
     */
    public BooleanProperty completeProperty() {
        if (complete == null) {
            complete = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return complete;
    }

    /**
     *
     */
    public void show() {
        setOpacity(0.0);
        setVisible(true);
        animate().startFade(0.5, MIN_OPACITY, MAX_OPACITY);
    }

    /**
     *
     */
    public void hide() {
        animate().startFade(0.5, MAX_OPACITY, MIN_OPACITY);
        setOpacity(0.0);
        setVisible(false);

    }

}
