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

import java.util.regex.Pattern;

/**
 *
 * @author Rohit Gupta
 */
public class Placeholder {

    /**
     * This class is used by input source/target files as well as paraphrases database for placeholder 
     * Replace -LRB-, -RRB-, -lrb-, -rrb-
     * Replace &amp with amp
     * Replace remaining & with and 
     * Replace all characters except [a-z0-9%] to "" (nothing)
     * Replace all no's with 'N' and all months with 'MONTH'
     * Sample input: Charles22 date *(90)* /((% of birth is 19th jul 1917 but he died on 20 january 2006 
     * Sample output: Charles22 date N % of birth is N MONTH N but he died on N MONTH N
     * @param sentence
     * @return
     * 
     * for future
     * ¡¿
ÄäÀàÁáÂâÃãÅåǍǎĄąĂăÆæ
ÇçĆćĈĉČč
ĎđĐďð
ÈèÉéÊêËëĚěĘę
ĜĝĢģĞğ
Ĥĥ
ÌìÍíÎîÏïı
Ĵĵ
Ķķ
ĹĺĻļŁłĽľ
ÑñŃńŇň
ÖöÒòÓóÔôÕõŐőØøŒœ
ŔŕŘř
ẞßŚśŜŝŞşŠš
ŤťŢţÞþ
ÜüÙùÚúÛûŰűŨũŲųŮů
Ŵŵ
ÝýŸÿŶŷ
ŹźŽžŻż


     */
    
    public static String replaceNoAndMonth(String sentence){
        
            Pattern AMP=Pattern.compile("((\\-LRB\\-)|(\\-RRB\\-)|(\\-lrb\\-)|(\\-rrb\\-))");
            String s=AMP.matcher(sentence).replaceAll("");
           // System.out.println(s);
             AMP=Pattern.compile("(\\&)(amp)"); 
             s=AMP.matcher(s).replaceAll("amp");
           //  System.out.println(s);
             AMP=Pattern.compile("(\\&)"); 
             s=AMP.matcher(s).replaceAll("and");
           //  System.out.println(s);
            AMP=Pattern.compile("[^a-z0-9%\\s]");
            s=AMP.matcher(s).replaceAll("");
          //  System.out.println(s);
            AMP = Pattern.compile("\\b([0-9]*2nd|[0-9]*1st|[0-9]+th|[0-9]+)\\b");
            s=AMP.matcher(s).replaceAll("N");
          //  System.out.println(s);
            //AMP=Pattern.compile("[\\s\\^][january|jan|february|feb|march|mar|april|apr|may|june|jun|july|jul|august|aug|september|sep|ocotober|oct|november|nov|december|dec][\\s\\$]");
            //AMP=Pattern.compile("^(?:J(anuary|u(ne|ly))|February|Ma(rch|y)|A(pril|ugust)|(((Sept|Nov|Dec)em)|Octo)ber)$");
            AMP=Pattern.compile("\\b(?:j(an(uary|)|u((ne|n)|(ly|l)))|feb(ruary|)|ma(r|rch|y)|a(pr(il|)|ug(ust|))|((sep(t|)|nov|dec)(ember|))|(oct(ober|)))\\b");
            s=AMP.matcher(s).replaceAll("MONTH");
            s=s.replaceAll("\\s+"," ");
            s=s.trim();
         //   System.out.println(s);
            return s;
    }
    /**
     *  19 july 2016
        july 19, 2016
        19th july 2016
        nineteenth july 2016
        nineteen july 2016
        nineteen july two thousand and sixteen
        nineteen july two thousand & sixteen
        on nineteenth of july in the year 2016
        on 19th of july in the year 2016
        on 19 july in the year 2016
        on 19th july in the year 2016
        on nineteenth july in the year 2016
        19-07-2016
        07-19-2016
        19/07/2016
        07/19/2016
        19/07/16
     * @param sentence
     * @return 
     */
    public static String replaceDate(String sentence){
        System.err.print("replaceDate NOT IMPLEMENTED YET");
        System.exit(1);
        return   "";
    }
    public static void main(String args[]){
        String sentence = "& 888290&* He786 &amp; 13th% jan -LRB- -RRB- mar -lrb- 21st 22nd january apr jun aug february march april 2nd may june july august sep october september october oct nov november dec december 1st jul 1986 is 95th feb 1670 mayhem year old 7899 &";
       // String expResult = "N e786 N% MONTH MONTH N N MONTH MONTH MONTH MONTH MONTH MONTH MONTH N MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH MONTH N MONTH N is N MONTH N mayhem year old N";
        String result = Placeholder.replaceNoAndMonth(sentence);
        
    }
}
