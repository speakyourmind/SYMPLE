/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class ScreenButton extends GenericButton{
    
    /**
     *
     */
    public static final String KEY = "Screen";
    public static final String DESCRIPTION ="Screen";
    
    
    public ScreenButton(String index) {
        super(index);
         initialize(index);
    }

    private void initialize(String index) {
        defaultTitle=index;
        setNavigatePostClick(Boolean.FALSE);
        configureTitle();
    }
    
    
    
}
