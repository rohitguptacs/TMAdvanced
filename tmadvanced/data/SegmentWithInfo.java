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


package tmadvanced.data;

/**
 *
 * @author rohit
 */
public class SegmentWithInfo {
    
    Token [] lotks;
    String source;
    String target;
    String rawsrctext;
    String rawtgttext;
    Integer num_punc;
    Integer num_tags;
    Integer num_nums;
    
    public SegmentWithInfo(Token[] lotks, String source, String target, String rawsrctext, String rawtgttext, Integer num_punc, Integer num_tags, Integer num_nums){
        this.lotks=lotks;
        this.source=source;
        this.target=target;
        this.num_punc=num_punc;
        this.num_tags=num_tags;
        this.num_nums=num_nums;
        this.rawsrctext=rawsrctext;
        this.rawtgttext=rawtgttext;
    }
    
}
