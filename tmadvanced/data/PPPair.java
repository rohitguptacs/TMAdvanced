/*
 * TMAdvanced: A tool to retrive semantically similar matches from a  Translation Memory using paraphrases
 * Copyright (C) 2015 Rohit Gupta, University of Wolverhampton.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tmadvanced.data;

import tmadvanced.data.Paraphrase;

/**
 *
 * @author Rohit Gupta
 */
public class PPPair {

    public PPPair(Paraphrase paraphrase, int location) {
        this.paraphrase = paraphrase;
        this.location = location;
    }

    public PPPair() {
        this.paraphrase = null;
        this.location = -1;
    }

    public Paraphrase getParaphrase() {
        return paraphrase;
    }

    public int getLocation() {
        return location;
    }
    private Paraphrase paraphrase;
    private int location;

}
