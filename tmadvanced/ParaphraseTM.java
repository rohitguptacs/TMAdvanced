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

import tmadvanced.data.ExtToken;
import tmadvanced.data.ExtTokenPP;
import tmadvanced.data.Paraphrase;
import tmadvanced.data.Token;
import tmadvanced.data.ppType;
import tmadvanced.EDMatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;
import tmadvanced.matching.LevDistWithParaphrasing;
import tmadvanced.Parameters;
import tmadvanced.Print;

/**
 * Collect all paraphrased sentences using sentencePP and ReadFile
 *
 * @author Rohit Gupta
 */
public class ParaphraseTM {

    ArrayList<SentencePP> spplist = new ArrayList();
    HashMap<String, ExtTokenPP> usedPhrasalPP = new HashMap();
    HashMap<String, HashMap<String, Double>> usedLexicalPP = new HashMap();
    Set set = new HashSet();
    private static final int LIMIT = Parameters.getMaxParaPerToken();  // limit on maximum paraphrases per token
    int unique = 0, nonunique = 0; // storing counts of paraphrases occured per sentence
    int sentnum = 0;
    int nullcount=0;
    ParaphraseTM(ArrayList<Token[]> sentences, HashMap<String, ArrayList<String>> ppdict, short[] types) {
        System.err.println("Processing TM");
        for (Token[] sentence : sentences) {
            /// rename sentencepp to avoid confusion
            // ExtToken[] sext = sentencePP(sentence, ppdict, types);
            ExtToken[] sext = newSentencePP(sentence, ppdict, types);//changed on 13 Sep  2015
            SentencePP spp = new SentencePP(sext);
            spplist.add(spp);

        }

    //    System.out.println("TOTAL PARAPHRASES USED FOR TM:" + set.size());
    //    System.out.println("TOTAL PARAPHRASES USED BY ALL SENTENCES UNIQUE:" + unique);
    //    System.out.println("TOTAL PARAPHRASES USED BY ALL SENTENCES NONUNIQUE:" + nonunique);
    //    System.out.println("AVERAGE PARAPHRASES USED PER SENTENCE UNIQUE" + (double) unique / (double) sentences.size());
    //    System.out.println("AVERAGE PARAPHRASES USED PER SENTENCE NONUNIQUE" + (double) nonunique / (double) sentences.size());
        //  System.out.println("\nType234Used:");
        //  int sizetype2=0;
        //  int sizetype34=0;
        Set type2set = new HashSet();
        Set type34set = new HashSet();
        Set type34setrt = new HashSet();
        for (String ke : usedPhrasalPP.keySet()) {
            //    System.out.println("KEY:"+ke);
            ExtTokenPP extpp = usedPhrasalPP.get(ke);
            // sizetype34=sizetype34+extpp.getType34PP().size();
            for (Paraphrase pp : extpp.getType34PP()) {
                //       System.out.print(" Type:"+pp.getType()+" S:"+pp.getleft()+" T:"+pp.getright()+" |");
                type34set.add(pp.getright());
                type34set.add(pp.getleft());
                type34setrt.add(pp.getright());
            }
            //    System.out.println("\nType2Used:");
            //     sizetype2=sizetype2+extpp.getType2PP().keySet().size();
            for (String pp : extpp.getType2PP().keySet()) {
                //        System.out.print(pp+" ");
                type2set.add(pp);
            }
            //    System.out.println();
        }
        int sizetype1 = 0;
        for (String key : usedLexicalPP.keySet()) {
            //       System.out.println("\nType1Used-Key:"+key);

            HashMap<String, Double> hmpp = usedLexicalPP.get(key);
            sizetype1 = sizetype1 + hmpp.size();
            for (String pp : hmpp.keySet()) {
                //       System.out.print(pp+" ");
            }
        }
     //   System.out.println("SIZE_TYPE1:" + sizetype1 + " SIZE_TYPE2:" + type2set.size() + " SIZE_TYPE34_all:" + type34set.size() + "SIZE_TYPE34_rightside:" + type34setrt.size());
     //   System.out.println("ALL_SIZE_TYPE1234:" + (sizetype1 + type2set.size() + type34set.size()));
     //   if(nullcount>0){
     //       System.err.println("Error: NULL count "+nullcount);
     //       System.exit(15);
     //   }
        
    }

