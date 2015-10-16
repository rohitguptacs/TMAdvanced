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

import tmadvanced.data.Token;
import java.util.ArrayList;
import java.util.List;
import tmadvanced.data.Match;
import java.util.Collections;

/**
 *
 * @author Rohit Gupta
 */
/* Stores matching segments from TM for a particular input candidate */
public class MatchStore {
    List<Match> matches= new ArrayList();
    Token [] cand;
    MatchStore(Token [] cand){
        cand= new Token[cand.length+1];
        this.cand=cand;
    }
   public void add(Match target){
        matches.add(target);
    }
   public List<Match> getMatches(){
       return matches;
   }
   public boolean hasMatches(){
       return !(matches.isEmpty());
   }
   public boolean hasNoMatches(){
       return matches.isEmpty();
   }
   
   public List<Match> SortAndReverse(){
       Collections.sort(matches);
       Collections.reverse(matches);
       return matches;
   }
}
