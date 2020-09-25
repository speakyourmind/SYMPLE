/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.query.string;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Javed Gangjee
 */
public class StringOperator {

    /**
     *
     * @param previous
     * @return
     */
    public static List<String> splitIntoWords(String previous) {
        List<String> words = Arrays.asList(removePunctuation(previous).split("\\s+"));
        return words;
    }

    /**
     *
     * @param list
     * @return
     */
    public static List<String> removeDuplicates(List<String> list) {
        list = list.stream().distinct().collect(Collectors.toList());
        return list;
    }

    /**
     *
     * @param previous
     * @return
     */
    public static String removePunctuation(String previous) {
        return previous.replaceAll("[^a-zA-Z'$\\s]", "");
    }
    
}
