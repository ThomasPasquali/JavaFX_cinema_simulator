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

public class ServerTeatro {

	private ServerSocket server;
	private HashMap<Integer, ClientTeatro> mapID_Client;
	private Connection connection;
	private String db_ip="127.0.0.1", db_port="3306", db_username="root", db_password="", db_name="teatro";
	
	public ServerTeatro(int port) throws IOException, SQLException {
		mapID_Client=new HashMap<>();
		String db_loc="jdbc:mysql://"+db_ip+":"+db_port+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
		connection=DriverManager.getConnection(db_loc, db_username, db_password);
		server=new ServerSocket(port);
		while(true) {
			Client c=new Client(server.accept());
			String request;
			
			while(!(request=c.readLine()).startsWith("LOGIN")) 
				c.write("Expected LOGIN [Username] [Password]");
			
			String[] params=request.split(" ");
			
			Statement s=connection.createStatement();
			s.execute("USE "+db_name);
			ResultSet set=s.executeQuery(
					"SELECT ID\r\n" + 
					"FROM accounts\r\n" + 
					"WHERE Username = '"+params[1]+"' AND Password = '"+(params.length>2?params[2]:"")+"'");
			set.next();
			
			//TODO giusto
			if(set.isLast()) {
				System.out.println("no");
				c.write("WRONG CREDENTIALS");
				c.close();
			}else {
				System.out.println("si");
				mapID_Client.put(set.getInt("ID"), new ClientTeatro(c, connection));
				c.write("OK");
			}
		}
	}
		
	public static void main(String[] args) throws IOException, SQLException {
		new ServerTeatro(1234);
	}

}
