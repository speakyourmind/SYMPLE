package org.symfound.controls.system;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.comm.web.ExternalURLLauncher;
import org.symfound.controls.ConfirmableControl;
import org.symfound.controls.user.AnimatedButton;

/**
 *
 * @author Javed Gangjee
 */
public final class WebLaunchButton extends ConfirmableControl {

    /**
     *
     */
    public static final String WEB_PAGE = "http://speakyourmindfoundation.org/donate.html#donateForm";

    /**
     *
     */
    public WebLaunchButton() {
        super("settings-button");
        initTitleText = "Leaving SYMPLE";
        initCaptionText = "You are about to leave the program. Are you sure you want to continue?";
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
    }

    // TODO: Create new WebPageLauncher class. 
    // TODO: Show in a pane in the Launch screen itself
    @Override
    public void run() {
        ExternalURLLauncher externalURLLauncher = new ExternalURLLauncher();
        externalURLLauncher.launchWebPage(getURL());
    }

    private StringProperty url;

    /**
     *
     * @param value
     */
    public void setURL(String value) {
        urlProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getURL() {
        return urlProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty urlProperty() {
        if (url == null) {
            url = new SimpleStringProperty(WEB_PAGE);
        }
        return url;
    }
}
