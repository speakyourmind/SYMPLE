/*
 * Copyright (C) 2014 SpeakYourMind Foundation
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

import org.symfound.comm.mail.MailSender;
import org.symfound.controls.RunnableControl;

/**
 *
 * @author Javed Gangjee
 */
public class MailButton extends RunnableControl {

    /**
     *
     */
    public MailButton() {
        super("mail-button");
    }

    /**
     *
     */
    @Override
    public void run() {
        MailSender mailSender = new MailSender(getUser().getSocial().getMailAccount());
        mailSender.send("javed@speakyourmindfoundation.org", "Testing", getUser().getTyping().getActiveText());
    }

}
