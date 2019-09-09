package org.symfound.controls.system;

import java.util.List;
import javafx.scene.control.Tab;

/**
 *
 * @author Javed Gangjee
 */
public class SettingsTab extends Tab {

    /**
     *
     * @param title
     * @param rows
     */
    public SettingsTab(String title, List<SettingsRow> rows) {
        super(title);
        build(rows);
    }

    private void build(List<SettingsRow> rows) {
        final int numOfSettings = rows.size();
        SettingsGrid settingsGrid = new SettingsGrid(numOfSettings);
        for (int i = 0; i < numOfSettings; i++) {
            settingsGrid.grid.add(rows.get(i), 0, i);
        }
        setContent(settingsGrid);
    }
}