    ArrayList<SentencePP> getSentencePP() {
        return spplist;
    }

    boolean isValidType(final short[] types, final short type) {
        boolean flag = false;
        for (int i = 0; i < types.length; i++) {
            if (type == types[i]) {
                flag = true;
            }
        }
        return flag;
    }

    ppType getType(String src, String s) {  
        src = src.trim();
        s = s.trim();
        String[] srcl = src.split("\\s+"); 
        String[] tarl = s.split("\\s+");
        ppType ppt;
        int start =0;// 23 Sep 2015
        int back = 0;
        //    int index=0;
        String left = "";
        String right = "";
        int i=0;
        for (i = 0; i < srcl.length && i < tarl.length; i++) {
            if (!srcl[i].equals(tarl[i])) {
                //start = i;
                break;
            }
        }
        start=i; // first non matching index or end index in case of AB -- ABC
        int j;
        for (i = srcl.length,  j = tarl.length; (i-start) > 0 && (j-start) > 0; i--, j--) {
            if (srcl[i - 1].equals(tarl[j - 1])) {
                back++;
            } else {
                break;
            }
        }
      //  int slen = srcl.length;
      //  int tlen = tarl.length;
        
       

        int slen = srcl.length - start - (back);
        int tlen = tarl.length - start - (back);
        if (slen == 0 || tlen == 0) {
            
            if (back > 0) {
                back--;    // ABC->AC or ABC->BC
           //     slen++;
           //     tlen++;
            } else if(start>0){     
                start--;    //ABC->AB
                //  slen++;tlen++;
            }
        }
        
         slen = srcl.length - start - back;
         tlen = tarl.length - start - back;
        if(slen==0 || tlen==0){
            System.err.println(src+"|||"+s); System.err.println(left+"|||"+right);
            System.exit(16);
        }
       // System.err.println(src+"|||"+s); System.err.println(left+"|||"+right);
        for ( i = start; i < start + slen; i++) {
            left = left + srcl[i] + " ";
        }
        for ( i = start; i < start + tlen; i++) {
            right = right + tarl[i] + " ";
        }
        
        left = left.trim();
        right = right.trim();
        
      //  System.err.println(src+"|||"+s); System.err.println(left+"|||"+right);
        //if(left.equals("")||right.equals("")||left.isEmpty()||right.isEmpty()){System.exit(14);}
        if(left.equals("")||right.equals("")){nullcount++; System.err.println(src+"|||"+s); System.err.println(left+"|||"+right);System.exit(13);}
   
        //if(left.isEmpty()||right.isEmpty()){System.exit(14);}

        if (srcl.length != tarl.length) {
            ppt = new ppType(left, right, start, slen, tlen, (short) 4);
            return ppt;
        } else { //when source and target have equal length
            if (srcl.length == 1) { // when source and target have length 1
                ppt = new ppType(left, right, start, slen, tlen, (short) 1);
                return ppt;
            } else { // when source and target have multiple words
                if (slen == 1 && tlen == 1) { // after reduction source and target have equal length 1
                    ppt = new ppType(left, right, start, slen, tlen, (short) 2);
                    return ppt;
                } else { // after reduction soure and target have multiple words
                    ppt = new ppType(left, right, start, slen, tlen, (short) 3);
                    return ppt;
                }
            }
        }
    }
    
 
    String getKey(String oldkey, String curcontext, int stIndex) {
        
        String dummy= "GGGG"+stIndex+"PPPP";
        if (oldkey.equals("||N||A||")) {
       // if (oldkey==null) {
            return ("NA" + curcontext+ dummy);
            //System.err.println("KeyChanged at :"+ppt.getIndex()+" "+key[i+ppt.getIndex()]+" SRCNOW:"+src+"\n----\n");
        } else {
            return (oldkey += ("NA" + curcontext+dummy));
        }

    }

