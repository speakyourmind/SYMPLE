package org.symfound.builder.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.loader.RuntimeExecutor;
import org.symfound.builder.session.Session;
import org.symfound.comm.port.JSONReader;
import org.symfound.comm.web.Downloader;

/**
 *
 * @author Javed Gangjee
 */
public final class VersionManager {

    private static final String NAME = VersionManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public Downloader msiDownloader;
    private final Session session;
    private String saveDir;
    private Thread thread;

    /**
     *
     * @param session
     */
    public VersionManager(Session session) {
        this.session = session;
        final String versionInfoURL = session.getVersionInfoURL();
        try {
            getJSONReader().loadPackage(versionInfoURL);
            LOGGER.info("Reading version info from URL " + versionInfoURL);
        } catch (InterruptedException | MalformedURLException | ExecutionException ex) {
            LOGGER.fatal("Unable to load version info from " + versionInfoURL, ex);
        }

        try {
            String fileURL = getDownloadURL();
            msiDownloader = new Downloader(fileURL);
            LOGGER.info("A new version of SYMPLE is available from " + fileURL);
        } catch (Exception ex) {
            LOGGER.fatal("Unable to download installer from " + getDownloadURL(), ex);
        }
    }


    private Boolean compareToRemote() {
        Boolean isSame = false;
        if (getRemoteVersion() != null) {
            isSame = !session.getVersion().equalsIgnoreCase(getRemoteVersion());
        }

        return isSame;
    }

    /**
     *
     * @return
     */
    public String getRemoteVersion() {
        return (String) getJSONReader().getObject("version");
    }

    /**
     *
     * @return
     */
    public String getDownloadURL() {
        return (String) getJSONReader().getObject("downloadURL");
    }

    /**
     *
     * @return
     */
    public String getSeverity() {
        return (String) getJSONReader().getObject("severity");
    }

    private JSONReader jsonReader;

    /**
     *
     * @return
     */
    public JSONReader getJSONReader() {
        if (jsonReader == null) {
            jsonReader = new JSONReader();
        }
        return jsonReader;
    }

    private BooleanProperty needsUpdate;

    /**
     *
     * @param value
     */
    public void setNeedsUpdate(Boolean value) {
        needsUpdateProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean needsUpdate() {
        setNeedsUpdate(compareToRemote());
        return needsUpdateProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty needsUpdateProperty() {
        if (needsUpdate == null) {
            Boolean initValue = compareToRemote();
            needsUpdate = new SimpleBooleanProperty(initValue);
        }
        return needsUpdate;
    }

    /**
     *
     */
    public void update() {
        thread = new Thread(msiDownloader);
        thread.start();
        LOGGER.info("Downloading new version of SYMPLE.");
        msiDownloader.getTracker().completeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                LOGGER.info("New install file has been downloaded and is ready to install from "+msiDownloader.saveDirectory);
                String command = "msiexec /i \"" + msiDownloader.saveDirectory.replace("\\", "\\\\") + "\\\\" + msiDownloader.fileName + "\"";
                LOGGER.info("Executing command -" + command);
                RuntimeExecutor runtimeExecutor = new RuntimeExecutor();
                runtimeExecutor.execute(command);
                session.shutdown(Boolean.TRUE);
            }
        });

    }

}
