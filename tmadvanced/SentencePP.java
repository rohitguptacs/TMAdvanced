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

import tmadvanced.data.Paraphrase;
import tmadvanced.data.ExtToken;
import tmadvanced.data.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

/**
 * This class collect all the paraphrases for a 
 * given sentence, indexing will be done 
 * per token and not per char
 * @author Rohit Gupta
 */
public class SentencePP {
    private ExtToken [] extsentence;
   // private static final  int LIMIT=100;  // limit on maximum paraphrases per token
    SentencePP(ExtToken [] extsentence){
        this.extsentence= extsentence;
    }
    
      
    /**
     * 
     * @return sentence (list of tokens) with collected paraphrases
     */
    ExtToken[] get(){
         return extsentence;   
    }
    
    /**
     * Print the sentence 
     */
    void print(){
        for(ExtToken etk:extsentence){
            etk.print();
            System.out.print(" ");
        }
    }
    
 /* Test Code */   
    public static void main(String args[]){
            String ppfilename="ppdbsphrasal.txt";
        CollectPP cpp=new CollectPP(ppfilename, false);
        HashMap<String, ArrayList<String> > ppdict=cpp.getPPDictionary();
        System.out.println(Arrays.toString(ppdict.keySet().toArray()));
        Token []tk=new Token[5];
        Token tk1 =new Token("china-u.s.",true);
        Token tk2 =new Token("and",true);
        Token tk3 =new Token("imprudent",true);
        Token tk4 =new Token("was",true);
        Token tk5 =new Token("enthusiastic",true);
        tk[0]=tk1;tk[1]=tk2;tk[2]=tk3;tk[3]=tk4;tk[4]=tk5;
      /*  SentencePP spp=new SentencePP(tk,ppdict);
        ExtToken []etk=spp.get();
        for(int i=0;i<5;i++){
            String tktext=etk[i].getToken().getText();
            for(int j=0;j<etk[i].getpplist().size();j++){
            String tkparaleft=etk[i].getpplist().get(j).getleft();
            String tkpararight=etk[i].getpplist().get(j).getright();
            }
        }*/
         
    }
}
