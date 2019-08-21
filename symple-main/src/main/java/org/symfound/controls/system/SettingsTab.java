package org.symfound.controls.system;

import java.util.List;
import javafx.scene.control.Tab;
import org.symfound.controls.system.SettingsGrid;
import org.symfound.controls.system.SettingsRow;

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
       
    }
}
