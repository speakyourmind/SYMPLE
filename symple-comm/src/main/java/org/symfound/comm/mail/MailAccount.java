/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.mail;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee
 */
public class MailAccount {

    /**
     *
     */
    public String initUsername;

    /**
     *
     */
    public String initPassword;

    /**
     *
     */
    public String initIncoming;

    /**
     *
     */
    public String initHost;

    /**
     *
     */
    public Integer initPort;

    /**
     *
     * @param username
     * @param password
     * @param incoming
     * @param outgoing
     * @param port
     */
    public MailAccount(String username, String password, String incoming, String outgoing, Integer port) {
        this.initUsername = username;
        this.initPassword = password;
        this.initIncoming = incoming;
        this.initHost = outgoing;
        this.initPort = port;
    }
    private StringProperty username;

    /**
     *
     * @param value
     */
    public void setUsername(String value) {
        usernameProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return usernameProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(initUsername);
        }
        return username;
    }

    private StringProperty password;

    /**
     *
     * @param value
     */
    public void setPassword(String value) {
        passwordProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return passwordProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty passwordProperty() {
        if (password == null) {
            password = new SimpleStringProperty(initPassword);
        }
        return password;
    }

    private StringProperty incoming;

    /**
     *
     * @param value
     */
    public void setIncoming(String value) {
        incomingProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getIncoming() {
        return incomingProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty incomingProperty() {
        if (incoming == null) {
            incoming = new SimpleStringProperty(initIncoming);
        }
        return incoming;
    }

    private StringProperty host;

    /**
     *
     * @param value
     */
    public void setHost(String value) {
        hostProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return hostProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty hostProperty() {
        if (host == null) {
            host = new SimpleStringProperty(initHost);
        }
        return host;
    }

    private IntegerProperty port;

    /**
     *
     * @param value
     */
    public void setPort(Integer value) {
        portProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Integer getPort() {
        return portProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty portProperty() {
        if (port == null) {
            port = new SimpleIntegerProperty(initPort);
        }
        return port;
    }
}
