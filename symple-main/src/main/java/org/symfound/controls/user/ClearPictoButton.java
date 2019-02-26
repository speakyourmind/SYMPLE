package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public final class ClearPictoButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = ClearPictoButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Clear Picto";

    /**
     *
     * @param index
     */
    public ClearPictoButton(String index) {
        super("", KEY, index, index);
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
        final ParallelList<String, String> order = new ParallelList<>();
        order.getFirstList().add("None");
        order.getSecondList().add("default");
        getPictoArea().getConfigurableGrid().setOrder(order);
        getPictoArea().getConfigurableGrid().reload();
        getPictoArea().setPictoText("");
    }

    /**
     *
     */
    public PictoArea picto;

    /**
     *
     * @return
     */
    public final PictoArea getPictoArea() {
        if (picto == null) {
            if (!getPictoID().isEmpty()) {
                final String pictoHash = "#" + getPictoID();
                // Or it can lookup another pane in the scene.
                picto = (PictoArea) getScene().lookup(pictoHash);
            }
        }
        return picto;
    }
    private StringProperty pictoID;

    /**
     *
     * @param value
     */
    public void setPictoID(String value) {
        pictoIDProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getPictoID() {
        return pictoIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoIDProperty() {
        if (pictoID == null) {
            pictoID = new SimpleStringProperty("");
        }
        return pictoID;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ClearPictoButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
