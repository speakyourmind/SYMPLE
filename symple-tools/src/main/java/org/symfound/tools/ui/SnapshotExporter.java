/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.tools.ui;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class SnapshotExporter {

    private static final String NAME = SnapshotExporter.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);
    
    public static void saveAsPng(Node node, String file) {
        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(file));
        } catch (IOException ex) {
            LOGGER.warn(ex);
        }
    }
}
