/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.system.dialog.ScreenDialog;
import org.symfound.controls.system.dialog.ScreenPopup;

/**
 *
 * @author Javed Gangjee
 */
public abstract class ConfirmableControl extends RunnableControl {

    /**
     *
     */
    public Boolean continueMethod = false;

    /**
     *
     */
    public Pane settingsPane;

    /**
     *
     */
    public String initTitleText = "Are you sure?";

    /**
     *
     */
    public String initCaptionText = "";

    /**
     *
     */
    public OKCancelDialog settingsDialog;

    /**
     *
     * @param CSSClass
     */
    public ConfirmableControl(String CSSClass) {
        super(CSSClass);
    }

    /**
     *
     */
    @Override
    public void execute() {
        if (isConfirmable()) {
            if (getSession().isBuilt()) {
                Pane errorPane = (Pane) getScene().lookup("#apMain");
                //  getParentPane().getChildren().add(getPopup(getDialog()));
                errorPane.getChildren().add(getPopup(getDialog()));
                final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
                getDialog().animate().startScale(selectionTime, 0.8, 1.0);
            }
        } else {
            super.execute();
        }
    }

    /**
     *
     * @return
     */
    public Pane getParentPane() {
        if (settingsPane == null) {
            if (!getPane().isEmpty()) {
                // Or it can lookup another pane in the scene.
                settingsPane = (Pane) getScene().lookup("#" + getPane());
            } else {
                settingsPane = (Pane) getParent();
            }
        }
        return settingsPane;
    }

    /**
     *
     * @return
     */
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = new OKCancelDialog("Are you sure?", "", "Yes", "No") {
                @Override
                public void onOk() {
                    //  getPrimaryControl().setOnFinished(srcText, srcStyle);
                    Platform.runLater(ConfirmableControl.this);
                }

                @Override
                public void onCancel() {

                }
            };
            settingsDialog.buildDialog();
            settingsDialog.titleTextProperty().bindBidirectional(titleTextProperty());
            settingsDialog.captionTextProperty().bindBidirectional(captionTextProperty());
            settingsDialog.okButton.textProperty().bindBidirectional(okTextProperty());
            settingsDialog.cancelButton.textProperty().bindBidirectional(cancelTextProperty());

        }
        return settingsDialog;
    }

    /**
     *
     */
    public ScreenPopup<ScreenDialog> popup;

    public ScreenPopup<ScreenDialog> getPopup(ScreenDialog dialog) {
        if (popup == null) {
            popup = new ScreenPopup<>(dialog);
        }
        return popup;
    }

    private BooleanProperty confirmable;

    /**
     *
     * @param value
     */
    public void setConfirmable(Boolean value) {
        confirmableProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isConfirmable() {
        return confirmableProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty confirmableProperty() {
        if (confirmable == null) {
            confirmable = new SimpleBooleanProperty(Boolean.TRUE);
        }
        return confirmable;
    }

    private StringProperty pane;

    /**
     *
     * @param value
     */
    public void setPane(String value) {
        paneProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getPane() {
        return paneProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty paneProperty() {
        if (pane == null) {
            pane = new SimpleStringProperty("");
        }
        return pane;
    }

    /**
     *
     */
    public StringProperty titleText;

    /**
     *
     * @return
     */
    public String getTitleText() {
        return titleTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setTitleText(String value) {
        titleTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty titleTextProperty() {
        if (titleText == null) {
            titleText = new SimpleStringProperty(initTitleText);
        }
        return titleText;
    }

    /**
     *
     */
    public StringProperty captionText;

    /**
     *
     * @return
     */
    public String getCaptionText() {
        return captionTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setCaptionText(String value) {
        captionTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty captionTextProperty() {
        if (captionText == null) {
            captionText = new SimpleStringProperty(initCaptionText);
        }
        return captionText;
    }
    private StringProperty okText;

    /**
     *
     * @param value
     */
    public void setOkText(String value) {
        okTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getOkText() {
        return okTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty okTextProperty() {
        if (okText == null) {
            okText = new SimpleStringProperty("Yes");
        }
        return okText;
    }
    private StringProperty cancelText;

    /**
     *
     * @param value
     */
    public void setCancelText(String value) {
        cancelTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getCancelText() {
        return cancelTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty cancelTextProperty() {
        if (cancelText == null) {
            cancelText = new SimpleStringProperty("No");
        }
        return cancelText;
    }

}
