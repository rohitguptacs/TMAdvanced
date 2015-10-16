/*
 * TMAdvanced (version 0.90): A tool to retrive semantically similar matches from a  Translation Memory using paraphrases
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

import java.util.ArrayList;
import java.util.List;
import tmadvanced.EDMatch;

/**
 *
 * @author rohit
 */

public class Main {

    public static void main(String[] args) {

        //  ReadXliffFile xliff=new ReadXliffFile("//Users//rohit//expert//hermes//Files//Trans_KES.xml");
        //  ReadTMXFile tmx=new ReadTMXFile("//Users//rohit//expert//hermes//Files//TM_KES.tmx", "en-US", "es-ES");
        //boolean placeholder = false;
      /*  boolean phTag = true;
        boolean phPunc = true;
        boolean phNum = false;
        boolean paraphrasing = false;
        //boolean enabledouble = false;
        boolean filtering = true;
       // boolean enableNE = false;
        boolean retrieveAll = false;
        String NEtagger = "no";
        int nbestsize = 100;
        String tmsrcfilename = "";
        String ppfilename = "<Paraphrasing disabled>";
        String tmtgtfilename = "";
        String inputfilename = "";
        String inputtgtfilename = "";
        String outfilename = "";
        double lenTH = 39.0;
        double beamTH = 35.0;
        double tmTH = 39.0;
        short[] typp = {1, 2, 3, 4};
        String inputformat = "plain";
        String TMformat = "plain";
        ////////////////valid formats////////////////////
        List<String> validformats = new ArrayList();
        validformats.add("plain");
        validformats.add("tmx");
        validformats.add("xliff");
        double[] pppenalties = {0, 0, 0, 0};
        String tokenizer = "stanford";
        String inputSourceLanguage = "EN-US";
        String inputTargetLanguage = "ES-ES";
        String TMSourceLanguage = "EN-US";
        String TMTargetLanguage = "ES-ES";
        int infolevel=0;
*/
        /*
         args=new String[12];
         args[0]="-tmth";
         args[1]="65.00";
         args[2]="-tms";
         args[3]="//Users//rohit//expert//programs//corpus//NEtesting//enfr.en.5000.txt";
         args[4]="-tmt";
         args[5]="//Users//rohit//expert//programs//corpus//NEtesting//enfr.fr.5000.txt";
         args[6]="-ins";
         args[7]="//Users//rohit//expert//programs//corpus//NEtesting//enfr.en.500.txt";
         args[8]="-int";
         args[9]="//Users//rohit//expert//programs//corpus//NEtesting//enfr.en.500.txt";
         args[10]="-o";
         args[11]="//Users//rohit//expert//programs//corpus//NEtesting//NE500.xml";
         */
        /*
         args=new String[22];
         args[0]="-bth";
         args[1]="35.0";
         args[2]="-lth";
         args[3]="49.0";
         args[4]="-nb";
         args[5]="100";
         args[6]="-tmth";
         args[7]="65.00";
         args[8]="-pp";
         args[9]="//Users//rohit//expert//corpusparaphrase//ppdbllexlphrasal.txt";
         args[10]="-tms";
         args[11]="//Users//rohit//expert//programs//corpus//test13//2011.en.txt";
         args[12]="-tmt";
         args[13]="//Users//rohit//expert//programs//corpus//test13//2011.fr.txt";
         args[14]="-ins";
         args[15]="//Users//rohit//expert//programs//corpus//test13//2013.en.txt";
         args[16]="-int";
         args[17]="//Users//rohit//expert//programs//corpus//test13//2013.fr.txt";
         args[18]="-o";
         args[19]="//Users//rohit//expert//programs//corpus//test13//2013tmp.xml";
         args[20]="-typp";
         args[21]="1,2,3,4";
         // args[22]="-pholder";
         */
        /*
         args=new String[38];
         args[0]="-bth";
         args[1]="35.0";
         args[2]="-lth";
         args[3]="39.0";
         args[4]="-nb";
         args[5]="100";
         args[6]="-tmth";
         args[7]="39.00";
         args[8]="-pp";
         args[9]="//Users//rohit//expert//corpusparaphrase//ppdbllexlphrasal.txt";
      //   args[9]="//Users//rohit//expert//hermes//tmadvanced//ppdbllexlphrasalTest.txt";
         
         args[10]="-tms";

         // args[11]="//Users//rohit//expert//hermes//test1//TM_KESv1.1.xml";
         args[11]="//Users//rohit//expert//hermes//test2//MSFT_EN_ES1.0.utf8.xml";
        // args[11]="//Users//rohit//expert//hermes//test2//sampleTM.tmx";
       //  args[11]="//Users//rohit//expert//hermes//tmadvanced//testTM.tmx";
         args[12]="-tmt";
         //args[13]="//Users//rohit//expert//hermes//test1//TM_KESv1.1.xml";
        //  args[13]="//Users//rohit//expert//hermes//test2//sampleTM.tmx";
         args[13]="//Users//rohit//expert//hermes//test2//MSFT_EN_ES1.0.utf8.xml";
        // args[13]="//Users//rohit//expert//hermes//tmadvanced//testTM.tmx";
         args[14]="-ins";
         //args[15]="//Users//rohit//expert//hermes//test1//Trans_KESv1.0.xml";
         args[15]="//Users//rohit//expert//hermes//test1//Trans_KES144.xml";
        
         // args[15]="//Users//rohit//expert//hermes//test5//4Rohit_Input.xml";
         args[16]="-int";
         //args[17]="//Users//rohit//expert//hermes//test1//Trans_KESv1.0.xml";
         args[17]="//Users//rohit//expert//hermes//test1//Trans_KES144.xml";
       
         // args[17]="//Users//rohit//expert//hermes//test5//4Rohit_Input.xml";
         args[18]="-o";
         args[19]="//Users//rohit//expert//hermes//test5//4Rohit_Input_MSFT_.toksf_out.xml";
         //args[19]="//Users//rohit//expert//hermes//test2//MSFT_EN_ES_out.xml";
         args[20]="-typp";
         args[21]="1,2,3,4";
         args[22]="-filtering";
         args[23]="on";
         args[24]="-inf";
         args[25]="xliff";
         args[26]="-tmf";
         args[27]="tmx";
         args[28]="-pholder";
         args[29]="off";
         args[30]="-inslang";
         args[31]="EN-US";
         args[32]="-intlang";
         args[33]="ES-ES";
         args[34]="-tmslang";
         args[35]="EN-US";
         args[36]="-tmtlang";
         args[37]="ES-EM";
         */
        /*
         args=new String[38];
         args[0]="-bth";
         args[1]="35.0";
         args[2]="-lth";
         args[3]="39.0";
         args[4]="-nb";
         args[5]="100";
         args[6]="-tmth";
         args[7]="39.00";
         args[8]="-pp";
         args[9]="//Users//rohit//expert//corpusparaphrase//ppdbllexlphrasal.txt";
         args[10]="-tms";
         args[11]="//Users//rohit//expert//hermes//tmadvanced//sampleTM.tmx";
         args[12]="-tmt";
         args[13]="//Users//rohit//expert//hermes//tmadvanced//sampleTM.tmx";
         args[14]="-ins";
         args[15]="//Users//rohit//expert//hermes//tmadvanced//sampleInput.tmx";
         args[16]="-int";
         args[17]="//Users//rohit//expert//hermes//tmadvanced//sampleInput.tmx";
         args[18]="-o";
         args[19]="//Users//rohit//expert//hermes//tmadvanced//sampletest";
         args[20]="-typp";
         args[21]="1,2,3,4";
         args[22]="-filtering";
         args[23]="on";
         args[24]="-inf";
         args[25]="tmx";
         args[26]="-tmf";
         args[27]="tmx";
         args[28]="-pholder";
         args[29]="off";
         args[30]="-inslang";
         args[31]="EN-US";
         args[32]="-intlang";
         args[33]="ES-ES";
         args[34]="-tmslang";
         args[35]="en-US";
         args[36]="-tmtlang";
         args[37]="es-ES";
         
         */
        
        
        if(args.length<1){
                System.err.println("Please provide mandatory options (see tmadvanced.sh for a sample run)");
                print_info();
                System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {

            // System.err.println("Processing:"+args[i]);
           /* if ("-ne".equals(args[i]) || "".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Please provide option (on/off) for -ne flag (Named Entity)");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        enableNE = true;
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        enableNE = false;
                        i++;
                    } else {
                        System.err.println("Please provide valid option (on/off) for -ne flag(Named Entity)");
                        System.exit(1);
                    }
                }
            }else*/ 
            if ("-rall".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide option (on/off) for -rall flag");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        Parameters.setRetrieveAll(true);//retrieveAll = true;
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        Parameters.setRetrieveAll(false);//retrieveAll = false;
                        i++;
                    } else {
                        System.err.println("Error:Please provide valid option (on/off) for -rall flag");
                        System.exit(1);
                    }
                }
            } else if ("-tag".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide option (on/off) for -tag flag");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        Parameters.setRemoveTags(true);
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        Parameters.setRemoveTags(false);;
                        i++;
                    } else {
                        System.err.println("Error:Please provide valid option (on/off) for -tag flag");
                        System.exit(1);
                    }
                }
            }else if ("-punc".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide option (on/off) for -punc flag");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        Parameters.setRemovePunctuations(true);//phPunc = true;
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        Parameters.setRemovePunctuations(false);
                        i++;
                    } else {
                        System.err.println("Error:Please provide valid option (on/off) for -punc flag");
                        System.exit(1);
                    }
                }
            }else if ("-num".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide option (on/off) for -num flag");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        Parameters.setReplaceNumWithPlaceholder(true);//phNum = true;
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        Parameters.setReplaceNumWithPlaceholder(false);
                        i++;
                    } else {
                        System.err.println("Error:Please provide valid option (on/off) for -num flag");
                        System.exit(1);
                    }
                }
            } else if ("-filtering".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide option (on/off) for -filtering flag");
                    System.exit(1);
                } else {
                    if (args[i + 1].toLowerCase().equals("on")) {
                        Parameters.setFiltering(true);//filtering = true;
                        i++;
                    } else if (args[i + 1].toLowerCase().equals("off")) {
                        Parameters.setFiltering(false);
                        i++;
                    } else {
                        System.err.println("Error:Please provide valid option (on/off) for -filtering flag");
                        System.exit(1);
                    }
                }
            }else if ("-tok".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide valid tokenization option (no, stanford)");
                    System.exit(1);
                } else {
                    Parameters.setTokenizer(args[++i]);
                }
            } else if ("-tms".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide TM source file name");
                    System.exit(1);
                } else {
                    Parameters.setTMSourceFileName(args[++i]);
                }
            } else if ("-pp".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide paraphrasing file name");
                    System.exit(1);
                } else {
                    Parameters.setParaphraseFileName(args[++i]);
                    Parameters.setParaphrasing(true);
                }
            } else if ("-tmt".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide TM target file name");
                } else {
                    Parameters.setTMTargetFileName(args[++i]);
                }
            } else if ("-typp".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide types of paraphrasing(comma separated without space e.g. -typp 1,2)");
                } else {
                    String sttypp = args[++i];
                    String[] typpl = sttypp.split(",");
                    short[] stypp = new short[typpl.length];
                    for (int intypp = 0; intypp < stypp.length; intypp++) {
                        stypp[intypp] = (short) (Integer.parseInt(typpl[intypp]));
                    }
                    Parameters.setTyPP(stypp);
                }
            } else if ("-pppenalty".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide penalty of paraphrasing (comma separated without space for types 1,2,3,4 respectively e.g. -pppenalty 0.5,0.5,0.5,0.5) ");
                } else {
                    String penalty = args[++i];
                    String[] penaltys = penalty.split(",");
                    double[] dpenaltys = new double[penaltys.length];
                    for (int intypp = 0; intypp < penaltys.length; intypp++) {
                        dpenaltys[intypp] = Double.parseDouble(penaltys[intypp]);
                    }
                    Parameters.setPPPenalties(dpenaltys);
                }
            } else if ("-inslang".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide input source language");
                    System.exit(1);
                } else {
                    Parameters.setInputSourceLanguage(args[++i]);
                }
            } else if ("-intlang".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide input target language");
                    System.exit(1);
                } else {
                    Parameters.setInputTargetLanguage(args[++i]);
                }
            } else if ("-tmslang".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide TM source language");
                    System.exit(1);
                } else {
                    Parameters.setTMSourceLanguage(args[++i]);
                }
            } else if ("-tmtlang".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide TM target language");
                    System.exit(1);
                } else {
                    Parameters.setTMTargetLanguage(args[++i]);
                }
            } else if ("-ins".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error:Please provide input source file name");
                    System.exit(1);
                } else {
                    Parameters.setInputFileName(args[++i]);
                }
            } else if ("-int".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide input target file name");
                    System.exit(1);
                } else {
                    Parameters.setInputTargetFileName(args[++i]);
                }
            } else if ("-o".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide output file name");
                    System.exit(1);
                } else {
                    Parameters.setOutputFileName(args[++i]);
                }
            } else if ("-lth".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide threshold for filtering based on length");
                    System.exit(1);
                } else {
                    Parameters.setLenTH(Double.parseDouble(args[++i]));
                }
            } else if ("-bth".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide threshold for filtering based on maximum gap allowed in edit-distance(beam th)");
                    System.exit(1);
                } else {
                    Parameters.setBeamTH(Double.parseDouble(args[++i]));
                }
            } else if ("-tmth".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide cut off threshold for TM matching ");
                    System.exit(1);
                } else {
                    Parameters.setTMTH(Double.parseDouble(args[++i])); 
                }
            } else if ("-nb".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide nbestlist size(used for filtering) ");
                    System.exit(1);
                } else {
                    Parameters.setNbestSize(Integer.parseInt(args[++i])); 
                }
            }else if ("-infolevel".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please provide nbestlist size(used for filtering) ");
                    System.exit(1);
                } else {
                    Parameters.setInfoLevel(Integer.parseInt(args[++i]));
                }
            }else if ("-maxpara".equals(args[i])) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    System.err.println("Error: Please maximum number of paraphrases allowed per token ");
                    System.exit(1);
                } else {
                    Parameters.setMaxParaPerToken(Integer.parseInt(args[++i]));
                }
            } else if ("-h".equals(args[i]) || "-help".equals(args[i])) {
                print_info();
                System.exit(1);
            } else {
                System.err.println("Error: Invalid option " + args[i] + " at " + i + "th place");
                print_info();
                System.exit(1);
            }
        }

        Parameters.setInputFormat(retrieveFormat(Parameters.getInputFileName()));
        Parameters.setTMFormat(retrieveFormat(Parameters.getTMSourceFileName()));
        
        checkMandatoryParameters();
        
        System.err.println("Calling with following parameters:");
       /*
        Parameters p = new Parameters(
                TMformat,
                inputformat,
                inputSourceLanguage,
                inputTargetLanguage,
                TMSourceLanguage,
                TMTargetLanguage,
                tmsrcfilename,
                tmtgtfilename,
                inputfilename,
                inputtgtfilename,
               // placeholder,
                phTag,
                phPunc,
                        phNum,
                paraphrasing,
                typp,
                pppenalties,//={0,0,0,0};
                filtering,
                lenTH,
                beamTH,
                tmTH,
                nbestsize,
                ppfilename,
                outfilename,
                tokenizer,
                NEtagger,
                retrieveAll,
                infolevel
        );*/

        Parameters.printParameters();
        EDMatch edmatch= new EDMatch();//.efficientparaphrasing(p);

        //Parameters p= new Parameters(TMformat,inputformat,"EN-US","ES-EM",enableNE,typp,enabledouble,placeholder,paraphrasing, filtering,lenTH,beamTH,tmTH,nbestsize,ppfilename,tmsrcfilename,tmtgtfilename,inputfilename ,inputtgtfilename,outfilename,pppenalties);
        //  efficientparaphrasing(TMformat,inputformat,pppenalties,"EN-US","ES-EM",enableNE,typp,enabledouble,placeholder,paraphrasing, filtering,lenTH,beamTH,tmTH,nbestsize,ppfilename,tmsrcfilename,tmtgtfilename,inputfilename ,inputtgtfilename,outfilename);
