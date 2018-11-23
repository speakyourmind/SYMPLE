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
import org.junit.jupiter.api.Test;
import org.symfound.prediction.phrasefinder.Phrase;
import org.symfound.prediction.phrasefinder.SearchResult;
import org.symfound.prediction.phrasefinder.SearchResult.Status;

class SearchResultTest {

  @Test
  void hasNoPublicConstructors() {
    assertEquals(0, Phrase.class.getConstructors().length);
  }

  @Test
  void emptyInstanceIsProperlyInitialized() {
    assertEquals(Status.OK, SearchResult.emptyInstance().getStatus());
    assertEquals(0, SearchResult.emptyInstance().getPhrases().length);
  }

}
