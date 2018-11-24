package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringJoiner;

import misc.Client;

public class ClientTeatro extends Thread{

	private Client client;
	private Connection connection;
	private ServerTeatro server;
	private int id;

	public ClientTeatro(Client client, int id, Connection connection, ServerTeatro server) {
		this.client = client;
		this.connection=connection;
		this.id=id;
		this.server=server;
		start();
	}
	
	@Override
	public void run() {
		String request;
		try {
			while(!(request=client.readLine()).equals("QUIT")){
				String[] params=request.split(" ");
				if(params[0].equals("GET_SHOWS")){
					Statement s=connection.createStatement();
					ResultSet set=s.executeQuery(
							"SELECT  s.ID AS id,  s.Titolo AS Titolo , \r\n" + 
							"			s.Ora AS Ora,  \r\n" + 
							"			sala.Posti -\r\n" + 
							"			(SELECT COUNT(*)\r\n" + 
							"			 FROM prenotazioni AS p\r\n" + 
							"			 WHERE p.Spettacolo = s.ID)AS Liberi, \r\n" + 
							"			sala.Posti AS Totali, \r\n" + 
							"			sala.Nome AS Hall\r\n" + 
							"FROM spettacoli AS s\r\n" + 
							"	JOIN sale AS sala ON s.Sala =  sala.ID\r\n" + 
							"WHERE s.Data = '"+params[1]+"'");
					StringJoiner sjAnd=new StringJoiner("&");
					while(set.next()) {
						StringJoiner sj=new StringJoiner("/");
						sj.add(set.getString("id"));
						sj.add(set.getString("Titolo"));
						sj.add(params[1]);
						sj.add(set.getString("Ora"));
						sj.add(set.getString("Liberi"));
						sj.add(set.getString("Totali"));
						sj.add(set.getString("Hall"));
						sjAnd.add(sj.toString());
					}
					if(sjAnd.toString().isEmpty()) {
						System.out.println("non trovato");
						client.write("NO_DATA");
					}
						
					else {
						System.out.println("trovato");
							client.write("SHOWS_FOR_"+params[1]+" "+sjAnd.toString());
					}
					
				}
			}
		} catch (Exception e) {
			server.removeClient(id);
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println(id+" LEFT");
		}
	}
	
}
