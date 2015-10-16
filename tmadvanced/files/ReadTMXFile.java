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
import tmadvanced.data.Token;
import tmadvanced.data.SegmentWithInfo;
import java.io.File;
import java.util.ArrayList;
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

/*ReadTMXFile class reads TMX 1.4 formatted file 
A example format:
<?xml version="1.0" encoding="utf-8"?>
<tmx version="1.4">
<body>
<tu>
  <tuv xml:lang="en-US">
   <seg>Applications</seg>
  </tuv>
  <tuv xml:lang="es-ES">
   <seg>aplicaciones</seg>
  </tuv>
</tu>
</body>
</tmx>
*/
public class ReadTMXFile {
    
    ArrayList<SegmentWithInfo> tmsegs=new ArrayList();
    ArrayList<Token []> lotks=new ArrayList();
    ArrayList<String> tmRawSource=new ArrayList();
    ArrayList<String> ids;//=new ArrayList();
    ArrayList<String> tmCleanTarget=new ArrayList();
    ArrayList<String> tmRawTarget=new ArrayList();
   
    
    public ReadTMXFile( String filename, String sourcelanguage, String targetlanguage){
        
    try{
        //FileWriter fw=new FileWriter(new File("tmp1.txt"));
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        Document docpp=db.parse(new File(filename));
        docpp.getDocumentElement().normalize();
        
        ids=new ArrayList();
        NodeList nl;
        nl=docpp.getElementsByTagName("tu");
        System.out.println("File "+filename+" contains "+nl.getLength()+" segments tu's with "+" SourceLanguage "+sourcelanguage+ " TargetLanguage "+targetlanguage);
        for(int s=0;s<nl.getLength();s++){
            //System.err.println("Entered Here....................................................");
            Node segment=nl.item(s);
            if(segment.getNodeType()==Node.ELEMENT_NODE){
                Element element=(Element) segment;
                NodeList nl2=element.getElementsByTagName("tuv");//.item(0).getTextContent());
                //System.out.println("tuv contains "+nl2.getLength()+" segments");
                //Check whether source-target pair exist
                // boolean validtuv=false;
                boolean validsrc=false;
                boolean validtgt=false;
                for(int s2=0;s2<nl2.getLength();s2++){
                    Node segment2=nl2.item(s2);
                    if(segment2.getNodeType()==Node.ELEMENT_NODE){
                       if(segment2.getAttributes().getNamedItem("xml:lang").getChildNodes().item(0).getTextContent().equals(sourcelanguage)){
                           validsrc=true;
                        }
                       if(segment2.getAttributes().getNamedItem("xml:lang").getChildNodes().item(0).getTextContent().equals(targetlanguage)){
                           validtgt=true;
                       }
                    }
                }
                     
                boolean notalltags=true;
                String cleansrctext="";
                String rawsrctext="";
                if(validsrc && validtgt){
                     for(int s2=0;s2<nl2.getLength();s2++){
                         Node segment2=nl2.item(s2);
                         if(segment2.getNodeType()==Node.ELEMENT_NODE){
                             Tags tags;
                             int numTagsSrc=-1;
                             Element element2=(Element) segment2;
                             if(segment2.getAttributes().getNamedItem("xml:lang").getChildNodes().item(0).getTextContent().equals(sourcelanguage)){
                                 rawsrctext=element2.getElementsByTagName("seg").item(0).getTextContent(); //Clean text is retrieved automatically
                                // System.err.println(s+":"+rawtext);
                                // rawsrctext=rawtext;
                                 if(Parameters.isRemoveTags()){ // if -tag flag is on , default on
                                    tags =new Tags(rawsrctext);
                                    cleansrctext= tags.getTextWithoutTags();
                                    numTagsSrc=tags.getNumTags();
                                 }else{
                                     cleansrctext=rawsrctext;
                                 }
                                 if(cleansrctext.isEmpty()){notalltags=false;
                                 }
                             }
                             if( notalltags && segment2.getAttributes().getNamedItem("xml:lang").getChildNodes().item(0).getTextContent().equals(targetlanguage)){
                                 String rawtgttext=element2.getElementsByTagName("seg").item(0).getTextContent();
                                 
                                 ids.add(Integer.toString(s));
                                 CollectTokens ct=new CollectTokens(cleansrctext,Parameters.isRemovePunctuations(),Parameters.isReplaceNumWithPlaceholder(),  Parameters.getTokenizer(), Parameters.getNEtagger());
                                 Token [] tokens=ct.get();
                                 String cleantgttext=rawtgttext;
                                 if(Parameters.isRemoveTags()){   // if -tag flag is on , default on
                                    tags =new Tags(rawtgttext);
                                    cleantgttext= tags.getTextWithoutTags();
                                    /*Do same preprocessing as of source*/
                                  //  cleantgttext=ct.preprocessSentence(cleantgttext, p.isRemovePunctuations(), p.isReplaceNumWithPlaceholder());
                                 }
                                 //printtokens(tokens);
                                 lotks.add(tokens);
                                 tmRawSource.add(rawsrctext);
                                 tmCleanTarget.add(cleantgttext);
                                 tmRawTarget.add(rawtgttext);
                                 //SegmentWithInfo ts=new SegmentWithInfo(tokens, cleansrctext, cleantgttext, rawsrctext, rawtgttext, ct.getNumNums(),ct.getNumPucs(),numTagsSrc);
                                 //tmsegs.add(ts);
                             }
                         }
                        
                    }
                }
       
             }
          }
          errorcheck();
            
       }catch(Exception e){
        e.printStackTrace();
       }
    }
    
    
    
    
    
 private void printtokens(Token[] tokens){
     for(int i=0;i<tokens.length;i++){
         System.err.print(tokens[i].getText());
     }
     System.err.println();
 }   

 private void errorcheck(){
            boolean err=false;
            System.out.println("idsize:"+ids.size()+" rawsource:"+tmRawSource.size()+" sourcetok:"+lotks.size()+" Rawtarget:"+tmRawTarget.size());
            if(ids.size()!= lotks.size()){
                System.err.print("Error in Reading TMX");
                System.err.println("Error: Number of source segments in TM (after tokenization) differs from the number of ids");
                err=true;
            }
          /*  if(ids.size()!= tmSource.size()){
                System.err.println("Number of source segments in TM (before tokenization) differ from the number of ids");
                err=true;
            }*/
            if(ids.size()!= tmRawTarget.size()){
                System.err.print("Error in Reading TMX");
                System.err.println("Error: Number of target segments in TM differ from the number of ids");
                err=true;
            }
            if(err){
                System.err.print("Error in Reading TMX");
                System.exit(1);
            }
        }
        
 public ArrayList<Token []> getTokenisedSource(){
        return lotks;
 }
 
 public ArrayList<String> getRawSource(){
        return tmRawSource;
 }
 
 public ArrayList<String> getCleanTarget(){
        return tmCleanTarget;
 }
        
 public ArrayList<SegmentWithInfo> getTMXSegments(){
        System.err.print("Not implemented yet");
        System.exit(20);
        return tmsegs;
 }   
 
 
}
