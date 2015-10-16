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
 * @author Rohit Gupta
 */
import java.util.Locale;
import java.util.regex.Pattern;

public class Token {
    
    @Override
    public boolean equals(Object other) {
        return ((this == other) || ((other instanceof Token) && (hash == ((Token) other).hash)));
    }
    private int hash;

    @Override
    public int hashCode() {
        return hash;
    }

    private static Pattern AMP = Pattern.compile("\\&");

    private final String stripAmpersand(String s) {
        return AMP.matcher(s).replaceAll("");
    }

    /**
     * Creates a new token.
     * @param _text the text of the token
     *            
     */
    public Token(String _text) {
        this(_text, true);
    }

    /**
     * Creates a new token.
     * @param _text the text of the token
     */
  
    public Token(String _text,  boolean valid) {
        this.valid = valid;
        hash = (_text == null) ? -1 : stripAmpersand(_text.toLowerCase()).hashCode();
        text=_text;
    }

    private String text;
    private boolean valid;

    
    /** Returns token's text
     * @return  */
    public final String getText(){
        return text;
    }
    
   
    public final boolean isvalid(){
        return valid;
    }
    
}
