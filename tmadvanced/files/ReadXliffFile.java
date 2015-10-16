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


package tmadvanced.files;

import tmadvanced.CollectTokens;
import tmadvanced.Parameters;
import tmadvanced.data.SegmentWithInfo;
import tmadvanced.data.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tmadvanced.preprocess.Tags;
/**
 *
 * @author rohit
 */

/*
ReadXliffFile class read xliff formatted file. 
A example minimal strucure:
<?xml version="1.0" encoding="UTF-8"?>

<body>
<trans-unit id="1">
  <source>Information</source>
  <target>Información</target> // target is optional
</trans-unit>
</body>

*/
public class ReadXliffFile {
    
    ArrayList<SegmentWithInfo> tmsegs=new ArrayList();
    ArrayList<Token []> lotks=new ArrayList();
    ArrayList<String> inRawSource=new ArrayList();
    ArrayList<String> ids;//=new ArrayList();
    ArrayList<String> inRawTarget=new ArrayList();
    ArrayList<String> inCleanTarget=new ArrayList();
    
    public ReadXliffFile(){
    try{
        
        
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document docpp=db.parse(new File(Parameters.getInputFileName()));
            docpp.getDocumentElement().normalize();
            
            //edfr=new ArrayList();
            
            ids=new ArrayList();
           
            NodeList nl;
            nl=docpp.getElementsByTagName("trans-unit");
            System.out.println("Total "+nl.getLength()+" segments to translate");
            
            for(int s=0;s<nl.getLength();s++){
                Node segment=nl.item(s);
                if(segment.getNodeType()==Node.ELEMENT_NODE){
                    Element element=(Element) segment;
                 //    System.out.println(element.getElementsByTagName("source").item(0).getTextContent());
                     String txt=element.getElementsByTagName("source").item(0).getTextContent();
                     inRawSource.add(txt);
                     Tags tags;
                     if(Parameters.isRemoveTags()){  // if -tag flag is on , default on
                        tags = new Tags(txt);
                        txt=tags.getTextWithoutTags();
                     }   
                    CollectTokens ct=new CollectTokens(txt, Parameters.isRemovePunctuations(), Parameters.isReplaceNumWithPlaceholder(), Parameters.getTokenizer(), Parameters.getNEtagger());
                    Token [] tokens=ct.get();
                    lotks.add(tokens);
                    
               //     System.out.println(element.getAttribute("id"));
                    ids.add(element.getAttribute("id"));
                    
                    /* Get also the target when present , mainly used for research */
                    if(element.getElementsByTagName("target").getLength()>0){
                        txt=element.getElementsByTagName("target").item(0).getTextContent();
                    }
                    inRawTarget.add(txt);
                    if(Parameters.isRemoveTags()){  // if -tag flag is on , default on
                        tags = new Tags(txt);
                        txt=tags.getTextWithoutTags();
                     }  
                    //tags=new Tags(txt);
                    //txt=tags.getTextWithoutTags();
                    inCleanTarget.add(txt);
                    
                }
            }
    }catch(Exception e){
        e.printStackTrace();
    }
    }
   
        
    public ArrayList<Token []> getSource(){
        return lotks;
    }
        
   public ArrayList<String> getCleanTarget(){
        return inCleanTarget;
    }     
   
   public ArrayList<String> getRawTarget(){
        return inRawTarget;
    }
    
}
