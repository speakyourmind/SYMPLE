package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.scene.control.ProgressBar;
import static javafx.scene.layout.AnchorPane.setBottomAnchor;
import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setRightAnchor;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.tools.animation.NodeAnimator;

/**
 *
 * @author Javed Gangjee
 */
public class VolumeBar extends VolumeControl {

    public static final String KEY ="Volume Progress";
    /**
     *
     */
    public NodeAnimator volumeAnimator;

    /**
     *
     */
    public ProgressBar volumeControl;

    /**
     *
     */
    public VolumeBar() {
        super("", KEY, "", "default");
        initialize();
    }

    /**
     *
     * @param volume
     */
    public VolumeBar(Double volume) {
        super("", "", "", "");
        this.initVolume = volume;
        initialize();
    }

    /**
     *
     */
    public final void initialize() {
        volumeControl = new ProgressBar(0.0);
        volumeControl.progressProperty();
        volumeControl.progressProperty().bindBidirectional(volumeProperty());
        volumeControl.getStylesheets().add(CSS_PATH);
        volumeControl.setPrefHeight(20.0);
        setTopAnchor(volumeControl, 0.0);
        setLeftAnchor(volumeControl, 0.0);
        setRightAnchor(volumeControl, 0.0);
        setBottomAnchor(volumeControl, 0.0);
        getChildren().add(volumeControl);
        volumeControl.toBack();
        volumeAnimator = new NodeAnimator(volumeControl);
    }

    @Override
    public void run() {

    }


    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends VolumeBar> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
