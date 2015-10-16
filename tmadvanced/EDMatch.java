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

import tmadvanced.files.ReadXliffFile;
import tmadvanced.files.ReadTMXFile;
import tmadvanced.files.ReadTargetFile;
import tmadvanced.files.ReadFile;
import tmadvanced.data.PPPair;
import tmadvanced.data.ExtToken;
import tmadvanced.data.ExtTokenPP;
import tmadvanced.data.Token;
import tmadvanced.data.LdPPSPair;
import tmadvanced.data.Match;
import tmadvanced.data.Paraphrase;
import evaluation.retrievalimprove75;
import tmadvanced.matching.LevenshteinDistance;
import tmadvanced.matching.LevDistWithParaphrasing;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Comparator;
import java.lang.Exception;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.text.NumberFormat;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Rohit Gupta
 */
public class EDMatch {

    /**
     * @param str
     * @param cand
     */
    static ArrayList<Token[]> inputtokens;// = null;
    static ArrayList<String> tgtinput;// = null;
    static ArrayList<String> tgtTM;// = null;
    static ArrayList<Token[]> tmsrctokens;// = null;

    static HashMap<String, ExtTokenPP> usedPhrasalPP;// = null;
    static HashMap<String, HashMap<String, Double>> usedLexicalPP;//= null;

    private double calcSimilarity(final Token[] str,
            final Token cand[]) {
        LevenshteinDistance dc = new LevenshteinDistance();
        if (str.length == 0 && cand.length == 0) {
            // empty token lists - can't calculate similarity
            return 0;
        }
        int ld = dc.compute(str, cand);
        double similarity = (100.0 * (Math.max(str.length, cand.length) - ld)) / Math.max(str.length, cand.length);
        return similarity;
    }

    public <T> T[] reverse(T[] array) {
        T[] copy = array.clone();
        Collections.reverse(Arrays.asList(copy));
        return copy;
    }

    /**
     *
     * @param str
     * @param cand
     * @param score edit distance
     * @return
     */
    private  double calcSimilarityPP(int str, int cand, int score
    ) {

        double similarity = (100.0 * (Math.max(str, cand) - score)) / Math.max(str, cand);
        // System.out.println("ED:"+score+"Sim:"+similarity+" input:"+str+" Match:"+cand);
        return similarity;
    }

