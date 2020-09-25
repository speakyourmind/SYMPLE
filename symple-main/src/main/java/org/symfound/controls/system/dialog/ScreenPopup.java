/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedPane;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public class ScreenPopup<T extends ScreenDialog> extends ScreenControl<AnimatedButton> {

    private final StackPane stackPane = new StackPane();
    private final T screenDialog;

    /**
     *
     * @param screenDialog
     */
    public ScreenPopup(T screenDialog) {
        this.screenDialog = screenDialog;
        AnimatedPane ap = new AnimatedPane();
        ap.getStylesheets().add(CSS_PATH);
        ap.setStyle("-fx-background-color:-fx-dark; -fx-opacity:0.8;");
        
        stackPane.getChildren().add(ap);

        screenDialog.doneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                close();
            }
        });
        stackPane.getChildren().add(screenDialog);
        addToPane(stackPane);
        initialize();
    }

    /**
     *
     * @return
     */
    public final T getDialog() {
        return screenDialog;
    }

    private void initialize() {
        setTopAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);
        setRightAnchor(this, 0.0);
        setBottomAnchor(this, 0.0);
    }

    /**
     *
     */
    public final void close() {
        if (getParent() instanceof Pane) {
            Pane parent = (Pane) getParent();
            parent.getChildren().remove(this);
            getDialog().setDone(false);
        }
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        //UNUSED
    }

}
