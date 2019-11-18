package org.symfound.controls.system;

import java.util.List;
import javafx.scene.control.Tab;
import static org.symfound.controls.ScreenControl.CSS_PATH;

/**
 *
 * @author Javed Gangjee
 */
public class SettingsTab extends Tab {

    private final String title;

    /**
     *
     * @param title
     * @param rows
     */
    public SettingsTab(String title, List<SettingsRow> rows) {
        super("");
        this.title = title;
        build(rows);
    }

    private void build(List<SettingsRow> rows) {
        setGraphic(new TabTitle(title));

        getStyleClass().add("transparent");
        final int numOfSettings = rows.size();
        SettingsGrid settingsGrid = new SettingsGrid(numOfSettings);
        for (int i = 0; i < numOfSettings; i++) {
            settingsGrid.grid.add(rows.get(i), 0, i);
        }
        setContent(settingsGrid);
    }
}
