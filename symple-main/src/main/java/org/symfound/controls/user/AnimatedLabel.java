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
package org.symfound.controls.user;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.symfound.builder.user.User;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.tools.animation.Animated;
import org.symfound.tools.animation.NodeAnimator;

/**
 *
 * @author Javed Gangjee
 */
public class AnimatedLabel extends Label implements Animated {

    /**
     *
     */
    public NodeAnimator nodeAnimator;

    private FullSession session;
   // private User user;

    /**
     *
     */
    public AnimatedLabel() {
        super();
        session = Main.getSession();
     //   user = session.getUser();
    }

    /**
     *
     * @param text
     */
    public AnimatedLabel(String text) {
        super(text);
    }

    /**
     *
     * @return
     */
    public FullSession getSession() {
        return session;
    }

    /**
     *
     * @return
     */
    @Override
    public NodeAnimator animate() {
        if (nodeAnimator == null) {
            nodeAnimator = new NodeAnimator(this);
            nodeAnimator.playingProperty().addListener((observable, oldValue, newValue) -> {
                getSession().setMutex(newValue);
            });
        }
        return nodeAnimator;
    }

    /**
     *
     */
    public final void removeFromParent() {
        if (getParent() instanceof Pane) {
            Pane parent = (Pane) getParent();
            parent.getChildren().remove(this);
        }
    }

}
