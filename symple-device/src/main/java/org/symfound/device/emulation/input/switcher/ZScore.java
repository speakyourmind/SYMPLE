/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import java.awt.Point;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Javed Gangjee
 */
public class ZScore {

    private DoubleProperty magnitude;

    /**
     *
     * @param value
     */
    public void setMagnitude(Double value) {
        magnitudeProperty().setValue(value);
    }

    /**
     *
     * @param tau
     * @param point
     */
    public void updateMagnitude(Double tau, Point point) {
        Double zScoreX = calculateZScoreDimensionX(tau, point.getX());
        Double zScoreY = calculateZScoreDimensionY(tau, point.getY());
        setTheta(zScoreY, zScoreX);
        setMagnitude(zScoreY / Math.sin(getTheta()));
    }

    private Double meanX = 0.0;
    private Double varianceX = 0.0;
    private Double standardDeviationX = 0.0;
    private Double deviationFactorX = 0.0;

    private Double calculateZScoreDimensionX(Double tau, Double referenceValue) {
        meanX = (tau - 1) / tau * meanX + 1 / tau * referenceValue; // recursive mean
        varianceX = (tau - 1) / tau * Math.pow(standardDeviationX, 2) + 1 / tau * Math.pow((referenceValue - meanX), 2); // recursive variance
        standardDeviationX = Math.sqrt(varianceX);
        if (standardDeviationX < 0.01) {
            deviationFactorX = 0.0;
        } else {
            deviationFactorX = 1 / standardDeviationX;
        }
        Double scoreX = (referenceValue - meanX) * deviationFactorX; // z-scored roiVal

        return scoreX;
    }

    private Double meanY = 0.0;
    private Double varianceY = 0.0;
    private Double standardDeviationY = 0.0;
    private Double deviationFactorY = 0.0;

    private Double calculateZScoreDimensionY(Double tau, Double referenceValue) {
        meanY = (tau - 1) / tau * meanY + 1 / tau * referenceValue; // recursive mean
        varianceY = (tau - 1) / tau * Math.pow(standardDeviationY, 2) + 1 / tau * Math.pow((referenceValue - meanY), 2); // recursive variance
        standardDeviationY = Math.sqrt(varianceY);
        if (standardDeviationY < 0.01) {
            deviationFactorY = 0.0;
        } else {
            deviationFactorY = 1 / standardDeviationY;
        }
        Double scoreY = (referenceValue - meanY) * deviationFactorY; // z-scored roiVal
        return scoreY;
    }

    /**
     *
     * @return
     */
    public Double getMagnitude() {
        return magnitudeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty magnitudeProperty() {
        if (magnitude == null) {
            magnitude = new SimpleDoubleProperty(0.0);
        }
        return magnitude;
    }

    private DoubleProperty theta;

    /**
     *
     * @param value
     */
    public void setTheta(Double value) {
        thetaProperty().setValue(value);
    }

    /**
     *
     * @param y
     * @param x
     */
    public void setTheta(Double y, Double x) {
        double thetaVal = Math.atan2(y, x);
        if (thetaVal < 0) {
            thetaVal += 2 * Math.PI;
        }
        setTheta(thetaVal);
    }

    /**
     *
     * @return
     */
    public Double getTheta() {
        return thetaProperty().getValue();
    }

    /**
     *
     * @param actual
     * @param target
     * @param sensitivity
     * @return
     */
    public Boolean isTheta(Double actual, Double target, Double sensitivity) {
        Double min = target - sensitivity;
        Double max = target + sensitivity;
        if (actual < sensitivity) {
            actual += 2 * Math.PI;
        }
        Boolean isDirection = actual >= min && actual < max;
        return isDirection;
    }

    /**
     *
     * @return
     */
    public DoubleProperty thetaProperty() {
        if (theta == null) {
            theta = new SimpleDoubleProperty();
        }
        return theta;
    }

}
