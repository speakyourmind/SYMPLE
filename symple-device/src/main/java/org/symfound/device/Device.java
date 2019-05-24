
/*
 * Copyright (C) 2015
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
package org.symfound.device;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.symfound.builder.user.User;
import org.symfound.comm.port.PortWriter;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Processability;
import org.symfound.device.processing.Processor;
import org.symfound.device.processing.WriteMethod;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public class Device<T extends Hardware> implements Runnable {

    private static final String NAME = Device.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private final T hardware;
    private final User user;

    private final ExecutorService executor = new ThreadPoolExecutor(24, 64, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    /**
     *
     * @param hardware
     * @param user
     */
    public Device(T hardware, User user) {
        
        this.hardware = hardware;
        this.user = user;
    }

    @Override
    public void run() {
        getHardware().processedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && getHardware().isEnabled()) {
                final JSONObject jsonObject = getHardware().getDataPackage();
                Processability processability = getHardware().getProcessability();
                switch (processability.getWriteMethod()) {
                    case WriteMethod.PORT:
                        final Integer port = processability.getWritePort();
                        PortWriter portWriter = new PortWriter(port, jsonObject);
                        portWriter.run();
                        break;
                    default:
                        getProcessor().setDataPackage(jsonObject);
                        break;
                }
                executor.submit(getProcessor());
            }
        });
        if (getHardware().connect()) {
            getHardware().launch();
        } else {
            LOGGER.warn("Please connect the device");
        }
    }

    /**
     *
     * @return
     */
    public T getHardware() {
        return hardware;
    }

    private Processor processor;

    /**
     *
     * @return
     */
    public Processor getProcessor() {
        if (processor == null) {
            processor = new Processor(user, hardware);
        }
        return processor;
    }

}
