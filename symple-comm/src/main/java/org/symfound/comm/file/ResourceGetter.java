/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.file;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Javed Gangjee
 */
public class ResourceGetter {

    /**
     *
     * @param path
     * @param name
     * @return
     * @throws MalformedURLException
     */
    public static ResourceBundle getExternalBundle(String path, String name)
            throws MalformedURLException {
        File file = new File(path);
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle rb = ResourceBundle.getBundle(name,
                Locale.getDefault(), loader);
        return rb;
    }

}
