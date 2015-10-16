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
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Rohit Gupta
 * 
 * This class contains the paraphrases associated with the each key, the class contains 
 *  type 3 and 4 paraphrases and key to lexical paraphrases of type 2
 *  type 1 are one word paraphrases and need not any association with key
 */


public class ExtTokenPP {
    ArrayList<Paraphrase> alpp34;
    HashMap<String,Short> type2hmap; 
  public  ExtTokenPP(ArrayList<Paraphrase> alpp34, HashMap<String,Short> type2hmap){
     this.alpp34=alpp34;
     this.type2hmap=type2hmap;
    }
  
  public boolean isEmpty(){
      return alpp34.isEmpty() && type2hmap.isEmpty();
  }
  
  public HashMap<String, Short> getType2PP(){
      return type2hmap;
  }
  
  public ArrayList<Paraphrase> getType34PP(){
      return alpp34;
  }
  
}
