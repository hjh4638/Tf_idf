package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.tartarus.snowball.ext.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class KeywordClipper {
	MaxentTagger tagger;
	EnglishStemmer stemmer;
	
	ArrayList<String> badWordList;
	
	public KeywordClipper() {
		tagger = new MaxentTagger("taggers/wsj-0-18-left3words-nodistsim.tagger");
		stemmer = new EnglishStemmer();
		
		badWordList = new ArrayList<String>();
		
		setBadWordList("bad_words.txt");
	}
	
	public ArrayList<String> KeywordClip(ArrayList<String> SentenceList) {
		ArrayList<String> strList = new ArrayList<String>();
		
		//문서의 한 문장씩 처리
		int listSize=SentenceList.size();
		for(int i=0;i<listSize;i++)
		{
			String strStep = SentenceList.get(i);			
			
			//Untokenizable: ? (U+FFFD, decimal: 65533) 해결 부분
			//이게 퍼포먼스에 영향을 많이 주면 지워야함(디버깅에만 활용)
			strStep=strStep.replace((char) 65533, ' ');
			
			//tagging
			strStep = tagger.tagString(strStep);
			
			//토큰으로 나누기
			StringTokenizer strTokenizer = new StringTokenizer(strStep," ");
			
			//단어별 처리
			while(strTokenizer.hasMoreTokens()){
				strStep = strTokenizer.nextToken();
				
				//단어 중 _NN*형
				if(strStep.contains("_NN")){
				
					//"_NN*" 제거
					String tmp = strStep.substring(0, strStep.indexOf("_"));
							
					//소문자로 변환
					tmp=tmp.toLowerCase();
					
					//stemming
					String stemmed=null;
					try {
						stemmed=stemming(tmp);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//bad_word검사, 리스트에 저장
					if(isBadWord(stemmed) == false){
						strList.add(stemmed);
					}
				}
			}
		}

		return strList;
	}
	
	String stemming(String token) throws Exception {
		try {
			stemmer.setCurrent(token);
			if (!stemmer.stem()) {
				return token;
			}

			return stemmer.getCurrent();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	void setBadWordList(String file){
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(new File(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
	
		while(true)
		{
			String badWord = null;			
			try {
				badWord = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();    		
			}
			
			if(badWord == null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		    	break;
		    }
			
			badWordList.add(badWord);
		}
	}
	
	boolean isBadWord(String str){
		boolean isBadWord = false;
		for(String badWord: badWordList){
			if(str.contains(badWord) || str.length()==1 || str.equals("")){
				isBadWord = true;
			}
		}
		
		return isBadWord;
	}
}
