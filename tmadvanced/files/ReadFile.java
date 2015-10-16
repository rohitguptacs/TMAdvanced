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
package tmadvanced.files;

import tmadvanced.CollectTokens;
import tmadvanced.data.Token;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import tmadvanced.Parameters;
import tmadvanced.preprocess.Tags;

/**
 *
 * @author Rohit Gupta
 */

/**
 * Read simple text file
 *
 * @author rohit
 */
public class ReadFile {

    ArrayList<Token[]> lotks = new ArrayList();
    ArrayList<String> tmRawSource = new ArrayList();

    /**
     * Read simple text file
     *
     * @param p list of all parameters
     * @param filename text file name
     */
    public ReadFile(String filename) {
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String sentence = sc.nextLine();
                tmRawSource.add(sentence);
                Tags tags;
                if (Parameters.isRemoveTags()) {  // if -tag flag is on , default on
                    tags = new Tags(sentence);
                    sentence = tags.getTextWithoutTags();
                }
                CollectTokens ct = new CollectTokens(sentence, Parameters.isRemovePunctuations(), Parameters.isReplaceNumWithPlaceholder(), Parameters.getTokenizer(), Parameters.getNEtagger());
                Token[] tokens = ct.get();
                lotks.add(tokens);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.print(e);
        }
    }

    public ArrayList<Token[]> getTokenisedSource() {
        return lotks;
    }

    public ArrayList<String> getRawSource() {
        return tmRawSource;
    }

}
