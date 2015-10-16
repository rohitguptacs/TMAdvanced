/*
 * TMAdvanced: A tool to retrive semantically similar matches from a  Translation Memory using paraphrases
 * Copyright 2015 Rohit Gupta, University of Wolverhampton.
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

package tmadvanced;

import tmadvanced.data.ExtToken;
import tmadvanced.data.Paraphrase;
import tmadvanced.data.Token;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rohit
 */
public class Print {
    
   private Print(){
   }
    
   public static void printToken(Token t) {
        System.out.println("TOKEN:" + t.getText());
    }

   
    public static void printtokens(Token[] tks) {
        for (Token tk : tks) {
            System.err.print(tk.getText() + " ");
        }
        System.err.println();
    }

    public static void printtokens(ExtToken[] tks) {
        for (ExtToken tk : tks) {
            System.err.print(tk.getToken().getText() + " ");
        }
        System.err.println();
    }

    public static void printParaphrases(ArrayList<Paraphrase> alpp) {
        for (Paraphrase pp : alpp) {
            System.out.println(pp.getleft() + " ||| " + pp.getright());
        }
    }

    public static void printParaphrases(String left, HashMap<String, Double> alpp) {
        for (String pp : alpp.keySet()) {
            System.out.println(left + " ||| " + pp);
        }
    }

    public static void printParaphrases(HashMap<String, Short> alpp, String left) {
        for (String pp : alpp.keySet()) {
            System.out.println(left + " ||| " + pp);
        }
    }
}
