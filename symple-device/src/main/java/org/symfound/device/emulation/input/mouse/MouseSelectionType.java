/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.mouse;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 */
public interface MouseSelectionType {
    
    /**
     *
     */
    String OFF = "Off";

    /**
     *
     */
    String ZOOM = "Zoom";

    /**
     *
     */
    String DOUBLE = "Double";

    /**
     *
     */
    String LEFT = "Left";

    /**
     *
     */
    String RIGHT = "Right";

    /**
     *
     */
    String SCROLL = "Scroll";

    /**
     *
     */
    String DRAG = "Drag";

    /**
     *
     */
    List<String> TYPES = Arrays.asList(
            OFF,
            LEFT,
            RIGHT,
            DOUBLE,
            DRAG,
            SCROLL,
            ZOOM);
}
