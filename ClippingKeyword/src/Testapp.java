/*
 * # 사용법
 * 1. build path 재설정
 *    build path -> configure build path -> libraries
 *    기존 lucene-snowball-3.0.2.jar, 
 *        stanford-postagger-3.5.0.jar,
 *        itextpdf-5.5.4.jar 제거
 *    add external jar -> 프로젝트 폴더에서 위의 jar파일 선택
 *    order and export에서 위의 jar파일 체크
 * 2. input 내용 변경
 *    키워드를 뽑아내고 싶은 파일의 text를 TestSample.txt에 넣기
 * 3. 불필요한 키워드 제거
 *    bad_words.txt에 빼고 싶은 키워드 기입
 *    
 * 출력
 *    전체 벡터의 갯수, 
 *    벡터 이름, 해당 벡터의 수
 *    ...
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import preprocessing.KeywordClipper;
import preprocessing.TF;
import preprocessing.TFVectorizer;
import textExtract.TextExtractor;


public class Testapp {
	ArrayList<String> SentenceList;
	ArrayList<String> keywordList;
	ArrayList<TF> TFVector;
	
	static String path = "C:/임의인풋/compiler";
	/*
	String fileName="computer_architecture.pdf";
	String filePath = "C:\\Users\\Drew\\Desktop\\test_input\\" + fileName;
	*/
	TextExtractor te = new TextExtractor(); 
	KeywordClipper kc = new KeywordClipper();
	
	Testapp(){	
	}
	
	public static void main(String[] args) {
		Testapp TA = new Testapp();
		
		TA.traverse(new File(path));
		/*
		for(int i=0;i<100;i++){
			System.out.println("끝났다!!!! 끝났다!!!! 끝났다!!!! 끝났다!!!! ");
		}*/
	}

	
	void traverse(File file) {
        if(file == null || !file.exists()) {
            return;
        }
       
        if(file.isDirectory()) {
            // 디렉토리면 자식요소를 뽑아 반복하여 재귀호출을 한다.
           String[] files = file.list();

           if(files != null) {
        	   for(int i=0; i<files.length; i++) {
        		   traverse(new File(file, files[i]));
        	   }
           }
       } else {
            // 파일이면 디렉토리 및  파일의 이름을 출력한다.
           try {
               String path = file.getCanonicalPath();
               if(path.endsWith(".pdf") || path.endsWith(".txt")) {
                    f(path);//System.out.println(path);
               }
           } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	void f(String filePath){
		
		String fileName = filePath.substring(filePath.lastIndexOf('\\')+1);
		System.out.println(fileName);
		
		//확장자 검사
				if(filePath.substring(filePath.length()-3).equals("pdf")){
					//System.out.println("PDF!!");
					try{
						SentenceList = te.textExtractFromPdf(filePath);
					} catch(Exception e){
						System.out.println(fileName + ": 해당 문서는 분류할 수 없습니다.");
						return;
					}
				}else if(filePath.substring(filePath.length()-3).equals("txt")){
					//System.out.println("TXT!!");
					SentenceList = te.textExtractFromTxt(filePath);
				}
				else{
					System.out.println(fileName + ": pdf 혹은 txt문서만 분류할 수 있습니다.");
					return;
				}

				//키워드 추출
				keywordList = kc.KeywordClip(SentenceList);
				
				//키워드 벡터화
				TFVector = TFVectorizer.vectorize(keywordList);
				/*
				//결과물 출력
				System.out.println(TFVector.size());
				for(TF tf : TFVector){
					System.out.println(tf.keyword+" "+tf.count);
				}
				*/
				//결과물 파일화
				FileWriter fileWriter = null;
				BufferedWriter bufferedWriter = null;
					
				try {
					fileWriter = new FileWriter(new File(path+"/" + fileName +".txt"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				bufferedWriter = new BufferedWriter(fileWriter);
				
				try {
					bufferedWriter.write(TFVector.size()+"");
					bufferedWriter.newLine();
					for(TF tf : TFVector){
						bufferedWriter.write(tf.keyword + " " + tf.count);
						bufferedWriter.newLine();
						bufferedWriter.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//닫기
				try {
					fileWriter.close();
				} catch (IOException e) {}	
				try {
					bufferedWriter.close();
				} catch (IOException e) {}
				
				SentenceList.clear();
				keywordList.clear();
				TFVector.clear();
	}
}
