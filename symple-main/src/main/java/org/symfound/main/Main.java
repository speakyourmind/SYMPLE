
 
package org.symfound.main;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.symfound.builder.session.SessionLauncher;
import org.symfound.builder.updater.VersionManager;
import org.symfound.builder.user.User;

/**
 *
 * @author Javed Gangjee
 */
public class Main extends Application {

    private static final String NAME = Main.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private static VersionManager versionManager;

    /**
     *
     */
    public static final String VERSION = "v2.3.5";

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Logging started");
        getSessionLauncher().launch();
        LOGGER.debug("Launching session");
        LOGGER.info("Current version: " + getSession().getVersion()
                + "; Remote version: " + getVersionManager().getRemoteVersion());
        LOGGER.info("Does the program need an update? " + getVersionManager().needsUpdate());
        readyProperty().bindBidirectional(getSession().builtProperty());
    }

    /**
     *
     * @return
     */
    public static FullSession getSession() {
        return getSessionLauncher().getSession();
    }

    private static SessionLauncher<FullSession> sessionLauncher;

    /**
     *
     * @return
     */
    public static SessionLauncher<FullSession> getSessionLauncher() {
        if (sessionLauncher == null) {
            LOGGER.debug("Initializing session");
            User user = new User();
            FullSession fullSession = new FullSession(user);
            sessionLauncher = new SessionLauncher<>(fullSession);
        }
        return sessionLauncher;
    }

    /**
     *
     * @return
     */
    public static VersionManager getVersionManager() {
        if (versionManager == null) {
            versionManager = new VersionManager(getSession());
        }
        return versionManager;
    }

    private static BooleanProperty ready;

    /**
     *
     * @return
     */
    public static BooleanProperty readyProperty() {
        if (ready == null) {
            ready = new SimpleBooleanProperty(false);
        }
        return ready;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
