package test;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

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
		//마이버티스 초기화
		String resource = "sqlMap/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		
		//단어 개수 지정
		words= new String[1001];
		
		//입력 벡터 개수
		input_dimension = 1000;
		vector_dimension = 360;
		assertNotEquals(sqlSessionFactory, null);
		assertNotEquals(session, null);
	}
	
	//mybatis 연결 테스트
	@Test
	public void mybatisInitTest() throws IOException{
		assertNotEquals(sqlSessionFactory, null);
	}
	
	//jdbc 연결 테스트
	@Test
	public void jdbcConnectTest() throws SQLException{
		Connection con = null;
		con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/knn",
					"root", "tkatjdthapa123");
	}
	@Test
	public void selectTest(){
		Integer in = session.selectOne("test.sel1");
		List<ClientVector> li1 = session.selectList("test.sel2");
		List<TrainedVector> li2 = session.selectList("test.sel3");
		assertNotEquals(li1.size(), 0);
		assertNotEquals(li2.size(), 0);
	}
	//NN 위반 등
	@Test(expected=PersistenceException.class)
	public void insertNullTest(){
		TrainedVector in1 = new TrainedVector();
		ClientVector in2 = new ClientVector();
		in1.Set("test1.txt", "apple" , "과일", null, null, null, null, null);
		in2.Set(1, "asdfasf", null, null, null);
		in1.Set(null, null, null, null, null, null, null, null);
		in2.Set(null, null, null, null, null);
		int size1 = session.insert("test.insert1", in1);
		int size2 = session.insert("test.insert2", in2);
		session.commit();
	}
	//pk 위반
	@Test(expected=PersistenceException.class)
	@Ignore
	public void insertDuplicatePk(){
		TrainedVector in1 = new TrainedVector();
		ClientVector in2 = new ClientVector();
		in1.Set("test1.txt", "apple" , "과일", null, null, null, null, 4);
		in2.Set(1, "asdfasf", null, null, 4);
		int size1 = session.insert("test.insert1", in1);
		int size2 = session.insert("test.insert2", in2);
		session.commit();
	}
	@Test
	@Ignore
	public void insertRandomTf() throws IOException{
		ClientVector in2 = new ClientVector();
		String str[] = getRandomWord();
		Random random = new Random();
		for(int i=0;i<input_dimension;i++){
			double ran_tf =  random.nextInt(1000);
			in2.Set(10, "testTF1.txt", str[i], ran_tf, null);
			session.insert("test.insert2", in2);
		}
		session.commit();
	}
	@Test
	@Ignore
	public void insertRandomTrained() throws IOException{
		TrainedVector in1 = new TrainedVector();
		String str[] = getRandomWord();
		Random random = new Random();
		int flag = 1;
		for(int j = 0; j <300;j++){
			for(int i=0;i<vector_dimension;i++){
				double ran_tf =  random.nextInt(1000);
				double ran_idf = random.nextDouble();
				String name = "Trained"+flag+".txt";
				in1.Set(name, str[i], null,ran_tf, null,ran_idf , ran_tf*ran_idf, null);
				session.insert("test.insert1", in1);
			}
			flag++;
		}
		session.commit();
	}
	public void setWord() throws IOException{
		File word_list = new File("words.txt");
		FileReader file_reader = new FileReader(word_list);
		BufferedReader reader = new BufferedReader(file_reader);
		String line = null;
		
		int count =0;
		while((line=reader.readLine())!=null){
			words[count]=line;
			count++;
			if(count>1000){
				break;
			}
		}
		reader.close();
	}
	public String[] getRandomWord() throws IOException{
		setWord();
		String str[] = new String[input_dimension];
		Random random = new Random();
		for(int i = 0; i<input_dimension;i++){
			int ran = random.nextInt(1000);
			str[i] = words[ran];
		}
		return str;
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
	@Ignore
	public void getID(){
		Integer id = session.selectOne("test.getClientSeq");
		Integer in = session.selectOne("test.sel1");
		List<TrainedVector> a = session.selectList("test.sel3");
		a.get(0).toString();
		System.out.println(id);
		System.out.println(in);
	}
	@Test
	@Ignore
	public void getLineFileAndInsertInput() throws IOException{
		session.delete("test.reset2");
		session.commit();
		
		Integer id = session.selectOne("test.getClientSeq");
		
		
		File f1 = new File("input/input0.txt");
	
		FileReader r = new FileReader(f1);
		BufferedReader br = new BufferedReader(r);
		ClientVector in2 = new ClientVector();
		
		String label = new String();
		int p_size = 6;
		String line =null;
		String str[] = new String[10];
		TrainedVector vec = new TrainedVector();
		
		
		while((line = br.readLine()) !=null){
			str = line.split("/ /");
			label = str[1];
			try{
				in2.Set(id, str[0], str[2], Double.parseDouble(str[3]), null);
			}
			catch(NumberFormatException e){
				System.out.println(str);
			}
			session.insert("test.insert2", in2);
		}
		session.commit();
		
		int num = 0;
		FileWriter fw = new FileWriter("output/output"+num+".txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		System.out.println("input의 원래 그룹 = " +label);
		System.out.println("------------------------------------------------------------------------");
		System.out.println("avg list");
		List<KnnResult> avg = session.selectList("test.getKnnAvg", id);
		String test =avg.toString();
		System.out.println("------------------------------------------------------------------------");
		System.out.println("dis list");
		List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
		dis.toString();
		System.out.println("------------------------------------------------------------------------");
		System.out.println();
	
		br.close();
	}
	@Test
	public void getOutput() throws IOException{
		int num = 0;
		FileWriter fw = new FileWriter("output/output.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int ou =0 ;ou<143;ou++){
			session.delete("test.reset2");
			session.commit();
			
			Integer id = session.selectOne("test.getClientSeq");
			
			
			File f1 = new File("input/input"+num+".txt");
		
			FileReader r = new FileReader(f1);
			BufferedReader br = new BufferedReader(r);
			ClientVector in2 = new ClientVector();
			
			String label = new String();
			int p_size = 6;
			String line =null;
			String str[] = new String[10];
			TrainedVector vec = new TrainedVector();
			
			
			while((line = br.readLine()) !=null){
				str = line.split("/ /");
				label = str[1];
				try{
					in2.Set(id, str[0], str[2], Double.parseDouble(str[3]), null);
				}
				catch(NumberFormatException e){
					System.out.println(str);
				}
				session.insert("test.insert2", in2);
			}
			session.commit();
			
		
			bw.write("input"+num+"의 원래 그룹 = " +label); bw.newLine();
			bw.write("------------------------------------------------------------------------"); 
			bw.newLine();
			bw.write("avg list"); bw.newLine();
			List<KnnResult> avg = session.selectList("test.getKnnAvg", id);
			for(int i=0;i<avg.size();i++){
				bw.write("label = "+avg.get(i).getLabel() + " | "+"average = "+avg.get(i).getAverage() + " | "+"distance = "+avg.get(i).getDistance());
				bw.newLine();
			}
			bw.write("------------------------------------------------------------------------"); 
			bw.newLine();
			bw.write("dis list");
			bw.newLine();
			List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
			for(int i=0;i<dis.size();i++){
				bw.write("label = "+dis.get(i).getLabel() + " | "+"average = "+dis.get(i).getAverage() + " | "+"distance = "+dis.get(i).getDistance());
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
	
	
	//@After
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
