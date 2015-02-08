package textExtract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class TextExtractor {
	ArrayList<String> badSentenceList;
	
	public TextExtractor(){
		badSentenceList = new ArrayList<String>();
		
		setBadSentenceList("bad_sentences.txt");
	}
	
	private void setBadSentenceList(String file) {
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
			
			badSentenceList.add(badWord);
		}
	}

	public ArrayList<String> textExtractFromPdf(String file){
		ArrayList<String> SentenceList = new ArrayList<String>();
		PDDocument pd;
		
		try {
			File input = new File(file);
			pd = PDDocument.load(input);
			 
			PDFTextStripper stripper = new PDFTextStripper();
			 
			//���� ������ ����
			stripper.setStartPage(1);
			stripper.setEndPage(pd.getNumberOfPages());
			 
			//��ȯ ���� ����
			String source = stripper.getText(pd);
			byte[] b = source.getBytes();
			source = new String(b, "UTF8");
			source = source.replace("?", " ");
			
			//System.out.println(source);
			
			//��ū���� ������
			StringTokenizer strTokenizer = new StringTokenizer(source,"\n");
			
			while(strTokenizer.hasMoreTokens()){
				String strStep = strTokenizer.nextToken();
				
				//bad_Sentence�˻�, ����Ʈ�� ����
				if(isBadSentence(strStep) == false){
					SentenceList.add(strStep);
				}
			}
			
			if(pd != null){
				pd.close();
			}
	
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		return SentenceList;
	}
	
	public ArrayList<String> textExtractFromTxt(String file){
		ArrayList<String> SentenceList = new ArrayList<String>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			fileReader = new FileReader(new File(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bufferedReader = new BufferedReader(fileReader);
		
		//������ �� ���徿 ó��
		while(true)
		{
			String strStep = null;			
			
			//�� ���� �б�
			try {
				strStep = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();    		
			}
			
			if(strStep == null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		    	break;
		    }
			
			//bad_Sentence�˻�, ����Ʈ�� ����
			if(isBadSentence(strStep) == false){
				SentenceList.add(strStep);
			}
		}
		
		return SentenceList;
	}
	
	boolean isBadSentence(String str){
		str=str.toLowerCase();
		
		boolean isBadSentence = false;
		for(String badWord: badSentenceList){
			if(str.contains(badWord) || str.equals(" ")){
				isBadSentence = true;
			}
		}
		
		return isBadSentence;
	}
}
