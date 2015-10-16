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

package tmadvanced;

import tmadvanced.data.Token;
import tmadvanced.preprocess.Placeholder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Exception;
import java.io.IOException;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.process.PTBTokenizer;
import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Rohit Gupta
 */


public class CollectTokens {
    Token[] tokens;
    Integer num_nums;
    Integer num_pucs;
    Integer num_tags;
    
    
    int index=0;
    private static Pattern AMP = Pattern.compile("[|]");

    
    /**
     * Constructor implementing pretokenized imput and old placeholder 
     * @param sentence input pretokenized sentence
     * @param phflag true for EAMT-14 like placeholder replacement
     */
 /*   public CollectTokens(String sentence,boolean phflag){
        //String sep=System.getProperty("line.separator"); 
        if(phflag)
            tokenizeWithPlaceholder(sentence);      
        else 
            tokenize(sentence);
    }*/
    
    /**
     * Code implementing old placeholder as used in EAMT-14
     * @param sentence input sentence
     */
  /*  private void tokenizeWithPlaceholder(String sentence){
            String s = Placeholder.replaceNoAndMonth(sentence);
            tokenize(s);
    }*/
    public CollectTokens(){}
    /**
     * Constructor implementation with punctuation removal and number placeholder
     * @param sentence  segment
     * @param phTag  flag for removing tags (enable for removing tags)
     * @param phPunc  flag for removing punctuations (enable for removing punctuations)
     * @param phNum  flag for replacing numbers by a placeholder
     * @param tokenizer tokenizer to be used (stanford, berkeley or notok (for space tokenization) )
     * @param netagger Named Entity tagger to be used (LBJ tagger)
     */
    public CollectTokens(String sentence,boolean phPunc, boolean phNum, String tokenizer, String netagger){
          
        String nlptok=NLPTokenize(sentence, tokenizer);
        tokenizeWithInfo(nlptok, phPunc, phNum); // Tag, punctuation and numbers
        
    }
    /**
     * This function tokenize using Stanford tokenizer
     * @param sentence
     * @return 
     */
    private String StanTokenizer(String sentence){
        Reader r= new  BufferedReader(new StringReader(sentence.trim()));
        PTBTokenizer ptbt= PTBTokenizer.newPTBTokenizer(r, false, false);          
        String stok="";
        for (CoreLabel label; ptbt.hasNext(); ) {
            label = (CoreLabel) ptbt.next();
            stok=stok+" "+label;
        }
        return stok;
    }
    
    /**
     * This function tokenize using Berkeley tokenizer
     * @param sentence
     * @return 
     */
    private String BerkTokenizer(String sentence){
        Reader r= new  BufferedReader(new StringReader(sentence.trim()));
        edu.berkeley.nlp.tokenizer.PTBTokenizer toke=new edu.berkeley.nlp.tokenizer.PTBTokenizer(r,true);
        //  if(lcount%10000==0)System.err.println(lcount);
        String stok="";
        List words=toke.tokenize();
        for(int i=0;i<words.size();i++){
             stok=stok+" "+words.get(i);
        }
        return stok;
    }
    
    /**
     * 
     * @param sentence  sentence
     * @param tokenizer tokenizer options:stanford, berkeley, notok(no tokenization)
     * @return 
     */
    private String NLPTokenize(String sentence, String tokenizer){
       String stok=""; 
       if(tokenizer.equalsIgnoreCase("stanford")){
             stok=StanTokenizer(sentence);
            
        }else if(tokenizer.equalsIgnoreCase("berkeley")){
             stok=BerkTokenizer(sentence);
            
        }else if(tokenizer.equalsIgnoreCase("no")){
             stok=sentence;
        }else {
            System.err.println("Please give a valid NLP tokenization option (stanford, berkeley, no)");
            System.exit(1);
        }   
     return stok.trim();
    }
    
    
    
    /**
     * Simple space tokenization (should be used for pretokenized input)
     * @param sentence 
     */    
    private void tokenize(String sentence){
        
    try{
            sentence=sentence.replaceAll("\\s+"," ");
            String [] al=sentence.split(" ");
            tokens= new Token[al.length];
            for (String al1 : al) {
                    Token tk = new Token(al1, true);
                    tokens[index++]=tk;
            }
        }catch(NullPointerException e){
            System.err.println("Collect Tokens Error");
            e.printStackTrace();
        }
    }
    
    
    /**
     * This function remove punctuation and replace numbers with a placeholder
     * @param sentence 
     */
    private void tokenizeWithInfo(String sentence,  boolean punc, boolean num){
        
            sentence= preprocessSentence(sentence,punc,num);
            if(Parameters.getInfoLevel()>0){System.out.println(sentence);}
            tokenize(sentence);
    }
    
