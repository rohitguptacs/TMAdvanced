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
package tmadvanced.preprocess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Partially implemented yet
import tmadvanced.CollectTokens;
import tmadvanced.data.Token;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import tmadvanced.preprocess.Placeholder;
/**
 *
 * @author Rohit Gupta
 */
public class processfile {
    
   // ArrayList<Token []> lotks=new ArrayList();
   // ArrayList<String> tmSource=new ArrayList();
    public processfile(String filename,String outfile){
        try{
        File file=new File(filename);
        Scanner sc=new Scanner(file);
        File ofile=new File(outfile);
        FileWriter fw= new FileWriter(ofile);
        while(sc.hasNextLine()){
            String sentence=sc.nextLine();
            String s=InversePlaceholder.replaceNoAndMonth(sentence);
            fw.write(s+'\n');           
        }
         sc.close();
         fw.close();
        }
        catch(FileNotFoundException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    public processfile(String filename,String outfile, String pp){
        try{
        File file=new File(filename);
        Scanner sc=new Scanner(file);
        File ofile=new File(outfile);
        FileWriter fw= new FileWriter(ofile);
        while(sc.hasNextLine()){
            String sentence=sc.nextLine();
            String[] strtokens=sentence.split("\\|\\|\\|");
                String left=strtokens[1].trim();
                String strtokens2=strtokens[2].trim();
                left=Placeholder.replaceNoAndMonth(left);
                String right=Placeholder.replaceNoAndMonth(strtokens2);
                String s=strtokens[0]+"|||"+left+"|||"+right+"|||"+"others";    
             fw.write(s+'\n');           
        }
         sc.close();
         fw.close();
        }
        catch(FileNotFoundException e){
            System.err.println(e);
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
    
    public static void main(String args[]){
        processfile pf= new processfile("//Users//rohit//expert//programs//corpus//enfr2013releaseutf8.en.fil.txt.r25000","//Users//rohit//expert//programs//corpus//enfr2013releaseutf8.en.fil.txt.r25000.NIPH");
      // processfile pf= new processfile("//Users//rohit//expert//programs//corpus//stest//enfr2013releaseutf8.en.fil.txt.r25000","//Users//rohit//expert//programs//corpus//stest//enfr2013releaseutf8.en.prepfil.txt.r25000");

    }
   
}
