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
public final class BackSpacePictoButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = BackSpacePictoButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "BackSpace Picto";

    /**
     *
     * @param index
     */
    public BackSpacePictoButton(String index) {
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
        final ConfigurableGrid configurableGrid = getPictoArea().getConfigurableGrid();
        final ParallelList<String, String> order = configurableGrid.getOrder();
        final int name = configurableGrid.getOrder().size();
        if (name > 1) {
            order.getFirstList().remove(name - 1);
            order.getSecondList().remove(name - 1);
            configurableGrid.setOrder(order);
        } else {
            final ParallelList<String, String> clearedOrder = new ParallelList<>();
            clearedOrder.getFirstList().add("None");
            clearedOrder.getSecondList().add("default");
             configurableGrid.setOrder(clearedOrder);
            getPictoArea().setPictoText("");
        }
            configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
            configurableGrid.triggerReload();
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
            Class<? extends BackSpacePictoButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
