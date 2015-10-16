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
 * @author rohit
 */
public class Parameters {
                    public static  String TMformat="plain";
                    private static String inputformat="plain";
                    private static String inputSourceLanguage="";
                    private static String inputTargetLanguage="";
                    private static String TMSourceLanguage="";
                    private static String TMTargetLanguage="";
                  private static  String tmsrcfilename="";
             private static  String tmtgtfilename="";
             private static  String inputfilename="";
             private static  String inputtgtfilename="";
             private static  boolean phTag=true;
             private static  boolean phPunc=true;
             private static  boolean phNum=false;
              private static boolean paraphrasing=false;
              private static short [] typp={1,2,3,4};
              private static double [] pppenalties={0,0,0,0};
              private static boolean filtering=true;
              private static double lenTH=39.0;
              private static double beamTH=35.0;
              private static double tmTH=39.0;
              private static int nbestsize=100;
              private static String ppfilename="";
              private static String outfilename="";
              private static String tokenizer="stanford";
              private static String NEtagger="NO";
              private static boolean enableNE=false;
              private static boolean retrieveAll=false;
              private static int infolevel=0;
              private static int maxpara_pertoken=2000;
              
              
              
    private    Parameters(){
        }
  /*  Parameters(String TMformat,
               String inputformat,
               String inputSourceLanguage,
               String inputTargetLanguage,
               String TMSourceLanguage,
               String TMTargetLanguage,
               String tmsrcfilename,
               String tmtgtfilename,
               String inputfilename,
               String inputtgtfilename,
           //    boolean placeholder,
               boolean phTag,
               boolean phPunc,
               boolean phNum,
               boolean paraphrasing,
               short [] typp,
               double [] pppenalties,//={0,0,0,0};
               boolean filtering,
               double lenTH,
               double beamTH,
               double tmTH,
               int nbestsize,
               String ppfilename,
               String outfilename,
               String tokenizer,
               String NEtagger,
               boolean retrieveAll,
               int infolevel){
               
               this.TMformat=TMformat;
               this.inputformat=inputformat;
               this.inputSourceLanguage=inputSourceLanguage;
               this.inputTargetLanguage=inputTargetLanguage;
               this.TMSourceLanguage=TMSourceLanguage;
               this.TMTargetLanguage=TMTargetLanguage;
               this.tmsrcfilename=tmsrcfilename;
               this.tmtgtfilename=tmtgtfilename;
               this.inputfilename=inputfilename;
               this.inputtgtfilename=inputtgtfilename;
              // this.placeholder=placeholder;
               this.phTag=phTag;
               this.phPunc=phPunc;
               this.phNum=phNum;
               this.paraphrasing=paraphrasing;
               this.typp=typp;
               this.pppenalties=pppenalties;//={0,0,0,0};
               this.filtering=filtering;
               this.lenTH=lenTH;
               this.beamTH=beamTH;
               this.tmTH=tmTH;
               this.nbestsize=nbestsize;
               this.ppfilename=ppfilename;
               this.outfilename=outfilename;
               this.tokenizer=tokenizer;
               this.NEtagger="no";
               this.enableNE=false;
               this.retrieveAll=retrieveAll;
               this.infolevel=infolevel;
    }
    */
              
    protected static void  setTMFormat( String TMformat){
        Parameters.TMformat=TMformat;
    }
    protected static void setInputFormat(String inputformat){
        Parameters.inputformat= inputformat;
    }
    
    protected static void setInputSourceLanguage(String inputSourceLanguage){
        Parameters.inputSourceLanguage= inputSourceLanguage;
    }
    
    protected static void setInputFileName(String inputfilename){
        Parameters.inputfilename= inputfilename;
    }
    
    protected static void setInputTargetFileName(String inputtgtfilename){
        Parameters.inputtgtfilename= inputtgtfilename;
    }
    
    protected static void setTMSourceFileName(String tmsrcfilename){
        Parameters.tmsrcfilename= tmsrcfilename;
    }
    
    protected static void setTMTargetFileName(String tmtgtfilename){
        Parameters.tmtgtfilename= tmtgtfilename;
    }
    
    protected static void setOutputFileName(String outfilename){
        Parameters.outfilename= outfilename;
    }

    protected static void setParaphraseFileName( String ppfilename){
        Parameters.ppfilename= ppfilename;
    }

    
    protected static void setInputTargetLanguage(String inputTargetLanguage){
        Parameters.inputTargetLanguage= inputTargetLanguage;
    }
    protected static void setTMSourceLanguage(String TMSourceLanguage){
        Parameters.TMSourceLanguage= TMSourceLanguage;
    }
    protected static void setTMTargetLanguage(String TMTargetLanguage){
        Parameters.TMTargetLanguage= TMTargetLanguage;
    }
            
    protected   static void  setRemoveTags(boolean phTag){
        Parameters.phTag= phTag;
    }
    
    protected   static void  setRemovePunctuations(boolean phPunc){
        Parameters.phPunc= phPunc;
    }
    
    protected   static void  setReplaceNumWithPlaceholder(boolean phNum ){
        Parameters.phNum= phNum;
    }
    
    protected   static void setParaphrasing(boolean paraphrasing){
        Parameters.paraphrasing= paraphrasing;
    }
    
    protected static void setTyPP(short[] typp){
        Parameters.typp= typp;
    }
    
    protected   static void setPPPenalties(double [] pppenalties){
        Parameters.pppenalties= pppenalties;
    }//={0,0,0,0};
    
