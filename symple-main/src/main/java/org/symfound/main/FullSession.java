/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

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
package org.symfound.main;

import com.sun.javafx.stage.StageHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.loader.UIPath;
import org.symfound.builder.session.Session;
import org.symfound.builder.user.User;
import org.symfound.comm.file.PathWriter;
import org.symfound.controls.system.SettingsExporter;
import org.symfound.controls.system.SettingsImporter;
import org.symfound.device.emulation.input.InputListener;
import org.symfound.main.builder.App;
import org.symfound.main.builder.StackedUI;
import org.symfound.main.manager.DeviceManager;
import org.symfound.main.manager.TTSManager;
import static org.symfound.text.TextOperator.EOL;
import org.symfound.tools.timing.clock.Clock;

/**
 *
 * @author Javed Gangjee
 */
public class FullSession extends Session {

    private static final String NAME = FullSession.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    // Path Constants

    /**
     *
     */
    public static final String ROOT_PACKAGE = "org.symfound.";

    /**
     *
     */
    public static final String ROOT_FOLDER = "/org/symfound/";

    /**
     *
     */
    public static final String SCREENS_FOLDER = "/fxml/";

    /**
     *
     */
    public static final String FONTS_FOLDER = "fonts/";

    /**
     *
     */
    public static final String STYLES_FOLDER = "styles/";

    /**
     *
     */
    public static final String IMAGES_FOLDER = "images/";

    // Screen List
    /**
     *
     */
    public static String PRELOADER = SCREENS_FOLDER + "preloader/preloader";

    // TO DO: Dynamically build as in apps using priority property
    /**
     *
     */
    public static String DIAGNOSTIC_APP = SCREENS_FOLDER + "main/diagnostic_main";

    /**
     *
     */
    public static String HOME = SCREENS_FOLDER + "main/home";

    /**
     *
     */
    public static String MAIN_SETTINGS = SCREENS_FOLDER + "main/main_settings";

    /**
     *
     */
    public static String APP_SETTINGS = SCREENS_FOLDER + "main/app_settings";

    /**
     *
     */
    public static String SWITCH_CALIBRATION = SCREENS_FOLDER + "device/switch_calibration";

    /**
     *
     */
    public static FontLoader localFonts;

    /**
     *
     * @param user
     */
    public FullSession(User user) {
        super(user);
    }

