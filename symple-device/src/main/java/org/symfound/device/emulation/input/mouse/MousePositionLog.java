/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.mouse;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Javed Gangjee
 */
public class MousePositionLog extends ArrayList<Point> {

    /**
     *
     * @param position
     * @param sampleSize
     */
    public void log(Point position, Integer sampleSize) {
        add(position);
        if (size() > sampleSize) {
            removeRange(0, size() - sampleSize);
        }
    }

    /**
     *
     * @return
     */
    public Integer getSampleSize() {
        return size();
    }
}
