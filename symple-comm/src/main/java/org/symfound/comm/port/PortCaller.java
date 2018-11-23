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
import java.net.Socket;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A Simple Socket client that connects to our socket server
 *
 * @author Javed Gangjee
 *
 */
public class PortCaller implements Callable<JSONObject> {

    private static final String NAME = PortCaller.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String EOL = System.getProperty("line.separator");

    private final String hostname;
    private final int port;
    private Socket socketClient;

    /**
     *
     * @param hostname
     * @param port
     */
    public PortCaller(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public JSONObject call() throws Exception {
        LOGGER.info("Attempting to connect to " + hostname + ":" + port);
        socketClient = new Socket(hostname, port);
        LOGGER.info("Connection Established");
        InputStream inputStream = socketClient.getInputStream();
        //if successful, read response from server
        JSONObject JSONObject = read(inputStream);
        socketClient.close();

        return JSONObject;
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public JSONObject read(InputStream inputStream) throws IOException, ParseException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String data = "";
        String userInput;
        while ((userInput = bufferedReader.readLine()) != null) {
            data += userInput + EOL;
        }
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(data);
        return (JSONObject) obj;
    }

}
