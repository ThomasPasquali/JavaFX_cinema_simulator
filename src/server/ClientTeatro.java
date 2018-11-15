package server;

import java.io.IOException;
import java.sql.Connection;

import misc.Client;

public class ClientTeatro extends Thread{

	private Client client;
	private Connection connection;

	public ClientTeatro(Client client, Connection connection) {
		this.client = client;
		this.connection=connection;
		start();
	}
	
	@Override
	public void run() {
		String request;
		try {
			while(!(request=client.readLine()).equals("QUIT")){
				String[] params=request.split(" ");
				if(params[0].equals("GET_ACCOUNT")){
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
