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

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import tmadvanced.CollectTokens;
import tmadvanced.preprocess.Tags;
import tmadvanced.Parameters;

/**
 *
 * @author Rohit Gupta
 */
public class ReadTargetFile {

    ArrayList<String> lotgtsentencesRaw = new ArrayList();
    ArrayList<String> lotgtsentencesClean = new ArrayList();

    public ReadTargetFile(String filename) {

        if (!filename.equals("")) {
            try {

                File file = new File(filename);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String sentence = sc.nextLine();
                    // System.out.println(sentence);
                    lotgtsentencesRaw.add(sentence);
                    Tags tags;
                    if (Parameters.isRemoveTags()) {  // if -tag flag is on , default on
                        tags = new Tags(sentence);
                        sentence = tags.getTextWithoutTags();
                    }
                    lotgtsentencesClean.add(sentence);
           // CollectTokens ct=new CollectTokens();
                    // lotgtsentencesClean.add(ct.preprocessSentence(sentence, punc, num));
                    //        System.out.println(tokens[0]);
                    //          for(int i=0;i<tokens.length;i++){
//                System.out.print("TEST");
                    //          System.out.print(tokens[i].getLength()+" ");
                    //    }
                    //   System.out.println();
                }
                sc.close();

            } catch (IOException e) {
                System.err.print(e);
            }
        }
    }

    /**
     *
     * @return Target sentences with tags removed
     */
    public ArrayList<String> getCleanTarget() {
        return lotgtsentencesClean;
    }

    /**
     *
     * @return Target sentences without removing tags
     */
    public ArrayList<String> getRawTarget() {
        return lotgtsentencesRaw;
    }

    public int sizeRaw() {
        return lotgtsentencesRaw.size();
    }

    public int sizeClean() {
        return lotgtsentencesClean.size();
    }
}

/*
 public class ReadTargetFile {
    
 ArrayList<String> lotgtsentences=new ArrayList();
 public ReadTargetFile(String filename){
 try{
 File file=new File(filename);
 Scanner sc=new Scanner(file);
 while(sc.hasNextLine()){
 String sentence=sc.nextLine();
 // System.out.println(sentence);
 lotgtsentences.add(sentence);
 //        System.out.println(tokens[0]);
 //          for(int i=0;i<tokens.length;i++){
 //                System.out.print("TEST");
 //          System.out.print(tokens[i].getLength()+" ");
 //    }
 //   System.out.println();
 }
 sc.close();
 }
 catch(IOException e){
 System.err.print(e);
 }
 }
 public ArrayList<String> get(){
 return lotgtsentences;
 }
 public int size(){
 return lotgtsentences.size();
 }
 }*/
