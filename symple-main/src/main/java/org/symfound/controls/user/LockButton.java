package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;

/**
 * This button disables the children of the pane, so they are no longer
 * selectable.
 * <p>
 * By default the parent of the button is assumed to be the one that is to be
 * disabled. However, the user can configure the <code>paneProperty</code> to
 * lookup a pane of that name within the button's scene.
 *
 * @author Javed Gangjee
 */
public final class LockButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = LockButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Lock";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Lock";

    /**
     *
     */
    public LockButton() {
        super("lock-button", KEY, DEFAULT_TITLE,"default");

        // Add a listener when the paused property is changed.
        pausedProperty().addListener((observable, oldValue, newValue) -> {
            // By default the button chooses the Parent pane.
            if (getSession().isBuilt()) {
                Pane root;
                if (!getPane().isEmpty()) {
                    // Or it can lookup another pane in the scene.
                    root = (Pane) getScene().lookup("#" + getPane());
                } else {
                    root = (Pane) getParent();
                }

                root.getChildren().stream().forEach((node) -> {
                    node.setDisable(newValue);
                });
                // Ensure that this button is still selectable.
                setDisable(false);
            }

        });

    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Locking screen at pane " + getPane());
        pause();
    }

    private BooleanProperty paused;

    /**
     * Toggles the paused boolean property.
     */
    public void pause() {
        pausedProperty().setValue(!isPaused());
    }

    /**
     * Finds out whether the pause button is active.
     *
     * @return paused if true, false otherwise
     */
    public Boolean isPaused() {
        return pausedProperty().getValue();
    }

    /**
     * Represents the pause function of the button.
     *
     * @return paused
     */
    public BooleanProperty pausedProperty() {
        if (paused == null) {
            paused = new SimpleBooleanProperty(false);
        }
        return paused;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
