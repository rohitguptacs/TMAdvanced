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
package tmadvanced.tokenization;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import edu.berkeley.nlp.tokenizer.PTBTokenizer;
import edu.berkeley.nlp.util.StringUtils;
/**
 *
 * @author rohit
 */
public class BerkeleyTokenizer {

    /**
     * @param args the command line arguments
     */
    static int Num_Words;
    static int Num_Segments;
    public BerkeleyTokenizer(String filetotokenize, String out){
        String []args=new String[2];
        args[0]=filetotokenize;
        args[1]=out;
        main(args);
    }
    
     static void main(String[] args) {
        try{
        //PTBTokenizer tokenizer=new PTBTokenizer(new FileReader("//Users//rohit//expert//corpusdgttm//release2013//enfr2013.fil.tok.en"),true);
        //FileWriter fw=new FileWriter("//Users//rohit//expert//corpusdgttm//release2013//enfr2013.wlc.fil.tok.en.tokenized.txt");
       // PTBTokenizer tokenizer=new PTBTokenizer(new FileReader("//Users//rohit//expert//programs//corpus//SAARLAND//de-en//Europarl.de-en.en.7_40fil"),true);
       // FileWriter fw=new FileWriter("//Users//rohit//expert//programs//corpus//SAARLAND//de-en//Europarl.de-en.en.7_40fil.btok");
        
        PTBTokenizer tokenizer=new PTBTokenizer(new FileReader(args[0]),true);
        FileWriter fw=new FileWriter(args[1]);
        
        List words=tokenizer.tokenize();
        int lcount=0;
        Num_Words=words.size();
        for(int i=0;i<words.size();i++){
            if(words.get(i)=="*CR*"){
                //System.out.println();
                lcount++;
                fw.write('\n');
            }else if(words.get(i+1)=="*CR*"){
                fw.write(words.get(i).toString());
                //System.out.print(words.get(i));
            }else{
                fw.write(words.get(i)+" ");
                //System.out.print(words.get(i)+" ");
            }
        }
        Num_Segments=lcount;
        fw.close();
        }catch(IOException e) {
            System.out.println("File not found "+e);
        }
        // TODO code application logic here
    }
    
     public void printInfo(){
         System.out.println("Total number of segments tokenized:"+Num_Segments+" , Words:"+Num_Words);
     }
}

