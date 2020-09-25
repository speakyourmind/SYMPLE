/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.tools.iteration;

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

    public void setFirstList(List<K> value) {
        first = value;
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

    public void setSecondList(List<V> value) {
        second = value;
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
    public void add(K firstValue, V secondValue) {
        getFirstList().add(firstValue);
        getSecondList().add(secondValue);

    }

    public void add(Integer index, K firstValue, V secondValue) {
        getFirstList().add(index, firstValue);
        getSecondList().add(index, secondValue);
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
