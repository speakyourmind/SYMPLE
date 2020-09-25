package org.symfound.controls.user.media.music;

import java.io.IOException;
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.audio.music.song.Song;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.AnimatedButton;
import static org.symfound.tools.animation.NodeAnimator.*;

/**
 *
 * @author Javed Gangjee
 */
public class AlbumArt extends RunnableControl {

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(AlbumArt.class.getName());

    /**
     *
     */
    public static final String CSS_CLASS = "music-album-art";

    /**
     *
     */
    public AlbumArt() {
        super(CSS_CLASS);
        initialize();
    }

    /**
     *
     */
    public final void initialize() {
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
        getStyleClass().add(CSS_PATH);
        getStyleClass().add(CSS_CLASS);
    }

    /**
     *
     */
    public void reset() {
        setStyle("");
    }

    /**
     *
     * @param song
     */
    public void set(Song song) {
        animate().startFade(FADE_OUT_TIME, MAX_OPACITY, MIN_OPACITY);
        animate().setOnFadeFinished((ActionEvent e) -> {
            reset();
            try {
                String albumArtStyle = song.getAlbumArtStyle("no-repeat", "center", "contain");
                if (albumArtStyle != null) {
                    setSymStyle(albumArtStyle);
                } else {
                    reset();
                }
            } catch (IOException ex) {
                LOGGER.warn(ex);
            }
            animate().startFade(FADE_IN_TIME, MIN_OPACITY, MAX_OPACITY);

        });

    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
    }


}
