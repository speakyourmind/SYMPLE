/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.builder.Builder;
import org.symfound.builder.component.StandalonePane;
import org.symfound.builder.loader.UIPath;
import org.symfound.controls.RunnableControl;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public class StandalonePopupButton extends RunnableControl {

    private static final StandalonePane standalone = new StandalonePane();

    /**
     *
     * @param fxmlLocation
     */
    public StandalonePopupButton(String fxmlLocation) {
        super();
        Builder builder = new Builder();
        UIPath file = new UIPath(fxmlLocation);
        standalone.build(file, builder);
    }

    @Override
    public void run() {
        UI.openAsPopup(standalone);
    }

}
