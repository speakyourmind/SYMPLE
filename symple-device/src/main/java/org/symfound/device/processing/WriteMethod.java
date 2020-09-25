/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.processing;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 */
public interface WriteMethod {

    /**
     *
     */
    String NONE = "None";

    /**
     *
     */
    String LOCAL = "Local";

    /**
     *
     */
    String PORT = "Port";

    /**
     *
     */
    String FILE = "File";
    
    /**
     *
     */
    List<String> TYPES = Arrays.asList(NONE, LOCAL, PORT, FILE);
}
