/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class Downloader implements Runnable {

    private static final String NAME = Downloader.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private static final int BUFFER_SIZE = 4096;
    private final String remoteURL;

    /**
     *
     */
    public HttpURLConnection httpConn;

    /**
     *
     */
    public String fileName = "";

    /**
     *
     */
    public String saveDirectory="";

    /**
     *
     * @param remoteURL
     */
    public Downloader(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(remoteURL);
            httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String disposition = httpConn.getHeaderField("Content-Disposition");
                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = remoteURL.substring(remoteURL.lastIndexOf("/") + 1,
                            remoteURL.length());
                }
                saveDirectory=createTempDir().getAbsolutePath();
                LOGGER.info("Content-Type = " + httpConn.getContentType());
                LOGGER.info("Content-Disposition = " + disposition);
                LOGGER.info("Content-Length = " + httpConn.getContentLength());
                LOGGER.info("fileName = " + fileName);
                LOGGER.info("Save Directory = " + saveDirectory);
                getTracker().setBegin(true);
                // opens input stream from the HTTP connection
                try (InputStream inputStream = httpConn.getInputStream()) {
                    String saveFilePath = saveDirectory + File.separator + fileName;
                    // opens an output stream to save into file
                    try (FileOutputStream outputStream = new FileOutputStream(saveFilePath)) {
                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        Integer byteCounter = 0;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            byteCounter += bytesRead;
                            Double progress = (double) byteCounter / httpConn.getContentLength();
                            getTracker().setProgress(progress);
                        }
                        outputStream.close();
                    }
                }
                LOGGER.info(fileName + " downloaded to: " + saveDirectory);
                getTracker().setComplete(true);
            } else {
                LOGGER.warn("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (MalformedURLException ex) {
            LOGGER.fatal(null, ex);
        } catch (IOException ex) {
            LOGGER.fatal(null, ex);
        }
    }

    /**
     *
     */
    public void abort() {
        if (getTracker().hasBegun()) {
            disconnect();
        }
    }

    /**
     *
     */
    public void disconnect() {
        httpConn.disconnect();
        getTracker().setComplete(false);
        getTracker().setBegin(false);
        getTracker().resetProgress();
    }

    private DownloadTracker downloadTracker;

    /**
     *
     * @return
     */
    public DownloadTracker getTracker() {
        if (downloadTracker == null) {
            downloadTracker = new DownloadTracker();
        }
        return downloadTracker;
    }
    
    
    /**
     *
     * @return
     */
    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";
        int TEMP_DIR_ATTEMPTS = 3;

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within "
                + TEMP_DIR_ATTEMPTS + " attempts (tried "
                + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
    }

}
