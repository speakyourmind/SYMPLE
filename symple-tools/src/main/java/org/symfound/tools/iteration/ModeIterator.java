/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

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
package org.symfound.tools.iteration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public class ModeIterator<T> {

    /**
     *
     */
    public List<T> types;

    

    /**
     *
     */
    public ModeIterator() {

    }

    /**
     *
     * @param types
     */
    public ModeIterator(List<T> types) {
        this.types = new ArrayList<>(types);
    }

    /**
     *
     * @return
     */
    public int size() {
        return getTypes().size();
    }

    /**
     *
     */
    public Integer index=0;
    /**
     *
     * @return
     */
    public int getCurrentIndex() {
        return index;
    }

    private ObjectProperty<T> mode;
    /**
     *
     * @return
     */
    public T get() {
        return modeProperty().getValue();
    }

    /**
     *
     * @param mode
     */
    public void set(T mode) {
        resetIndex();
        if (mode != null) {
            while (!mode.equals(getTypes().get(index)) && getCurrentIndex() != size()) {
                index++;
            }
            set(getCurrentIndex());
        } else {
            throw new NullPointerException("ModeChooser cannot be set with null String");
        }
    }

    /**
     *
     */
    public void resetIndex() {
        index = 0;
    }

    /**
     *
     * @param index
     */
    public void set(Integer index) {
        this.index = index;
        modeProperty().setValue(getTypes().get(index));
    }

    /**
     *
     * @return
     */
    public ObjectProperty<T> modeProperty() {
        if (mode == null) {
            mode = new SimpleObjectProperty<>(getTypes().get(index));
        }
        return mode;
    }

    /**
     *
     * @param mode
     * @return
     */
    public Boolean containsMode(T mode) {
        Boolean modeExists = false;
        for (T type : getTypes()) {
            if (type.equals(mode)) {
                modeExists = true;
            }
        }
        return modeExists;
    }

    /**
     *
     * @param mode
     */
    public void addMode(T mode) {
        getTypes().add(mode);
    }

    /**
     *
     * @param mode
     */
    public void removeMode(T mode) {
        getTypes().remove(mode);
    }

    /**
     *
     * @param value
     */
    public void setTypes(List<T> value){
        types = new ArrayList<>(value);
        resetIndex();
        modeProperty().setValue(getTypes().get(getCurrentIndex()));
    }
    /**
     *
     * @return
     */
    public List<T> getTypes() {
        if (types == null){
            types = new ArrayList<>();
        }
        return types;
    }

    /**
     *
     */
    public void next() {
        if (!hasNext()) {
            set(0);        
        } else {
            set(getCurrentIndex() + 1);
        }
    }

    /**
     *
     */
    public void previous() {
        if (!hasPrevious()) {
            set(getTypes().size() - 1);
        } else {
            set(getCurrentIndex() - 1);
        }
    }

    /**
     *
     * @return
     */
    public Boolean hasPrevious() {
        return (getCurrentIndex() > 0);
    }

    /**
     *
     * @return
     */
    public Boolean hasNext() {
        return (getCurrentIndex() < getTypes().size() - 1);
    }

    /**
     *
     */
    public void shuffle() {
        Collections.shuffle(getTypes());
    }
}
