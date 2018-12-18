package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import misc.Client;

public class ServerTeatro extends Thread{

	private ServerSocket server;
	private HashMap<Integer, ClientTeatro> map_AccountID_Client_unicast;
	private HashMap<Integer, ClientTeatro> map_AccountID_Client_broadcast;
	private ImagesServer imagesServer;
	private Connection connection;
	private int port;
	private String db_ip, db_port, db_username, db_password, db_name, imgsPath;
	
	public ServerTeatro(int port, String db_ip, String db_port, String db_username, String db_password, String db_name, String imgsPath) {
		this.db_ip = db_ip;
		this.db_port = db_port;
		this.db_username = db_username;
		this.db_password = db_password;
		this.db_name = db_name;
		this.imgsPath = imgsPath.endsWith("\\")?imgsPath:imgsPath+"\\";
		this.port = port;
	}

	public ServerTeatro(int port) {
		this.port = port;
		db_ip="127.0.0.1";
		db_port="3306";
		db_username="root";
		db_password="";
		db_name="cinema";
		imgsPath="";
	}
	
	@Override
	public void run() {
		map_AccountID_Client_unicast=new HashMap<>();
		map_AccountID_Client_broadcast=new HashMap<>();
		String db_loc="jdbc:mysql://"+db_ip+":"+db_port+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
		try {
			connection=DriverManager.getConnection(db_loc, db_username, db_password);
			server=new ServerSocket(port);
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		imagesServer = new ImagesServer(port+1, imgsPath);
		imagesServer.start();
		
		while(true) {
			try {
				Client c=new Client(server.accept());
				String request;
				
				while(!(request=c.readLine()).startsWith("LOGIN")) 
					c.write("Expected LOGIN [Username] [Password] [UNI/MUL]");
				
				String[] params=request.split(" ");
				
				if(!(params[3].equals("MUL")||params[3].equals("UNI"))) {
					c.write("Expected LOGIN [Username] [Password] [UNI/MUL]");
				}else{
					if(params.length>1) {
						Statement s=connection.createStatement();
						s.execute("USE "+db_name);
						ResultSet set=s.executeQuery(
								"SELECT ID, Password\r\n" + 
								"FROM accounts\r\n" + 
								"WHERE Username = '"+params[1]+"'");
						boolean isSetEmpty=!set.next();
						if(alreadyLogged(set.getInt("ID"), params[3].equals("UNI"))) 
							c.write("ACCOUNT ALREADY LOGGED");
						else {
							if(isSetEmpty) {
								c.write("WRONG CREDENTIALS");
								c.close();
							}else {
								if(params[2].equals(set.getString("Password"))) {
									if(params[3].equals("UNI")) {
										map_AccountID_Client_unicast.put(set.getInt("ID"), new ClientTeatro(c, set.getInt("ID"), connection, this, params[3]));
										c.write("OK "+set.getInt("ID"));
									}else {
										map_AccountID_Client_broadcast.put(set.getInt("ID"), new ClientTeatro(c, set.getInt("ID"), connection, this, params[3]));
										c.write("OK "+set.getInt("ID"));
									}
								}else {
									c.write("WRONG CREDENTIALS");
									c.close();
								}
							}
						}
					}else {
						c.write("INVALID SINTAX");
						c.close();
					}
				}	
			}catch(Exception e) {
				System.out.println("Cosa strana");
				e.printStackTrace();
			}
		}
	}
	
	public ImagesServer getImagesServer() {
		return imagesServer;
	}
	
	public String getImagesPath() {
		return imgsPath;
	}
	
	private boolean alreadyLogged(int id, boolean isUnicast) {
		HashMap<Integer, ClientTeatro> map=isUnicast?map_AccountID_Client_unicast:map_AccountID_Client_broadcast;
		for (Integer i : map.keySet()) 
			if(i==id)
				return true;
		return false;
	}
	
	public void removeClient(int id) {
		map_AccountID_Client_unicast.remove(id);
		map_AccountID_Client_broadcast.remove(id);
	}
	
	public void broadcast(String message, int excludedID) throws IOException {
		for (ClientTeatro c : map_AccountID_Client_broadcast.values()) 
			if(c.getAccountID()!=excludedID) 
				c.write(message);
	}
		
	public static void main(String[] args) throws IOException, SQLException {
		//TODO parse params -dbip 127.0.0.1 -dbport 3306...
		new ServerTeatro(1234).start();
	}

}
