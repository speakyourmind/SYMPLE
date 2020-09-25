package org.symfound.app;

import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;
import org.symfound.builder.controller.ControllerBase;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.device.Device;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.main.builder.UI;
import org.symfound.controls.device.DeviceManager;

/**
 *
 * @author Javed Gangjee
 */
public abstract class CommonController extends ControllerBase implements Usable {

    private final FullSession session;
    private final User user;

    /**
     *
     */
    public CommonController() {
        session = Main.getSession();
        user = session.getUser();
    }

    /**
     *
     * @param parent
     * @return
     */
    public UI getParentUI(AnchorPane parent) {
        return (UI) parent.getScene().getWindow();
    }

    /**
     *
     * @return
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     *
     * @return
     */
    public FullSession getSession() {
        return session;
    }

    /**
     *
     * @return
     */
    public Hardware getSelectedHardware() {
        final DeviceManager deviceManager = getSession().getDeviceManager();
        String deviceName = deviceManager.getIterator().get();
        Device device = deviceManager.get(deviceName);
        Hardware hardware = device.getHardware();
        return hardware;
    }

    /**
     *
     * @return
     */
    public Hardware getCurrentHardware() {
        return getSession().getDeviceManager().getCurrent().getHardware();
    }

    private ObjectProperty<ResourceBundle> resourceBundle;

    /**
     *
     * @param value
     */
    public void setResourceBundle(ResourceBundle value) {
        resourceBundleProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        if (resourceBundle == null) {
            resourceBundle = new SimpleObjectProperty<>();
        }

        return resourceBundle;
    }

}
