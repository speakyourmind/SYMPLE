package org.symfound.controls.user;

import images.Images;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.symfound.controls.AppableControl;
import org.symfound.controls.device.DeviceSettings;
import org.symfound.controls.device.GenericDeviceSettings;
import org.symfound.controls.device.SwiftySettings;
import org.symfound.controls.device.EyeTrackerSettings;
import org.symfound.controls.system.dialog.ScreenPopup;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.manager.DeviceManager;
import org.symfound.builder.user.selection.SelectionMethod;

/**
 *
 * @author Javed Gangjee
 */
public final class DeviceButton extends AppableControl {

    /**
     *
     */
    public static final String KEY = "Device";

    /**
     *
     */
    public static final String DEFAULT_TITLE_PREFIX = "Configure ";

    /**
     *
     */
    public DeviceButton() {
        super("calibrate-button", KEY, "Configure Your Device","default");
        initialize();
        setEditable(Boolean.FALSE);
        setConfirmable(Boolean.TRUE);
        initTitleText = "Activate To Configure";
        initCaptionText = "In order to configure this device, it needs to first be launched";
        setOkText("CONFIRM");
        setCancelText("CANCEL");
        titleProperty().bind(Bindings.concat(DEFAULT_TITLE_PREFIX, getUser().deviceNameProperty()));
    }

    /**
     *
     */
    public final void initialize() {
        iconTypeProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
            getUser().deviceNameProperty().addListener((observable, oldValue, newValue) -> {
                updateIcon();
            });
        });
    }

    /**
     *
     */
    @Override
    public void execute() {
        if (isConfirmable() && !getSession().getDeviceManager().isSelectedCurrent()) {
            if (getSession().isBuilt()) {
                getParentPane().getChildren().add(getPopup());
                final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
                getDialog().animate().startScale(selectionTime, 0.8, 1.0);
            }
        } else {
            Platform.runLater(this);
            getPrimaryControl().setOnFinished(srcText, srcStyle);
        }
    }

    /**
     *
     */
    public void updateIcon() {
        if (getIconType().equalsIgnoreCase("Device")) {
            String image = Images.class.getResource("device_"
                    + getUser().getDeviceName().toLowerCase().replaceAll(" ", "").trim() + ".png").toExternalForm();
            getPrimaryControl().setStyle("-fx-background-image: url('" + image + "'); "
                    + "-fx-background-size: 80 80; "
                    + "-fx-background-repeat: no-repeat; "
                    + "-fx-background-position: center;");
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExit(MouseEvent e) {
        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
            exit();
            updateIcon();
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        final String selectedDeviceName = getSession().getDeviceManager().getIterator().get();
        getUser().setDeviceName(selectedDeviceName);
        DeviceManager deviceManager = getSession().getDeviceManager();
        switch (deviceManager.getIterator().get()) {
            case Hardware.SWIFTY:
                SwiftySettings swiftyConfiguration = new SwiftySettings();
                launchConfiguration(swiftyConfiguration);
                break;
            case Hardware.EYE_TRACKER:
                EyeTrackerSettings tobiiConfiguration = new EyeTrackerSettings();
                launchConfiguration(tobiiConfiguration);
                break;
            case Hardware.GENERIC:
                GenericDeviceSettings genericConfiguration = new GenericDeviceSettings();
                launchConfiguration(genericConfiguration);
                break;
        }
    }

    /**
     *
     * @param <T>
     * @param settings
     */
    public <T extends DeviceSettings> void launchConfiguration(T settings) {
        final double launchTime = getSession().getUser().getInteraction().getSelectionTime();
        settings.animate().startScale(launchTime, 0.8, 1.0);
        ScreenPopup popup = new ScreenPopup(settings);
        Pane popupPane = (Pane) getScene().lookup("#" + getPane());
        popupPane.getChildren().add(popup);
    }

    /**
     *
     * @param value
     */
    public void setDisabled(Boolean value) {
        getPrimaryControl().setDisable(value);
    }

    private StringProperty iconType;

    /**
     *
     * @return
     */
    public String getIconType() {
        return iconTypeProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setIconType(String value) {
        iconTypeProperty().set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty iconTypeProperty() {
        if (iconType == null) {
            iconType = new SimpleStringProperty();
        }
        return iconType;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
