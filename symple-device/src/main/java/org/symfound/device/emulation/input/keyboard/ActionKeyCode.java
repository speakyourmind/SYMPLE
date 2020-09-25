/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.keyboard;

/**
 *
 * @author Javed Gangjee
 */
public interface ActionKeyCode {

    /**
     *
     */
    int SHIFT_DOWN = -1;

    /**
     *
     */
    int SYMBOL_DOWN = -2;

    /**
     *
     */
    int CLOSE = -3;

    /**
     *
     */
    int TAB = -4;

    /**
     *
     */
    int BACK_SPACE = -5;

    /**
     *
     */
    int CTRL_DOWN = -6;

    /**
     *
     */
    int LOCALE_SWITCH = -7;

    /**
     *
     */
    int DELETE = -8;

    /**
     *
     */
    int ENTER = -9;

    // Direction Keys

    /**
     *
     */
    int ARROW_UP = -10;

    /**
     *
     */
    int ARROW_DOWN = -11;

    /**
     *
     */
    int ARROW_LEFT = -12;

    /**
     *
     */
    int ARROW_RIGHT = -13;

    // Shortcut Keys

    /**
     *
     */
    int UNDO = -14;

    /**
     *
     */
    int REDO = -15;

    /**
     *
     */
    int COPY = -16;

    /**
     *
     */
    int PASTE = -17;

    /**
     *
     */
    int CUT = -18;

    /**
     *
     */
    int SAVE = -19;

    /**
     *
     */
    int SELECT_ALL = -20;
    
    /**
     *
     */
    int BLANK = -99;

    /**
     *
     */
    int UNASSIGNED = -100;
    
    /**
     *
     */
    int SPACE = -21;
    
    int CLEAR=-22;
    
}