//        retrievalimprove75(outfilename);
    }
    
     private static String retrieveFormat(String filename){
         
            if(filename.endsWith(".tmx")||filename.endsWith(".tmx.xml"))
                return "tmx";
            else if(filename.endsWith(".xliff")||filename.endsWith(".xliff.xml"))
                return "xliff";
            else return "plain";
    }
    
     private static void checkMandatoryParameters(){
         
         String message="";
         if(Parameters.getTMSourceFileName().equals("")){
            message+="Please provide TM file -tms tmfilename\n";
         }
         
         if(Parameters.getTMFormat().equals("plain") && Parameters.getTMTargetFileName().equals("")){
             System.err.println("TM Format:"+Parameters.getTMFormat());
            message+="If your TM is in text format. Please provide TM target file also -tmt targetfilename\n";
         }
         
         if(Parameters.getInputFileName().equals("")){
            message+="Please provide input file -ins inputFilename\n";
         }
         
         if(!Parameters.isParaphrasing()){
            message+="Please provide paraphrase file -pp paraphraseFilename\n";
         }
         
         if(Parameters.getInputSourceLanguage().equals("")){
            message+="Please provide input source language -inslang inputSourcelanguage\n";
         }
         
         if(Parameters.getInputTargetLanguage().equals("")){
            message+="Please provide input target language -intlang inputTargetlanguage";
         }
         
         if(Parameters.getTMSourceLanguage().equals("")){
            message+="Please provide TM source language -inslang inputSourcelanguage\n";
         }
         
         if(Parameters.getTMTargetLanguage().equals("")){
            message+="Please provide TN target language -intlang inputTargetlanguage\n";
         }
         
         if(Parameters.getOutputFileName().equals("")){
            message+="Please provide output filename -o outputFilename\n";
         }
         
         if(!message.equals("")){
             System.err.println(message);
             print_info();
             System.exit(4);
         }
         
     }
    public static void print_info(){
        
        //System.err.println("-ne: Please provide option (on/off) for -ne flag (Named Entity)");
        //System.err.println("-pholder: Please provide option (on/off) for -pholder ");
        System.err.println("\nOptions are case sensitive, -ins en-US and -ins EN-US are different. Also -INS or Ins are invalid options. ");
        System.err.println("\nMandatory Parameters -----------------------------------\n");
        System.err.println("-tms: TM source file name (tmx file with .tmx extension  or txt file with each segment per line) [mandatory][e.g. -tms sampleTM.tmx]");
        System.err.println("-tmt: TM target file name (txt with each segment per line) [mandatory if -tms is a txt file and not required for tmx][e.g. -tmt sampleTM.txt]");
        System.err.println("-ins: Input source file name (xliff file with .xliff extension or tmx file with .tmx extension or txt file with each segment per line) [mandatory][e.g. -ins sampleInput.tmx]");
        System.err.println("-pp: paraphrasing file name (-pp yourparaphrasefile.txt [please check sample paraphrase file for the format]) [mandatory]");
        System.err.println("-inslang: input (language of file you want to translate) source language [mandatory][e.g. -inslang en-US][default EN-US]");
        System.err.println("-intlang: Please provide input target language (the language in which you want to translate)[note: this option is used for research only] [optional][e.g. -intlang es-ES][default ES-ES]");
        System.err.println("-tmslang: Translation Memory source language [mandatory][e.g. -tmslang en-US][default EN-US]");
        System.err.println("-tmtlang: Translation Memory target language [mandatory][e.g. -tmtlang es-ES][default ES-ES]");
        System.err.println("-o: Output file name [mandatory][e.g. -o myparatm]");
        
        System.err.println("\nOptional Parameters -----------------------------------\n");
        System.err.println("-int: Input target file name (txt with each segment per line) [note: this option is used for research only] [optional][ if -tms is a txt file, please provide the corresponding target file]");
        //System.err.println("-filtering: Option to enable filtering so that only a set of segments above a certain threshold participate in paraphrasing, please provide option (on/off) [optional, default: on]");
        System.err.println("-lth: Threshold for filtering based on length [ignored when -filtering off][optional, default: 39.0 ]");
        System.err.println("-bth: Threshold for filtering based on maximum gap allowed in edit-distance(beam threshold)[ignored when -filtering off][optional, default: 35.0 ]");
        System.err.println("-tmth: Cut off threshold for TM matching, only segments having similarity above this threshold will be used for paraphrasing [ignored when -filtering off][optional, default: 39.0 ]");
        System.err.println("-nb: N-best-list size (e.g. if -nb 100, not more than top 100 candidates participate for paraphrasing) [ignored when -filtering off][optional, default: 100 ]");
        System.err.println("-tok: Tokenization option (no: tokenization or any kind of preprocessing is not performed, stanford: used stanford tokenizer for tokenization and also remove tags before processing) [optional, default: stanford]");
        System.err.println("-tag: Remove tags when calculating fuzzy score [optional, default: on]");
        System.err.println("-punc: Remove punctuation when calculating fuzzy score [optional, default: on]");
        System.err.println("-num: Replace number with a placeholder when calculating fuzzy score [optional, default: off]");
        System.err.println("-infolevel: 0 for less information, 1 for moderate information and 2 for huge information e.g. -infolevel 1) [optional, default: 0, less information]");
        System.err.println("-typp: Types of paraphrasing (comma separated e.g. -typp 1,2 (note: no space between options -typp 1,2 and not -typp 1, 2 or -typp 1 , 2  )) [optional, default: all four types of paraphrases 1,2,3,4]");
       // System.err.println("-pppenalty: Penalty of paraphrasing (comma separated values between 0(no penalty) and 1 without space for types 1,2,3,4 respectively e.g. -pppenalty 0.5,0.5,0.5,0.5) (default no penalty: 0,0,0,0 )");
        //  System.err.println("-inf: Input file format (plain|xliff|tmx) (mandatory)");
      //  System.err.println("-tmf: TM file format (plain|xliff|tmx) (mandatory)");
        System.err.println("\nPrint Help -----------------------------------\n");
        System.err.println("-h or -help: Print this information\n");
        
    }


}
