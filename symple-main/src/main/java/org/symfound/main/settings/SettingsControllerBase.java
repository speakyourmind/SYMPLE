/*
 * Copyright (C) 2014 SpeakYourMind Foundation
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
package org.symfound.main.settings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.symfound.app.CommonController;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;

/**
 *
 * @author Javed Gangjee
 */
public abstract class SettingsControllerBase extends CommonController implements Initializable, SetResetable {

    /**
     *
     * @throws IOException
     */
    public void updateSettings() throws IOException {
        setSettings();
        closeSettings();
    }

    /**
     *
     */
    public void closeSettings() {
        if (getMainUI().isShowing()) {
            getMainUI().getStack().load(FullSession.MAIN_SETTINGS);
            getMainUI().setCurrentProperties();
        }
/*        App app = getSession().getApp(DesktopController.KEY);
        if (app != null) {
            StandaloneUI get = app.getUIs().get(0);
            if (get.isShowing()) {
                get.setCurrentProperties();
            }
        }*/
    }

    /**
     *
     */
    public void cancelSettings() {
        resetSettings();
        closeSettings();
    }

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

}
