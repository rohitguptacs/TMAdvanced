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
/**
 * This class contains information from LevDistWithParaphrasing matching
 * @author Rohit Gupta
 */

public class LdPPSPair {
    private final Token[] alt;
    private final double score;
    private final ArrayList<PPPair> pppair;
    private final short length;
    
   public LdPPSPair(Token[] alt,short length,double score,ArrayList<PPPair> pppair){
        this.alt=alt;
        this.score=score;
        this.pppair=pppair;
        this.length=length;
    }
   
   public LdPPSPair(){
       this.alt=null;
       this.score=100;
       this.pppair=new ArrayList<PPPair>();
       this.length=0;
   }
    
   public double getEditDistance(){
        return score;
    }
   public ArrayList<PPPair> getMatchedParaphrases(){
        return pppair;
    }
   public Token[] getSentence(){
        return alt;
    }
   public Token getTokenAt(short index){
        return alt[index];
    }
  public  short length(){
        return length;
    }
}
