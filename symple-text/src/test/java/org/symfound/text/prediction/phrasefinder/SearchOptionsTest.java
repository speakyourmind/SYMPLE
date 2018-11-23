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

package org.symfound.text.prediction.phrasefinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.symfound.prediction.phrasefinder.SearchOptions;

class SearchOptionsTest {

  @Test
  void defaultConstructorDoesProperInitialization() {
    SearchOptions options = new SearchOptions();
    assertEquals(1, options.getMinPhraseLength());
    assertEquals(5, options.getMaxPhraseLength());
    assertEquals(100, options.getMaxResults());
  }

  @Test
  void defaultInstanceHasDefaultValues() {
    SearchOptions defaultOptions = SearchOptions.defaultInstance();
    assertEquals(1, defaultOptions.getMinPhraseLength());
    assertEquals(5, defaultOptions.getMaxPhraseLength());
    assertEquals(100, defaultOptions.getMaxResults());
  }

  @Test
  void defaultInstanceIsImmutable() {
    SearchOptions defaultOptions = SearchOptions.defaultInstance();
    assertThrows(UnsupportedOperationException.class, () -> {
      defaultOptions.setMinPhraseLength(0);
    });
    assertThrows(UnsupportedOperationException.class, () -> {
      defaultOptions.setMaxPhraseLength(0);
    });
    assertThrows(UnsupportedOperationException.class, () -> {
      defaultOptions.setMaxResults(0);
    });
  }

  @Test
  void defaultInstanceEqualsDefaultConstructedInstance() {
    assertEquals(SearchOptions.defaultInstance(), new SearchOptions());
  }

}
