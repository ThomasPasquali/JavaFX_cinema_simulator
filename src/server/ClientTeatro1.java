package server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;

import misc.Client;
import misc.ProtocolFormatter;

public class ClientTeatro1 extends Thread{

	private Client client;
	private Connection connection;
	private ServerTeatro server;
	private String mode;
	private int id;

	public ClientTeatro1(Client client, int id, Connection connection, ServerTeatro server, String mode) {
		this.client = client;
		this.connection=connection;
		this.id=id;
		this.mode=mode;
		this.server=server;
		start();
	}
	
	@Override
	public void run() {
		String request;
		try {
			while(!(request=client.readLine()).startsWith("QUIT")){
				String[] params=request.split(" ");
				if(params[0].equals("GET_SHOWS"))
					get_shows(params);
				if(params[0].equals("GET_OCCUPIED_SEATS_FOR"))
					get_occupied_seats(params);
				if(params[0].equals("GET_SHOW_FROM_ID")){
					get_show_from_id(params);
				}
				if(params[0].equals("GET_IMAGE_FOR")){
					get_image_for(params);
				}
				if(params[0].equals("GET_ALL_SHOWS")){
					get_all_shows(params);
				}
				if(params[0].equals("GET_ACTIVE_RESERV")){
					get_active_reserv(params);
				}
				else if(params[0].equals("BOOK")){
					try {
						String[] seats=params[2].split("/");
						Statement s=connection.createStatement();
						for (String seat : seats) {
							s.executeUpdate(
									"INSERT INTO prenotazioni (Account, Spettacolo, Numero_posto)\r\n" + 
									"VALUES ("+id+", "+params[1]+", "+seat+")");
							server.broadcast("BOOKED "+params[1]+" "+seat, id);
						}
						write("OK");
					} catch (Exception e) {
						write("ERROR "+e.toString());
					}
				}else if(params[0].equals("RESERVE")) {
					server.broadcast("RESERVED "+params[1]+" "+params[2], id);
				}else if(params[0].equals("FREE")) {
					server.broadcast("FREED "+params[1]+" "+params[2], id);
				}
					
	private void get_active_reserv(String[] params){
		Statement s=connection.createStatement();
		ResultSet set=s.executeQuery(
				"SELECT  p.ID AS id,\r\n" +
								"p.Numero_posto AS seat, \r\n" +
								"s.Data AS date, \r\n" +  
								"s.Ora AS time, \r\n" +
								"s.Titolo AS title, \r\n" + 
								"s.Sala AS hall \r\n" +
				"FROM prenotazioni AS p \r\n" +
					"JOIN spettacoli AS s ON p.Spettacolo = s.ID \r\n"+
				"WHERE p.Account = "+params[1]+"\r\n"+
					 "AND if(s.data > CURRENT_DATE(), true, if(s.data = CURRENT_DATE(), s.Ora > CURRENT_TIME(), false))\r\n"+
				"ORDER BY s.ID, s.Ora, p.Numero_posto");
		StringJoiner sjAnd=new StringJoiner("&");
		while(set.next()) {
			StringJoiner sj=new StringJoiner("/");
			sj.add(set.getString("id"));
			sj.add(set.getString("seat"));
			sj.add(ProtocolFormatter.encode(set.getString("title")));
			sj.add(ProtocolFormatter.encode(set.getString("hall")));
			sj.add(ProtocolFormatter.encode(set.getString("date")));
			sj.add(ProtocolFormatter.encode(set.getString("time")));
			sjAnd.add(sj.toString());
		}
		if(sjAnd.toString().isEmpty())
			client.write("NO_DATA");
		else
			client.write("SHOWS_FOR_"+params[1]+" "+sjAnd.toString());
	}else if(params[0].equals("DELETE_RESERV")){
		try {
			connection.createStatement().executeUpdate("DELETE FROM prenotazioni WHERE  ID = "+params[1]);
			client.write("OK");
		} catch (SQLException e) {
			client.write("ERROR "+e.getMessage());
		}
	}
			}		
				
	private void get_shows(String[] params) {
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
				"WHERE s.Data = '"+params[1]+"'\r\n" +
				"ORDER BY s.Titolo");
		StringJoiner sjAnd=new StringJoiner("&");
		while(set.next()) {
			StringJoiner sj=new StringJoiner("/");
			sj.add(set.getString("id"));
			sj.add(ProtocolFormatter.encode(set.getString("Titolo")));
			sj.add(ProtocolFormatter.encode(params[1]));
			sj.add(ProtocolFormatter.encode(set.getString("Ora")));
			sj.add(set.getString("Liberi"));
			sj.add(set.getString("Totali"));
			sj.add(set.getString("Hall"));
			sjAnd.add(sj.toString());
		}
		if(sjAnd.toString().isEmpty())
			write("NO_DATA");
		else
			write("SHOWS_FOR_"+params[1]+" "+sjAnd.toString());	
	}
	
	private void get_occupied_seats(String[] params){
		ResultSet set=connection.createStatement().executeQuery(
				"SELECT Numero_posto AS n\r\n" + 
				"FROM prenotazioni\r\n" + 
				"WHERE Spettacolo = "+params[1]);
		StringJoiner sj=new StringJoiner("/");
		while(set.next())
			sj.add(set.getString("n"));
		write("OCCUPIED "+sj.toString());
	}

	private void get_show_from_id(String[] params){
	Statement s=connection.createStatement();
	ResultSet set=s.executeQuery(
			"SELECT  s.ID AS id,  s.Titolo AS Titolo , \r\n" + 
			"			s.Data AS Data,  \r\n" + 
			"			s.Ora AS Ora,  \r\n" + 
			"			sala.Posti -\r\n" + 
			"			(SELECT COUNT(*)\r\n" + 
			"			 FROM prenotazioni AS p\r\n" + 
			"			 WHERE p.Spettacolo = s.ID)AS Liberi, \r\n" + 
			"			sala.Posti AS Totali, \r\n" + 
			"			sala.Nome AS Hall\r\n" + 
			"FROM spettacoli AS s\r\n" + 
			"	JOIN sale AS sala ON s.Sala =  sala.ID\r\n" + 
			"WHERE s.ID = "+params[1]);
	set.next();
	StringJoiner sj=new StringJoiner("/");
	sj.add(set.getString("id"));
	sj.add(ProtocolFormatter.encode(set.getString("Titolo")));
	sj.add(ProtocolFormatter.encode(set.getString("Data")));
	sj.add(ProtocolFormatter.encode(set.getString("Ora")));
	sj.add(set.getString("Liberi"));
	sj.add(set.getString("Totali"));
	sj.add(set.getString("Hall"));
		
	if(sj.toString().isEmpty())
		client.write("NO_DATA");
	else
		client.write("SHOW_FOR_"+params[1]+" "+sj.toString());
	}
	
	private void get_all_shows(String[] params){
		Statement s=connection.createStatement();
		ResultSet set=s.executeQuery(
				"SELECT *\r\n" + 
				"FROM spettacoli\r\n" + 
				"WHERE if(Data > CURRENT_DATE(), true, if(Data = CURRENT_DATE(), Ora > CURRENT_TIME(), false))");
		StringJoiner sjAnd=new StringJoiner("&");
		while(set.next()) {
			StringJoiner sj=new StringJoiner("/");
			sj.add(set.getString("id"));
			sj.add(ProtocolFormatter.encode(set.getString("Titolo")));
			sj.add(ProtocolFormatter.encode(set.getString("Data")));
			sj.add(ProtocolFormatter.encode(set.getString("Ora")));
			sjAnd.add(sj.toString());
		}
		if(sjAnd.toString().isEmpty())
			client.write("NO_DATA");
		else
			client.write("ALL_SHOWS "+sjAnd.toString());
	}
	
	private void get_image_for(String[] params){
		File f=new File(server.getImagesPath()+params[1]);
		if(f.exists()) {
			int token;
			while(!server.getImagesServer().createToken((token=(int)(Math.random() * 10000)), params[1]));
			client.write("YOUR_TOKEN "+token);
		}else
			client.write("IMAGE_NOT_FOUND");
	}//More...
}
server.removeClient(id);
client.write("BYE");
client.close();
System.out.println(id+" "+mode+" LEFT");
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

	public int getAccountID() {
		return id;
	}
	
	public void write(String message) throws IOException {
		client.write(message);
	}
	
}
