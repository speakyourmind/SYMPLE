/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.port;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Javed Gangjee
 */
public class JSONReader {

    private static final String NAME = JSONReader.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String EOL = System.getProperty("line.separator");

    private URLCaller urlCaller;
    private Object object = new Object();

    /**
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object get = null;
        try {
            get = ((JSONObject) object).get(key);
        } catch (Exception ex) {

        }
        return get;
    }

    /**
     *
     * @param index
     * @return
     */
    public Object getArrayObject(Integer index) {
        return ((JSONArray) object).get(index);
    }

    /**
     *
     * @param urlLocation
     * @throws InterruptedException
     * @throws MalformedURLException
     * @throws ExecutionException
     */
    public void loadPackage(String urlLocation) throws InterruptedException, MalformedURLException, ExecutionException {
        final URL url = new URL(urlLocation);
        urlCaller = new URLCaller(url);
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Object> future = es.submit(urlCaller);
        object = future.get();
        es.shutdown();
    }
}