    public  String preprocessSentence(String sentence, boolean punc, boolean num){
            
          //  if(tag)sentence=filtertags(sentence);
            
            sentence=sentence.replaceAll("\\s+"," ");
            
            if(punc)sentence=removePunct(sentence);
            
            if(num)sentence=replaceNum(sentence);
            
            return sentence;
    }
    
    /**
     * This function removes all punctuation
     * @param sentence segment
     * @return segment after removing punctuation
     */
    private String removePunct(String sentence){
        
            Pattern AMP=Pattern.compile("((\\-L[RSC]B\\-)|(\\-R[RSC]B\\-)|(\\-l[rsc]b\\-)|(\\-r[rsc]b\\-))");
            String s=AMP.matcher(sentence).replaceAll("");
           // System.out.println(s);
             AMP=Pattern.compile("(\\&)(amp)"); 
             s=AMP.matcher(s).replaceAll("amp");
           //  System.out.println(s);
             AMP=Pattern.compile("(\\&)"); 
             s=AMP.matcher(s).replaceAll("and");
         //    System.out.println(s);
            //AMP=Pattern.compile("[^a-z0-9%\\s]");
             AMP=Pattern.compile("\\-");
             s=AMP.matcher(s).replaceAll(" ");
             AMP=Pattern.compile("[!\\+\\-\\[\\]\\(\\)\\*\\,\\.\\/:;#%?@\\\\_{}¡«•»¿``''\"]");
             Matcher m= AMP.matcher(s);
             int punc_count=0;
             while(m.find())punc_count++;
             
             num_pucs=punc_count;
         //    System.out.println("Num of Punctuations:"+punc_count);
            //AMP=Pattern.compile("[!\"#%&'()*,-./:;?@\\[\\\\]_{}¡«•»¿]");
            s=AMP.matcher(s).replaceAll("");
           // System.out.println(s);
            //AMP = Pattern.compile("\\b([0-9]*2nd|[0-9]*1st|[0-9]+th|[0-9]+)\\b");
            return s;
    }
    
    /**
     * This function replace all numbers with placeholder NNN (using this function after removePunct will replace any number with punctuation also e.g. 12/07/1988)
     * @param s
     * @return segment after replacing number with placeholder NNN
     */
    private String replaceNum(String s){
             AMP = Pattern.compile("\\b([0-9][0-9\\s]+[0-9])\\b");
             Matcher m= AMP.matcher(s);
             int num_count=0;
             while(m.find())num_count++;
             
             num_nums=num_count;
           //  System.out.println("Num of Numbers:"+num_count);
            s=AMP.matcher(s).replaceAll("99");
            s=s.replaceAll("\\s+"," ");
            s=s.trim();
         //   System.out.println(s);
            return s;
    }
    
   
    
    /**
     * 
     * @return tokenized segment
     */
    public Token[]  get(){
        return tokens;
    }
    
 /*   public Integer getNumTags(){
        return num_tags;
    }*/
    
    public Integer getNumNums(){
        return num_nums;
    }
    
    public Integer getNumPucs(){
        return num_pucs;
    }
    
    public static void main(String []args){
    
           //CollectTokens ct=new CollectTokens("*,-./:; #% _{}¡«•»¿ [13th] 888290&* !!!He\"786 *,-./:; #% _{}¡«•»¿ [13th] jan ;?@ mar 21st 22nd january apr jun aug february march april 2nd may june july august sep october september october oct nov november dec december 1st jul 1986 is 95th feb 1670 mayhem year old 7899",false, "stanford");
        //CollectTokens ct=new CollectTokens("Barack Obama purchased Microsoft Corporation and acquired Apple in 2016.",false);
     //   CollectTokens ct=new CollectTokens("Intel +347 88889  -98 Celeron<ph id=\"1\">&lt;x id=&quot;1033&quot; /&gt;</ph> CPU 1.66 GHz or faster on 32-bit</ph> system or </ph2>64bit system IIIT-Allahabad",true, "stanford", "no");
     //   System.out.println(ct.num_nums+" "+ct.num_pucs+" "+ct.num_tags);
    
    }
}
