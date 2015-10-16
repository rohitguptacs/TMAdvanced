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
public class ppType {

    String src;
    String tgt;
    int start;
    int slen;
    int tlen;
    short type;

    public ppType(String src, String tgt, int start, int slen, int tlen, short type) {
        this.src = src;
        this.tgt = tgt;
        this.start = start;
        this.slen = slen;
        this.tlen = tlen;
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public int getSrclen() {
        return slen;
    }

    public int getTgtlen() {
        return tlen;
    }

    public String getLeft() {
        return src;
    }

    public String getRight() {
        return tgt;
    }

    public int getIndex() {
        return start;
    }

}
