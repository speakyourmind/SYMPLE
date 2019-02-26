// This file is part of PhraseFinder. http://phrasefinder.io
//
// Copyright (C) 2016-2018 Martin Trenkmann
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.symfound.prediction.phrasefinder;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Phrase represents an n-gram from the dataset. For this reason, objects of this class cannot be
 * constructed by the user (there are no public constructors) and objects returned from a search
 * request are immutable (there are no public setter methods). Client code that needs mutable phrase
 * objects is required to introduce their own Phrase class.
 */
public class Phrase {

    /**
     *
     */
    public Token[] tokens;

    /**
     *
     */
    protected long matchCount;

    /**
     *
     */
    protected int volumeCount;

    /**
     *
     */
    protected int firstYear;

    /**
     *
     */
    protected int lastYear;

    /**
     *
     */
    protected double score;

    /**
     *
     */
    protected long id;

  private static final Token[] EMPTY_TOKENS = new Token[0];

    /**
     *
     */
    protected Phrase() {
    this.tokens = EMPTY_TOKENS;
  }

  private static final Phrase EMPTY_INSTANCE = new Phrase();

  /**
   * Returns an empty phrase with a zero-size token array and all metadata set to zero. The empty
   * phrase can be used instead of {@code null} to represent the absence of a phrase and/or to
   * reduce null checks in client code.
     * @return 
   */
  public static Phrase emptyInstance() {
    return EMPTY_INSTANCE;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null) {
        return false;
    }
    if (!(obj instanceof Phrase)) {
        return false;
    }
    Phrase other = (Phrase) obj;
    // Since instances of this class are immutable and can only be created by this library and not
    // by the user, it is guaranteed that the id of a phrase sent by the server is always unique.
    // Hence, it is not necessary to compare the actual tokens.
    return id != other.id;
  }

  /**
   * Returns a whitespace-delimited concatenation of the token's text.
   * 
     * @return 
   * @see Token#getText()
   */
  @Override
  public String toString() {
    return Arrays.stream(tokens).map(t -> t.text).collect(Collectors.joining(" "));
  }

  /**
   * Returns the phrase's tokens.
     * @return 
   */
  public Token[] getTokens() {
    return tokens;
  }

  /**
   * Returns the phrase's match count, also called its absolute frequency.
     * @return 
   */
  public long getMatchCount() {
    return matchCount;
  }

  /**
   * Returns the phrase's volume count, which is the number of books the phrase appears in.
     * @return 
   */
  public int getVolumeCount() {
    return volumeCount;
  }

  /**
   * Returns the phrase's first year of occurrence.
     * @return 
   */
  public int getFirstYear() {
    return firstYear;
  }

  /**
   * Returns the phrase's last year of occurrence.
     * @return 
   */
  public int getLastYear() {
    return lastYear;
  }

  /**
   * Returns the phrase's score, also called its relative frequency.
     * @return 
   */
  public double getScore() {
    return score;
  }

  /**
   * Returns the phrase's id which is unique among all corpora.
     * @return 
   */
  public long getId() {
    return id;
  }

  /**
   * Returns the corpus enum constant the phrase belongs to.
     * @return 
   */
  public Corpus getCorpus() {
    return Corpus.fromOrdinal((int) (id >>> 40) & 0xFFFF);
  }

}
