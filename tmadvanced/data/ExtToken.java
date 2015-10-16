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
import java.util.ArrayList;

public class ExtToken{

    Token tk;
    String ppalkey;
    
   public ExtToken(Token tk, String ppalkey){
        this.tk=tk;
        this.ppalkey=ppalkey;
    }
  public ExtToken(Token tk){
        this.tk=tk;
        ppalkey=null; //no paraphrase exists
    }
   public ExtToken(){
        tk=null;
        ppalkey=null;
    }
  
   public Token getToken(){
        return tk;
    }
   public String getKey(){
       return ppalkey;
   }
   public void print(){
        System.out.print(tk.getText());
      
    }
}