    protected   static void setFiltering(boolean filtering){
        Parameters.filtering= filtering;
        
    }
        
    protected   static void setLenTH(double lenTH){
        Parameters.lenTH= lenTH;
    }
    
    protected   static void setBeamTH(double beamTH){
        Parameters.beamTH= beamTH;
    }
    
    protected   static void setTMTH(double tmTH){
        Parameters.tmTH= tmTH; 
    }
    
    protected   static void setNbestSize(int nbestsize){
        Parameters.nbestsize= nbestsize;
    }
    
    protected   static void setTokenizer(String tokenizer){
        Parameters.tokenizer= tokenizer;
    }
            
    protected   static void setNEtagger(String NEtagger){
        Parameters.NEtagger= NEtagger;
    }
    
    protected   static void setEnableNE(boolean enableNE){
        Parameters.enableNE= enableNE;
    }        
            
    protected static void setRetrieveAll(boolean retrieveAll){
        Parameters.retrieveAll= retrieveAll;
    }
    
    protected static void setInfoLevel(int infolevel){
        Parameters.infolevel= infolevel;
    }
    
    protected static void setMaxParaPerToken(int maxpara_pertoken){
        Parameters.maxpara_pertoken= maxpara_pertoken;
    }
              
              
    
    
              
    public static String getTMFormat(){
        return TMformat;
    }
    public static String getInputFormat(){
        return inputformat;
    }
    
    public static String getInputSourceLanguage(){
        return inputSourceLanguage;
    }
    
    public static String getInputFileName(){
        return inputfilename;
    }
    
    public static String getInputTargetFileName(){
        return inputtgtfilename;
    }
    
    public static String getOutputFileName(){
        return outfilename;
    }
    
    
    public static String getTMSourceFileName(){
        return tmsrcfilename;
    }
    
    public static String getTMTargetFileName(){
        return tmtgtfilename;
    }
    
    
    
    public static String getParaphraseFileName(){
        return ppfilename;
    }
    
    public   static String getInputTargetLanguage(){
        return inputTargetLanguage;
    }
    public static String getTMSourceLanguage(){
        return TMSourceLanguage;
    }
    public static String getTMTargetLanguage(){
        return TMTargetLanguage;
    }
            
    public  static  boolean isRemoveTags(){
        return phTag;
    }
    
    public  static  boolean isRemovePunctuations(){
        return phPunc;
    }
    
    public  static  boolean isReplaceNumWithPlaceholder(){
        return phNum;
    }
    
    public  static  boolean isParaphrasing(){
        return paraphrasing;
    }
    
    public static short[] getTyPP(){
        return typp;
    }
    
    public  static double [] getPPPenalties(){
        return pppenalties;
    }//={0,0,0,0};
    
    public  static boolean isFiltering(){
        return filtering;
        
    }
        
    public  static double getLenTH(){
        return lenTH;
    }
    
    public  static double getBeamTH(){
        return beamTH;
    }
    
    public  static double getTMTH(){
        return tmTH;
    }
    
    public  static int getNbestSize(){
        return nbestsize;
    }
    
    public static  String getTokenizer(){
        return tokenizer;
    }
            
    public static  String getNEtagger(){
        return NEtagger;
    }
    
    public static  boolean isEnableNE(){
        return enableNE;
    }        
            
    public static boolean isRetrieveAll(){
        return retrieveAll;
    }
    
    public static int getInfoLevel(){
        return infolevel;
    }
    
    public static int getMaxParaPerToken(){
        return maxpara_pertoken;
    }
            
    public static void  printParameters(){
        System.err.println("TMFormat: "+TMformat+"\n"+
               "inputFormat: "+inputformat+"\n"+
               "inputSourceLanguage: "+inputSourceLanguage+"\n"+
               "inputTargetLanguage: " +inputTargetLanguage+"\n"+
               "TMSourceLanguage: "+ TMSourceLanguage+"\n"+
               "TMTargetLanguage: "+ TMTargetLanguage+"\n"+
               "TMSourceFilename: " +tmsrcfilename+"\n"+
               "TMTargetFilename: "+ tmtgtfilename+"\n"+
               "inputSourceFilename: " +inputfilename+"\n"+
               "inputTargetFilename: "+ inputtgtfilename+"\n"+
            //   "placeholder " +placeholder+"\n"+
               "removeTags: " +phTag+"\n"+
               "removePunctuations: " +phPunc+"\n"+
               "removeNumbers: " +phNum+"\n"+
               "paraphrasing: "+ paraphrasing+"\n"+
               "typp: "+typp[0]+","+typp[1]+","+typp[2]+","+typp[3]+"\n"+
               "pppenalties: "+ pppenalties[0]+","+pppenalties[1]+","+ pppenalties[2]+","+ pppenalties[3]+"\n"+
               "filtering: "+ filtering+"\n"+
               "lenTH: " +lenTH+"\n"+
               "beamTH: "+ beamTH+"\n"+
               "tmTH: "+ tmTH+"\n"+
               "nbestsize: " +nbestsize+"\n"+
               "ppfilename: "+ ppfilename+"\n"+
               "outfilename: "+ outfilename+"\n"+
               "tokenizer: "+ tokenizer+"\n"+
               "NEtagger: "+ NEtagger+"\n"+
               "enableNE: " +enableNE+"\n"+
               "retrieveAll: "+ retrieveAll+"\n"+
                "info: "+ infolevel);
    }
}
