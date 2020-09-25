package org.symfound.controls.user.type.picto;

import org.symfound.controls.user.type.picto.PictoArea;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public final class PictoBackspaceButton extends PictoControl {

    /**
     *
     */
    public static final String NAME = PictoBackspaceButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Backspace";

    /**
     *
     * @param index
     */
    public PictoBackspaceButton(String index) {
        super("", KEY, "Backspace", index);
        initialize();
    }

    private void initialize() {
        configureStyle("Roboto", FontWeight.BOLD);

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
            //TODO: Replace with PictoGrid().clear();
            final ParallelList<String, String> clearedOrder = new ParallelList<>();
            clearedOrder.getFirstList().add("Replace Key");
            clearedOrder.getSecondList().add("default");
             configurableGrid.setOrder(clearedOrder);
            getPictoArea().setPictoText("");
        }
            configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
            configurableGrid.triggerReload();
    }


    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends PictoBackspaceButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
