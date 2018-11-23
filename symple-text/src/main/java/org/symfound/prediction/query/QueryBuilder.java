/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.query;

import java.util.List;
import org.symfound.prediction.query.string.StringAnalyzer;
import org.symfound.prediction.query.string.StringOperator;
import static org.symfound.prediction.retriever.PredictionRetriever.LOGGER;

/**
 *
 * @author Javed Gangjee
 */
public class QueryBuilder {

    /**
     *
     */
    public enum QueryCase {

        /**
         *
         */
        PREFIX,

        /**
         *
         */
        NEXT_WORD,

        /**
         *
         */
        SKIP,

        /**
         *
         */
        NONE
    }
    
    /**
     *
     * @param predictionLength
     * @param modified
     * @param original
     * @return
     */
    public String buildQuery(Integer predictionLength, String modified, String original) {
        String queryBuild = "";
        String end = original.substring(original.length()-1);
        
        
        if (StringAnalyzer.isContinuing(original)) {
            queryBuild = queryBuild.concat(" ");
            while (predictionLength != 0) {
                queryBuild = queryBuild.concat("?");
                predictionLength--;
            }
        } else {
            //Prefix query
            queryBuild = queryBuild.concat("+");
        }
        String query = modified.concat(queryBuild);
        return query;
    }

    /**
     *
     * @param original
     * @param queryLength
     * @return
     */
    public String extractRetrievablePhrase(String original, Integer queryLength) {
        List<String> words = StringOperator.splitIntoWords(original);
        LOGGER.info("List of words:" + words);

        Integer n = 0;
        n = ((words.size() > queryLength) ? queryLength : words.size());
        List<String> subList = words.subList(words.size() - n, words.size());

        LOGGER.info("Words to search with: " + subList);
        String queryPhrase = "";

        for (String word : subList) {
            queryPhrase = queryPhrase.concat(" ").concat(word);
        }
        return queryPhrase;

    }

}
