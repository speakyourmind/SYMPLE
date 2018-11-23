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

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.main.builder.UI;
import org.symfound.tools.animation.Animated;
import org.symfound.tools.animation.NodeAnimator;

/**
 *
 * @author Javed Gangjee
 */
public class AnimatedPane extends AnchorPane implements Usable, Animated {

    /**
     *
     */
    public NodeAnimator nodeAnimator;
    private final FullSession session;
    private final User user;

    /**
     *
     */
    public AnimatedPane() {
        super();
        session = Main.getSession();
        user = session.getUser();
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
     * @param location
     */
    public void setBackgroundStyle(String location) {
        setStyle("-fx-background-image: url(\"" + location + "\");");
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

    /**
     * Get the stage that the provided node belongs to.
     *
     * @return
     */
    public UI getParentUI() {
        Scene scene = getScene();
        Window window = scene.getWindow();
        return (UI) window;
    }
}
