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
	private String db_ip="127.0.0.1", db_port="3306", db_username="root", db_password="", db_name="cinema";
	
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
			if(params.length>1) {
				Statement s=connection.createStatement();
				s.execute("USE "+db_name);
				ResultSet set=s.executeQuery(
						"SELECT ID, Password\r\n" + 
						"FROM accounts\r\n" + 
						"WHERE Username = '"+params[1]+"'");
				set.next();
				if(!set.isLast()) {
					c.write("WRONG CREDENTIALS");
					c.close();
				}else {
					if(params[2].equals(set.getString("Password"))) {
						mapID_Client.put(set.getInt("ID"), new ClientTeatro(c, set.getInt("ID"), connection, this));
						c.write("OK "+set.getInt("ID"));
					}else {
						c.write("WRONG CREDENTIALS");
						c.close();
					}
				}
			}else {
				c.write("INVALID SINTAX");
				c.close();
			}
		}
	}
	
	public void removeClient(int id) {
		mapID_Client.remove(id);
	}
		
	public static void main(String[] args) throws IOException, SQLException {
		new ServerTeatro(1234);
	}

}
