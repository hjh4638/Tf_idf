package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.junit.*;

import com.mysql.jdbc.Connection;

import dto.ClientVector;
import dto.TrainedVector;
import static org.junit.Assert.*;
public class TestT1 {

	SqlSessionFactory sqlSessionFactory;
	SqlSession session;
	String words[];
	int input_dimension;
	int vector_dimension;
	@Before
	public void INIT() throws IOException {
		//���̹�Ƽ�� �ʱ�ȭ
		String resource = "sqlMap/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		
		//�ܾ� ���� ����
		words= new String[1001];
		
		//�Է� ���� ����
		input_dimension = 30;
		vector_dimension = 10;
		assertNotEquals(sqlSessionFactory, null);
		assertNotEquals(session, null);
	}
	//mybatis ���� �׽�Ʈ
	@Test
	public void mybatisInitTest() throws IOException{
		assertNotEquals(sqlSessionFactory, null);
	}
	
	//jdbc ���� �׽�Ʈ
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
	//NN ���� ��
	@Test(expected=PersistenceException.class)
	public void insertNullTest(){
		TrainedVector in1 = new TrainedVector();
		ClientVector in2 = new ClientVector();
		in1.Set("test1.txt", "apple" , "����", null, null, null, null, null);
		in2.Set(1, "asdfasf", null, null, null);
		in1.Set(null, null, null, null, null, null, null, null);
		in2.Set(null, null, null, null, null);
		int size1 = session.insert("test.insert1", in1);
		int size2 = session.insert("test.insert2", in2);
		session.commit();
	}
	//pk ����
	@Test(expected=PersistenceException.class)
	public void insertDuplicatePk(){
		TrainedVector in1 = new TrainedVector();
		ClientVector in2 = new ClientVector();
		in1.Set("test1.txt", "apple" , "����", null, null, null, null, 4);
		in2.Set(1, "asdfasf", null, null, 4);
		int size1 = session.insert("test.insert1", in1);
		int size2 = session.insert("test.insert2", in2);
		session.commit();
	}
	@Test
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
	public void insertRandomTrained() throws IOException{
		TrainedVector in1 = new TrainedVector();
		String str[] = getRandomWord();
		Random random = new Random();
		int flag = 1;
		for(int j = 0; j <500;j++){
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
	
	//@After
	public void resetDB(){
		session.delete("test.reset1");
		session.delete("test.reset2");
		session.commit();
		session.close();
	}
}





//@Test(timeout=5000) : �޼��� ����ð� �����ϱ�
//@Test(expected=RuntimeException.class) : ���ܰ� �߻��ϸ� ���
//@Before : ����� �ѹ� ����
//@After : ����� �ѹ� ����
