/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.scene.Node;
import org.symfound.controls.RunnableControl;
import static org.symfound.controls.user.BuildableGrid.LAUNCH_TIME_INITIAL;
import static org.symfound.controls.user.BuildableGrid.LAUNCH_TIME_PER_CONTROL;
import org.symfound.tools.animation.Animated;
import org.symfound.tools.animation.NodeAnimator;

/**
 *
 * @author Javed Gangjee
 */
public class AnimatedGrid extends CommonGrid implements Animated {

    /**
     *
     */
    public static final double LAUNCH_TIME_PER_CONTROL = 0.03;

    /**
     *
     */
    public static final double LAUNCH_TIME_INITIAL = 0.1;

    /**
     *
     */
    public NodeAnimator nodeAnimator;

    /**
     *
     * @return
     */
    @Override
    public NodeAnimator animate() {
        if (nodeAnimator == null) {
            nodeAnimator = new NodeAnimator(this);
            getSession().mutexProperty().bindBidirectional(nodeAnimator.playingProperty());
        }
        return nodeAnimator;
    }


    /**
     *
     */
    public void hideAnimation() {
        Double time = LAUNCH_TIME_INITIAL;
        setVisibleAll(true);
        for (int i = 0; i < getSpecRows(); i++) {
            for (int j = 0; j < getSpecColumns(); j++) {
                Node child = get(i, j);
                if (child != null) {
                    time += LAUNCH_TIME_PER_CONTROL;
                    if (child instanceof RunnableControl) {
                        RunnableControl control = (RunnableControl) child;
                        control.setVisible(true);
                        control.animate().startScale(time, NodeAnimator.MAX_OPACITY, NodeAnimator.MIN_OPACITY);
                    }
                }
            }
        }
    }
}
