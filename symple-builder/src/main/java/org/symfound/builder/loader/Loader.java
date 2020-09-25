/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.builder.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.log4j.Logger;
import org.symfound.comm.file.ExtensionAnalyzer;

/**
 * Calls the FXMLLoader in a new Task which returns a Parent.
 *
 * @author Javed Gangjee
 * @see Task
 */
public class Loader extends Task<Parent> {

    private static final String NAME = Loader.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public URL url;

    /**
     *
     */
    public ResourceBundle rb;

    /**
     *
     */
    public FXMLLoader fxmlLoader;

    /**
     *
     * @param url
     */
    public Loader(URL url) {
        this.url = url;
    }

    /**
     *
     * @param path
     */
    public Loader(UIPath path) {
        this.url = getClass().getResource(path.get());
    }

    /**
     *
     * @param path
     */
    public Loader(String path) {
        this.url = getClass().getResource(path);
    }

    /**
     *
     * @param url
     * @param rb
     */
    public Loader(URL url, ResourceBundle rb) {
        this.url = url;
        this.rb = rb;
    }

    /**
     *
     * @param path
     * @param rb
     */
    public Loader(String path, ResourceBundle rb) {
        ExtensionAnalyzer pathChecker = new ExtensionAnalyzer(path);
        if (!pathChecker.isScreen()) {
            path = pathChecker.addExtension(ExtensionAnalyzer.FXML_EXTENSION);
        }
        this.url = getClass().getResource(path);
        this.rb = rb;
    }

    /**
     *
     * @return loaded Parent with FXML components
     *
     */
    @Override
    public Parent call() {
        // Check to see if the loader has been called before
        if (fxmlLoader == null) {
            // URL is always required, but a ResourceBundle is optional
            fxmlLoader = (rb != null) ? new FXMLLoader(url, rb) : new FXMLLoader(url);
        }
        // Load the parent with the FXML components
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException ex) {
            LOGGER.info("FXML could not be loaded" + url.toExternalForm());
            LOGGER.fatal(null, ex);
        }
        return parent;
    }

    /**
     *
     * @return
     */
    public ResourceBundle getResource() {
        return rb;
    }

    /**
     *
     * @return
     */
    public FXMLLoader getFXMLLoader() {
        return fxmlLoader;
    }

    /**
     *
     * @param url
     */
    public void setURL(URL url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public URL getURL() {
        return url;
    }

    /**
     *
     * @param rb
     */
    public void setResource(ResourceBundle rb) {
        this.rb = rb;
    }

}
