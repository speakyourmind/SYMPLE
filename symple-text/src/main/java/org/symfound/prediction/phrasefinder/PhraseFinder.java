// This file is part of PhraseFinder. http://phrasefinder.io
//
// Copyright (C) 2016-2018 Martin Trenkmann
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.symfound.prediction.phrasefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.prediction.phrasefinder.SearchResult.Status;
import org.symfound.prediction.phrasefinder.Token.Tag;

/**
 * PhraseFinder provides static routines for querying the
 * <a href="http://phrasefinder.io">PhraseFinder</a> web service.
 *
 * @see PhraseFinder#search(Corpus, String)
 * @see PhraseFinder#search(Corpus, String, SearchOptions)
 */
public final class PhraseFinder {

    private static final String NAME = PhraseFinder.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public PhraseFinder() {
    }

    /**
     * The URL to send search requests to.
     */
    public static final String BASE_URL = "http://phrasefinder.io/search";

    /**
     * Sends a search request with default parameters.
     *
     * @param corpus is the corpus to be searched.
     * @param query is the query string.
     * @return A {@link SearchResult} object that contains matching phrases if
     * {@link SearchResult#getStatus()} yields {@link SearchResult.Status#OK}.
     * Any other status value is considered an unsuccessful request.
     * @throws IOException when sending the request or receiving the response
     * failed.
     */
    public static SearchResult search(Corpus corpus, String query) throws IOException {
        return search(corpus, query, SearchOptions.defaultInstance());
    }

    /**
     * Sends a search request with custom parameters.
     *
     * @param corpus is the corpus to be searched.
     * @param query is the query string.
     * @param options are additional request parameters.
     * @return Same as {@link #search(Corpus, String)}
     * @throws IOException Same as {@link #search(Corpus, String)}
     */
    public static SearchResult search(Corpus corpus, String query, SearchOptions options)
            throws IOException {
        HttpURLConnection connection
                = (HttpURLConnection) makeUrl(corpus, query, options).openConnection();
        Status status = Status.fromHttpStatusCode(connection.getResponseCode());
        if (status == Status.OK) {
            LOGGER.info("Status is " + status.name());
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(),
                    java.nio.charset.StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = null;
                List<Phrase> phrases = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split("\t");
                    String[] tokens = fields[0].split(" ");
                    Phrase phrase = new Phrase();
                    phrase.tokens = new Token[tokens.length];

                    for (int i = 0; i < tokens.length; i++) {
                        int tokenLength = tokens[i].length();
                        int tagOrdinal = Integer.parseInt(tokens[i].substring(tokenLength - 1));
                        phrase.tokens[i] = new Token(Tag.fromOrdinal(tagOrdinal), tokens[i].substring(0, tokenLength - 2));
                    }
                    phrase.matchCount = Long.parseLong(fields[1]);
                    phrase.volumeCount = Integer.parseInt(fields[2]);
                    phrase.firstYear = Integer.parseInt(fields[3]);
                    phrase.lastYear = Integer.parseInt(fields[4]);
                    phrase.id = Long.parseLong(fields[5]);
                    phrase.score = Double.parseDouble(fields[6]);
                    phrases.add(phrase);
                    LOGGER.info("Adding phrase:" + phrase.toString());
                }
                if (phrases.size() > 0) {
                    LOGGER.info(phrases.size() + " phrases found");
                } else {
                    LOGGER.warn("No phrases found");
                }
                Phrase[] phraseArray = SearchResult.emptyInstance().getPhrases();
                SearchResult searchResult = new SearchResult(phrases.toArray(phraseArray));
                return searchResult;
            }
        } else {
            LOGGER.warn("Status is " + status.name());
        }
        return new SearchResult(status);
    }

    private static URL makeUrl(Corpus corpus, String query, SearchOptions options)
            throws UnsupportedEncodingException, MalformedURLException {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL).append("?format=tsv");
        sb.append("&corpus=").append(corpus.shortName());
        sb.append("&query=")
                .append(URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8.toString()));
        if (options.getMinPhraseLength() != SearchOptions.defaultInstance().getMinPhraseLength()) {
            sb.append("&nmin=").append(options.getMinPhraseLength());
        }
        if (options.getMaxPhraseLength() != SearchOptions.defaultInstance().getMaxPhraseLength()) {
            sb.append("&nmax=").append(options.getMaxPhraseLength());
        }
        if (options.getMaxResults() != SearchOptions.defaultInstance().getMaxResults()) {
            sb.append("&topk=").append(options.getMaxResults());
        }
        return new URL(sb.toString());
    }

}
