/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.main;

import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.prediction.retriever.PredictionRetriever;

/**
 *
 * @author Javed Gangjee
 */
public class PhraseMain {

    private static final String NAME = PhraseMain.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        PredictionRetriever retriever = new PredictionRetriever();
        List<String> name = retriever.retrievePredictions("I thought tha",5);
        LOGGER.info(name);
    }
}
