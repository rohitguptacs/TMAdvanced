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

import tmadvanced.data.Token;

/**
 *
 * @author Rohit Gupta
 */
public class Paraphrase {
    String src;
    String srcpp;
     Token [] srcpptk;
    int lensrc;
    int lensrcpp;
    int tokenno;
    short type;   //1 lexical, 2 phrasal 1diff, 3 and 4 phrasal multiword 
    
    public Paraphrase(String src, String srcpp,int tokenno, short type){
        this.src=src.trim();
        this.srcpp=srcpp.trim();
        this.tokenno=tokenno;
        this.type=type;
      //  System.out.println(src);
      //  System.out.println(srcpp);
        lensrc=src.split("\\s+").length;
        lensrcpp=srcpp.split("\\s+").length;
        srcpptk=new Token[lensrcpp];
        String [] srcppsplit=srcpp.split("\\s+");
        for(int i=0;i<lensrcpp;i++){
            Token tk=new Token(srcppsplit[i],true);
            srcpptk[i]=tk;
        }
    }
   
   public short getType(){
       return type;
   }
   public String getleft(){
        return src;
    }
   public String getright(){
        return srcpp;
    }
  public  int noOfWordsSrc(){
        return lensrc;
    }
    public int noOfWordsPP(){
        return lensrcpp;
    }
  public  Token [] getAllpptokens(){
        return srcpptk;
    }
  public  Token getpptokenAtIndex(int index){
        return srcpptk[index];
    }
  public  boolean haspptokenAtIndex(int index){
        return (index>=0 && index<lensrcpp);        
    }
  public  boolean hasSrctokenAtIndex(int index){
        return (index>=0 && index<lensrc);        
    }
  public int getIndexOfSourceTokenMatched(){
      return tokenno;
  }
  
}
