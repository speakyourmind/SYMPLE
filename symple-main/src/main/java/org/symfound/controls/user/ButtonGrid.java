package org.symfound.controls.user;

import org.symfound.controls.system.grid.editor.EditGridButton;
import org.symfound.controls.system.grid.editor.ReplaceKeyButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.AnchorPane.setBottomAnchor;
import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setRightAnchor;
import static javafx.scene.layout.AnchorPane.setTopAnchor;
import javafx.scene.media.MediaPlayer.Status;
import org.apache.log4j.Logger;
import org.symfound.app.DesktopController;
import org.symfound.app.SpeakController;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.system.EditButton;
import org.symfound.controls.user.media.music.MusicControlButton;
import org.symfound.controls.user.media.music.MusicTagButton;
import org.symfound.controls.user.voice.SpeakUserButton;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.controls.user.media.MediaViewer;
import org.symfound.controls.user.media.calendar.CalendarControlButton;
import org.symfound.controls.user.media.calendar.CalendarViewer;
import org.symfound.controls.user.media.gmail.GMailControlButton;
import org.symfound.controls.user.media.gmail.GMailSpeakButton;
import org.symfound.controls.user.media.gmail.GMailViewer;
import org.symfound.controls.user.media.music.MusicButton;
import org.symfound.controls.user.media.music.MusicPlayer;
import org.symfound.controls.user.media.pageflip.PageFlipControlButton;
import org.symfound.controls.user.media.pageflip.PageFlipViewer;
import org.symfound.controls.user.media.photos.PhotoControlButton;
import org.symfound.controls.user.media.photos.PhotoViewer;
import org.symfound.controls.user.media.reddit.RedditControlButton;
import org.symfound.controls.user.media.reddit.RedditViewer;
import org.symfound.controls.user.media.twitter.TwitterControlButton;
import org.symfound.controls.user.media.twitter.TwitterViewer;
import org.symfound.controls.user.media.video.VideoControlButton;
import org.symfound.controls.user.media.video.VideoViewer;
import org.symfound.controls.user.media.youtube.YouTubeControlButton;
import org.symfound.controls.user.media.youtube.YouTubeViewer;
import org.symfound.controls.user.voice.SpeakGrid;
import org.symfound.controls.user.voice.SpeakPictoButton;
import org.symfound.controls.user.voice.TwilioSendButton;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.HomeController;
import static org.symfound.main.Main.getVersionManager;
import org.symfound.main.builder.App;
import org.symfound.tools.selection.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public abstract class ButtonGrid extends FillableGrid {

    private static final String NAME = ButtonGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String BUTTON_DELIMITER = ", ";

    /**
     *
     */
    public static final String KEY_DELIMITER = "=";

    /**
     *
     */
    public static final Integer MAX_PHRASES = 6;

    /**
     *
     */
    public static Boolean mutex = false;

    /**
     *
     */
    public static final List<String> KEY_CATALOGUE = Arrays.asList(SubGrid.KEY,
            SpeakGrid.KEY,
            SpeakUserButton.KEY,
            SpeakPictoButton.KEY,
            TwilioSendButton.KEY,
            BackSpacePictoButton.KEY,
            ClearPictoButton.KEY,
            PhotoControlButton.KEY,
            MusicTagButton.KEY,
            MusicControlButton.KEY,
            VideoControlButton.KEY,
            CalendarControlButton.KEY,
            RedditControlButton.KEY,
            PageFlipControlButton.KEY,
            TwitterViewer.KEY,
            TwitterControlButton.KEY,
            YouTubeViewer.KEY,
            CalendarViewer.KEY,
            YouTubeControlButton.KEY,
            VolumeGridButton.KEY,
            GMailViewer.KEY,
            GMailSpeakButton.KEY,
            GMailControlButton.KEY,
            EditButton.KEY,
            NavigateButton.KEY,
            MinimizeButton.KEY,
            ScriptButton.KEY,
            ActiveTextArea.KEY,
            PictoArea.KEY,
            GenericButton.KEY,
            ExitButton.KEY,
            SettingsButton.KEY,
            IftttButton.KEY,
            LockButton.KEY,
            ReplaceKeyButton.KEY,
            EditGridButton.KEY,
            DeviceButton.KEY,
            VersionUpdateButton.KEY,
            ClockButton.KEY,
            HomeController.KEY,
            SpeakController.KEY,
            DesktopController.KEY);

    /**
     *
     */
    public final List<String> masterKeys;

    /**
     *
     */
    public ButtonGrid() {
        this.masterKeys = KEY_CATALOGUE;
        initialize();
    }

    private void initialize() {
        setHgap(getGap());
        setVgap(getGap());
        gapProperty().bindBidirectional(vgapProperty());
        gapProperty().bindBidirectional(hgapProperty());

        setStyle(getOverrideStyle());
        overrideStyleProperty().addListener((obversable1, oldValue1, newValue1) -> {
            LOGGER.info("Setting style to " + newValue1);
            getStyleClass().clear();
            setStyle(newValue1);
        });

        ChangeListener<ParallelList<String, String>> listener = (observable, oldValue, newValue) -> {
            if (!newValue.getFirstList().contains(PhotoControlButton.KEY)) {
                if (photoViewer != null) {
                    photoViewer.end();
                }
            }
            if (!newValue.getFirstList().contains(VideoControlButton.KEY)) {
                if (calendarViewer != null) {
                    calendarViewer.end();
                }
            }
            if (!newValue.getFirstList().contains(VideoControlButton.KEY)) {
                if (videoViewer != null) {
                    videoViewer.end();
                }
            }
            if (!newValue.getFirstList().contains(RedditControlButton.KEY)) {
                if (redditViewer != null) {
                    redditViewer.end();
                }
            }
            if (!newValue.getFirstList().contains(PageFlipControlButton.KEY)) {
                if (pageFlipViewer != null) {
                    pageFlipViewer.end();
                }
            }
        };
        orderProperty().addListener(listener);

    }

    /**
     *
     * @param requestedOrder
     * @return
     */
    public ParallelList<String, String> getValidatedKeyOrder(ParallelList<String, String> requestedOrder) {
        ParallelList<String, String> validatedOrder = new ParallelList<>();
        for (int i = 0; i < requestedOrder.size(); i++) {
            String requestedKey = requestedOrder.getFirstList().get(i);
            if (validateKey(requestedKey)) {
                validatedOrder.put(requestedKey, requestedOrder.getSecondList().get(i));
            } else {
                LOGGER.fatal("Key " + requestedKey + " is not available in Master Key list: " + masterKeys.toString());
            }
        }

        if (validatedOrder.size() < 2) {
            LOGGER.warn("Only 1 key is available to populate the grid");
        } else if (validatedOrder.size() < 1) {
            LOGGER.warn("No keys are available to populate the grid.");
        }
        return validatedOrder;
    }

    /**
     *
     * @param requestedKey
     * @return
     */
    public Boolean validateKey(String requestedKey) {
        return masterKeys.contains(requestedKey);
    }

    private List<String> availableKeys;

    /**
     *
     * @return
     */
    public List<String> getAvailableKeys() {
        if (availableKeys == null) {
            availableKeys = new ArrayList<>();
        }
        return availableKeys;
    }

    public void build(ParallelList<String, String> buildOrder,
            FillMethod method, FillDirection direction, Double size) {
        setStatus(ScreenStatus.ENDING);
        clear();
        setStatus(ScreenStatus.READY);
        int rowSize = getRowSize(size);
        setSpecRows(rowSize);
        int columnSize = getColumnSize(size);
        setSpecColumns(columnSize);
        setStatus(ScreenStatus.REQUESTED);
        fill(buildOrder, rowSize * columnSize);
        setStatus(ScreenStatus.LOADING);
        build();
        configure(getControlsQueue(), method, direction);
        Insets insets = new Insets(DEFAULT_GRID_GAP);
        setPadding(insets);
        /*        if (!AppGrid.inEditMode()) {
        launchAnimation();
        }*/
        setStatus(ScreenStatus.PLAYING);
        toBack();
    }

    @Override
    public void clear() {
        getChildren().removeAll(getChildren());
        getRowConstraints().clear();
        getColumnConstraints().clear();
    }

    public void reload(ParallelList<String, String> buildOrder, FillMethod method, FillDirection direction, Double size) {
        Platform.runLater(() -> {
            if (!mutex) {
                mutex = true;
                build(buildOrder, method, direction, size);
                mutex = false;
            }
        });
    }

     /*    public void initializeControl(RunnableControl control) {
    if (control instanceof AppableControl) {
    AppableControl appableControl = (AppableControl) control;
    appableControl.configureTitle();
    appableControl.getKeyRemoveButton().orderProperty().bindBidirectional(this.orderProperty());
    }
    }*/
    public List<RunnableControl> requestedControls = new ArrayList<>();

    public void fill(ParallelList<String, String> buildOrder, Integer size) {
        if (buildOrder.getFirstList().size() > 0) {
            getAvailableKeys().clear();
            requestedControls = new ArrayList<>();
            getAvailableKeys().addAll(masterKeys);
            Integer requestedSize = buildOrder.getFirstList().size();
            Integer buildSize = (size < requestedSize) ? size : requestedSize;

            if (!buildOrder.getFirstList().contains(MusicControlButton.KEY)
                    && !buildOrder.getFirstList().contains(MusicTagButton.KEY)) {
                MusicPlayer view = MusicButton.getMusicView();
                if (view.getMediaPlayer() != null) {
                    if (view.getMediaPlayer().getStatus().equals(Status.PLAYING)) {
                        view.stop();
                    }
                }
            }
            if (!buildOrder.getFirstList().contains(YouTubeControlButton.KEY)) {
                if (youtubeViewer != null) {
                    youtubeViewer.end();
                }
            }
            if (!buildOrder.getFirstList().contains(TwitterControlButton.KEY)) {
                if (twitterViewer != null) {
                    twitterViewer.end();
                }
            }

            /*  */
            for (int i = 0; i < buildSize; i++) {
                String toBuild = buildOrder.getFirstList().get(i);
                String index = buildOrder.getSecondList().get(i);
                switch (toBuild.trim()) {
                    case ScriptButton.KEY:
                        ScriptButton scriptButton = new ScriptButton(index);
                        scriptButton.setTextAreaID("gridTextArea");
                        scriptButton.setPictoID("gridPictoArea");
                        configureItem(scriptButton);
                        scriptButton.setGridLocation(i);
                        requestedControls.add(scriptButton);
                        break;
                    case ActiveTextArea.KEY:
                        ActiveTextArea textArea = new ActiveTextArea();
                        textArea.setId("gridTextArea");
                        textArea.setGridLocation(i);
                        requestedControls.add(textArea);
                        break;
                        case TwilioSendButton.KEY:
                        TwilioSendButton twilioButton = new TwilioSendButton(index);
                        configureItem(twilioButton);
                        twilioButton.setGridLocation(i);
                        twilioButton.setPane("agApp");
                        requestedControls.add(twilioButton);
                        break;
                    case SpeakUserButton.KEY:
                        SpeakUserButton speakButton = new SpeakUserButton(index);
                        configureItem(speakButton);
                        speakButton.setGridLocation(i);
                        speakButton.setPane("agApp");
                        requestedControls.add(speakButton);
                        break;
                    case PictoArea.KEY:
                        PictoArea pictoArea = new PictoArea();
                        pictoArea.setId("gridPictoArea");
                        pictoArea.setGridLocation(i);
                        requestedControls.add(pictoArea);
                        break;
                    case SpeakPictoButton.KEY:
                        SpeakPictoButton speakPictoButton = new SpeakPictoButton(index);
                        speakPictoButton.setPictoID("gridPictoArea");
                        configureItem(speakPictoButton);
                        speakPictoButton.setPane("agApp");
                        speakPictoButton.setGridLocation(i);
                        requestedControls.add(speakPictoButton);
                        break;
                    case ClearPictoButton.KEY:
                        ClearPictoButton clearPictoButton = new ClearPictoButton(index);
                        clearPictoButton.setPictoID("gridPictoArea");
                        configureItem(clearPictoButton);
                        clearPictoButton.setPane("agApp");
                        clearPictoButton.setGridLocation(i);
                        requestedControls.add(clearPictoButton);
                        break;
                    case BackSpacePictoButton.KEY:
                        BackSpacePictoButton bsPictoButton = new BackSpacePictoButton(index);
                        bsPictoButton.setPictoID("gridPictoArea");
                        configureItem(bsPictoButton);
                        bsPictoButton.setPane("agApp");
                        bsPictoButton.setGridLocation(i);
                        requestedControls.add(bsPictoButton);
                        break;
                    case ExitButton.KEY:
                        ExitButton exitButton = new ExitButton();
                        configureItem(exitButton);
                        exitButton.setPane("agApp");
                        exitButton.setGridLocation(i);
                        requestedControls.add(exitButton);
                        break;
                    case VolumeGridButton.KEY:
                        VolumeGridButton volumeGridButton = new VolumeGridButton(index);
                        volumeGridButton.setGridLocation(i);
                        requestedControls.add(volumeGridButton);
                        break;
                    case PhotoControlButton.KEY:
                        PhotoControlButton photoControl = new PhotoControlButton(index, getPhotoViewer(index));
                        configureItem(photoControl);
                        photoControl.setGridLocation(i);
                        requestedControls.add(photoControl);
                        break;
                    case VideoControlButton.KEY:
                        VideoControlButton videoControl = new VideoControlButton(index, getVideoViewer(index));
                        configureItem(videoControl);
                        videoControl.setGridLocation(i);
                        requestedControls.add(videoControl);
                        break;
                    case PageFlipControlButton.KEY:
                        PageFlipControlButton pageFlipControl = new PageFlipControlButton(index, getPageFlipViewer(index));
                        pageFlipControl.setGridLocation(i);
                        configureItem(pageFlipControl);
                        requestedControls.add(pageFlipControl);
                        break;

                    case CalendarControlButton.KEY:
                        CalendarControlButton calendarControl = new CalendarControlButton(index, getCalendarViewer(index));
                        calendarControl.setGridLocation(i);
                        configureItem(calendarControl);
                        requestedControls.add(calendarControl);
                        break;
                    case RedditControlButton.KEY:
                        RedditControlButton redditControl = new RedditControlButton(index, getRedditViewer(index));
                        redditControl.setGridLocation(i);
                        configureItem(redditControl);
                        requestedControls.add(redditControl);
                        break;
                    case TwitterControlButton.KEY:
                        TwitterControlButton twitterControl = new TwitterControlButton(index, getTwitterViewer(index));
                        configureItem(twitterControl);
                        twitterControl.setGridLocation(i);
                        requestedControls.add(twitterControl);
                        break;
                    case YouTubeControlButton.KEY:
                        YouTubeControlButton youtubeControl = new YouTubeControlButton(index, getYouTubeViewer(index));
                        configureItem(youtubeControl);
                        youtubeControl.setGridLocation(i);
                        requestedControls.add(youtubeControl);
                        break;

                    case CalendarViewer.KEY:
                        CalendarViewer calendarButton = new CalendarViewer(index);
                        calendarButton.setGridLocation(i);
                        requestedControls.add(calendarButton);
                        break;
                    case GMailViewer.KEY:
                        GMailViewer gmailButton = new GMailViewer(index);
                        gmailButton.setGridLocation(i);
                        requestedControls.add(gmailButton);
                        break;
                    case GMailSpeakButton.KEY:
                        GMailSpeakButton gmailSpeak = new GMailSpeakButton(index);
                        gmailSpeak.setGridLocation(i);
                        requestedControls.add(gmailSpeak);
                        break;
                    case GMailControlButton.KEY:
                        GMailControlButton gmailControl = new GMailControlButton(index);
                        gmailControl.setGridLocation(i);
                        requestedControls.add(gmailControl);
                        break;
                    case MusicTagButton.KEY:
                        MusicTagButton musicTagButton = new MusicTagButton(index);
                        musicTagButton.setGridLocation(i);
                        requestedControls.add(musicTagButton);
                        break;
                    case MusicControlButton.KEY:
                        MusicControlButton musicButton = new MusicControlButton(index);
                        musicButton.setGridLocation(i);
                        requestedControls.add(musicButton);
                        break;
                    case SettingsButton.KEY:
                        SettingsButton settingsButton = new SettingsButton();
                        configureItem(settingsButton);
                        settingsButton.setGridLocation(i);
                        requestedControls.add(settingsButton);
                        break;
                    case IftttButton.KEY:
                        IftttButton button = new IftttButton(index);
                        configureItem(button);
                        button.setGridLocation(i);
                        requestedControls.add(button);
                        break;
                    case LockButton.KEY:
                        LockButton lockButton = new LockButton();
                        configureItem(lockButton);
                        lockButton.setGridLocation(i);
                        lockButton.setPane("agApp"); // Change to apMain for Menu Grid
                        requestedControls.add(lockButton);
                        break;
                    case EditGridButton.KEY:
                        EditGridButton editKeysButton = new EditGridButton(this);
                        editKeysButton.setPane("apMain"); // Change to apMain for Menu Grid
                        editKeysButton.setGridLocation(i);
                        requestedControls.add(editKeysButton);
                        break;
                    case ReplaceKeyButton.KEY:
                        ReplaceKeyButton addKeyButton = new ReplaceKeyButton(this);
                        addKeyButton.setPane("apMain"); // Change to apMain for Menu Grid
                        addKeyButton.setGridLocation(i);
                        requestedControls.add(addKeyButton);
                        break;

                    case NavigateButton.KEY:
                        NavigateButton navigateButton = new NavigateButton(index);
                        configureItem(navigateButton);
                        navigateButton.setGridLocation(i);
                        requestedControls.add(navigateButton);
                        break;
                    case DeviceButton.KEY:
                        DeviceButton deviceButton = new DeviceButton();
                        deviceButton.configureTitle();
                        deviceButton.setIconType("Device");
                        deviceButton.setPane("apMain");
                        deviceButton.updateIcon();
                        deviceButton.setGridLocation(i);
                        if (!getUser().getDeviceName().equalsIgnoreCase(Hardware.GENERIC)) {
                            requestedControls.add(deviceButton);
                        }
                        break;
                    case VersionUpdateButton.KEY:
                        VersionUpdateButton versionUpdateButton = new VersionUpdateButton();
                        versionUpdateButton.setPane("apMain");
                        configureItem(versionUpdateButton);
                        versionUpdateButton.setGridLocation(i);
                        if (getVersionManager().needsUpdate()) {
                            requestedControls.add(versionUpdateButton);
                        }
                        break;
                    case GenericButton.KEY:
                        GenericButton blankButton = new GenericButton(index);

                        blankButton.setGridLocation(i);
                        configureItem(blankButton);
                        requestedControls.add(blankButton);
                        break;
                    case SubGrid.KEY:
                        SubGrid subGrid = new SubGrid(index);
                        subGrid.setGridLocation(i);
                        requestedControls.add(subGrid);
                        break;
                    case SpeakGrid.KEY:
                        SpeakGrid speakGrid = new SpeakGrid(index);
                        speakGrid.setGridLocation(i);
                        requestedControls.add(speakGrid);
                        break;
                    case ClockButton.KEY:
                        ClockButton clockButton = new ClockButton(index);
                        clockButton.setGridLocation(i);
                        configureItem(clockButton);
                        requestedControls.add(clockButton);
                        break;
                    case HomeController.KEY:
                        final String currentIndex = index;
                        ConfigurableGrid homeGrid = HomeController.getGrid().getConfigurableGrid();
                        GenericButton screenButton = new GenericButton(currentIndex) {
                            @Override
                            public void run() {
                                LOGGER.info("Setting index to " + currentIndex);
                                homeGrid.setIndex(currentIndex);
                                super.run();
                            }
                        };
                        screenButton.setKey(HomeController.KEY);
                        configureItem(screenButton);
                        screenButton.setGridLocation(i);
                        screenButton.setVisible(true);
                        if (Double.valueOf(homeGrid.getGridManager().getPrefs(currentIndex).get("minDifficulty", "0.0")) <= getUser().getAbility().getLevel()) {
                            requestedControls.add(screenButton);
                        }
                        break;

                    default:
                        if (toBuild.length() > 0) {
                            System.out.println(getSession().appMap.keySet());
                            App appToBuild = getSession().appMap.get("Speak");
                            String appString = appToBuild.getValue();
                            System.out.println(appString);
                            int usablesSize = appToBuild.getUsables().size();
                            Integer maxNumApps = getUser().getNavigation().getButtonMap().get(appString);
                            Integer numOfApps;

                            if (usablesSize < maxNumApps) {
                                numOfApps = usablesSize;
                            } else {
                                numOfApps = maxNumApps;
                            }
                            for (int j = 0; j < numOfApps; j++) {
                                AppButton appButton = new AppButton(toBuild, index + "/" + String.valueOf(j), appToBuild.getUsables().get(j).getDescription(), j);
                                configureItem(appButton);
                                requestedControls.add(appButton);
                            }
                        }
                        break;
                }
                getAvailableKeys().remove(toBuild);
            }

            resetControlsQueue();
            if (requestedControls.size() > 0) {
                getControlsQueue().addAll(requestedControls);
                getControlsQueue().forEach((control) -> {
                    LOGGER.debug("Control " + control.getText() + " is at " + control.getGridLocation());
                });
            } else {
                LOGGER.warn("No controls available!");
                ParallelList<String, String> defaultList = new ParallelList<>();
                defaultList.put(GenericButton.KEY, "default");
                fill(defaultList, size);
            }
        }
    }

    private void addToParent(MediaViewer viewer) {
        Parent parent = getParent();
        if (parent instanceof AnchorPane) {
            setTopAnchor(viewer, 0.0);
            setLeftAnchor(viewer, 0.0);
            setRightAnchor(viewer, 0.0);
            setBottomAnchor(viewer, 0.0);
            viewer.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            AnchorPane pane = (AnchorPane) parent;
            if (!pane.getChildren().contains(viewer)) {
                pane.getChildren().add(viewer);
            }
        }
    }
    private CalendarViewer calendarViewer;

    private CalendarViewer getCalendarViewer(String index) {
        if (calendarViewer == null) {
            calendarViewer = new CalendarViewer(index);
        }
        calendarViewer.setIndex(index);
        addToParent(calendarViewer);
        calendarViewer.play();
        return calendarViewer;
    }
    private GMailViewer gmailViewer;

    private GMailViewer getGMailViewer(String index) {
        if (gmailViewer == null) {
            gmailViewer = new GMailViewer(index);
        }
        gmailViewer.setIndex(index);
        addToParent(gmailViewer);
        gmailViewer.play();
        return gmailViewer;
    }

    private VideoViewer videoViewer;

    private VideoViewer getVideoViewer(String index) {
        if (videoViewer == null) {
            videoViewer = new VideoViewer(index);
        }
        videoViewer.setIndex(index);
        addToParent(videoViewer);
        videoViewer.play();
        return videoViewer;
    }

    private PhotoViewer photoViewer;

    private PhotoViewer getPhotoViewer(String index) {
        if (photoViewer == null) {
            photoViewer = new PhotoViewer(index);

        }
        photoViewer.setIndex(index);

        addToParent(photoViewer);
        photoViewer.play();
        return photoViewer;
    }

    private static TwitterViewer twitterViewer;

    private TwitterViewer getTwitterViewer(String index) {
        if (twitterViewer == null) {
            twitterViewer = new TwitterViewer(index);

        }
        twitterViewer.setIndex(index);
        addToParent(twitterViewer);
        twitterViewer.play();
        return twitterViewer;
    }

    private static YouTubeViewer youtubeViewer;

    private YouTubeViewer getYouTubeViewer(String index) {
        if (youtubeViewer == null) {
            youtubeViewer = new YouTubeViewer(index);

        }
        youtubeViewer.setIndex(index);
        addToParent(youtubeViewer);
        youtubeViewer.play();
        return youtubeViewer;
    }
    private PageFlipViewer pageFlipViewer;

    private PageFlipViewer getPageFlipViewer(String index) {
        if (pageFlipViewer == null) {
            pageFlipViewer = new PageFlipViewer(index);

        }
        pageFlipViewer.setIndex(index);
        addToParent(pageFlipViewer);
        pageFlipViewer.play();
        return pageFlipViewer;
    }
    private RedditViewer redditViewer;

    private RedditViewer getRedditViewer(String index) {
        if (redditViewer == null) {
            redditViewer = new RedditViewer(index);

        }
        redditViewer.setIndex(index);
        addToParent(redditViewer);
        redditViewer.play();
        return redditViewer;
    }

    public void configureItem(AppableControl button) {
        button.configureTitle();
    }

    private BooleanProperty triggerReload;

    /**
     *
     * @return
     */
    public Boolean isReloading() {
        return triggerReloadProperty().getValue();
    }

    /**
     *
     */
    public void triggerReload() {
        setTriggerReload(Boolean.TRUE);
    }

    /**
     *
     * @param value
     */
    public void setTriggerReload(Boolean value) {
        triggerReloadProperty().setValue(value);
    }

    /**
     *
     * @return triggerReload
     */
    public BooleanProperty triggerReloadProperty() {
        if (triggerReload == null) {
            triggerReload = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return triggerReload;
    }

    private DoubleProperty gap;

    /**
     *
     * @param value
     */
    public void setGap(Double value) {
        gapProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final Double getGap() {
        return gapProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty gapProperty() {
        if (gap == null) {
            gap = new SimpleDoubleProperty(DEFAULT_GRID_GAP);
        }
        return gap;
    }
    public ObjectProperty<ParallelList<String, String>> order;

    /**
     *
     * @param value
     */
    public void setOrder(ParallelList<String, String> value) {
        orderProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public ParallelList<String, String> getOrder() {
        return orderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ParallelList<String, String>> orderProperty() {
        if (order == null) {
            order = new SimpleObjectProperty<>();
        }
        return order;
    }

    /**
     *
     */
    public ObjectProperty<FillableGrid.FillMethod> fillMethod;

    /**
     *
     * @param value
     */
    public void setFillMethod(FillableGrid.FillMethod value) {
        fillMethodProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public FillMethod getFillMethod() {
        return fillMethodProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<FillMethod> fillMethodProperty() {
        if (fillMethod == null) {
            fillMethod = new SimpleObjectProperty<>();
        }
        return fillMethod;
    }

    private StringProperty overrideStyle;

    /**
     *
     * @param value
     */
    public void setOverrideStyle(String value) {
        overrideStyleProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getOverrideStyle() {
        return overrideStyleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty overrideStyleProperty() {
        if (overrideStyle == null) {
            overrideStyle = new SimpleStringProperty();
        }
        return overrideStyle;
    }

    /**
     *
     */
    public ObjectProperty<FillableGrid.FillDirection> fillDirection;

    /**
     *
     * @param value
     */
    public void setFillDirection(FillableGrid.FillDirection value) {
        fillDirectionProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public FillDirection getFillDirection() {
        return fillDirectionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<FillDirection> fillDirectionProperty() {
        if (fillDirection == null) {
            fillDirection = new SimpleObjectProperty<>();
        }
        return fillDirection;
    }

    public DoubleProperty maxDifficulty;

    /**
     *
     * @param value
     */
    public void setMaxDifficulty(Double value) {
        maxDifficultyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getMaxDifficulty() {
        return maxDifficultyProperty().getValue();
    }

    public DoubleProperty maxDifficultyProperty() {
        if (maxDifficulty == null) {
            maxDifficulty = new SimpleDoubleProperty();
        }
        return maxDifficulty;
    }

    public DoubleProperty minDifficulty;

    /**
     *
     * @param value
     */
    public void setMinDifficulty(Double value) {
        minDifficultyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getMinDifficulty() {
        return minDifficultyProperty().getValue();
    }

    public DoubleProperty minDifficultyProperty() {
        if (minDifficulty == null) {
            minDifficulty = new SimpleDoubleProperty();
        }
        return minDifficulty;
    }

  
    private ObjectProperty<ScreenStatus> status;

    public void setStatus(ScreenStatus value) {
        statusProperty().setValue(value);
    }

    public ScreenStatus getStatus() {
        return statusProperty().getValue();
    }

    public ObjectProperty<ScreenStatus> statusProperty() {
        if (status == null) {
            status = new SimpleObjectProperty<>(ScreenStatus.CLOSED);
        }
        return status;
    }
}
