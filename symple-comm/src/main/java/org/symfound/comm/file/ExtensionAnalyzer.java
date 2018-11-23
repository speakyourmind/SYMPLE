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

import java.util.Arrays;
import java.util.List;

/**
 * Checks the validity of a <code>String</code> file name by comparing it to a
 * list of extensions.
 *
 * @author Javed Gangjee
 */
public class ExtensionAnalyzer {

    // Picture Extensions

    /**
     *
     */
    public static final String BMP_EXTENSION = ".bmp";

    /**
     *
     */
    public static final String PNG_EXTENSION = ".png";

    /**
     *
     */
    public static final String JPG_EXTENSION = ".jpg";

    /**
     *
     */
    public static final String JPEG_EXTENSION = ".jpeg";

    // Text Extensions

    /**
     *
     */
    public static final String TXT_EXTENSION = ".txt";

    /**
     *
     */
    public static final String ASC_EXTENSION = ".asc";

    // Video Extensions

    /**
     *
     */
    public static final String MP4_EXTENSION = ".mp4";

    /**
     *
     */
    public static final String FLV_EXTENSION = ".flv";

    // Screen Extensions

    /**
     *
     */
    public static final String FXML_EXTENSION = ".fxml";

    /**
     *
     */
    public String filename;

    /**
     * Requires the name of the file to be analyzed.
     *
     * @param filename file to be analyzed
     */
    public ExtensionAnalyzer(String filename) {
        this.filename = filename;
    }

    /**
     * Checks to see if the file is any of the given extensions in the provided
     * list.
     *
     * @param extensions extensions to check for
     * @return is one of the extensions if true, false otherwise
     */
    public Boolean isExtension(List<String> extensions) {
        // Check to see if there is a period at the start of 
        // each element in the list.
        checkPeriod(extensions);
        Boolean isExtension = false;
        // Go through the list
        for (String extension : extensions) {
            // If the file name contains the extension...
            if (filename.toLowerCase().contains(extension)) {
                // set the result to true. Stays false otherwise.
                isExtension = true;
            }
        }
        return isExtension;
    }

    /**
     * Checks the file name to see if it ends in the specified extension.
     *
     * @param extension extension to check for
     * @return ends with the extension if true, false otherwise
     */
    public Boolean isExtension(String extension) {
        return filename.endsWith(extension);
    }

    /**
     * Adds the specified extension to the end of the file name.
     *
     * @param extension extension to be added to file name
     * @return file name with extension
     */
    public String addExtension(String extension) {
        return filename.concat(extension);
    }

    /**
     *
     * @param extension
     * @return
     */
    public String getWithoutExtension(String extension) {
        String path = filename;
        replaceLast(path, extension, "");
        return path;

    }

    private String replaceLast(String string, String fromValue, String toValue) {
        int lastIndex = string.lastIndexOf(fromValue);
        if (lastIndex < 0) {
            return string;
        }
        String tail = string.substring(lastIndex).replaceFirst(fromValue, toValue);
        return string.substring(0, lastIndex) + tail;
    }

    /**
     * Checks whether the list of extensions starts with a period. If not it
     * adds it.
     *
     * @param extensions list to check
     */
    private void checkPeriod(List<String> extensions) {
        for (int i = 0; i < extensions.size(); i++) {
            // If the extension does not start with a period
            if (!extensions.get(i).startsWith(".")) {
                // add in the period
                extensions.set(i, "." + extensions.get(i));
            }
        }
    }

    /**
     * Check the file extension to see if it is a picture.
     *
     * <b>NOTE:</b> Supports JPG, JPEG, PNG and BMP files.
     *
     * @return true if picture, false otherwise
     */
    public Boolean isPictureFile() {
        String filenameLower = filename.toLowerCase();
        return filenameLower.endsWith(JPG_EXTENSION)
                || filenameLower.endsWith(JPEG_EXTENSION)
                || filenameLower.endsWith(BMP_EXTENSION)
                || filenameLower.endsWith(PNG_EXTENSION);
    }

    /**
     * Checks the file extension to see if it is a text file.
     *
     * <b>NOTE:</b> Supports TXT and ASC files.
     *
     * @return true if text file, false otherwise
     */
    public Boolean isTextFile() {
        List<String> extensions = Arrays.asList(TXT_EXTENSION, ASC_EXTENSION);
        return isExtension(extensions);
    }

    /**
     *
     * @return
     */
    public Boolean isVideoFile() {
        List<String> extensions = Arrays.asList(MP4_EXTENSION, FLV_EXTENSION);
        return isExtension(extensions);
    }

    /**
     * Checks if the file path is an FXML.
     *
     * <b>NOTE:</b> Supports FXML files.
     *
     * @return file is a ui if true, false otherwise.
     */
    public Boolean isScreen() {
        return isExtension(FXML_EXTENSION);
    }

}
