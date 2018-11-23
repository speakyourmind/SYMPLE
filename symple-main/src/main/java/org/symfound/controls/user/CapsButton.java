package org.symfound.controls.user;

import java.io.IOException;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.controls.RunnableControl;

/**
 * Toggles the <code>isUpperCase</code> to change the case being typed
 *
 * @author Javed Gangjee
 */
public final class CapsButton extends RunnableControl {

    /**
     *
     * @throws IOException
     */
    public CapsButton() throws IOException {
        super("caps-button");
        setCaps();

    }

    /**
     *
     */
    @Override
    public void run() {
        final Typing typing = getUser().getTyping();
        typing.setUpperCase(!typing.isUpperCase());
        setCaps();
    }

    /**
     *
     */
    public void setCaps() {
        String strStyle = "";
        if (getUser().getTyping().isUpperCase()) {
            strStyle = "-fx-background-color:" + getUser().getInteraction().getSelectionColour()
                    + ";-fx-text-fill: #333232;";
        }
        // Set button selection animation
        getPrimaryControl().setStyle(strStyle);
    }

}
