/*
 * Copyright (C) 2015 SpeakYourMind Foundation
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
package org.symfound.comm.port;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Javed Gangjee
 */
public class URLCaller implements Callable<Object> {

    private static final String NAME = URLCaller.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String EOL = System.getProperty("line.separator");
    private final URL url;

    /**
     *
     * @param url
     */
    public URLCaller(URL url) {
        this.url = url;
    }

    @Override
    public Object call() throws IOException, ParseException {
        //if successful, read response from server
        JSONParser parser = new JSONParser();
        Object object = parser.parse(read(url.openStream()));
        return object;
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public String read(InputStream inputStream) throws IOException, ParseException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String data = "";
        String userInput;
        while ((userInput = bufferedReader.readLine()) != null) {
            data += userInput + EOL;
        }
        return data;
    }

}
