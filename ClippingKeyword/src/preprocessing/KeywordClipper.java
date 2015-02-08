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
		
		//������ �� ���徿 ó��
		int listSize=SentenceList.size();
		for(int i=0;i<listSize;i++)
		{
			String strStep = SentenceList.get(i);			
			
			//Untokenizable: ? (U+FFFD, decimal: 65533) �ذ� �κ�
			//�̰� �����ս��� ������ ���� �ָ� ��������(����뿡�� Ȱ��)
			strStep=strStep.replace((char) 65533, ' ');
			
			//tagging
			strStep = tagger.tagString(strStep);
			
			//��ū���� ������
			StringTokenizer strTokenizer = new StringTokenizer(strStep," ");
			
			//�ܾ ó��
			while(strTokenizer.hasMoreTokens()){
				strStep = strTokenizer.nextToken();
				
				//�ܾ� �� _NN*��
				if(strStep.contains("_NN")){
				
					//"_NN*" ����
					String tmp = strStep.substring(0, strStep.indexOf("_"));
							
					//�ҹ��ڷ� ��ȯ
					tmp=tmp.toLowerCase();
					
					//stemming
					String stemmed=null;
					try {
						stemmed=stemming(tmp);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//bad_word�˻�, ����Ʈ�� ����
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
