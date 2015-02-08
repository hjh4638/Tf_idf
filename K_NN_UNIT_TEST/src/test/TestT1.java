package test;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysql.jdbc.Connection;

import dto.ClientVector;
import dto.KnnResult;
import dto.TrainedVector;
public class TestT1 {

	SqlSessionFactory sqlSessionFactory;
	SqlSession session;
	String words[];
	int input_dimension;
	int vector_dimension;
	
	@Before
	public void INIT() throws IOException {
		String resource = "sqlMap/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		
		words= new String[1001];
		
		input_dimension = 1000;
		vector_dimension = 360;
		assertNotEquals(sqlSessionFactory, null);
		assertNotEquals(session, null);
	}
	
	
	@Test
	public void getID(){
		Integer id = session.selectOne("test.getClientSeq");
		Integer in = session.selectOne("test.sel1");
		List<TrainedVector> a = session.selectList("test.sel3");
		a.get(0).toString();
	}
	@Test
	@Ignore
	public void getLineFileAndInsert() throws IOException{
		File f1 = new File("tf_idf_group/vector.txt");
		
		FileReader r = new FileReader(f1);
		BufferedReader br = new BufferedReader(r);
		
		int p_size = 6;
		String line =null;
		String str[] = new String[10];
		TrainedVector vec = new TrainedVector();
		while((line = br.readLine()) !=null){
			str = line.split("/ /");
			vec.Set(str[0], str[2], str[1], Double.parseDouble(str[3]), Double.parseDouble(str[4]), Double.parseDouble(str[5]), Double.parseDouble(str[6]), null);
			session.insert("test.insert1", vec);
		}
		session.commit();
		br.close();
	}
	
	@Test
	public void getRealOutput() throws IOException{
		int num = 0;
		FileWriter fw = new FileWriter("output/output.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int ou =0 ;ou<32;ou++){
			session.delete("test.reset2");
			session.commit();
			
			Integer id = session.selectOne("test.getClientSeq");
			
			
			File f1 = new File("input/input"+num+".txt");
		
			FileReader r = new FileReader(f1);
			BufferedReader br = new BufferedReader(r);
			ClientVector in2 = new ClientVector();
			
			String label = new String();
			String file_name = new String();
			int p_size = 6;
			String line =null;
			String str[] = new String[10];
			TrainedVector vec = new TrainedVector();
			
			if((line=br.readLine()) !=null){
				label = line;
				br.readLine();
			}
			
			while((line = br.readLine()) !=null){
				str = line.split(" ");
				file_name = "input/input"+num+".txt";
				
				if(str[0].length()<50){
					try{
						in2.Set(id, null, str[0], Double.parseDouble(str[1]), null);
					}
					catch(NumberFormatException e){
						System.out.println(str);
					}
					session.insert("test.insert2", in2);
				}
			}
			session.commit();
			
			bw.write("filename = " +file_name);
			bw.write(" , 원래 그룹 = " +label); bw.newLine();
			bw.write("Distance List");
			bw.newLine();
			List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for(int i=0;i<dis.size();i++){
				dis.get(i).setLabel(session.selectOne("test.getLabel",dis.get(i).getFile_name()));
				String key = dis.get(i).getLabel();
				if(map.containsKey(key)){
					Integer count = map.get(key);
					map.put(key, count+1);
				}
				else{
					map.put(key, 1);
				}
				
				bw.write("label = "+dis.get(i).getLabel() + " | "+"average = "+dis.get(i).getAverage() + " | "+"distance = "+dis.get(i).getDistance());
				bw.newLine();
			}
			bw.write("------------------------------------------------------------------------"); 
			bw.newLine();
	       
			Iterator it = TestT3.sortByValue(map).iterator();
			String high_label = new String();
			if(it.hasNext()){
				high_label = (String) it.next();
				bw.write("Label = " + high_label + " | count = " + map.get(high_label));
		        bw.newLine();
			}
			while(it.hasNext()){
	            String temp = (String) it.next();
	            bw.write("Label = " + temp + " | count = " + map.get(temp));
	            bw.newLine();
	        }	
			if(label.equals(high_label)){
				bw.write("분류가 성공하였습니다");
	            bw.newLine();
			}
			else{
				bw.write("분류가 실패하였습니다");
	            bw.newLine();
			}
			bw.write("------------------------------------------------------------------------"); 
			bw.newLine();
			bw.newLine();
			bw.flush();
			br.close();
			
			num++;
		}
		
	}
	public void resetDB(){
		session.delete("test.reset1");
		session.delete("test.reset2");
		session.commit();
		session.close();
	}
}





//@Test(timeout=5000) : 메서드 수행시간 제한하기
//@Test(expected=RuntimeException.class) : 예외가 발생하면 통과
//@Before : 실행시 한번 실행
//@After : 실행시 한번 실행

//
//SELECT 
//input.keyword,
//input.tf,
//trained.idf,
//input.tf * trained.idf AS tf_idf
//FROM
//(SELECT 
//    keyword, AVG(tf) AS tf
//FROM
//    knn.client_vector
//GROUP BY keyword) AS input
//    LEFT OUTER JOIN
//(SELECT 
//    keyword, AVG(idf) AS idf
//FROM
//    knn.trained_vector
//GROUP BY keyword) AS trained ON input.keyword = trained.keyword
//ORDER BY tf_idf DESC
//limit 360
