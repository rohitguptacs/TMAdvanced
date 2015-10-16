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
 * Store TM match including similarity score and paraphrases used for this matching
 * @author Rohit Gupta
 */

public class Match implements Comparable<Match>{
    private final ExtToken [] match;
   // private ExtToken [] edmatch;
    private final double similarity; //fuzzy match score
    private final int id; // index in TM
    private final boolean isppused; //is match used any paraphrase
    private final boolean isppapplied; //is paraphrasing enabled
    private final LdPPSPair ldpp;//paraphrases used in matching
    
    
    public Match(ExtToken [] ppmatch,int id, double similarity, LdPPSPair ldpp){
        this.match=ppmatch;
       // this.edmatch=null;
        this.similarity=similarity;
        this.id=id;
        this.isppapplied=true;
        this.isppused= ldpp.getMatchedParaphrases().isEmpty();
        this.ldpp=ldpp;
    }
   public Match(ExtToken [] edmatch, int id,double similarity){
        this.similarity=similarity;
        this.match=edmatch;
      //  this.ppmatch=null;
        this.id=id;
        isppused=false;
        isppapplied=false;
        ldpp=null;
    }
   
   public Match(int id,double similarity){
        this.similarity=similarity;
        this.match=null;
      //  this.ppmatch=null;
        this.id=id;
        isppused=false;
        isppapplied=false;
        ldpp=null;
    }
   
   @Override
   public int compareTo(Match other){
        return Double.valueOf(this.similarity).compareTo(other.similarity);
   }
   public int getId(){
       return id;
   }
   public boolean isppApplied(){
           return isppapplied;
   }
   public boolean hasPP(){
       return isppused;
   }
   public double similarity(){
       return similarity;
   } 
   public LdPPSPair getLdPP(){
       return ldpp;
   }
   public boolean hasLdPP(){
       return isppapplied;
   }
   public ExtToken [] getEDMatch(){
       return match;
   }
   
   public ExtToken [] getPPMatch(){
       return match;
   }
}
