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

package tmadvanced.preprocess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author rohit
 */
public  class Tags {
    
    int num_tags;
    String text;
    public   Tags(String raw){
    
         Pattern   AMP=Pattern.compile("<[^>]*>"); 
         Matcher m=AMP.matcher(raw);
         int tag_count=0;
         while(m.find())tag_count++;
         
         this.num_tags=tag_count;
         this.text=AMP.matcher(raw).replaceAll("");
           
    }
    public int getNumTags(){
        return num_tags;
    } 
    public String getTextWithoutTags(){
        return text;
    }
    
}
