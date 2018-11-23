/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.web;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class Webhook {

    private static final String NAME = Webhook.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     * 
     * @param postURL
     * @throws IOException 
     */
    public void request(String postURL) throws IOException {
        HttpClient httpClient = buildClient();
        executePost(postURL, httpClient);
    }

    /**
     * 
     * @param postURL
     * @param httpClient
     * @throws IOException 
     */
    public void executePost(String postURL, HttpClient httpClient) throws IOException {
        LOGGER.info("POST URL:" + postURL);
        HttpPost httpPost = new HttpPost(postURL);
        httpClient.execute(httpPost);
    }

    /**
     * 
     * 
     * @return 
     */
    public HttpClient buildClient() {
        HttpClientBuilder client = HttpClientBuilder.create();
        HttpClient httpClient = client.build();
        return httpClient;
    }
}
