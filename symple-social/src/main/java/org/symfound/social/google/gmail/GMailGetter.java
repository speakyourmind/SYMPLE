/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.google.gmail;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 */
public class GMailGetter {

    /**
     *
     * @param message
     * @return
     */
    public String getContent(Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<MessagePart> parts = message.getPayload().getParts();
            getPlainTextFromMessageParts(parts, stringBuilder);
            byte[] bodyBytes = Base64.decodeBase64(stringBuilder.toString());
            String text = new String(bodyBytes, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            //  logger.error("UnsupportedEncoding: " + e.toString());
            return message.getSnippet();
        }
    }

    private void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder stringBuilder) {
        if (messageParts != null) {
            for (int i = 0; i < messageParts.size(); i++) {
                MessagePart messagePart = messageParts.get(i);
                if (messagePart != null) {
                    if (messagePart.getMimeType().equals("text/plain")) {
                        stringBuilder.append(messagePart.getBody().getData());
                    }

                    if (messagePart.getParts() != null) {
                        getPlainTextFromMessageParts(messagePart.getParts(), stringBuilder);
                    }
                }
            }
        }
    }
}
