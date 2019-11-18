package org.symfound.controls.user.type.picto;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public final class PictoClearButton extends PictoControl {

    /**
     *
     */
    public static final String NAME = PictoClearButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Clear";

    /**
     *
     * @param index
     */
    public PictoClearButton(String index) {
        super("control-clear", KEY, "Clear", index);
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
        final ParallelList<String, String> order = new ParallelList<>();
        order.getFirstList().add("Replace Key");
        order.getSecondList().add("default");
        
        getPictoArea().getConfigurableGrid().setOrder(order);
        getPictoArea().getConfigurableGrid().reload();
        getPictoArea().setPictoText("");
    }

  

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends PictoClearButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
