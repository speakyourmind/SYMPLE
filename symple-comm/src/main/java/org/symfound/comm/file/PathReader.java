/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

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
package org.symfound.comm.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.comm.web.Downloader;

/**
 *
 * @author Javed Gangjee
 */
public class PathReader {

    private static final String NAME = PathReader.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    /**
     *
     */
    public StringProperty path;

    /**
     *
     */
    public String initPath;

    /**
     *
     */
    public ObjectProperty<File> file;

    /**
     *
     */
    public File initFile;

    /**
     *
     * @param path
     */
    public PathReader(String path) {
        initPath = path;
        initFile = new File(initPath);
    }

    /**
     *
     * @param file
     */
    public PathReader(File file) {
        initFile = file;
    }

    /**
     *
     * @param pathRead
     */
    public void setPath(String pathRead) {
        pathProperty().setValue(pathRead);
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return pathProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty pathProperty() {
        if (path == null) {
            path = new SimpleStringProperty(initPath);
        }
        return path;
    }

    /**
     *
     * @param value
     */
    public void setFile(File value) {
        fileProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public File getFile() {
        return fileProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<File> fileProperty() {
        if (file == null) {
            file = new SimpleObjectProperty<>(initFile);
        }
        return file;
    }

    /**
     *
     * @return @throws FileNotFoundException
     */
    public String getFileText()
            throws FileNotFoundException, IOException {
        String fileText = "";

        // Initialize FileReader with file
        FileReader fr = new FileReader(getFile().getAbsoluteFile());
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            //While the line being read is not null
            while ((line = br.readLine()) != null) {
                //Load each line to the textArea
                fileText += line;
            }

        }

        return fileText;
    }

    /**
     *
     * @param folder
     * @return
     */
    public List<String> getSubFolderNames(File folder) {
        List<String> folders = new ArrayList<>();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    folders.add(file.getName());
                }
            }
        }
        return folders;
    }

    /**
     *
     * @return @throws URISyntaxException
     */
    public List<String> getResourceFileNames() throws URISyntaxException {
        List<String> fileList = new ArrayList<>();
        File folder = getResourceFile(getPath());
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File subFile : files) {
                fileList.add(subFile.getName());
            }
        }
        return fileList;
    }

    /**
     *
     * @param path
     * @return
     */
    public File getResourceFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        File folder = new File(classLoader.getResource(path).getFile());
        return folder;
    }

    /**
     *
     * @return
     */
    public List<String> getFolderFileNames() {
        List<String> fileList = new ArrayList<>();
        File folder = new File(getPath());
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File fileToList : files) {
                fileList.add(fileToList.getName());
            }
        } else {

        }
        return fileList;
    }

    /**
     *
     * @return
     */
    public List<String> getFolderFilePaths() {
        List<String> fileList = new ArrayList<>();
        File folder = new File(getPath());
        addFolderFilesToList(folder, fileList);
        return fileList;
    }

    /**
     *
     * @param folder
     * @param fileList
     */
    public static void addFolderFilesToList(File folder, List<String> fileList) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    addFolderFilesToList(file, fileList);
                } else {
                    fileList.add(file.getAbsolutePath());
                }
            }

        } else {
            LOGGER.warn("Folder is not a directory:" + folder);
        }
    }
}
