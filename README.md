# ############################################################################
 Copyright (C) 2015 Rohit Gupta, University of Wolverhampton.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
# TMAdvanced: A tool to retrive semantically similar matches from a  Translation Memory using paraphrases
# ###########################################################################
Current Translation Memory (TM) systems work at the surface level and lack semantic knowledge while matching. This tool implements an approach to incorporating semantic knowledge in the form of paraphrasing in matching and retrieval. Most of the TMs use Levenshtein edit- distance or some variation of it. This tool implements an efficient approach to incorporating paraphrasing with edit-distance. The approach is based on greedy approximation and dynamic programming. We have obtained significant improvement in both retrieval and translation of retrieved segments. 
More details about the approach and evaluations given in the following publications:

Approach: Rohit Gupta and Constantin Orasan. 2014. [Incorporating Paraphrasing in Translation Memory Matching and Retrieval.](http://pers-www.wlv.ac.uk/~in4089/publications/2014/EAMT2014.pdf) In Proceedings of the European Association of Machine Translation (EAMT-2014).

Human Evaluations: Rohit Gupta, Constantin Orasan, Marcos Zampieri, Mihaela Vela and Josef van Genabith. 2015. [Can Transfer Memories afford not to use paraphrasing?](http://rgcl.wlv.ac.uk/wp-content/uploads/2015/05/paper-35-2.pdf) In Proceeding of EAMT-2015, Antalya Turkey.

## Software Requirements
java >= 1.7

## Libraries
- [Stanford postagger-3.4.1](http://nlp.stanford.edu/software/stanford-postagger-2014-08-27.zip) Used only for tokenization

- [BerkeleyParser](https://github.com/slavpetrov/berkeleyparser) Used only for tokenization

## Resources
- This code uses lexical and phrasal paraphrases from [PPDB:Paraphrase database](http://www.cis.upenn.edu/~ccb/ppdb/). More details can be found [here](http://www.cis.upenn.edu/~ccb/ppdb/).
 
## Running
Run the following:
```
java -jar TMAdvanced.jar -pp paraphrase_file -tms TM_file -ins Input_file -inslang input_source_language -intlang input_target_language -tmslang TM_source_language -tmtlang TM_target_language> -o output_file_name
```
## Help
```
java -jar -h
```
Above command will display the options you can provide. Please provide all mandatory options to run, output will be in the file "file_name.tmx" where file_name is provided by -o  
Please refer sample1.sh or sample2.sh for a sample run.

The options available are also given below:
(Options are case sensitive, -ins en-US and -ins EN-US are different. Also -INS or Ins are invalid options.)

#### Valid input formats:
- xliff
- txt (require file with one segment per line)
- tmx 

#### Valid TM formats:
- tmx
- txt (require two files with one segment per line (source and target segment aligned))  

#### Mandatory Parameters:
	 -tms: TM source file name (tmx file with .tmx extension  or txt file with each segment per line) [mandatory][e.g. -tms sampleTM.tmx]
	 -tmt: TM target file name (txt with each segment per line) [mandatory if -tms is a txt file and not required for tmx][e.g. -tmt sampleTM.txt]
	 -ins: Input source file name (xliff file with .xliff extension or tmx file with .tmx extension or txt file with each segment per line) [mandatory][e.g. -ins sampleInput.tmx]
	 -pp: paraphrasing file name (-pp yourparaphrasefile.txt [please check sample paraphrase file for the format]) [mandatory]
	 -inslang: Please provide input (language of file you want to translate) source language [mandatory][e.g. -inslang en-US]
	 -intlang: Please provide input target language (the language in which you want to translate) [mandatory][e.g. -intlang es-ES]
	 -tmslang: Please provide Translation Memory source language [mandatory][e.g. -tmslang en-US]
	 -tmtlang: Please provide Translation Memory target language [mandatory][e.g. -tmtlang es-ES]
	 -o: Output file name [mandatory][e.g. -o myparatm]
        
#### Optional Parameters:
        -int: Input target file name (txt with each segment per line) [note: this option is used for research only] [mandatory if -tms is a txt file, please provide the corresponding target file]   
-lth: Threshold for filtering based on length [ignored when -filtering off][optional, default: 39.0 ]
        -bth: Threshold for filtering based on maximum gap allowed in edit-distance(beam threshold)[ignored when -filtering off][optional, default: 35.0 ]
        -tmth: Cut off threshold for TM matching, only segments having similarity above this threshold will be used for paraphrasing [ignored when -filtering off][optional, default: 39.0 ]
        -nb: N-best-list size (e.g. if -nb 100, not more than top 100 candidates participate for paraphrasing) [ignored when -filtering off][optional, default: 100 ]
        -tok: Tokenization option (no: tokenization or any kind of preprocessing is not performed, stanford: used stanford tokenizer for tokenization and also remove tags before processing) [optional, default: stanford]
        -tag: Remove tags in preprocessing. Fuzzy score is calculated without tags and cleaned source and target segments are written in the output tmx file [optional, default: on]
        -punc: Remove punctuation when calculating fuzzy score (punctuations are not removed from the target side). [optional, default: on]
        -num: Replace number with a placeholder when calculating fuzzy score (numbers are not removed from the target side) [optional, default: off]
        -typp: Types of paraphrasing (comma separated e.g. -typp 1,2 (note: no space between options -typp 1,2 and not -typp 1, 2 or -typp 1 , 2  )) [optional, default: all four types of paraphrases 1,2,3,4]
        -infolevel: 0 for less information, 1 for moderate information and 2 for huge information e.g. -infolevel 1) [optional, default: 0, less information]

#### Print Help:
        -h or -help: Print this information

 