    private  double calcSimilarityPP(int str, int cand, double score
    ) {

        double similarity = (100.0 * (Math.max(str, cand) - score)) / Math.max(str, cand);
        if(Parameters.getInfoLevel()>0){System.out.println("ED:" + score + "Sim:" + similarity + " input:" + str + " Match:" + cand);}
        return similarity;
    }

    
    /**
     * extract matches in three steps step1: Filter using length step2: find
     * matches using simple edit distance step3: Apply paraphrasing to matches
     * find from step 1
     *
     *
     * @param th threshold for step 1 filtering using edit distance
     * @param max filtering too many extracted using simple edit distance
     * @param flag type of paraphrasing '-p' or '-pa'
     * @param inputtokens user input file tokens
     * @param tmsrctokens tm source tokens
     * @param tgtTM tm target tokens
     * @param tgtinput expected target for user input file
     * @param outfile output file in xml format
     * @return
     */
    private ArrayList<MatchStore> extractMatchAboveTH(ArrayList<Token[]> inputtokens, ArrayList<Token[]> tmsrctokens, ArrayList<SentencePP> tmsrcexttokens, ArrayList<String> tgtTM, ArrayList<String> tgtinput) {
        ArrayList<MatchStore> listms = new ArrayList();

        try {

            int sno = -1;

            
            XMLOutputFactory xmlOutputFactoryinfo = XMLOutputFactory.newInstance();

            XMLEventWriter xmlEventWriterinfo = xmlOutputFactoryinfo
                    .createXMLEventWriter(new FileOutputStream(Parameters.getOutputFileName() + ".info.xml"), "UTF-8");
            XMLEventFactory eventFactoryinfo = XMLEventFactory.newInstance();
            XMLEvent endinfo = eventFactoryinfo.createDTD("\n");
            if(Parameters.getInfoLevel()>0){
                xmlEventWriterinfo.add(eventFactoryinfo.createStartDocument());
                xmlEventWriterinfo.add(endinfo);
            }
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

            XMLEventWriter xmlEventWriter = xmlOutputFactory
                    .createXMLEventWriter(new FileOutputStream(Parameters.getOutputFileName() + ".tmx"), "UTF-8");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");

            printTMXHeader(xmlEventWriter, eventFactory, end);

            //System.err.println("extractMatchAboveTH: TM size " + tmsrctokens.size() + " Input size " + inputtokens.size());
            for (Token[] sen : inputtokens) {
                MatchStore m = new MatchStore(sen);
                sno++;
                System.err.print("Executing->" + sno + " :");
                Print.printtokens(sen);

                int index = 0;
                List<Match> thmatches = new ArrayList();
                for (Token[] tmsen : tmsrctokens) {
                    double lenratio = (100.0 * Math.min(tmsen.length, sen.length)) / (1.0 * Math.max(tmsen.length, sen.length));
                    //    System.out.println("LenRatio:"+lenratio);
                    if (lenratio >= Parameters.getLenTH()) {
                        double sim = calcSimilarity(tmsen, sen);
                        //    printtokens(tmsen);
                        // printtokens(sen);
                        //    System.out.println(" SIM:"+sim+" th:"+th);
                        if (Parameters.getLenTH() < sim) {
                            thmatches.add(new Match(index, sim));
                        }

                    }
                    index++;
                }

                Collections.sort(thmatches);
                Collections.reverse(thmatches);
                ArrayList<SentencePP> topmatches = new ArrayList();
                if (!thmatches.isEmpty()) {
                    double maxsim = thmatches.get(0).similarity();
                   // System.err.print("maxsim=" + maxsim + " :");
                    for (int i = 0; i < Parameters.getNbestSize() && i < thmatches.size() && Parameters.getBeamTH() >= (maxsim - thmatches.get(i).similarity()); i++) {
                        //System.out.println("th="+th+" "+maxsim+" "+thmatches.get(i).sim+" "+);
                        // System.err.print(thmatches.get(i).index+" "+thmatches.get(i).sim+":");// commented on 8 July 2015
                        //      printtokens(tmsrctokens.get(thmatches.get(i).index));
                        topmatches.add(tmsrcexttokens.get(thmatches.get(i).getId()));

                    }
                }
                double maxsim = -1.0;
                int maxmatchid = -1;
                LdPPSPair bestmatch = new LdPPSPair();
                index = 0;
                if(Parameters.getInfoLevel()>0){System.out.print("Processing:");}
                //  int tmsno=0;
                double[] pppenalty = new double[4];// Not used
                for (SentencePP tmsen : topmatches) {
                    // System.err.print(++tmsno+"=");
                    LdPPSPair ldpair;
                    double sim;
                    //System.out.print(" " + thmatches.get(index).getId());
                    LevDistWithParaphrasing dc = new LevDistWithParaphrasing(usedLexicalPP, usedPhrasalPP);

                    ldpair = dc.compute(sen, tmsen.get(), pppenalty);
                    if (ldpair == null) {
                        continue;
                    }
                    sim = calcSimilarityPP(sen.length, ldpair.length(), ldpair.getEditDistance());
                    if(Parameters.getInfoLevel()>0){
                        Print.printtokens(tmsen.get());
                        Print.printtokens(sen);
                        System.out.println(" SIM:" + sim);
                    }
                    if (maxsim < sim) {
                        maxsim = sim;
                        maxmatchid = index;
                        bestmatch = ldpair;

                    }

                    if (sim > Parameters.getTMTH()) {
                        m.add(new Match(tmsen.get(), thmatches.get(index).getId(), sim, ldpair));
                    }
                    if (thmatches.get(index).similarity() > Parameters.getTMTH()) {
                        m.add(new Match(tmsen.get(), thmatches.get(index).getId(), thmatches.get(index).similarity()));
                    }
                    index++;

                }
                m.SortAndReverse();//think
                if(Parameters.getInfoLevel()>0)printThisSegmentinInfoFile(xmlEventWriterinfo, eventFactoryinfo, endinfo, sno, maxsim, maxmatchid, topmatches, thmatches, sen, bestmatch, m);
                printThisSegmentinTMXFile(xmlEventWriter, eventFactory, end, sno, maxsim, maxmatchid, topmatches, thmatches, sen, bestmatch, m, tmsrcexttokens);

            }

            xmlEventWriter.add(eventFactory.createEndElement("", "", "body"));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndDocument());
            xmlEventWriter.close();

            xmlEventWriterinfo.add(eventFactory.createEndDocument());
            xmlEventWriterinfo.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return listms;
    }

