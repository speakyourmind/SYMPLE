/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.builder;

import java.util.List;
import org.symfound.builder.Builder;
import org.symfound.builder.component.Stacker;
import org.symfound.builder.loader.UIPath;

/**
 *
 * @author Javed Gangjee
 */
public class StackedUI extends UI {

    /**
     *
     */
    public List<UIPath> files;

    /**
     *
     * @param files
     */
    public StackedUI(List<UIPath> files) {
        super();
        this.files = files;
    }

    /**
     *
     * @param builder
     */
    @Override
    public void build(Builder builder) {
        getStack().build(files, builder);
        CommonPane basePane = new CommonPane(getStack(), false, false);
        scene = create(basePane, false);
        getStack().builtProperty().bindBidirectional(builtProperty());
    }

    private Stacker stack;

    /**
     *
     * @return
     */
    public Stacker getStack() {
        if (stack == null) {
            stack = new Stacker();
        }
        return stack;
    }
}
