/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import org.symfound.builder.Scannable;
import org.symfound.controls.RunnableControl;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class FlipStack extends RunnableControl implements Scannable {

    private final StackPane stackPane = new StackPane();

    /**
     *
     */
    public FlipStack() {
        ScriptButton sb1 = new ScriptButton("YES");
        sb1.setText(sb1.getTitle());
        sb1.setOverrideStyle("-fx-background-color:-fx-green;\n"
                + "-fx-text-fill:-fx-light;\n"
                + "-fx-font-size:72pt;");
        sb1.setDisable(true);
        sb1.setSymStyle("word-1");
        getScreenControls().add(sb1);

        ScriptButton sb2 = new ScriptButton("NO");
        sb2.setText(sb2.getTitle());
        sb2.setOverrideStyle("-fx-background-color:-fx-red;\n"
                + "-fx-text-fill:-fx-light;\n"
                + "-fx-font-size:72pt;");
        sb2.setDisable(true);
        sb2.setSymStyle("word-2");
        getScreenControls().add(sb2);

        initialize();
    }

    /**
     *
     * @param screenControls
     */
    public FlipStack(List<RunnableControl> screenControls) {
        this.screenControls = screenControls;
        initialize();
    }

    /**
     *
     */
    public final void initialize() {
        stackPane.getChildren().addAll(screenControls);
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                scan();
                getUser().getTiming().scanTimeProperty().addListener(
                        (observeableValue1, oldValue1, newValue1) -> {
                            scan();
                        });

            }
        });

        setSelection(this);
        addToPane(stackPane);
    }

    /**
     *
     */
    @Override
    public final void scan() {
        getLoopedEvent().end();
        getLoopedEvent().setup(getUser().getTiming().getScanTime(), (ActionEvent e) -> {
            incrementIndex();
            if (getIndex() > getScreenControls().size() - 1) {
                resetIndex();
            }
            getCurrent().toFront();
        });
        getLoopedEvent().play();
    }

    @Override
    public void run() {
        getCurrent().run();
    }

    private List<RunnableControl> screenControls;

    private RunnableControl getCurrent() {
        return getScreenControls().get(getIndex());
    }

    private List<RunnableControl> getScreenControls() {
        if (screenControls == null) {
            screenControls = new ArrayList<>();
        }
        return screenControls;
    }

    private LoopedEvent loopedEvent = new LoopedEvent();

    private LoopedEvent getLoopedEvent() {
        if (loopedEvent == null) {
            loopedEvent = new LoopedEvent();
        }
        return loopedEvent;
    }

    private static Integer DEFAULT_VALUE = 0;
    private static Integer INCREMENT_SIZE = 1;
    private IntegerProperty index;

    private void incrementIndex() {
        setIndex(getIndex() + INCREMENT_SIZE);
    }

    private void resetIndex() {
        setIndex(DEFAULT_VALUE);
    }

    /**
     *
     * @param value
     */
    private void setIndex(Integer value) {
        indexProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    private Integer getIndex() {
        return indexProperty().getValue();
    }

    private IntegerProperty indexProperty() {
        if (index == null) {
            index = new SimpleIntegerProperty();
        }
        return index;
    }

}
