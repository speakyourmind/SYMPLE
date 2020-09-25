/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.query.string;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Javed Gangjee
 */
public class StringAnalyzer {

    /**
     *
     * @param prediction
     * @return
     */
    public static Boolean isPunctuation(String prediction) {
        return Pattern.matches("\\p{Punct}", prediction);
    }

    /**
     *
     */
    public static final List<String> SENTENCE_ENDING = Arrays.asList(".", "!");

    /**
     *
     * @param previous
     * @return
     */
    public static Boolean isEndOfSentence(String previous) {
        Boolean isPunctuated = false;
        for (String puncutation : SENTENCE_ENDING) {
            isPunctuated = isPunctuated || previous.trim().endsWith(puncutation);
        }
        return isPunctuated;
    }
    
    /**
     *
     */
    public static final List<String> SENTENCE_CONTINUING = Arrays.asList(" ", ", ", "; ",",", ";");

    /**
     *
     * @param previous
     * @return
     */
    public static Boolean isContinuing(String previous) {
        Boolean isContinuing = false;
        for (String continuation : SENTENCE_CONTINUING) {
            isContinuing = isContinuing || previous.substring(0, previous.length()-1).endsWith(continuation);
        }
        return isContinuing;
    }
}
