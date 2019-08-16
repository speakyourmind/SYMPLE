/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.type.picto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public abstract class PictoControl extends AppableControl{
    
    public PictoControl(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
    }
    
    
    /**
     *
     */
    public PictoArea picto;

    /**
     *
     * @return
     */
    public final PictoArea getPictoArea() {
        if (picto == null) {
            if (!getPictoID().isEmpty()) {
                final String pictoHash = "#" + getPictoID();
                picto = (PictoArea) getScene().lookup(pictoHash);
            }
        }
        return picto;
    }
    private StringProperty pictoID;

    /**
     *
     * @param value
     */
    public void setPictoID(String value) {
        pictoIDProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getPictoID() {
        return pictoIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoIDProperty() {
        if (pictoID == null) {
            pictoID = new SimpleStringProperty("gridPictoArea");
        }
        return pictoID;
    }
    
    
    
}
