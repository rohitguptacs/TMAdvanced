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

/**
 *
 * @author Rohit Gupta
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import tmadvanced.preprocess.Placeholder;


public class CollectPP {
    
    /* Collect paraphrase from PPDB formatted file and store it in ppdict hashmap*/
    /* The placeholder are also replaced if option is enabled */
    HashMap<String,ArrayList<String> > ppdict=new HashMap();
   static final private  int LIMIT=500; //max allowed paraphases of a phrase 
   static final private int MIN_LENGTH=2;// Require during placeholding,less than 2 char words are ignored 
   CollectPP(String filename,boolean placeholder){
      // if(placeholder){   // if placeholder flag true
    //      collectPPwithPH(filename);
    //   }else{
           collectPP(filename);
    //   }
   }
    private void  collectPP(String filename){
        try{
            File file=new File(filename);
            Scanner sc=new Scanner(file);
            while(sc.hasNextLine()){
                String str=sc.nextLine();
                String[] strtokens=str.split("\\|\\|\\|");
                String left=strtokens[1].trim();
            //    System.out.println("L:"+left+" R:"+strtokens[2].trim());
                ArrayList<String> right=new ArrayList();
                if (ppdict.containsKey(left)){
                    right=ppdict.get(left);
                     if(right.size()<LIMIT &&(!(right.contains(strtokens[2].trim())))){
                         right.add(strtokens[2].trim());
                    }
                }else right.add(strtokens[2].trim());
                ppdict.put(left,right);
            }
        }catch(FileNotFoundException e){
            System.err.print("Error opening paraphrase file"+e);
            System.exit(2);
        }
    }
    
    
    // CollectPP with placeholder, uses edmatch.preprocess.Placeholder
    private void collectPPwithPH(String filename){
        try{
            File file=new File(filename);
            Scanner sc=new Scanner(file);
            while(sc.hasNextLine()){
                String str=sc.nextLine();
                String[] strtokens=str.split("\\|\\|\\|");
                String left=strtokens[1].trim();
                String strtokens2=strtokens[2].trim();
                
                    left=Placeholder.replaceNoAndMonth(left);
                    strtokens2=Placeholder.replaceNoAndMonth(strtokens2);
                    
                if(left.isEmpty()||left.length()<MIN_LENGTH)continue;
                if(strtokens2.isEmpty()||strtokens2.length()<MIN_LENGTH)continue;
                ArrayList<String> right=new ArrayList();
                if (ppdict.containsKey(left)){
                    right=ppdict.get(left);
                     if(right.size()<LIMIT &&(!(right.contains(strtokens2)))){
                         right.add(strtokens2);
                    }
                }else right.add(strtokens2);
                ppdict.put(left,right);
            }
        }catch(FileNotFoundException e){
            System.err.print("Error opening paraphrase file"+e);
        }
    }
    public HashMap<String, ArrayList<String>> getPPDictionary(){
        return ppdict;
    }
    
    public static void main(String [] args){
    String ppfilename="//Users//rohit//expert//corpusparaphrase//ppdbsphrasal.txt";
        CollectPP cpp=new CollectPP(ppfilename,true);
        HashMap<String, ArrayList<String> > ppdict=cpp.getPPDictionary();
        System.out.println(Arrays.toString(ppdict.keySet().toArray()));
        
    }
    /**
     * Delete all the mappings , the dictionary will be empty after this operation
     */
    void delete(){
        if(ppdict.isEmpty()){
            System.err.print("Can't delete ppdict , already empty!!!");
        }
        else ppdict.clear();
    }
}
