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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Javed Gangjee
 */
public class PortWriter implements Runnable {

    private static final String NAME = PortWriter.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private ServerSocket serverSocket;
    private final int port;
    private final JSONObject json;

    /**
     *
     * @param port
     * @param json
     */
    public PortWriter(int port, JSONObject json) {
        this.port = port;
        this.json = json;
    }

    /**
     *
     * @param jsonObject
     * @throws IOException
     */
    public void write(JSONObject jsonObject) throws IOException {

        LOGGER.info("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
        LOGGER.info("Waiting for clients...");
        Socket client = serverSocket.accept();
        LOGGER.info("The following client has connected:".concat(client.getInetAddress().getCanonicalHostName()));

        OutputStream outputStream = client.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.flush();
        }

        serverSocket.close();

    }

    @Override
    public void run() {
        try {
            write(json);
        } catch (IOException ex) {
            LOGGER.fatal(null, ex);
        }
    }

}
