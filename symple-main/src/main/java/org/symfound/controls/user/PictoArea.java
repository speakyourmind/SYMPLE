/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed
 */
public class PictoArea extends AppableControl {

    /**
     *
     */
    public static final String NAME = PictoArea.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Area";
    
    /**
     *
     */
    public PictoArea() {
        super("", KEY, "picto", "picto");
        initialize();
    }
    
    private void initialize() {
        addToPane(getConfigurableGrid());
       speakableProperty().bindBidirectional(selectableProperty());
       setSpeakable(false);
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
    }
    @Override
    public void run() {
        if(this.isSpeakable()){
            //speak
        }
    }

    /**
     *
     * @param button
     */
    public void add(ScriptButton button) {
        final ConfigurableGrid configurableGrid = getConfigurableGrid();
        final ParallelList<String, String> order = configurableGrid.getOrder();
        order.getFirstList().add("Script");
        order.getSecondList().add(button.getIndex());
        configurableGrid.setOrder(order);
        
        if (configurableGrid.getOrder().getFirstList().contains("None")) {
            configurableGrid.getOrder().remove("None");
        }
        if (configurableGrid.getOrder().getFirstList().contains("Replace Key")) {
            configurableGrid.getOrder().remove("Replace Key");
        }
        configurableGrid.getGridManager().setOverrideColumn(order.size().doubleValue());
        configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
        configurableGrid.triggerReload();
    }
    
    private ConfigurableGrid pictoGrid;
    
    /**
     *
     * @return
     */
    public ConfigurableGrid getConfigurableGrid() {
        if (pictoGrid == null) {
            pictoGrid = new ConfigurableGrid();
            pictoGrid.setIndex(getIndex());
            pictoGrid.getGridManager().setOverrideRow(1.0);
            //   pictoGrid.getGridManager().overrideColumnProperty().bind()
            
            pictoGrid.setMinDifficulty(10.0);
            pictoGrid.configure();
            pictoGrid.setDisable(true);
            setCSS("subgrid", this);
        }
        return pictoGrid;
    }
    
    private StringProperty pictoText;
    
    /**
     *
     */
    public void updatePictoText() {
        String text = "";
        final int size = getConfigurableGrid().getChildren().size();
        for (int i = 0; i < size; i++) {
            Node node = getConfigurableGrid().getChildren().get(i);
            if (node instanceof RunnableControl) {
                RunnableControl button = (RunnableControl) node;
                final String currentButtonText = button.getPrimaryControl().getText();
                text = text + currentButtonText + " ";
            } else {
                LOGGER.warn("Unreadable item in picto area");
            }
        }
        setPictoText(text);
    }

    /**
     *
     * @param value
     */
    public void setPictoText(String value) {
        pictoTextProperty().set(value);
        getPreferences().put("webhook.event", value);
    }

    /**
     *
     * @return
     */
    public String getPictoText() {
        return pictoTextProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoTextProperty() {
        if (pictoText == null) {
            pictoText = new SimpleStringProperty(getPreferences().get("text", ""));
        }
        return pictoText;
    }
    
    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
