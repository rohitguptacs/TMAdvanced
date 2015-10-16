/*
 * TMAdvanced: A tool to retrive semantically similar matches from a  Translation Memory using paraphrases
 * Copyright 2015 Rohit Gupta, University of Wolverhampton.
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
/**
 *
 * @author Rohit Gupta
 *
 */
package tmadvanced.matching;

import tmadvanced.data.ExtToken;
import tmadvanced.data.ExtTokenPP;
import tmadvanced.EDMatch;
import tmadvanced.data.LdPPSPair;
import tmadvanced.data.PPPair;
import tmadvanced.data.Paraphrase;
import tmadvanced.data.Token;
import java.util.ArrayList;
import java.util.HashMap;
import tmadvanced.Parameters;
import tmadvanced.Print;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class LevDistWithParaphrasing {

    /**
     * Maximal number of items compared.
     */
    private static final int MAX_N = 1000;

    /**
     * Maximum number of paraphrases handled for per token
     */
    private static final int MAX_PP = Parameters.getMaxParaPerToken()+1;
    private final short MAX_OFFSET = 50;

    private HashMap<String, ExtTokenPP> usedPhrasalPP;
    private HashMap<String, HashMap<String, Double>> usedLexicalPP;

    /**
     * Cost array, horizontally to avoid excessive allocation and garbage
     * collection.
     */
    private double[][] distance = new double[MAX_PP][MAX_N + 1];
    /**
     * "Previous" cost array, horizontally to avoid excessive allocation and
     * garbage collection.
     */
    private double[][] prev_dist = new double[MAX_PP][MAX_N + 1];
    private double[] d0 = new double[MAX_N + 1];
    private double[] dp = new double[MAX_N + 1];

    HashMap<Integer, double[]> dold = new HashMap();

    /*   double penalty1;//Penalty for each word in type 1 Praphrasing
     double penalty2;//Penalty for each word in type 2 Praphrasing
     double penalty3;//Penalty for each word in type 3 Praphrasing
     double penalty4;//Penalty for each word in type 4 Praphrasing
     */
    public LevDistWithParaphrasing(HashMap<String, HashMap<String, Double>> usedLexicalPP, HashMap<String, ExtTokenPP> usedPhrasalPP) {
        this.usedLexicalPP = usedLexicalPP;
        this.usedPhrasalPP = usedPhrasalPP;

    }

    public LevDistWithParaphrasing() {
    }

    /**
     * Get minimum of three values
     */
    private static short minimum(int a, int b, int c) {
        return (short) Math.min(a, Math.min(b, c));
    }

    /**
     * Get minimum of three values
     */
    private static double minimum(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }

    /**
     * getmaxtargetsize -- get the maximum length of target size of paraphrase
     *
     * @param p
     * @return
     */
    int getmaxtargetsize(ArrayList<Paraphrase> p) {
        int max = 0;
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).noOfWordsPP() > max) {
                max = p.get(i).noOfWordsPP();
            }
        }
        return max;
    }

    /**
     * getmaxsourcesize -- get the maximum length of source size of paraphrase
     *
     * @param p
     * @return
     */
    int getmaxsourcesize(ArrayList<Paraphrase> p) {
        int max = 0;
        for (int i = 0; i < p.size(); i++) {

            if (p.get(i).noOfWordsSrc() > max) {
                max = p.get(i).noOfWordsSrc();
            }
        }
        return max;
    }

    void printd(int pind, int upto) {
        for (int i = 0; i < upto; i++) {
            System.err.print(distance[pind][i] + "\t");
        }
        System.err.println();
    }

    public LdPPSPair compute(Token[] source, ExtToken[] target, double[] pppenalty) {
        if (source == null || target == null) {
            throw new IllegalArgumentException();
        }

        int n = (source.length > MAX_N) ? (MAX_N - 1) : source.length; // length of s
        int m = (target.length > MAX_N) ? (MAX_N - 1) : target.length; // length of t

        if (Parameters.getInfoLevel() > 0) {
            System.err.println("Calculating distance between:");
            System.err.println("Source:");
            Print.printtokens(source);
            System.err.println("Target:");
            Print.printtokens(target);
        }

        Token[] targetsentencematch = new Token[m + MAX_OFFSET];
        short matchindex = 0;
        ArrayList<PPPair> ppusedinsentence = new ArrayList();

        ArrayList<PPPair> ppusedinsentence12 = new ArrayList();

        LdPPSPair ldpair;

        /* boundry case when either of the segment is empty */
        if (n == 0) {
            ldpair = new LdPPSPair(targetsentencematch, (short) (matchindex), m, ppusedinsentence);
            return ldpair;
        } else if (m == 0) {
            ldpair = new LdPPSPair(targetsentencematch, (short) (matchindex), n, ppusedinsentence);
            return ldpair;
        }

        double cost;
        for (short i = 0; i <= n; i++) {
            prev_dist[0][i] = i;
        }
        int maxts = 0; // get the size of the largest source or corresponding paraphrase, used to loop over all the paraphrases (and sources) and to know that all the paraphrases are considered
        int maxsrc = 0; // get the size of the largest source, used to make the decision whether all source side tokens are finished or not so that a dummy token can be used if maxts > maxsrc   
        int maxtscount = 1;
        boolean flag = false;// flag to consider the case when paraphrases are also considered
        int noiter = 1;
        ArrayList<Paraphrase> alpp = new ArrayList();
        // short oldj=0;
        double oldmindistance = 10000;  //initialized with some large not possible value for edit-distance
        // oldeffj -  used to adjust the value of effj after paraphrases on a particular token are considered
        // effj - iterates over tm segment with increment each time by +1 but also used for backtracking.
        // oldj - used to adjust the value of j as well as decision making 
        // j - iterates over tm segment with increment each time by +1 but also used for backtracking.
        for (short j = 1, effj = 1, oldeffj = 1, oldj = 0; effj <= m || flag == true; j++, effj++) { // iteration over tm segment

            if (Parameters.getInfoLevel() > 1) {
                System.out.println();
                System.out.println("j=" + j + "\t" + "effj=" + effj + "\t" + "m=" + m + " flag=" + flag);
            }
            HashMap<String, Double> type1pp = new HashMap(); // For type 1 paraphrases
            HashMap<String, Short> type2pp = new HashMap(); // For type 2 paraphrases
            ExtToken extt_j;
            if (effj <= m) {
                extt_j = target[effj - 1];
                if (isLexicalPP(extt_j.getToken().getText())) {
                    type1pp = getUsedLexicalPPbyKey(extt_j.getToken().getText());
                    if (Parameters.getInfoLevel() > 1) {
                        System.out.println("Type1PP_KEY:" + extt_j.getKey());
                        Print.printParaphrases(extt_j.getKey(), type1pp);
                    }
                }
                type2pp = getUsedPhrasalPPbyKey(extt_j.getKey()).getType2PP();
                if (Parameters.getInfoLevel() > 1) {
                    System.out.println("Type2PP_KEY:" + extt_j.getKey());
                    Print.printParaphrases(type2pp, extt_j.getKey());
                }
            } else {
                extt_j = new ExtToken(new Token("DUMMY", false)); //when tm segment is made bigger than original because of paraphrasing (Type 3 case)
            }

            //Initialize first value of the every array (to hold the edit-distance of each paraphrase) in the matix with the value of j
            for (int k = 0; k < MAX_PP; k++) {
                distance[k][0] = (short) j;
            }
            //  System.err.print("d at 0178: ");
            if (Parameters.getInfoLevel() > 2) {
                System.out.print("distance at step1: ");
                printd(0, m + 5);
            }

            if (true == flag) {
                maxtscount++;
            } else {   //if the flag is false, reset all the values to consider a new list of Types 3 and 4 paraphrases
                maxts = 0; //reset 
                noiter = 1; //reset
                maxtscount = 1;
                alpp = getUsedPhrasalPPbyKey(extt_j.getKey()).getType34PP();
                if (Parameters.getInfoLevel() > 1) {
                    System.err.println("Type34PP_KEY:" + extt_j.getKey());
                    Print.printParaphrases(alpp);
                }
                if ((j <= n) && (!alpp.isEmpty())) {// stop considering paraphrases when tm segment considered crossed the source segment length (or token do not have paraphrase)
                    flag = true;
                    oldj = j;
                    oldmindistance = prev_dist[0][oldj];
                    oldeffj = effj;
                    maxsrc = getmaxsourcesize(alpp);
                    maxts = Math.max(getmaxtargetsize(alpp), maxsrc);
                    noiter = 1 + alpp.size();
                }
            }

            /*This loop executes number_of_paraphrases_available +1 times; In the absence of any paraphrases at this token, executes only once*/
            //pind - paraphrase index, pind=0 for source and >0 for paraphrases
            //noiter - number of paraphrases + 1 (for source)
            for (int pind = 0; pind < noiter; pind++) {
                Token t_jp = extt_j.getToken();
                if (pind > 0) {
                    ////////backup d0/////
                    if (flag == true) {
                        int lensrc = alpp.get(pind - 1).noOfWordsSrc();
                        if ((oldj + lensrc - 1) == j || (oldj + lensrc - 1) == n) {
                            int valj = ((oldj + lensrc - 1) > n) ? n : (oldj + lensrc - 1);  // j iterate over m, but decision making is till n==m so valid j should be <=n (source)
                            d0[valj] = distance[0][valj];
                            dold.put(lensrc, distance[0]);
                        }
                    }
                    /////////////////////////////
                    if (alpp.get(pind - 1).haspptokenAtIndex(maxtscount - 1)) {  //pp has token
                        t_jp = alpp.get(pind - 1).getpptokenAtIndex(maxtscount - 1);
                    } else {     //pind>0 and pp dont have token, take help from offset
                        t_jp = new Token("DUMMY", false);  // when a paraphrase is short than biggest paraphrase
                    }
                } else {
                    if (flag == true && maxtscount > maxsrc) {
                        t_jp = new Token("DUMMY", false); // when one of the paraphrase is bigger than biggest source, in this case source is taken dummy
                    }
                }//endIFELSE(pind,0)

                Token source_i;//= null; // ith object of s
                if (t_jp.isvalid()) {   //when effj<=m
                    if (Parameters.getInfoLevel() > 1) {
                        Print.printToken(t_jp);
                    }
                    for (short i = 1; i <= n; i++) { // iteration over source segment
                        source_i = source[i - 1];
                        cost = source_i.equals(t_jp) ? (double) 0 : (double) 1;
                        boolean istype2ppexist = false;
                        boolean istype1ppexist = false;
                        if (pind == 0) {

                            if (cost == (double) 1 && !type2pp.isEmpty()) {
                                istype2ppexist = type2pp.containsKey(source_i.getText());
                            }
                            if (istype2ppexist) {
                                cost = (double) pppenalty[1];
                            }

                            if (cost == (double) 1 && !type1pp.isEmpty()) {
                                istype1ppexist = type1pp.containsKey(source_i.getText());
                            }
                            if (istype1ppexist) {
                                cost = (double) pppenalty[0];//penalty type 1
                            }
                        }
                        //  if(typep!=0 && cost==0){cost=cost+pppenalty[typep];}
                        // minimum of cell to the left+1, to the top+1, diagonally left
                        // and up +cost
                        if (pind > 0 && cost == 0) {
                            cost = pppenalty[2];//penalty type3 and 4
                        }
                        if (maxtscount == 1) {
                            distance[pind][i] = minimum(distance[pind][i - 1] + 1, prev_dist[0][i] + 1, prev_dist[0][i - 1] + cost); //use p of basic 
                        } else {
                            distance[pind][i] = minimum(distance[pind][i - 1] + 1, prev_dist[pind][i] + 1, prev_dist[pind][i - 1] + cost);   //use p of pp
                        }                    //    System.out.print(s_i.getText()+" "+t_jp.getText()+" "+d[pind][i]+"\t");

                        ///storing pp used ///
                        if ((pind == 0) && (distance[pind][i] != (distance[pind][i - 1] + 1)) && (distance[pind][i] != (prev_dist[pind][i] + 1))) {
                            //  if(pind==0){
                            if (istype2ppexist) {
                                //         ppusedinsentence12.add(new PPPair(new Paraphrase(t_jp.getText(), source_i.getText(), i-1, (short) 1), effj - 1));
                                ppusedinsentence.add(new PPPair(new Paraphrase(t_jp.getText(), source_i.getText(), i - 1, (short) 1), effj - 1));
                            } else if (istype1ppexist) {
                                //        ppusedinsentence12.add(new PPPair(new Paraphrase(t_jp.getText(), source_i.getText(), i-1, (short) 2), effj - 1));
                                ppusedinsentence.add(new PPPair(new Paraphrase(t_jp.getText(), source_i.getText(), i - 1, (short) 2), effj - 1));

                            }
                            //  System.out.println("type2:"+istype2ppexist+" type1:"+istype1ppexist+" cost:"+cost+" si:"+s_i.getText()+" tjp:"+t_jp.getText());
                        }

                    }

                    if (Parameters.getInfoLevel() > 2) {
                        System.out.print("distance at step2: ");
                        printd(0, m + 5);
                    }

                    if (flag == false) {
                        if (ppusedinsentence12.isEmpty()) {
                            targetsentencematch[matchindex++] = t_jp;
                        } else {
                            if (Parameters.getInfoLevel() > 1) {
                                System.out.print("PARA-TARGET:");
                                for (int tsm = 0; tsm < target.length; tsm++) {
                                    System.out.print(target[tsm].getToken().getText() + " ");
                                }
                                System.out.print("PARA-TSM:");
                                for (int tsm = 0; tsm < matchindex; tsm++) {
                                    System.out.print(targetsentencematch[tsm].getText() + " ");
                                }
                                System.out.println();
                                for (PPPair p : ppusedinsentence12) {
                                    System.out.println("PARA" + p.getParaphrase().getleft() + " " + p.getParaphrase().getright());
                                }
                            }
                            Token pptoken = getParaphrasedTokenUsed(ppusedinsentence12);
                            targetsentencematch[matchindex++] = pptoken;

                        }
                        ppusedinsentence12.clear();
                        //  System.err.println("This  executes when no type34 paraphrases are available ");
                    } //target sentence matched in calculation of edit distance
                    if (maxtscount == 1 && pind == 0 && flag == true) {
                        dp = distance[0];
                    }
                } else {
                    /*If the token is dummy, just copy the previous distance*/
                    distance[pind] = prev_dist[pind]; //copy previous value
                    if (maxtscount == 1 && pind == 0 && flag == true) { // is this condition possible for a dummy ?? maxtscount cant be 1 when the dummy is needed
                        dp = distance[0];
                    }
                }//endIFELSE(t_jp.isvalid())
                //  System.err.print("d at 0272: ");
                if (Parameters.getInfoLevel() > 2) {
                    System.out.print("distance at step3: ");
                    printd(0, m + 5);
                }
                //   System.err.println("\n"+t_jp.getText()+" j="+j+" pind:"+pind);
                //   printd(pind, 30);
            }//endFor(pind,noiter)

            /*Decision making process: Choosing the best paraphrase for Type 3 and Type 4 (maxts==maxtscount). Also, this (if) does not execute when the paraphrases are not available or length of tm segment considered crossed the source segment (maxts==0)   */
            if ((maxts != 0) && (maxts == maxtscount)) {//condition changed on 15:09, 5 feb 14, changed on 7 feb 18:13 
                int ind = 0;
                double mindistance = 1500;
                //mindistance= (j>n) ? d[0][n] : mindistance;
                int validj;//= (effj>n)? n :effj;
                //mindistance= (j<=m && j<=n) ? d[0][j] : d[0][m];  //why m
                // mindistance=d[0][validj];
                int len = 0;
                boolean ppwin = false;
                for (int pind = 1; pind < noiter; pind++) {
                    int lenpp = alpp.get(pind - 1).noOfWordsPP();
                    validj = ((oldj + lenpp - 1) > n) ? n : (oldj + lenpp - 1);
                    if (distance[pind][validj] < mindistance || (distance[pind][validj] == mindistance && lenpp >= len)) {
                        mindistance = distance[pind][validj];
                        ind = pind;
                        len = lenpp;
                        ppwin = true;
                        //       offset_j=offset[pind];
                    }
                    int lensrc = alpp.get(pind - 1).noOfWordsSrc();
                    validj = ((oldj + lensrc - 1) > n) ? n : (oldj + lensrc - 1);
                    if (d0[validj] <= mindistance || (d0[validj] == mindistance && lensrc >= len)) {
                        mindistance = d0[validj];
                        ind = pind;
                        ppwin = false;
                        len = lensrc;
                        //        offset_j=0;
                    }
                }
                if (ppwin) {
                    distance[0] = distance[ind];
                    //      System.err.println("PPWin Index:"+ind);

                    if (Parameters.getInfoLevel() > 2) {
                        System.out.print("distance at step4: ");
                        printd(0, m + 5);
                        int lensrc = alpp.get(ind - 1).noOfWordsSrc();
                        int lenpp = alpp.get(ind - 1).noOfWordsPP();
                        System.out.println("Lensrc:" + lensrc + "  \t Lenpp:" + lenpp);
                        System.out.println("ppwin because: ed is " + d0[((oldj + lensrc - 1) > n) ? n : (oldj + lensrc - 1)]);
                        System.out.println("and pp is " + distance[ind][((oldj + lenpp - 1) > n) ? n : (oldj + lenpp - 1)]);
                    }
                    ppusedinsentence.add(new PPPair(alpp.get(ind - 1), effj - maxts));
                    
                    for (int k = 0; k < alpp.get(ind - 1).noOfWordsPP(); k++) {
                        targetsentencematch[matchindex++] = alpp.get(ind - 1).getpptokenAtIndex(k);
                    }

                    effj = (short) (oldeffj - 1 + alpp.get(ind - 1).noOfWordsSrc());
                    j = (short) (oldj - 1 + alpp.get(ind - 1).noOfWordsPP());

                    if (Parameters.getInfoLevel() > 2) {
                        System.out.print("distance at step5: ");
                        printd(0, m + 5);
                    }

                } else if (oldmindistance == mindistance) {
                    /// get ind for which edit-distance was same 
                    if (len != maxsrc) {
                        distance[0] = dold.get(len);
                    }
                    for (int k = 0; k < len; k++) {
                        targetsentencematch[matchindex++] = target[oldeffj - 1 + k].getToken();
                    }
                    j = (short) (oldj - 1 + len);
                    effj = (short) (oldeffj - 1 + len);

                    if (Parameters.getInfoLevel() > 2) {
                        System.out.print("distance at step6: ");
                        printd(0, m + 5);
                    }

                } else {
                    distance[0] = dp;
                    for (int k = 0; k < 1; k++) {
                        targetsentencematch[matchindex++] = target[oldeffj - 1 + k].getToken();
                    }
                    j = (short) (oldj - 1 + 1);
                    effj = (short) (oldeffj - 1 + 1);

                    if (Parameters.getInfoLevel() > 2) {
                        System.out.print("distance at step7: ");
                        printd(0, m + 5);
                    }
                }
                maxtscount = 1;
                flag = false;
                //      offset=new short[MAX_PP];
            }//end if , selection of best paraphrase/source if no paraphrase was better than source 

            // copy current distance counts to 'previous row' distance counts
            // swap = prev_dist;
            prev_dist = distance;
            //short [][]swap2;
            distance = new double[MAX_PP][MAX_N + 1];   //boundry condition when j >= m, saving last value of d

            if (Parameters.getInfoLevel() > 2) {
                System.out.print("distance at step8: ");
                printd(0, m + 5);
            }

            for (int dum = 0; dum < MAX_PP; dum++) {
                for (int dum2 = 0; dum2 < MAX_N + 1; dum2++) {
                    distance[dum][dum2] = 500;
                }
            }

            if (Parameters.getInfoLevel() > 2) {
                System.out.print("distance at step9: ");
                printd(0, m + 5);
            }
        }

        if (Parameters.getInfoLevel() > 1) {
            System.err.println("edit-distance is:" + prev_dist[0][n]);
        }
        ldpair = new LdPPSPair(targetsentencematch, (short) (matchindex), prev_dist[0][n], ppusedinsentence);

        if (Parameters.getInfoLevel() > 2) {
            System.out.print("distance at step10: ");
            printd(0, m + 5);
        }

        return ldpair;
    }

    private Token getParaphrasedTokenUsed(ArrayList<PPPair> ppusedinsentence12) {
        int closest = 1000;
        int index = -1;
        for (int i = 0; i < ppusedinsentence12.size(); i++) {
            int diff = Math.abs(ppusedinsentence12.get(i).getLocation() - ppusedinsentence12.get(i).getParaphrase().getIndexOfSourceTokenMatched());
            if (diff < closest) {
                closest = diff;
                index = i;
            }
        }
        return ppusedinsentence12.get(index).getParaphrase().getpptokenAtIndex(0);
    }

    private ExtTokenPP getUsedPhrasalPPbyKey(String key) {
        if (key == null) {
            return null;
        }
        return usedPhrasalPP.get(key);

    }

    private HashMap<String, Double> getUsedLexicalPPbyKey(String key) {
        //  if(key==null)return null;
        return usedLexicalPP.get(key);
    }

    private boolean isLexicalPP(String key) {
        return usedLexicalPP.containsKey(key);
    }

}
