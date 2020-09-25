/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
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
package org.symfound.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee
 */
public final class Result extends ModeIterator<String> {

    /**
     *
     */
    public static final String SUCCESS_TITLE = "SUCCESS";

    /**
     *
     */
    public static final String WARNING_TITLE = "WARNING";

    /**
     *
     */
    public static final String CRITICAL_TITLE = "CRITICAL";

    /**
     *
     */
    public static final String SUCCESS_CONSENSUS = "Click play to try next level";

    /**
     *
     */
    public static final String WARNING_CONSENSUS = "Recalibrate and try again";

    /**
     *
     */
    public static final String CRITICAL_CONSENSUS = "Level found";

    /**
     *
     */
    public static final Map<String, String> CONSENSUS_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put(SUCCESS_TITLE, SUCCESS_CONSENSUS);
        map.put(WARNING_TITLE, WARNING_CONSENSUS);
        map.put(CRITICAL_TITLE, CRITICAL_CONSENSUS);
        CONSENSUS_MAP = Collections.unmodifiableMap(map);
    }

    /**
     *
     */
    public Map<String, String> mapConsensus;

    /**
     *
     */
    public static final String SUCCESS_COLOUR = "27AE60";

    /**
     *
     */
    public static final String WARNING_COLOUR = "E67E22";

    /**
     *
     */
    public static final String CRITICAL_COLOUR = "E74C3C";

    /**
     *
     */
    public static final Map<String, String> COLOUR_MAP;

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put(SUCCESS_TITLE, SUCCESS_COLOUR);
        map.put(WARNING_TITLE, WARNING_COLOUR);
        map.put(CRITICAL_TITLE, CRITICAL_COLOUR);
        COLOUR_MAP = Collections.unmodifiableMap(map);
    }

    /**
     *
     */
    public Map<String, String> mapColour;
    private StringProperty consensus;
    private ObjectProperty<Background> background;

    /**
     *
     */
    public static final List<String> DEFAULT_TYPES = Arrays.asList(SUCCESS_TITLE, WARNING_TITLE, CRITICAL_TITLE);

    /**
     *
     */
    public Result() {
        this(DEFAULT_TYPES, CONSENSUS_MAP, COLOUR_MAP);
    }

    // TO DO: Change the paramaters to 3 strings and build the HashMaps out of them.

    /**
     *
     * @param defaultTypes
     * @param mapConsensus
     * @param mapColour
     */
    public Result(List<String> defaultTypes, Map<String, String> mapConsensus,
            Map<String, String> mapColour) {
        this.types = defaultTypes;
        this.mapConsensus = mapConsensus;
        this.mapColour = mapColour;
        configureListener();
    }

    /**
     *
     */
    public void configureListener() {
        modeProperty().addListener((observable, oldValue, newValue) -> {
            setConsensus(mapConsensus.get(newValue));
            setBackground(new Background(
                    new BackgroundFill(Color.valueOf(mapColour.get(newValue)), CornerRadii.EMPTY, Insets.EMPTY)));
        });

    }

    /**
     *
     * @param value
     */
    public void setConsensus(String value) {
        consensusProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getConsensus() {
        return consensusProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty consensusProperty() {
        if (consensus == null) {
            consensus = new SimpleStringProperty(CONSENSUS_MAP.get(SUCCESS_TITLE));
        }
        return consensus;
    }

    /**
     *
     * @param value
     */
    public void setBackground(Background value) {
        backgroundProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Background getBackground() {
        return backgroundProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Background> backgroundProperty() {
        if (background == null) {
            background = new SimpleObjectProperty<>();
        }
        return background;
    }

    /**
     *
     * @return
     */
    public String getColour() {
        return mapColour.get(get());
    }

}
