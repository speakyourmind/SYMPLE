/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.retriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.prediction.phrasefinder.Corpus;
import org.symfound.prediction.phrasefinder.Phrase;
import org.symfound.prediction.phrasefinder.PhraseFinder;
import org.symfound.prediction.phrasefinder.SearchOptions;
import org.symfound.prediction.phrasefinder.SearchResult;
import org.symfound.prediction.query.QueryBuilder;
import org.symfound.prediction.query.string.StringAnalyzer;
import org.symfound.prediction.query.string.StringOperator;

/**
 *
 * @author Javed Gangjee
 */
public class PredictionRetriever {

    /**
     *
     */
    public static final String NAME = PredictionRetriever.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final int MAX_N_GRAM = 5;

    /**
     *
     */
    public static final int MAX_RESULTS = 10;

    /**
     *
     */
    public static final int DEFAULT_NUM_WORDS = 1;

    /**
     *
     */
    public static final List<String> DEFAULT_PREDICTIONS = Arrays.asList("If", "The", "Of", "I");

    /**
     *
     * @param previous
     * @return
     */
    public List<String> retrievePredictions(String previous) {
        return retrievePredictions(previous, DEFAULT_NUM_WORDS, MAX_RESULTS);
    }

    /**
     *
     * @param previous
     * @param numResults
     * @return
     */
    public List<String> retrievePredictions(String previous, Integer numResults) {
        return retrievePredictions(previous, DEFAULT_NUM_WORDS, numResults);
    }

    /**
     *
     * @param original
     * @param numWordsToPredict
     * @param numResults
     * @return
     */
    public List<String> retrievePredictions(String original, Integer numWordsToPredict, Integer numResults) {

        List<String> suggestions = new ArrayList<>();
        List<String> predictions;

        if (!StringAnalyzer.isEndOfSentence(original)) {
            Integer numWordsToQuery = MAX_N_GRAM;
            while (numWordsToQuery > 0) {
                //Gather phrase to be queried
                String retrievablePhrase = getBuilder().extractRetrievablePhrase(original, numWordsToQuery);
                //Add operators for query
                String query = getBuilder().buildQuery(numWordsToPredict, retrievablePhrase, original);
                // Optional: set the maximum number of phrases to return.
                SearchOptions options = new SearchOptions();
                options.setMaxResults(10);
                // Send the request.
                SearchResult result;
                try {
                    LOGGER.info("Searching phrases for query: " + query);
                    result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query, options);
                    if (result.getStatus() != SearchResult.Status.OK) {
                        LOGGER.warn("Request was not successful: " + result.getStatus());
                    } else {
                        Phrase[] phrases = result.getPhrases();
                        if (phrases.length >= 1) {
                            // Print phrases line by line.
                            for (Phrase phrase : phrases) {
                                LOGGER.info(phrase.getScore() + " - " + phrase.toString());
                                String prediction = phrase.toString().substring(retrievablePhrase.length() - 1);
                                if (!StringAnalyzer.isPunctuation(prediction)) {
                                    suggestions.add(prediction);
                                }
                            }
                            if (suggestions.size() > 0) {
                                LOGGER.info(suggestions);
                                numWordsToQuery = 0;
                            } else {
                                numWordsToQuery--;
                            }
                        } else {
                            numWordsToQuery--;
                        }
                    }
                } catch (IOException ex) {
                    LOGGER.fatal("Unable to retrieve result", ex);
                }
            }

            //Remove duplicates from suggestions
            suggestions = StringOperator.removeDuplicates(suggestions);

            //Extract required number of predictions from suggestions.
            Integer n = (suggestions.size() > numResults) ? numResults : suggestions.size();
            predictions = suggestions.subList(0, n);
            //Set to default if nothing is found
            if (predictions.isEmpty()) {
                predictions = DEFAULT_PREDICTIONS;
            }
        } else {
            //Set to default if ends in a sentence
            predictions = DEFAULT_PREDICTIONS;
        }
        return predictions;

    }

    private QueryBuilder builder;

    /**
     *
     * @return
     */
    public QueryBuilder getBuilder() {
        if (builder == null){
            builder = new QueryBuilder();
        }
        return builder;
    }
}