    /**
     *
     * @param builder
     */
    @Override
    public void build(Builder builder) {
        String masterFile = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Master.xml";
        File file = new File(masterFile);
        if (file.exists()) {
            SettingsImporter settingsImporter = new SettingsImporter(masterFile);
            final Thread thread = new Thread(settingsImporter);
            thread.start();
            try {
                thread.join();
                LOGGER.info("Settings import from " + masterFile + " complete");
                
            } catch (InterruptedException ex) {
                LOGGER.warn(ex);
            }
        } else {
            LOGGER.warn("Master file does not exist in " + masterFile + " Proceeding with default settings");
        }

        //   System.getProperties().list(System.out);
        getBuilder().start(getBuildTimeout());

        localFonts = loadFonts();

        //TODO: Move to thread
        //  Thread deviceThread = new Thread(getDeviceManager());
        //deviceThread.start();
        getDeviceManager().run();
        //Thread thread = new Thread(getTTSManager());
        getTTSManager().run();
        //thread.start();

        buildSystemUIs();

        buildApps();

        try {
            getBuilder().stop();
        } catch (InterruptedException ex) {
            LOGGER.fatal("Unable to stop builder", ex);
        }

        builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                LOGGER.info("Session is ready to start");
                LOGGER.info("Launching current device...");
                getDeviceManager().launch(getUser().getDeviceName());
                getBuilder().setProgress(1.0); // TO DO: Combine built and progress
                LOGGER.info("Launching session");
                //   log(": Starting SYMPLE");
                if (getUser().getProfile().isFirstUse()) {
                    //      uiMain.getStack().load(WIZARD_DEVICE);
                } else {
                    getMainUI().getStack().load(HOME);
                    getMainUI().open();
                }
                LOGGER.info("Total Start Time = ".concat(getBuilder().getTimeElapsed().toString()));
                getBuilder().end();
                setPlaying(true);

                LOGGER.info(StageHelper.getStages().size() + " stages opened");

            }
        });
    }

    private static TTSManager ttsManager;

    /**
     *
     * @return
     */
    public TTSManager getTTSManager() {
        if (ttsManager == null) {
            LOGGER.info("Initializing TTS Manager...");
            ttsManager = new TTSManager(getUser());
        }

        return ttsManager;
    }

    private static DeviceManager deviceManager;

    /**
     *
     * @return
     */
    public DeviceManager getDeviceManager() {
        if (deviceManager == null) {
            LOGGER.info("Initializing Device Manager...");
            deviceManager = new DeviceManager(getUser());
        }
        return deviceManager;
    }

    private FontLoader loadFonts() {
        ResourceLister availableFonts = new ResourceLister("fonts");
        List<String> fontNames = availableFonts.getFileNames();
        FontLoader fontLoader = new FontLoader(FONTS_FOLDER);
        fontLoader.load(fontNames);
        return fontLoader;
    }

    public Map<String, App> appMap;

    /**
     *
     * @param name
     * @return
     */
    public App getApp(String name) {
        return appMap.get(name);
    }

    private void buildApps() {
        HashMap<String, App> map = new HashMap<>();
        for (String appName : getUser().getNavigation().getAppBuildOrder()) {
            App app = new App(user, appName);
            app.build(getBuilder());
            app.builtProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    LOGGER.info("App " + appName + " is built with " + app.buildList.size() + " screens");
                    checkCompletion();
                }
            });
            map.put(appName, app);
            appMap = Collections.unmodifiableMap(map);
        }
    }

    private void buildSystemUIs() {
        addWizard();
        addMainScreens();
        LOGGER.info("Building Main List");
        getMainUI().build(getBuilder());
        getMainUI().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                LOGGER.info("Main session UI is checking for completion");
                checkCompletion();
            }
        });

    }

    /**
     *
     */
    @Override
    public void checkCompletion() {
        getBuilder().incrementProgress();
        Boolean mainReady = getMainUI().isBuilt();
        Boolean appsReady = Boolean.TRUE;
        for (String appName : getUser().getNavigation().getAppBuildOrder()) {
            appsReady = appsReady && getApp(appName).isBuilt();
            if (!getApp(appName).isBuilt()) {
                LOGGER.info("Build for App " + appName + " is pending");
            }
        }

        if (appsReady) {
            LOGGER.info("All apps have been built");
        }
        Boolean ready = mainReady && appsReady
                && !isBuilt();
        setBuilt(ready);

    }

    /**
     *
     * @param message
     */
    @Deprecated
    public static void log(String message) {
        Clock clock = new Clock("yyyy-MM-dd h:mm:ss a");
        String homeFolder = Main.getSession().getUser().getContent().getHomeFolder();
        PathWriter txtFileWriter = new PathWriter(homeFolder
                + "/Documents/SYMPLE/UsageLog.txt");
        try {
            txtFileWriter.writeToFile(clock.getTimestamp() + "" + message
                    + EOL, Boolean.TRUE, Boolean.TRUE);
        } catch (IOException ex) {
            LOGGER.fatal(null, ex);
        }
    }

    @Override
    public void shutdown(Boolean backupSettings) {
        LOGGER.info("Shutting down SYMPLE");

        LOGGER.info("Closing hardware");
        Main.getSession().getDeviceManager().getCurrent().getHardware().close();
        LOGGER.info("Stopping Input Listener");
        InputListener.stop();

        if (backupSettings) {
            String backupFolder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Backup";
            PathWriter backupPathWriter = new PathWriter(backupFolder);
            backupPathWriter.file.mkdirs();
            SettingsExporter backupSettingsExporter = new SettingsExporter(backupFolder);
            LOGGER.info("Backing up settings");
            Thread backupThread = new Thread(backupSettingsExporter);
            try {
                backupThread.start();
                backupThread.join();
            } catch (InterruptedException ex) {
                LOGGER.warn("Unable to backup settings file to " + backupFolder, ex);
            }
        } else {
            LOGGER.info("Settings have not been backed up");
        }

        String folder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings";
        PathWriter savePathWriter = new PathWriter(folder);
        savePathWriter.file.mkdirs();
        SettingsExporter settingsExporter = new SettingsExporter(folder, "/Master.xml");

        LOGGER.info("Backing up master settings");
        Thread thread = new Thread(settingsExporter);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException ex) {
            LOGGER.warn(ex);
        }

        Platform.exit();
        System.exit(0);

    }

    /**
     *
     */
    public void addMainScreens() {
        ResourceLister resourceList = new ResourceLister("fxml/main");
        List<String> mainList = resourceList.getFileNames();
        List<UIPath> mainPaths = new ArrayList<>();
        for (String main : mainList) {
            if (main.endsWith(".fxml")) {
                String fxmlName = main.replaceAll(".fxml", "");
                UIPath uiPath = new UIPath(SCREENS_FOLDER + "main/" + fxmlName);
                mainPaths.add(uiPath);
            }
        }
        Comparator<UIPath> comparator = (UIPath t, UIPath t1)
                -> t.getPriority() - t1.getPriority();
        Collections.sort(mainPaths, comparator);
        getBuildList().addAll(mainPaths);
    }

    /**
     *
     */
    public void addWizard() {
        List<UIPath> wizardList = Arrays.asList();
        if (getUser().getProfile().isFirstUse()) {
            String message = "Adding wizard screens";
            LOGGER.info(message);
            getBuildList().addAll(wizardList);
        }
    }

    private static List<UIPath> buildList;

    /**
     *
     * @return
     */
    public static List<UIPath> getBuildList() {
        if (buildList == null) {
            buildList = new ArrayList<>();
        }
        return buildList;
    }

    private static StackedUI uiMain;

    /**
     *
     * @return
     */
    public static StackedUI getMainUI() {
        if (uiMain == null) {
            uiMain = new StackedUI(getBuildList());
        }
        return uiMain;
    }

}
