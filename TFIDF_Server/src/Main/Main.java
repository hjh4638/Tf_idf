package Main;

import java.io.IOException;

import Network.Server;

public class Main {

	public static void main(String[] args) throws IOException {
		//�������� �ҽ� ������
		//�ӽŷ���
		/*
		KnnExecutor knn = new KnnExecutor();
		knn.MechineLearning();
		*/
		Server relayServer= new Server();
		relayServer.start();
	}
}
