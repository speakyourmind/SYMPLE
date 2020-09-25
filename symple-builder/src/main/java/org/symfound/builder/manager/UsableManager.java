/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.manager;

import org.symfound.builder.manager.Manager;
import java.util.List;
import java.util.Map;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class UsableManager<T> extends Manager<T> implements Usable {

    /**
     *
     */
    public User user;

    /**
     *
     * @param user
     */
    public UsableManager(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     *
     */
    public Map<String, T> map;

    /**
     *
     * @param user
     * @return
     */
    public abstract Map<String, T> buildMap(User user);

    /**
     *
     * @return
     */
    public Map<String, T> getMap() {
        if (map == null) {
            map = buildMap(user);
        }
        return map;
    }

    /**
     *
     * @param name
     * @return
     */
    public T get(String name) {
        return getMap().get(name);
    }

    /**
     *
     * @param name
     */
    public void setCurrent(String name) {
        setCurrent(get(name));
    }

    // Iterator Methods
    private ModeIterator<String> mode;

    /**
     *
     * @return
     */
    public ModeIterator<String> getIterator() {
        if (mode == null) {
            mode = new ModeIterator<>(getNames());
            mode.set(getInitName());
        }
        return mode;
    }

    /**
     *
     * @return
     */
    public abstract String getInitName();

    /**
     *
     * @return
     */
    public abstract List<String> getNames();
}
