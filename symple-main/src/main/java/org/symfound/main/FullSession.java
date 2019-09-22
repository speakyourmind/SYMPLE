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

import org.symfound.text.font.FontLoader;
import com.sun.javafx.stage.StageHelper;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.loader.UIPath;
import org.symfound.builder.session.Session;
import org.symfound.builder.user.User;
import org.symfound.comm.file.PathWriter;
import org.symfound.builder.settings.PreferencesExporter;
import org.symfound.builder.settings.PreferencesImporter;
import org.symfound.comm.web.Downloader;
import org.symfound.device.emulation.input.InputListener;

import org.symfound.main.builder.StackedUI;
import org.symfound.controls.device.DeviceManager;
import org.symfound.controls.user.voice.TTSManager;

import static org.symfound.text.TextOperator.EOL;
import org.symfound.tools.timing.LoopedEvent;
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
        initialize();
    }

    private void initialize() {
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
            PreferencesImporter settingsImporter = new PreferencesImporter(masterFile);
            final Thread thread = new Thread(settingsImporter);
            thread.start();
            try {
                thread.join();
                LOGGER.info("Settings import from " + masterFile + " complete");
            } catch (InterruptedException ex) {
                LOGGER.warn(ex);
            }
        } else {
            LOGGER.warn("Master file does not exist in " + masterFile
                    + ". Proceeding with default settings.");
            /* if (getUser().getProfile().isFirstUse()) {
            String defaultFile = "/profiles/default.xml";
            PreferencesImporter settingsImporter = new PreferencesImporter(defaultFile);
            settingsImporter.run();
            LOGGER.info("Settings import from " + defaultFile + " complete");
            
            getUser().getProfile().setFirstUse(Boolean.FALSE);
            }*/
        }

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

        //   buildApps();
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
                //  if (getUser().getProfile().isFirstUse()) {

                // getUser().getStatistics().setFirstUsed(System.currentTimeMillis());
                //getUser().getProfile().setFirstUse(Boolean.FALSE);
                //      uiMain.getStack().load(WIZARD_DEVICE);
                //} else {
                getMainUI().getStack().load(HOME);
                getMainUI().open();
                //}
                LOGGER.info("Total Start Time = ".concat(getBuilder().getTimeElapsed().toString()));
                getBuilder().end();
                setPlaying(true);

                if (user.getStatistics().isRecording()) {
                    getSessionTimer().playFromStart();
                }
                user.getStatistics().recordProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue) {
                        getSessionTimer().playFromStart();
                    } else {
                        getSessionTimer().end();
                    }
                });
                if (user.getStatistics().isRecording()) {
                    user.getStatistics().setSessionStartTime(System.currentTimeMillis());

                    if (!user.getInteraction().isInAssistedMode()) {
                        user.getStatistics().incrementTotalSessionCount();
                    }
                }

                LOGGER.info(StageHelper.getStages().size() + " stages opened");

            }
        });
    }

    /**
     *
     */
    private LoopedEvent sessionTimer;

    /**
     *
     * @return
     */
    public LoopedEvent getSessionTimer() {
        if (sessionTimer == null) {
            sessionTimer = new LoopedEvent();
            sessionTimer.setup(1.0, (ActionEvent) -> {

                if (user.getStatistics().isRecording()) {
                    if (!getUser().getInteraction().isInAssistedMode()) {
                        getUser().getStatistics().incrementTotalTimeInUse(1);
                        getUser().getStatistics().incrementSessionTimeInUse(1);
                        /*
                    Date date = new Date(getUser().getStatistics().getTotalTimeUsed()*1000);
                    DateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss aaa");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    getUser().getStatistics().setTotalTimeString(formatter.format(date));*/
                    }
                }
            });
        }
        return sessionTimer;
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

    /**
     *
     */
    /*   public Map<String, App> appMap;

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
     */
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
        //    Boolean appsReady = Boolean.TRUE;
        /*    for (String appName : getUser().getNavigation().getAppBuildOrder()) {
            appsReady = appsReady && getApp(appName).isBuilt();
            if (!getApp(appName).isBuilt()) {
                LOGGER.info("Build for App " + appName + " is pending");
            }
        }

        if (appsReady) {
            LOGGER.info("All apps have been built");
        }*/
        Boolean ready = mainReady && !isBuilt();
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
    public void exit(Boolean backupSettings) {
        shutdown(backupSettings);
        close();

    }

    public void shutdown(Boolean backupSettings) {
        resetStats();
        saveSettings(backupSettings);
    }

    public void saveSettings(Boolean backupSettings) {
        if (backupSettings) {
            String backupFolder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Backup";
            PathWriter backupPathWriter = new PathWriter(backupFolder);
            backupPathWriter.file.mkdirs();
            PreferencesExporter backupSettingsExporter = new PreferencesExporter(backupFolder, getSettingsFileName("All"), "/org/symfound");
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
        PreferencesExporter settingsExporter = new PreferencesExporter(folder, "/Master.xml", "/org/symfound");

        LOGGER.info("Backing up master settings");
        Thread thread = new Thread(settingsExporter);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException ex) {
            LOGGER.warn(ex);
        }
    }

    public void resetStats() {
        LOGGER.info("Shutting down SYMPLE");
        
        LOGGER.info("Resetting Stats");
        
        getUser().getStatistics().resetSessionTimeInUse();
        getUser().getStatistics().resetSessionSpokenWordCount();
        getUser().getStatistics().resetSessionSelections();
        
        if (user.getStatistics().isRecording()) {
            getUser().getStatistics().setLastUsed(System.currentTimeMillis());
        }
    }

    public void close() {
        LOGGER.info("Closing hardware");
        getDeviceManager().getCurrent().getHardware().close();
        LOGGER.info("Stopping Input Listener");
        InputListener.stop();
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

    public static String getSettingsFileName(String suffix) {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        final String fullName = Main.getSession().getUser().getProfile().getFullName();
        String fileName = "/" + fullName + " " + suffix + " " + dateFormat.format(date) + ".xml";
        return fileName;
    }
}
