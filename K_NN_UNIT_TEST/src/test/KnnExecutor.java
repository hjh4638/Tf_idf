package test;

import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dto.ClientVector;
import dto.KnnResult;
import dto.TrainedVector;

public class KnnExecutor {
	SqlSessionFactory sqlSessionFactory;
	SqlSession session;
	String words[];
	int input_dimension;
	int vector_dimension;
	
	KnnExecutor() throws IOException {
		String resource = "sqlMap/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		
		words= new String[1001];
		
		input_dimension = 1000;
		vector_dimension = 360;
	}
	
	public void MechineLearning() throws IOException{
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
	
}
