/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.prediction.phrasefinder;

import java.util.Objects;

/**
 * Token represents a single token (word, punctuation mark, etc.) as part of a
 * phrase.
 */
public final class Token {

    /**
     * Tag is an enum type that denotes the role of a token with respect to the
     * query.
     */
    public static enum Tag {
        /**
         * The token was given as part of the query string.
         */
        GIVEN,
        /**
         * The token has been inserted either by an application of the <b>?</b>
         * or the <b>*</b>
         * operator.
         */
        INSERTED,
        /**
         * The token was given as the left- or right-hand side of the <b>/</b>
         * operator.
         */
        ALTERNATIVE,
        /**
         * The token has been completed by an application of the <b>+</b>
         * operator.
         */
        COMPLETED;

        /**
         * Returns the enum constant of this type with the specified ordinal
         * number.
         *
         * @param ordinal the ordinal number of the enum constant to be
         * returned.
         * @return the enum constant with the specified ordinal number
         */
        public static Tag fromOrdinal(int ordinal) {
            if (ordinal >= 0 && ordinal < values().length) {
                return values()[ordinal];
            }
            throw new IllegalArgumentException("Invalid ordinal number");
        }
    }

    private final Tag tag;

    /**
     *
     */
    public final String text;

    /**
     *
     * @param tag
     * @param text
     */
    protected Token(Tag tag, String text) {
        this.tag = Objects.requireNonNull(tag);
        this.text = Objects.requireNonNull(text);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag.hashCode();
        result = prime * result + text.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Token other = (Token) obj;
        if (tag != other.tag) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the token's tag.
     *
     * @return
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Returns the token's text.
     *
     * @return
     */
    public String getText() {
        return text;
    }
}
