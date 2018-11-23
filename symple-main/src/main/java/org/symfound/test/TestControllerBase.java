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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import org.symfound.app.CommonController;

/**
 *
 * @author Javed Gangjee
 */
public abstract class TestControllerBase extends CommonController {

    /**
     *
     * @param e
     */
    public abstract void play(MouseEvent e);

    /**
     *
     */
    public abstract void done();

    /**
     *
     */
    public abstract void complete();

    /**
     * Returns an getAverage number of tries for the test.
     *
     * @param sum total number of tries for the full test
     * @param total total number of target buttons used in the test
     * @return
     */
    public Double getAverage(Double sum, Double total) {
        Double average = sum / total;
        return average;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