    public static void printThisSegmentinInfoFile(XMLEventWriter xmlEventWriter, XMLEventFactory eventFactory, XMLEvent end, int sno, double maxsim, int maxmatchid, ArrayList<SentencePP> topmatches, List<Match> thmatches, Token[] sen, LdPPSPair bestmatch, MatchStore m) {

        String input = "";
        for (Token sen1 : sen) {
            input += sen1.getText() + " ";
        }
        
        String exptd = "";
        
        if(!Parameters.getInputTargetFileName().equals("")){ //no expected target available, expected target is used for research only 
            exptd = tgtinput.get(sno);
        }
        String match = "";
        for (short i = 0; i < bestmatch.length(); i++) {
            match += bestmatch.getTokenAt(i).getText() + " ";
        }
        String ms = Math.round(maxsim * 100.0) / 100.0 + "";

        try {

            StartElement tuStartElement = eventFactory.createStartElement("", "", "segment");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("id", sno + ""));
            xmlEventWriter.add(end);

            createNode(xmlEventWriter, "input", detokenize(input));
            createNode(xmlEventWriter, "exptd", exptd);

            tuStartElement = eventFactory.createStartElement("", "", "match");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("score", ms + ""));
            xmlEventWriter.add(eventFactory.createAttribute("prevrank", maxmatchid + ""));
            Characters characters = eventFactory.createCharacters(detokenize(match));
            xmlEventWriter.add(characters);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "match"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "paraphrases");
            xmlEventWriter.add(tuStartElement);
            for (PPPair item : bestmatch.getMatchedParaphrases()) {
                tuStartElement = eventFactory.createStartElement("", "", "pp");
                xmlEventWriter.add(tuStartElement);
                xmlEventWriter.add(eventFactory.createAttribute("index", item.getLocation() + ""));
                xmlEventWriter.add(eventFactory.createAttribute("type", item.getParaphrase().getType() + ""));
                xmlEventWriter.add(eventFactory.createAttribute("text", item.getParaphrase().getleft() + ""));
                xmlEventWriter.add(eventFactory.createAttribute("para", item.getParaphrase().getright() + ""));
                xmlEventWriter.add(eventFactory.createEndElement("", "", "pp"));
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "paraphrases"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "retvd");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMTargetLanguage() + ""));

            int id = -1;
            if (!topmatches.isEmpty()) {
                String french = tgtTM.get(thmatches.get(maxmatchid).getId());
                id = thmatches.get(maxmatchid).getId();
                characters = eventFactory.createCharacters(french);
                xmlEventWriter.add(characters);
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "retvd"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "retvd");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMSourceLanguage() + ""));
            xmlEventWriter.add(eventFactory.createAttribute("id", id + ""));
            if (!topmatches.isEmpty()) {
                SentencePP spp = topmatches.get(maxmatchid);
                ExtToken[] english = spp.get();
                String en = "";
                for (ExtToken extk : english) {
                    en += extk.getToken().getText() + " ";
                }
                characters = eventFactory.createCharacters(detokenize(en));
                xmlEventWriter.add(characters);
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "retvd"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "prevretvd");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMTargetLanguage() + ""));

            id = -1;
            double prevscore = 0.0;
            if (!topmatches.isEmpty()) {
                String french = tgtTM.get(thmatches.get(0).getId());
                id = thmatches.get(0).getId();
                prevscore = thmatches.get(0).similarity();
                characters = eventFactory.createCharacters(french);
                xmlEventWriter.add(characters);
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "prevretvd"));
            xmlEventWriter.add(end);

            String sprevscore = Math.round(prevscore * 100.0) / 100.0 + "";
            tuStartElement = eventFactory.createStartElement("", "", "prevretvd");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMSourceLanguage() + ""));
            xmlEventWriter.add(eventFactory.createAttribute("prevscore", sprevscore + ""));

            if (!topmatches.isEmpty()) {
                SentencePP spp = topmatches.get(0);
                ExtToken[] english = spp.get();
                String en = "";
                for (ExtToken extk : english) {
                    en += extk.getToken().getText() + " ";
                }
                characters = eventFactory.createCharacters(detokenize(en));
                xmlEventWriter.add(characters);
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "prevretvd"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "thmatches");
            xmlEventWriter.add(tuStartElement);

            xmlEventWriter.add(eventFactory.createAttribute("th", Parameters.getTMTH() + ""));
            tuStartElement = eventFactory.createStartElement("", "", "edmatches");
            xmlEventWriter.add(tuStartElement);

            for (Match mch : m.getMatches()) {
                if (!mch.isppApplied()) {
                    tuStartElement = eventFactory.createStartElement("", "", "edmatch");
                    xmlEventWriter.add(tuStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("id", mch.getId() + ""));
                    double score = Math.round(mch.similarity() * 100.0) / 100.0;
                    xmlEventWriter.add(eventFactory.createAttribute("score", score + ""));
                    String en = "";
                    for (ExtToken edMatch : mch.getEDMatch()) {
                        en += edMatch.getToken().getText() + " ";
                    }
                    characters = eventFactory.createCharacters(en);
                    xmlEventWriter.add(characters);
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "edmatch"));
                    xmlEventWriter.add(end);

                    tuStartElement = eventFactory.createStartElement("", "", "target");
                    xmlEventWriter.add(tuStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMTargetLanguage() + ""));
                    characters = eventFactory.createCharacters(tgtTM.get(mch.getId()));
                    xmlEventWriter.add(characters);
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "target"));
                    xmlEventWriter.add(end);

                }
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "edmatches"));
            xmlEventWriter.add(end);

            tuStartElement = eventFactory.createStartElement("", "", "ppmatches");
            xmlEventWriter.add(tuStartElement);

            for (Match mch : m.getMatches()) {
                if (mch.isppApplied()) {
                    tuStartElement = eventFactory.createStartElement("", "", "ppmatch");
                    xmlEventWriter.add(tuStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("id", mch.getId() + ""));
                    double score = Math.round(mch.similarity() * 100.0) / 100.0;
                    xmlEventWriter.add(eventFactory.createAttribute("score", score + ""));
                    String en = "";
                    for (ExtToken ppMatch : mch.getPPMatch()) {
                        en += ppMatch.getToken().getText() + " ";
                    }
                    characters = eventFactory.createCharacters(en);
                    xmlEventWriter.add(characters);
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "ppmatch"));
                    xmlEventWriter.add(end);

                    tuStartElement = eventFactory.createStartElement("", "", "target");
                    xmlEventWriter.add(tuStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("lang", Parameters.getTMTargetLanguage() + ""));
                    characters = eventFactory.createCharacters(tgtTM.get(mch.getId()));
                    xmlEventWriter.add(characters);
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "target"));
                    xmlEventWriter.add(end);

                }
            }
            xmlEventWriter.add(eventFactory.createEndElement("", "", "ppmatches"));
            xmlEventWriter.add(end);

            String ranking = "";
            for (Match mch : m.getMatches()) {
                if (mch.isppApplied()) {
                    ranking += 1 + " ";
                } else {
                    ranking += 0 + " ";
                }
            }
            createNode(xmlEventWriter, "ranking", ranking);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "thmatches"));
            xmlEventWriter.add(end);

            xmlEventWriter.add(eventFactory.createEndElement("", "", "segment"));
            xmlEventWriter.add(end);

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    private static void printTMXHeader(XMLEventWriter xmlEventWriter, XMLEventFactory eventFactory, XMLEvent end) {
        StartDocument startDocument = eventFactory.createStartDocument();
        try {
            xmlEventWriter.add(startDocument);
            xmlEventWriter.add(end);

            XMLEvent doctype = eventFactory.createDTD("<!DOCTYPE tmx SYSTEM \"tmx14.dtd\">\n");
            xmlEventWriter.add(doctype);
            StartElement configStartElement = eventFactory.createStartElement("",
                    "", "tmx");
            xmlEventWriter.add(configStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("version", "1.4"));

            xmlEventWriter.add(end);
            StartElement headerStartElement = eventFactory.createStartElement("",
                    "", "header");
            xmlEventWriter.add(headerStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("creationtool", "TMAdvanced"));
            xmlEventWriter.add(eventFactory.createAttribute("creationtoolversion", "0.9"));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "header"));
            xmlEventWriter.add(end);
            StartElement bodyStartElement = eventFactory.createStartElement("",
                    "", "body");
            xmlEventWriter.add(bodyStartElement);
            xmlEventWriter.add(end);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static String replaceWithParaphrase(LdPPSPair bestmatch, SentencePP spp) {
        String english = "";
        String otherinfo = "";
        ArrayList<PPPair> alpppair = bestmatch.getMatchedParaphrases();
        ExtToken[] englishEXT = spp.get();
        for (int i = 0; i < englishEXT.length;) {
            int paranum = -1;
            ArrayList<Integer> paranumber = new ArrayList();
            boolean type34 = false;
            ArrayList<Integer> type34num = new ArrayList();
            for (PPPair pppair : alpppair) {
                paranum++;
                if ((pppair.getLocation()) == i) {
                    // break;
                    if (pppair.getParaphrase().getType() == 3 || pppair.getParaphrase().getType() == 4) {
                        type34 = true;
                        type34num.add(paranum);
                    }
                    paranumber.add(paranum);
                }
            }
            if (type34 == false && !paranumber.isEmpty()) {
                paranum = paranumber.get(0);
            } else if (type34 == true) {
                paranum = type34num.get(0);
            }

            if (type34num.size() > 1) {
                System.err.println("Multiple paraphrases for the same location");
                System.exit(19);
            }
            if (paranum != -1 && (alpppair.get(paranum).getLocation()) == i) {
                english = english + alpppair.get(paranum).getParaphrase().getright() + " ";
                i = i + alpppair.get(paranum).getParaphrase().noOfWordsSrc();
                otherinfo = otherinfo + alpppair.get(paranum).getParaphrase().getleft() + "|" + alpppair.get(paranum).getParaphrase().getright() + "||";
            } else {
                //english = english + bestmatch.getTokenAt((short)i).getText() + " ";
                english = english + englishEXT[i].getToken().getText() + " ";
                i++;
            }
        }
        if (otherinfo.endsWith("||")) {
            otherinfo = otherinfo.substring(0, otherinfo.length() - 2);
        }
        return english + "\t" + otherinfo;
    }

    public static String replaceWithParaphraseOld(LdPPSPair bestmatch, SentencePP spp) {
        String english = "";
        String otherinfo = "";
        ArrayList<PPPair> alpppair = bestmatch.getMatchedParaphrases();
        ExtToken[] englishEXT = spp.get();
        for (int i = 0; i < englishEXT.length;) {
            int paranum = -1;
            for (PPPair pppair : alpppair) {
                paranum++;
                if ((pppair.getLocation()) == i) {
                    break;
                }
            }

            if (paranum != -1 && (alpppair.get(paranum).getLocation()) == i) {
                english = english + alpppair.get(paranum).getParaphrase().getright() + " ";
                i = i + alpppair.get(paranum).getParaphrase().noOfWordsSrc();
                otherinfo = otherinfo + alpppair.get(paranum).getParaphrase().getleft() + "|" + alpppair.get(paranum).getParaphrase().getright() + "||";
            } else {
                //english = english + bestmatch.getTokenAt((short)i).getText() + " ";
                english = english + englishEXT[i].getToken().getText() + " ";
                i++;
            }
        }
        if (otherinfo.endsWith("||")) {
            otherinfo = otherinfo.substring(0, otherinfo.length() - 2);
        }
        return english + "\t" + otherinfo;
    }
    

    public static void exmpleTMXwriter() {
        String fileName = "mytm.tmx";
        String rootElement = "tmx";
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        try {
            XMLEventWriter xmlEventWriter = xmlOutputFactory
                    .createXMLEventWriter(new FileOutputStream(fileName), "UTF-8");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            StartDocument startDocument = eventFactory.createStartDocument();
            xmlEventWriter.add(startDocument);
            xmlEventWriter.add(end);

            XMLEvent doctype = eventFactory.createDTD("<!DOCTYPE tmx SYSTEM \"tmx14.dtd\">\n");
            xmlEventWriter.add(doctype);
            StartElement configStartElement = eventFactory.createStartElement("",
                    "", rootElement);
            xmlEventWriter.add(configStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("version", "1.4"));
            xmlEventWriter.add(end);

            StartElement headerStartElement = eventFactory.createStartElement("", "", "header");
            xmlEventWriter.add(headerStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("creationtool", "TMAdvanced"));
            xmlEventWriter.add(eventFactory.createAttribute("creationtoolversion", "0.9"));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "header"));
            xmlEventWriter.add(end);

            StartElement bodyStartElement = eventFactory.createStartElement("", "", "body");
            xmlEventWriter.add(bodyStartElement);
            xmlEventWriter.add(end);

            String note = "note";
            StartElement noteStartElement = eventFactory.createStartElement("", "", note);
            xmlEventWriter.add(noteStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("para", "No"));
            xmlEventWriter.add(eventFactory.createAttribute("pp", "No"));
            xmlEventWriter.add(eventFactory.createAttribute("ed", "No"));
            xmlEventWriter.add(eventFactory.createAttribute("ppused", "No"));
            xmlEventWriter.add(eventFactory.createAttribute("original", "No"));
            xmlEventWriter.add(eventFactory.createEndElement("", "", "note"));
            xmlEventWriter.add(end);

            StartElement tuStartElement = eventFactory.createStartElement("",
                    "", "tu");
            xmlEventWriter.add(tuStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("id", "1"));
            xmlEventWriter.add(end);

            StartElement tuvStartElement = eventFactory.createStartElement("",
                    "", "tuv");
            xmlEventWriter.add(tuvStartElement);
            xmlEventWriter.add(eventFactory.createAttribute("xml:lang", "EN-us"));
            xmlEventWriter.add(end);

            createNode(xmlEventWriter, "seg", "english english english english");
            xmlEventWriter.add(eventFactory.createEndElement("", "", "tuv"));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "tu"));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndElement("", "", "body"));
            xmlEventWriter.add(end);

            // Attribute atr= .
            xmlEventWriter.add(eventFactory.createEndDocument());
            xmlEventWriter.close();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createNode(XMLEventWriter eventWriter, String element, String value) throws XMLStreamException {
        XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
        XMLEvent end = xmlEventFactory.createDTD("\n");
        XMLEvent tab = xmlEventFactory.createDTD("\t");
        //Create Start node
        StartElement sElement = xmlEventFactory.createStartElement("", "", element);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        //Create Content
        Characters characters = xmlEventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = xmlEventFactory.createEndElement("", "", element);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    private  void printThisSegmentinTMXFile(XMLEventWriter xmlEventWriter, XMLEventFactory eventFactory, XMLEvent end, int sno, double maxsim, int maxmatchid, ArrayList<SentencePP> topmatches, List<Match> thmatches, Token[] sen, LdPPSPair bestmatch, MatchStore m,  ArrayList<SentencePP> tmsrcexttokens) {

        try {
            String french = "";
            String isPara = "No";
            String english = "";
            String oldenglishin = "";
            String oldenglishtm = "";
            String replacedEnglish = "";
            String otherinfo = "";
            double prevscore = -1;
            if (!topmatches.isEmpty()) {

                prevscore = thmatches.get(0).similarity();
                for (short i = 0; i < sen.length; i++) {
                    oldenglishin = oldenglishin + sen[i].getText() + " ";
                }
                // ExtToken[] etsent=tmsrcexttokens.get(m.getMatches().get(0).getId()).get();
                ExtToken[] etsent = topmatches.get(maxmatchid).get();
                for (ExtToken et : etsent) {
                    oldenglishtm += et.getToken().getText() + " ";
                }
                oldenglishtm = oldenglishtm.trim();
                if (prevscore >= maxsim) {
                    french = tgtTM.get(thmatches.get(0).getId());
                    for (short i = 0; i < tmsrcexttokens.get(thmatches.get(0).getId()).get().length; i++) {
                        english = english + tmsrcexttokens.get(thmatches.get(0).getId()).get()[i].getToken().getText() + " ";
                    }

                } else {
                    french = tgtTM.get(thmatches.get(maxmatchid).getId());
                    // bestmatch.getMatchedParaphrases().
                    for (short i = 0; i < bestmatch.length(); i++) {
                        english = english + bestmatch.getTokenAt(i).getText() + " ";
                    }
                    String[] enginfo = replaceWithParaphrase(bestmatch, topmatches.get(maxmatchid)).split("\t");
                    replacedEnglish = enginfo[0];
                    //replacedEnglish=replaceWithParaphrase(bestmatch);
                    if (enginfo.length == 2) {
                        otherinfo = enginfo[1];
                    }
                    isPara = "Yes";
                }
                        //System.out.println(french);// commented on 8 July 2015
                //id=thmatches.get(maxmatchid).index;
                if (isPara.equals("Yes") || Parameters.isRetrieveAll()) {

                    StartElement tuStartElement = eventFactory.createStartElement("",
                            "", "tu");
                    xmlEventWriter.add(tuStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("id", sno + ""));
                    xmlEventWriter.add(end);

                    String note = "note";
                    StartElement noteStartElement = eventFactory.createStartElement("",
                            "", note);
                    xmlEventWriter.add(noteStartElement);
                    String ms = Math.round(maxsim * 100.0) / 100.0 + "";
                    String ps = Math.round(prevscore * 100.0) / 100.0 + "";

                    xmlEventWriter.add(eventFactory.createAttribute("para", isPara));
                    xmlEventWriter.add(eventFactory.createAttribute("pp", ms + ""));
                    xmlEventWriter.add(eventFactory.createAttribute("ed", ps + ""));
                    xmlEventWriter.add(eventFactory.createAttribute("ppused", otherinfo + ""));
                    xmlEventWriter.add(eventFactory.createAttribute("input", detokenize(oldenglishin) + ""));
                    xmlEventWriter.add(eventFactory.createAttribute("originalTM", detokenize(oldenglishtm) + ""));

                    xmlEventWriter.add(eventFactory.createEndElement("", "", "note"));
                    xmlEventWriter.add(end);
                    StartElement tuvStartElement = eventFactory.createStartElement("",
                            "", "tuv");
                    xmlEventWriter.add(tuvStartElement);
                    xmlEventWriter.add(eventFactory.createAttribute("xml:lang", Parameters.getTMSourceLanguage()));
                    xmlEventWriter.add(end);

                    createNode(xmlEventWriter, "seg", detokenize(replacedEnglish));
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "tuv"));
                    xmlEventWriter.add(end);

                    StartElement tuvStartElement2 = eventFactory.createStartElement("",
                            "", "tuv");
                    xmlEventWriter.add(tuvStartElement2);
                    xmlEventWriter.add(eventFactory.createAttribute("xml:lang", Parameters.getTMTargetLanguage()));
                    xmlEventWriter.add(end);

                    createNode(xmlEventWriter, "seg", french.trim());

                    xmlEventWriter.add(eventFactory.createEndElement("", "", "tuv"));
                    xmlEventWriter.add(end);
                    xmlEventWriter.add(eventFactory.createEndElement("", "", "tu"));
                    xmlEventWriter.add(end);

                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    public static String detokenize(String s) {
        HashMap<String, String> hmap = new HashMap();
        hmap.put("-LRB-", "(");
        hmap.put("-RRB-", ")");
        hmap.put("-LSB-", "[");
        hmap.put("-RSB-", "]");
        hmap.put("-LCB-", "{");
        hmap.put("-RCB-", "}");
        String[] st = s.split(" ");
        String detoksent = "";
        for (String word : st) {
            if (hmap.containsKey(word)) {
                detoksent += hmap.get(word) + " ";
            } else {
                detoksent += word + " ";
            }
        }
        return detoksent.trim();
    }

    
    /**
     *
     * @param inputtokens
     * @param tmsrctokens
     * @param tgtTM
     * @param tgtinput
     * @param outfile
     * @return
     */
    private ArrayList<MatchStore> extractMatchSimpleEditDistText(ArrayList<Token[]> inputtokens, ArrayList<Token[]> tmsrctokens, ArrayList<String> tgtTM, ArrayList<String> tgtinput, String outfile) {
        ArrayList<MatchStore> listms = new ArrayList();

        class match implements Comparable<match> {

            double sim;
            int index;

            match(double sim, int index) {
                this.sim = sim;
                this.index = index;
            }

            @Override
            public int compareTo(match other) {
                return Double.valueOf(this.sim).compareTo(other.sim);
            }
        }

        try {
            FileWriter fw = new FileWriter(outfile);
            FileWriter fwfrench = new FileWriter("EDEvalfile.txt");
            FileWriter nlp4tmen = new FileWriter("edmatches_nlp4tm.en.txt");
            FileWriter nlp4tmfr = new FileWriter("edmatches_nlp4tm.fr.txt");
            FileWriter nlp4tmscores = new FileWriter("edmatches_nlp4tm.scores.txt");
            int sno = -1;
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fw.write("<document>");

            for (Token[] sen : inputtokens) {
                //   MatchStore m=new MatchStore(sen);
                sno++;
                System.err.print("Executing->" + sno + " :");
                Print.printtokens(sen);

                double maxsim = 0.0;
                int maxmatchid = 0;
                int index = 0;
                List<match> thmatches = new ArrayList();

                for (Token[] tmsen : tmsrctokens) {
                    double sim = calcSimilarity(tmsen, sen);

                    // printtokens(tmsen);
                    // printtokens(sen);
                    //  System.out.println(" SIM:"+sim);
                    if (maxsim < sim) {
                        maxsim = sim;
                        maxmatchid = index;
                    }
                    thmatches.add(new match(sim, index));
                 
                    index++;

                }
                Collections.sort(thmatches);
                Collections.reverse(thmatches);

                for (int i = 0; i < 100; i++) {
                    Token[] bestmatch = tmsrctokens.get(thmatches.get(i).index);
                    String enmatch = "";
                    for (Token bestmatch1 : bestmatch) {
                        enmatch = enmatch + " " + bestmatch1.getText();
                    }
                    nlp4tmen.write(enmatch + "\t");
                    nlp4tmfr.write(tgtTM.get(thmatches.get(i).index).trim() + "\t");
                    nlp4tmscores.write(thmatches.get(i).sim + "\t");
                }
                nlp4tmen.write("\n");
                nlp4tmfr.write("\n");
                nlp4tmscores.write("\n");

                fw.write("<segment id=\"" + sno + "\">");
                for (int i = 0; i < sen.length; i++) {
                    fw.write(sen[i].getText() + " ");
                }
                fw.write('\n');
                fw.write("<exptd>");
                fw.write(tgtinput.get(sno));
                fw.write("</exptd>\n");
                String ms = Math.round(maxsim * 100.0) / 100.0 + "";
                fw.write("<match score=\"" + ms + "\" id=\"" + maxmatchid + "\">");
                Token[] bestmatch = tmsrctokens.get(maxmatchid);
                String french = tgtTM.get(maxmatchid);
                for (Token bestmatch1 : bestmatch) {
                    fw.write(bestmatch1.getText() + " ");
                }
                fw.write("</match>\n");
                fw.write("<retvd lang=\"FR\">");
                fw.write(french);
                fwfrench.write(french.trim() + "\n");
                fw.write("</retvd>\n");
                fw.write("</segment>\n");

                //       listms.add(m);
            }
            fw.write("</document>");
            fw.close();
            fwfrench.close();
            nlp4tmen.close();
            nlp4tmfr.close();
            nlp4tmscores.close();
        } catch (IOException e) {
            System.err.print(e);
        }
        return listms;
    }

    private void readTM() {

        if (Parameters.TMformat.equalsIgnoreCase("tmx")) {
            ReadTMXFile readtm = new ReadTMXFile(Parameters.getTMSourceFileName(), Parameters.getTMSourceLanguage(), Parameters.getTMTargetLanguage());
            tmsrctokens = readtm.getTokenisedSource();
            tgtTM = readtm.getCleanTarget();
        } else if (Parameters.getTMFormat().equalsIgnoreCase("plain")) {
            ReadTargetFile rtf = new ReadTargetFile(Parameters.getTMTargetFileName());
            tgtTM = rtf.getCleanTarget();
            ReadFile rf2 = new ReadFile(Parameters.getTMSourceFileName()); 
            tmsrctokens = rf2.getTokenisedSource();
        } else {
            System.err.println("Please provide a valid TM file format");
            System.exit(1);
        }

    }

    private void readInput() {

        if (Parameters.getInputFormat().equalsIgnoreCase("xliff")) {

            ReadXliffFile readinput = new ReadXliffFile();
            inputtokens = readinput.getSource();
            tgtinput = readinput.getCleanTarget();

        } else if (Parameters.getInputFormat().equalsIgnoreCase("tmx")) {

            ReadTMXFile readinput = new ReadTMXFile(Parameters.getInputFileName(), Parameters.getInputSourceLanguage(), Parameters.getInputTargetLanguage()); //simple reading
            inputtokens = readinput.getTokenisedSource();
            tgtinput = readinput.getCleanTarget();

        }  else if (Parameters.getInputFormat().equalsIgnoreCase("plain")) {
            ReadFile rf = new ReadFile(Parameters.getInputFileName()); //simple reading
            inputtokens = rf.getTokenisedSource();
            //read input target file
            ReadTargetFile rtf2 = new ReadTargetFile(Parameters.getInputTargetFileName());
            tgtinput = rtf2.getCleanTarget();
        } else {
            System.err.println("Please provide a valid Input file format");
            System.exit(1);
        }
    }

    private static ArrayList<SentencePP> paraphraseTM() {
        // read PP dictionary and collect Paraphrases
        CollectPP cpp = new CollectPP(Parameters.getParaphraseFileName(), false);
       // CollectPP cpp = new CollectPP(p.ppfilename, p.placeholder);

        HashMap<String, ArrayList<String>> ppdict = cpp.getPPDictionary();
        System.out.println("PPDICT " + ppdict.entrySet().size());
        // Paraphrase translation memory
        ParaphraseTM cspp = new ParaphraseTM(tmsrctokens, ppdict, Parameters.getTyPP());
        ArrayList<SentencePP> alspp = cspp.getSentencePP();
        usedPhrasalPP = cspp.getUsedPhrasalPPDictionary();
        usedLexicalPP = cspp.getUsedLexicalPPDictionary();
        // delete PP dictionary
        ppdict.clear();
        System.out.println("Collecting paraphrases finished");
        //alspp.get(200).print();
        return alspp;
    }

    public static long bytes2Megabytes(long bytes) {
        return bytes / (1024L * 1024L);
    }

    public static long getMemoryUsageinMB() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        return bytes2Megabytes(memory);

    }

    public EDMatch() {

        long starttime = System.nanoTime();
        readTM();
        readInput();
        long endtime1;

        if (Parameters.isParaphrasing()) { // with paraphrasing

            ArrayList<SentencePP> alspp = paraphraseTM();
            endtime1 = System.nanoTime();
            System.err.println("Total time in Collecting tokens and parphrases(Seconds)=" + TimeUnit.SECONDS.convert(endtime1 - starttime, TimeUnit.NANOSECONDS));
            System.err.println("Total memory usgae in Collecting tokens and parphrases(MB)=" + getMemoryUsageinMB());

            if (!Parameters.isFiltering()) {  //without filtering executing old code
                System.err.println("Error:Fltering is off, execution will be too slow");
                System.err.println("Calling ExtractMatch with filtering off");
                //   extractMatch('a',inputtokens,alspp,tgtTM,tgtinput,outfile);
                System.err.println("NOT implemented");

            } else {   // with filtering 
                if (Parameters.getTMTH() > 0.0) {
                    System.err.println("Calling: extractMatchAboveTH " + " input:" + inputtokens.size() + " TM:" + tmsrctokens.size());
                    extractMatchAboveTH(inputtokens, tmsrctokens, alspp, tgtTM, tgtinput);
               } else {
                    System.err.println("Calling: extractTopMatchOnly filtering:" + Parameters.isFiltering());
                    System.err.println("NOT implemented");
                    // extractTopMatchOnly(lenTH,beamTH, nbest,inputtokens,tmsrctokens, alspp,tgtTM,tgtinput,outfile);
                }
            }

        } else {    // without paraphrasing
            endtime1 = System.nanoTime();
            if (!Parameters.isFiltering()) {
                System.err.println("Total time in Collecting tokens (Seconds)=" + TimeUnit.SECONDS.convert(endtime1 - starttime, TimeUnit.NANOSECONDS));
                System.err.println("Calling ExtractMatch with filtering off, simple edit-distance");
                extractMatchSimpleEditDistText(inputtokens,tmsrctokens,tgtTM,tgtinput,Parameters.getOutputFileName());
            } else {
                System.err.println("Error:Simple (without paraphrasing) match with filtering not implemented yet, please use -filtering off");
                System.exit(1);
            }
        }
        long endtime2 = System.nanoTime();
        System.err.println("Total time in EditdistancePP(Seconds)=" + TimeUnit.SECONDS.convert(endtime2 - endtime1, TimeUnit.NANOSECONDS));
        System.err.println("Complete time(Seconds)=" + TimeUnit.SECONDS.convert(endtime2 - starttime, TimeUnit.NANOSECONDS));

    }

}
