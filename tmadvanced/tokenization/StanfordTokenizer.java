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

/**
 *
 * @author rohit
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.io.Reader;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.HasWord;
//import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.Timing;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.io.RuntimeIOException;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class StanfordTokenizer {

  public StanfordTokenizer(String filetotokenize, String out) {
     // Reader r= new  BufferedReader(new StringReader(filetotokenize));
    try{
           
       FileWriter fw=new FileWriter(out);
       Scanner sc=new Scanner(new File(filetotokenize));
      // Reader r= new  FileReader(new File(filetotokenize));
         int lcount=0;
       while(sc.hasNextLine()){
            Reader r= new  BufferedReader(new StringReader(sc.nextLine().trim()));
            PTBTokenizer ptbt= PTBTokenizer.newPTBTokenizer(r, false, false);
          
            if(lcount%10000==0)System.err.println(lcount);
            for (CoreLabel label; ptbt.hasNext(); ) {
                label = (CoreLabel) ptbt.next();
                fw.write(label.word()+" ");
                //System.out.print(label.word());
                  //  if(label.word().equals("*NL*")){
                         //System.out.println();
                        
                  //  }else{
                  //      fw.write(label.word()+" ");
                        //System.out.print(words.get(i)+" ");
                 //   }
            }
            lcount++;
            fw.write('\n');
      }
       fw.close();
       System.err.println("Total Lines tokenized:"+lcount);
      }catch(IOException e){
          e.printStackTrace();
          System.exit(1);
      }
  }
  
  public void printInfo(){
      //   System.out.println("Total number of segments tokenized:"+Num_Segments+" , Words:"+Num_Words);
     }
}

