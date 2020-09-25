/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.voice.engine.marytts;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.symfound.voice.builder.VoiceManager;

/**
 *
 * @author Javed Gangjee
 */
public class MaryVoiceManager extends VoiceManager{
   
    /**
     *
     */
    public static final String MARY_VOICE = "Mary";

    /**
     *
     */
    public static final String MARY_MARYTTS = "cmu-slt-hsmm";
       
    /**
     *
     */
    public static final String JOHN_VOICE = "John"; // Not Implemented: Requires external libs

    /**
     *
     */
    public static final String JOHN_MARYTTS = "cmu-rms";
    
    /**
     *
     */
    public static final List<String> AVAILABLE_VOICES =           
           asList(MARY_VOICE);
    
    static {
        HashMap<String, String> map = new HashMap<>();
        map.put(MARY_VOICE, MARY_MARYTTS);
        VOICE_MAP = unmodifiableMap(map);
    }

    /**
     *
     */
    public static Map<String, String> VOICE_MAP;
    
    /**
     *
     * @return
     */
    @Override
    public List<String> getAvailableVoices() {
        return AVAILABLE_VOICES;
    } 
}
