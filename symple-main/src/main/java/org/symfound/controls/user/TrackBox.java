/*
 * Copyright (C) 2015
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.controls.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import org.symfound.builder.user.feature.Eye;
import org.symfound.controls.ScreenControl;

/**
 *
 * @author Javed Gangjee
 */
public class TrackBox extends ScreenControl<AnimatedButton> {

    /**
     *
     */
    public static final double PUPIL_SCALE_FACTOR = 0.1;

    /**
     *
     */
    public static final String ADJUST_INSTRUCTION = "Adjust the setup to get your eyes in the centre.";

    /**
     *
     */
    public static final String READY_INSTRUCTION = "Ready. Starting test...";

    /**
     *
     */
    public static final String CONNECT_INSTRUCTION = "Eye Tracker not detected. Please check your connection to continue";
    private AnimatedLabel label;

    private static final Double LOWER_BOUND_X = 0.1;
    private static final Double UPPER_BOUND_X = 0.9;
    private static final Side SIDE_X = Side.BOTTOM;
    private static final Double LOWER_BOUND_Y = 0.5;
    private static final Double UPPER_BOUND_Y = 0.7;
    private static final Side SIDE_Y = Side.LEFT;

    private ScatterChart.Series<Number, Number> series;
    private BubbleChart<Number, Number> trackBoxView;

    /**
     *
     */
    public TrackBox() {
        super();
    }

    /**
     *
     */
    public void initialize() {
        NumberAxis xAxis = configureAxis(LOWER_BOUND_X, UPPER_BOUND_X, SIDE_X);
        NumberAxis yAxis = configureAxis(LOWER_BOUND_Y, UPPER_BOUND_Y, SIDE_Y);
        trackBoxView = new BubbleChart<>(xAxis, yAxis);
        trackBoxView.getStylesheets().add(CSS_PATH);
        trackBoxView.setAlternativeRowFillVisible(false);
        trackBoxView.setAlternativeColumnFillVisible(false);
        trackBoxView.setAnimated(false);
        trackBoxView.setHorizontalGridLinesVisible(false);
        trackBoxView.setHorizontalZeroLineVisible(false);
        trackBoxView.setLegendVisible(false);
        trackBoxView.setVerticalGridLinesVisible(false);
        trackBoxView.setVerticalZeroLineVisible(false);
        final ObservableList<XYChart.Series<Number, Number>> trackBoxData
                = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
        trackBoxView.setData(trackBoxData);
        addToPane(trackBoxView);

        label = new AnimatedLabel(ADJUST_INSTRUCTION);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.POSITIVE_INFINITY);
        label.toFront();
        AnchorPane.setBottomAnchor(label, 0.0);
        getChildren().add(label);
        getStylesheets().add(CSS_PATH);

        show();
    }

    /**
     *
     * @param lowerBound
     * @param upperBound
     * @param side
     * @return
     */
    public NumberAxis configureAxis(Double lowerBound, Double upperBound, Side side) {
        NumberAxis axis = new NumberAxis();
        axis.setAutoRanging(false);
        axis.setMinorTickLength(1.0);
        axis.setMinorTickCount(0);
        axis.setMinorTickVisible(false);
        axis.setTickLabelsVisible(false);
        axis.setTickMarkVisible(false);
        axis.setTickUnit(0.1);
        axis.setSide(side);
        axis.setLowerBound(0.0);
        axis.setUpperBound(1.0);
        return axis;
    }

    /**
     *
     */
    public void hide() {
        if (isVisible()) {
            setVisible(false);
            trackBoxView.toBack();
            label.toBack();
        }
    }

    /**
     *
     */
    public void show() {
        if (!isVisible()) {
            setVisible(true);
            trackBoxView.toFront();
            label.toFront();
        }
    }

    /**
     *
     * @param eye
     */
    private void addEye(Eye eye) {
        if (eye.isVisible()) {
            Double x = eye.getPosX();
            Double y = eye.getPosY();
            Double scale = PUPIL_SCALE_FACTOR * (eye.getSize() / Eye.MAX_PUPIL_SIZE);
            XYChart.Data<Number, Number> data = new BubbleChart.Data<>(x, y, scale);
            series.getData().add(data);
        }
    }

    /**
     *
     */
    public void updateEyes() {
        if (true) {
            if (series != null) {
                trackBoxView.getData().remove(series);
            }
            series = new BubbleChart.Series<>();

            addEye(getUser().getPhysical().getLeftEye());
            addEye(getUser().getPhysical().getRightEye());
            trackBoxView.getData().add(series);
        } else {
            //
        }

        label.toFront();
    }

    /**
     *
     * @return
     */
    public Boolean isWithinBounds() {
        Boolean isWithinBounds = false;

        if (getUser().getPhysical().getLeftEye().getPosX() < UPPER_BOUND_X
                && getUser().getPhysical().getLeftEye().getPosY() < UPPER_BOUND_Y
                && getUser().getPhysical().getLeftEye().getPosX() > LOWER_BOUND_X
                && getUser().getPhysical().getLeftEye().getPosY() > LOWER_BOUND_Y
                && getUser().getPhysical().getRightEye().getPosX() < UPPER_BOUND_X
                && getUser().getPhysical().getRightEye().getPosY() < UPPER_BOUND_Y
                && getUser().getPhysical().getRightEye().getPosX() > LOWER_BOUND_X
                && getUser().getPhysical().getRightEye().getPosY() > LOWER_BOUND_Y) {
            isWithinBounds = true;
        } else {
            // set Colour to Red;
        }
        return isWithinBounds;
    }

    /**
     *
     * @param text
     */
    public void setStatus(String text) {
        label.setText(text);
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton();
        load(primary);
    }

}
