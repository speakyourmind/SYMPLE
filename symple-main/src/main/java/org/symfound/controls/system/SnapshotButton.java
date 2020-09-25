/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.io.File;
import java.util.prefs.Preferences;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.SystemControl;
import static org.symfound.controls.system.EditButton.KEY;
import org.symfound.tools.timing.clock.Clock;
import org.symfound.tools.ui.SnapshotExporter;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class SnapshotButton extends SystemControl {

    final Node node;
    final String title;

    public static final String KEY = "Snapshot";

    public SnapshotButton(String CSSClass, Node node, String title) {
        super(CSSClass, KEY, title, "default");
        this.node = node;
        this.title = title;
    }

    @Override
    public void run() {
        saveSnapshot(node, title);
    }

    public void saveSnapshot(Node node, String title) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Export as .PNG");
        File selectedFile = fileChooser.showDialog(getPrimaryControl().getParentUI());
        Clock clock = new Clock("yyyyMMddhmmss");
        if (selectedFile != null) {
            SnapshotExporter.saveAsPng(node, selectedFile.getAbsolutePath() + "\\" + title + "_" + clock.getTimestamp() + ".png");
        } else {
            LOGGER.info("No folder selected for snapshot");
        }
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends SnapshotButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
