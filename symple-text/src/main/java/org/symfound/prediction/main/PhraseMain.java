/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.main;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.symfound.prediction.phrasefinder.retriever.PredictionRetriever;

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
        try {
            PredictionRetriever retriever = new PredictionRetriever();
            List<String> name = retriever.retrievePredictions("I would like to go to *",1,5);
            LOGGER.info(name);
        } catch (UnsupportedEncodingException | URISyntaxException ex) {
            java.util.logging.Logger.getLogger(PhraseMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
