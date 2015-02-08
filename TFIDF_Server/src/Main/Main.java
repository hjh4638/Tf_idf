package Main;

import Network.Server;

public class Main {

	public static void main(String[] args) {
		Server relayServer= new Server();
		relayServer.start();
	}
}
