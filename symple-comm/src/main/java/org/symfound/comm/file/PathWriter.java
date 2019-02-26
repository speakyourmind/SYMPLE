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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class PathWriter {

    private static final String NAME = PathWriter.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public File file;

    /**
     *
     */
    public String path;

    /**
     *
     * @param path
     */
    public PathWriter(String path) {
        this.path = path;
        file = new File(path);
    }

    /**
     *
     * @param file
     */
    public PathWriter(File file) {
        this.file = file;
    }

    /**
     * Creates a file if the provided file does not exist
     *
     * @throws IOException
     */
    public void create() throws IOException {
        if (!file.isDirectory()) {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
    }

    /**
     * Write the text from the specified textArea into file.
     *
     * @param text
     * @param append
     * @param create
     * @throws IOException
     */
    public void writeToFile(String text, Boolean append, Boolean create)
            throws IOException {
        if (text != null) {
            if (create) {
                // Create the file if it does not exist
                create();
            }
        //    if (text.length() >= 0) {
                // Initialize FileWriter and get the file
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), append);
                // Write text to file
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    //bw.newLine();
                    bw.append(text);
                }
          //  }
        }
    }

    /**
     *
     */
    public void deleteFile() {
        if (file.delete()) {
            LOGGER.info(file.getName() + " deleted.");
        } else {
            LOGGER.fatal("Delete operation failed.");
        }
    }

    /**
     *
     * @param newName
     */
    public void renameFile(String newName) {
        File newNameFile = new File(newName);
        if (!newNameFile.exists()) {
            file.renameTo(newNameFile);
        }

    }

}
