/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.loader.Loader;
import org.symfound.builder.loader.UIPath;

/**
 *
 * @author Javed Gangjee
 */
public class StandalonePane extends AnchorPane {

    private static final String NAME = StandalonePane.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public StandalonePane() {
        super();
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
    }

    /**
     * Uses a Loader to loadScreens the parent of the UI.
     * <p>
     * Then adds the parent to an empty <code>AnchorPane</code> along with a
     * menu if the <code>hasMenu</code> boolean is true.
     * <p>
     * The generated <code>AnchorPane</code> is added to a {@link Stage} and
     * class the <code>notifyMain</code> method which checks for program
     * completion.
     *
     * @param file
     * @param builder
     * @see Loader
     */
    public void build(UIPath file, Builder builder) {
        // Initialize the Loader
        Loader loader = new Loader(file);
        // Listen for a change in state. 
        loader.stateProperty().addListener((stateProp, oldState, newState) -> {
            switch (newState) {
                case SCHEDULED:
                    // progress.setProgress(1);
                    break;
                case SUCCEEDED:
                    // Get the generated UI and assign it to the parent field.
                    Parent standalone = loader.getValue();
                    getChildren().add(standalone);
                    AnchorPane.setTopAnchor(standalone, 0.0);
                    AnchorPane.setBottomAnchor(standalone, 0.0);
                    AnchorPane.setLeftAnchor(standalone, 0.0);
                    AnchorPane.setRightAnchor(standalone, 0.0);
                    setBuilt(Boolean.TRUE);
                    break;
                case FAILED: {
                    setBuilt(Boolean.FALSE);
                    try {
                        throw loader.getException();
                    } catch (Throwable ex) {
                        LOGGER.fatal("UI " + file.get() + " failed build", ex);
                    }
                }
                break;
                case CANCELLED:
                    setBuilt(Boolean.FALSE);
                    break;
            }
        });
        builder.addLoader(loader);
    }

    private BooleanProperty built;

    /**
     *
     * @return
     */
    public Boolean isBuilt() {
        return builtProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setBuilt(Boolean value) {
        builtProperty().setValue(value);
    }

    /**
     *
     * @return built
     */
    public BooleanProperty builtProperty() {
        if (built == null) {
            built = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return built;
    }

}
