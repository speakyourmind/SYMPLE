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

/**
 * SearchOptions represents optional parameters that can be sent along with a query.
 */
public class SearchOptions {

  private int minPhraseLength = 1;
  private int maxPhraseLength = 5;
  private int maxResults = 100;

  /**
   * Creates a new instance initialized with default values.
   */
  public SearchOptions() {}

  private static final class ImmutableSearchOptions extends SearchOptions {

    private ImmutableSearchOptions() {
      super();
    }

    @Override
    public void setMinPhraseLength(int minPhraseLength) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxPhraseLength(int maxPhraseLength) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxResults(int maxResults) {
      throw new UnsupportedOperationException();
    }

  }

  private static final SearchOptions DEFAULT_INSTANCE = new ImmutableSearchOptions();

  /**
   * Returns an instance whose fields are set to values which reflect the default search options.
     * @return 
   */
  public static SearchOptions defaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + maxPhraseLength;
    result = prime * result + maxResults;
    result = prime * result + minPhraseLength;
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
    if (!(obj instanceof SearchOptions)) {
        return false;
      }
    SearchOptions other = (SearchOptions) obj;
    if (maxPhraseLength != other.maxPhraseLength) {
        return false;
      }
    if (maxResults != other.maxResults) {
        return false;
      }
    return minPhraseLength == other.minPhraseLength;
  }

  /**
   * Returns the minimum length of matching phrases (number of tokens) to be included in the result
   * set.
     * @return 
   */
  public int getMinPhraseLength() {
    return minPhraseLength;
  }

  /**
   * Sets the minimum length of matching phrases (number of tokens) to be included in the result
   * set. Defaults to 1 if not set.
     * @param minPhraseLength
   */
  public void setMinPhraseLength(int minPhraseLength) {
    this.minPhraseLength = minPhraseLength;
  }

  /**
   * Returns the maximum length of matching phrases (number of tokens) to be included in the result
   * set.
     * @return 
   */
  public int getMaxPhraseLength() {
    return maxPhraseLength;
  }

  /**
   * Sets the maximum length of matching phrases (number of tokens) to be included in the result
   * set. Defaults to 5 if not set.
     * @param maxPhraseLength
   */
  public void setMaxPhraseLength(int maxPhraseLength) {
    this.maxPhraseLength = maxPhraseLength;
  }

  /**
   * Returns the maximum number of phrases to be returned.
     * @return 
   */
  public int getMaxResults() {
    return maxResults;
  }

  /**
   * Sets the maximum number of phrases to be returned. A smaller value may lead to slightly faster
   * response times. Defaults to 100 if not set.
     * @param maxResults
   */
  public void setMaxResults(int maxResults) {
    this.maxResults = maxResults;
  }

}
