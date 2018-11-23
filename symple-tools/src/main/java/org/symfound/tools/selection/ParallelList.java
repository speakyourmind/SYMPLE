/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.tools.selection;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 * @param <K>
 * @param <V>
 */
public class ParallelList<K extends Object, V extends Object> {

    private List<K> first;

    /**
     *
     * @return
     */
    public List<K> getFirstList() {
        if (first == null) {
            first = new ArrayList<>();
        }
        return first;
    }

    private List<V> second;

    /**
     *
     * @return
     */
    public List<V> getSecondList() {
        if (second == null) {
            second = new ArrayList<>();
        }
        return second;
    }

    /**
     *
     * @return
     */
    public Integer size() {
        return getFirstList().size();
    }

    /**
     *
     * @param firstValue
     * @param secondValue
     */
    public void put(K firstValue, V secondValue) {
        getFirstList().add(firstValue);
        getSecondList().add(secondValue);
    }

    /**
     *
     * @param firstValue
     */
    public void remove(K firstValue) {
        Integer index = getFirstList().indexOf(firstValue);
        remove(index);
    }



    /**
     *
     * @param index
     */
    public void remove(int index) {
        K removeFirst = getFirstList().remove(index);
        V removeSecond = getSecondList().remove(index);
    }

    /**
     *
     * @return
     */
    public String asString() {
        String storedValue = "";
        for (int i = 0; i < size(); i++) {
            storedValue = storedValue + getFirstList().get(i) + "=" + getSecondList().get(i) + ",";
        }
        return storedValue;
    }

}
