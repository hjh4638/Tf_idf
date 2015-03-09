package Main;

import java.io.IOException;

import Network.Server;

public class Main {

	public static void main(String[] args) throws IOException {
		//주찬이형 소스 돌려서
		//머신러닝
		/*
		KnnExecutor knn = new KnnExecutor();
		knn.MechineLearning();
		*/
		Server relayServer= new Server();
		relayServer.start();
	}
}
