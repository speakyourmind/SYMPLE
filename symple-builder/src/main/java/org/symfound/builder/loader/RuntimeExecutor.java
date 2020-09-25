/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import static org.symfound.text.TextOperator.EOL;

/**
 *
 * @author Javed Gangjee
 */
public class RuntimeExecutor {

    private static final String NAME = RuntimeExecutor.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private Process process;

    /**
     *
     * @param command
     */
    public void execute(String command) {
        Runnable runnable = prepare(command);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(runnable);
    }

    /**
     *
     * @param command
     * @return
     */
    public Runnable prepare(String command) {
        Runnable runnable = () -> {
            try {
                LOGGER.debug("Running command " + command);
                process = Runtime.getRuntime().exec(command);
                configureLogging(process);
            } catch (IOException ex) {
                LOGGER.fatal("Issue running command: " + command, ex);
            }
        };
        return runnable;
    }

    /**
     *
     */
    public void destroy() {
        if (process.isAlive()) {
            LOGGER.info("Stopping process");
            process.destroy();
        } else {
            LOGGER.warn("Process is not running and does not require a close");
        }
    }

    /**
     *
     * @param process
     * @throws IOException
     */
    public void configureLogging(Process process) throws IOException {
        logInputStream(process);
        logErrorStream(process);
    }

    /**
     *
     * @param process
     * @throws IOException
     */
    public void logErrorStream(Process process) throws IOException {
        LOGGER.debug("Configuring error stream");
        streamConsole(process.getErrorStream());
    }

    /**
     *
     * @param process
     * @throws IOException
     */
    public void logInputStream(Process process) throws IOException {
        LOGGER.debug("Configuring input stream");
        streamConsole(process.getInputStream());
    }

    /**
     *
     * @param inputStream
     * @throws IOException
     */
    public void streamConsole(final InputStream inputStream) throws IOException {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader stdInput = new BufferedReader(reader);
        String stream;
        while ((stream = stdInput.readLine()) != null) {
            LOGGER.debug(stream);
            writeConsole(stream);
        }
    }

    /**
     *
     */
    public static final String DEFAULT_CONSOLE_TEXT = "";
    private StringProperty consoleText;

    /**
     *
     * @param value
     */
    public void setConsoleText(String value) {
        consoleTextProperty().setValue(value);
    }

    /**
     *
     * @param stream
     */
    public void writeConsole(String stream) {
        setConsoleText(getConsoleText().concat(stream).concat(EOL));
    }

    /**
     *
     */
    public void clearConsole() {
        setConsoleText(DEFAULT_CONSOLE_TEXT);
    }

    /**
     *
     * @return
     */
    public String getConsoleText() {
        return consoleTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty consoleTextProperty() {
        if (consoleText == null) {
            consoleText = new SimpleStringProperty(DEFAULT_CONSOLE_TEXT);
        }
        return consoleText;
    }

}
