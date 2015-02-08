/*
 * # ����
 * 1. build path �缳��
 *    build path -> configure build path -> libraries
 *    ���� lucene-snowball-3.0.2.jar, 
 *        stanford-postagger-3.5.0.jar,
 *        itextpdf-5.5.4.jar ����
 *    add external jar -> ������Ʈ �������� ���� jar���� ����
 *    order and export���� ���� jar���� üũ
 * 2. input ���� ����
 *    Ű���带 �̾Ƴ��� ���� ������ text�� TestSample.txt�� �ֱ�
 * 3. ���ʿ��� Ű���� ����
 *    bad_words.txt�� ���� ���� Ű���� ����
 *    
 * ���
 *    ��ü ������ ����, 
 *    ���� �̸�, �ش� ������ ��
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
	
	static String path = "C:/������ǲ/compiler";
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
			System.out.println("������!!!! ������!!!! ������!!!! ������!!!! ");
		}*/
	}

	
	void traverse(File file) {
        if(file == null || !file.exists()) {
            return;
        }
       
        if(file.isDirectory()) {
            // ���丮�� �ڽĿ�Ҹ� �̾� �ݺ��Ͽ� ���ȣ���� �Ѵ�.
           String[] files = file.list();

           if(files != null) {
        	   for(int i=0; i<files.length; i++) {
        		   traverse(new File(file, files[i]));
        	   }
           }
       } else {
            // �����̸� ���丮 ��  ������ �̸��� ����Ѵ�.
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
		
		//Ȯ���� �˻�
				if(filePath.substring(filePath.length()-3).equals("pdf")){
					//System.out.println("PDF!!");
					try{
						SentenceList = te.textExtractFromPdf(filePath);
					} catch(Exception e){
						System.out.println(fileName + ": �ش� ������ �з��� �� �����ϴ�.");
						return;
					}
				}else if(filePath.substring(filePath.length()-3).equals("txt")){
					//System.out.println("TXT!!");
					SentenceList = te.textExtractFromTxt(filePath);
				}
				else{
					System.out.println(fileName + ": pdf Ȥ�� txt������ �з��� �� �ֽ��ϴ�.");
					return;
				}

				//Ű���� ����
				keywordList = kc.KeywordClip(SentenceList);
				
				//Ű���� ����ȭ
				TFVector = TFVectorizer.vectorize(keywordList);
				/*
				//����� ���
				System.out.println(TFVector.size());
				for(TF tf : TFVector){
					System.out.println(tf.keyword+" "+tf.count);
				}
				*/
				//����� ����ȭ
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
				
				//�ݱ�
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
