package org.symfound.controls.user;

import java.awt.Point;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.EmulationRequest;
import org.symfound.main.HomeController;
import org.symfound.main.builder.UI;

/**
 * 
 *
 * @author Javed Gangjee
 */
@Deprecated
public class NavigateButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = NavigateButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Navigate";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Navigate";
    private String navigateTo = "home";

    /**
     *
     */
    public NavigateButton() {
        super("button", KEY, DEFAULT_TITLE, "default");
        initialize();
    }

    /**
     *
     * @param index
     */
    public NavigateButton(String index) {
        super("button", KEY, DEFAULT_TITLE, index);
        initialize();
    }

    /**
     *
     * @param navigateTo
     * @param index
     */
    public NavigateButton(String navigateTo, String index) {
        super("", KEY, navigateTo, index);
        this.navigateTo = navigateTo;
        initialize();
    }

    private void initialize() {
        configureStyle();

    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Requesting navigation to " + getNavigateIndex());

            UI ui = (UI) getScene().getWindow();
            if (ui.inEditMode()) {
                LOGGER.info("Exiting edit mode before navigating");
                ui.setEditMode(Boolean.FALSE);
            }
        
        openHomeScreen();

        ConfigurableGrid configurableGrid = HomeController.getGrid().getConfigurableGrid();

        if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.SCAN) || getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.STEP)) {
            Device current = getSession().getDeviceManager().getCurrent();
            EmulationManager em = current.getProcessor().getEmulationManager();
            final EmulationRequest emulationRequest = new EmulationRequest();
            emulationRequest.setPosition(new Point(0, 0));
            em.getMouse().getAutomator().navigate(new Point((int) (getParentUI().getWidth() / 2), (int) (getParentUI().getHeight() / 2)));

        }
        configurableGrid.setIndex(getNavigateIndex());
        getSession().setPlaying(false);

    }
    private TextField navigateIndexField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setNavigateIndex(navigateIndexField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        navigateIndexField.setText(getNavigateIndex());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRowA = createSettingRow("Navigate", "Text to be displayed on this button");
        navigateIndexField = new TextField();
        navigateIndexField.setText(getNavigateIndex());
        navigateIndexField.maxHeight(80.0);
        navigateIndexField.maxWidth(60.0);
        navigateIndexField.getStyleClass().add("settings-text-area");
        settingsRowA.add(navigateIndexField, 1, 0, 1, 1);

        actionSettings.add(settingsRowA);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends NavigateButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