    ExtToken[] newSentencePP(Token[] sentence, HashMap<String, ArrayList<String>> ppdict, final short[] types) {
        ExtToken[] extsentence;
        Paraphrase[][] aap = new Paraphrase[sentence.length][LIMIT];
        int[] aapin = new int[sentence.length];
        String[] key = new String[sentence.length];
        for (int si = 0; si < sentence.length; si++) {
            key[si] = "||N||A||";
        }
        extsentence = new ExtToken[sentence.length];
        Set sentset = new HashSet();
        ArrayList<String> sentlist = new ArrayList();
        if((sentnum%10000)==0)System.out.println("Processed sentence:" + sentnum++);
      //  EDMatch.printtokens(sentence);
        for (int i = 0; i < sentence.length; i++) {
            String src = "";
            for (int j = i; j < sentence.length; j++) {
                if (aapin[i] < LIMIT) {
                    src = src + " " + sentence[j].getText();
                    src = src.trim();
                    //  System.err.println("Input-> Src:"+src);
                    if (ppdict.containsKey(src)) {
                        set.add(src);//Only for counting
                        
                        ArrayList<String> als = ppdict.get(src); //list of paraphrases for this string
                        boolean[] updatekey = new boolean[1000];
                        for (String s : als) {
                            sentset.add(s);
                            sentlist.add(s);
                            ppType ppt = getType(src, s);
                            //    System.err.println("SRC_getType:"+src+" para:"+s);
                            if (isValidType(types, ppt.getType())) {
                                if (ppt.getType() != 1 && aapin[i + ppt.getIndex()] < LIMIT) {
                                    //      System.err.println("Type:"+ppt.getType()+" Index:"+ppt.getIndex()+" LS:"+ppt.getSrclen()+" LT:"+ppt.getTgtlen()+" S:"+ppt.getLeft()+" T:"+ppt.getRight());
                                    Paraphrase pp;
                                    pp = new Paraphrase(ppt.getLeft(), ppt.getRight(), i, ppt.getType());
                                    aap[i + ppt.getIndex()][aapin[i + ppt.getIndex()]] = pp;
                                    aapin[i + ppt.getIndex()] = aapin[i + ppt.getIndex()] + 1;
                                    //String keysrc=getKey(i,j,ppt.getIndex(), sentence);
                                    if (!updatekey[i + ppt.getIndex()]) {
                                        key[i + ppt.getIndex()] = getKey(key[i + ppt.getIndex()], src, ppt.getIndex());
                                      //  System.err.println(key[i + ppt.getIndex()]);
                                       // printParaphrase(pp);
                                        updatekey[i + ppt.getIndex()] = true;
                                    }
                                } else if (ppt.getType() == 1) {
                                    if (usedLexicalPP.containsKey(ppt.getLeft())) {
                                        if (!usedLexicalPP.get(ppt.getLeft()).containsKey(ppt.getRight())) {
                                            HashMap<String, Double> tmp = usedLexicalPP.get(ppt.getLeft());
                                            tmp.put(ppt.getRight(), Double.NaN);
                                            usedLexicalPP.put(ppt.getLeft(), tmp);
                                        }
                                    } else {
                                        HashMap<String, Double> tmp = new HashMap();
                                        tmp.put(ppt.getRight(), Double.NaN);
                                        usedLexicalPP.put(ppt.getLeft(), tmp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        unique = unique + sentset.size();
        nonunique = nonunique + sentlist.size();
       // printKeys();
        for (int i = 0; i < sentence.length; i++) {
            ExtToken exttk = new ExtToken(sentence[i], key[i]);
            //System.err.print(key[i]+" ||| ");
            ArrayList<Paraphrase> alpp34 = new ArrayList();
            // System.err.println(aap[i].length);
            HashMap<String, Short> pp12hmap = new HashMap();
            for (int k = 0; k < aap[i].length && k < aapin[i]; k++) {
                if (aap[i][k].getType() == 1) {
                    //do nothing
                } else if (aap[i][k].getType() == 2) {
                    pp12hmap.put(aap[i][k].getright(), aap[i][k].getType());
                } else {
                    alpp34.add(aap[i][k]);
                }
            }
            ExtTokenPP extokenpp = new ExtTokenPP(alpp34, pp12hmap);

            if (Parameters.getInfoLevel()>0 && usedPhrasalPP.containsKey(key[i])) {
                checkUpdateProcess(key[i], extokenpp);
            }
            usedPhrasalPP.put(key[i], extokenpp);
            extsentence[i] = exttk;
        }
        
        //System.err.println();
        return extsentence;
    }

    void printKeys() {
        System.err.println("All Keys upto sentence " + sentnum + ":");
        for (Entry<String, ExtTokenPP> s : usedPhrasalPP.entrySet()) {
            System.err.println("For key " + s.getKey() + " before sentence num " + sentnum + ":");
            printExtTokenPP(s.getValue(), s.getKey());
        }
    }
    void printParaphrase(Paraphrase pp){
        System.err.println("PP:"+pp.getleft()+" ||| "+pp.getright());
 
    }
    boolean checkType2(HashMap<String, Short> old2, HashMap<String, Short> new2) {
       // Set<Entry<String, Short>> sleso = old2.entrySet();
       // Set<Entry<String, Short>> slesn = new2.entrySet();
        String o2 = "";
        String n2 = "";
        for (String pp : old2.keySet()) {
            o2 += pp;
        }
        for (String pp : new2.keySet()) {
            n2 += pp;
        }
        if (!o2.equals(n2)) {
            System.err.println("o2:" + o2);
            System.err.println("n2:" + n2);
        }
        return o2.equals(n2);
    }
    
    boolean checkType34(ArrayList<Paraphrase> old34, ArrayList<Paraphrase> new34){
        String o34="";
        String n34="";
        for(Paraphrase pp:old34){
            o34=o34+pp.getleft()+pp.getright();
        }
        for(Paraphrase pp:new34){
            n34=n34+pp.getleft()+pp.getright();
        }
        if (!o34.equals(n34)) {
            System.err.println("o34:" + o34);
            System.err.println("n34:" + n34);
        }
        return o34.equals(n34);
    }

    void printExtTokenPP(ExtTokenPP tk, String key) {
        if (tk == null) {
            System.err.print(tk);
            return;
        }
        System.err.println("Type2:");
        Print.printParaphrases(tk.getType2PP(), key);
        System.err.println("Type34:");
        Print.printParaphrases(tk.getType34PP());
    }

    void checkUpdateProcess(String key, ExtTokenPP newtk) {
        if (key.equals("||N||A||")) {
            return;
        }
      //  System.err.println("Key already present, checking update for Key:"+key);
        ExtTokenPP oldtk = usedPhrasalPP.get(key);
        //System.err.println(oldtk);
        if ((null == oldtk) != (null == newtk)) {
            if (oldtk != null) {
                System.err.println("oldtk:");
                printExtTokenPP(oldtk, key);
            }
            if (newtk != null) {
                System.err.println("newtk:");
                printExtTokenPP(newtk, key);
            }
            System.err.println("Error: Key:" + key + "Some Update process differ ");
            System.exit(11);
        }
        if (null != oldtk && null != newtk) {
            if (!checkType2(oldtk.getType2PP(), newtk.getType2PP())) {
                System.err.println("oldtk:");
                printExtTokenPP(oldtk, key);
                System.err.println("newtk:");
                printExtTokenPP(newtk, key);
                System.err.println("Error: Type2 Update process differ ");
                System.exit(22);
            }
            if (!checkType34(oldtk.getType34PP(), newtk.getType34PP())) {
                System.err.println("oldtk:");
                printExtTokenPP(oldtk, key);
                System.err.println("newtk:");
                printExtTokenPP(newtk, key);
                System.err.println("Error: Type34 Update process differ ");
                System.exit(34);
            }
            
        }

    }

    ExtTokenPP getUsedPhrasalPPbyKey(String key) {
        return usedPhrasalPP.get(key);
    }

    HashMap<String, Double> getUsedLexicalPPbyKey(String key) {
        return usedLexicalPP.get(key);
    }

    HashMap<String, ExtTokenPP> getUsedPhrasalPPDictionary() {
        return usedPhrasalPP;
    }

    HashMap<String, HashMap<String, Double>> getUsedLexicalPPDictionary() {
        return usedLexicalPP;
    }

}
