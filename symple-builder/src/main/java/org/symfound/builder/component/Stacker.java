package org.symfound.builder.component;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.symfound.builder.Builder;
import org.symfound.builder.loader.Loader;
import org.symfound.builder.loader.UIPath;
import org.symfound.comm.file.ExtensionAnalyzer;

/**
 *
 * @author Javed Gangjee
 */
public class Stacker extends StackPane {

    private static final String NAME = Stacker.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private BooleanProperty built;

    /**
     *
     */
    public HashMap<String, Parent> screens = new HashMap<>(); // TO DO: Change to UIPath,Parent

    /**
     *
     */
    public Parent parent;

    /**
     *
     */
    public Stacker() {
        super();
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
    }

    /**
     * Add the screen to the collection
     *
     * @param name
     * @param screen
     */
    public void addScreen(String name, Parent screen) {
        screens.put(name, screen);
    }

    /**
     *
     * @param file
     */
    public void load(String file) {
        ExtensionAnalyzer extensionAnalyzer = new ExtensionAnalyzer(file);
        if (!extensionAnalyzer.isScreen()) {
            file = extensionAnalyzer.addExtension(ExtensionAnalyzer.FXML_EXTENSION);
        }
        setCurrent(file);
        setScreenFade(file);
    }

    /**
     * Returns the Node with the appropriate name
     *
     * @param name
     * @return
     */
    public Node getScreen(String name) {
        return screens.get(name);
    }

    /**
     *
     * @param names
     * @param builder
     */
    public void build(List<UIPath> names, Builder builder) {
        names.stream().forEach((file) -> {
            Loader loader = new Loader(file);
            // Listen for a change in state. 
            loader.stateProperty().addListener((ObservableValue<? extends Worker.State> stateProp,
                    Worker.State oldState, Worker.State newState) -> {
                        switch (newState) {
                            case SCHEDULED:
                                // progress.setProgress(1);
                                break;
                            case SUCCEEDED:
                                // Get the generated UI and assign it to the parent field.
                                parent = loader.getValue();
                                AnchorPane.setTopAnchor(parent, 0.0);
                                AnchorPane.setBottomAnchor(parent, 0.0);
                                AnchorPane.setLeftAnchor(parent, 0.0);
                                AnchorPane.setRightAnchor(parent, 0.0);
                                addScreen(file.get(), parent);
                                LOGGER.info("Screen " + file.get() + " built");
                                if (screens.size() == names.size()) {
                                    setBuilt(true);
                                }

                                break;
                            case FAILED: {
                                try {
                                    throw loader.getException();
                                } catch (Throwable ex) {
                                    LOGGER.fatal(null, ex);
                                }
                            }

                            break;
                            case CANCELLED:
                                break;
                        }
                    });
            builder.addLoader(loader);
        });
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean setScreen(final String name) {
        if (screens.get(name) != null) {
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            } else {
                getChildren().add(screens.get(name));
            }
            LOGGER.info("Screen " + name + " loaded");
            return true;
        } else {
            LOGGER.log(Level.ERROR, "Screen " + name + " has not been loaded!");
            return false;
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean setScreenFade(final String name) {
        if (screens.get(name) != null) {
            if (!getChildren().isEmpty()) {
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            } else {
                getChildren().add(screens.get(name));
            }
            return true;
        } else {
            LOGGER.log(Level.ERROR, "Screen " + name + " has not been loaded!");
            return false;
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            LOGGER.warn( "Screen " + name + " does not exist");
            return false;
        } else {
            return true;
        }
    }

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

    private ObjectProperty<UIPath> current;
    /**
     *
     * @param value
     */
    public void setCurrent(String value) {
        UIPath uiPath = new UIPath(value);
        setCurrent(uiPath);
    }

    /**
     *
     * @param value
     */
    public void setCurrent(UIPath value) {
        currentProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public UIPath getCurrent() {
        return currentProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<UIPath> currentProperty() {
        if (current == null) {
            current = new SimpleObjectProperty<>();
        }
        return current;
    }
}
