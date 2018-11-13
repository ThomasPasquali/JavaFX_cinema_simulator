package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
	
	private Socket s;
	private BufferedReader reader;
	private PrintWriter writer;
	
	
	public Client(String ip, int port) throws UnknownHostException, IOException {
		this(new Socket(ip, port));
	}
	
	public Client(Socket s) throws IOException {
		this.s=s;
		reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer=new PrintWriter(s.getOutputStream(), true);
	}
	public void write(String s) throws IOException {
		writer.println(s);
		System.out.println("TO "+this.s.getInetAddress()+":"+this.s.getPort()+": "+s);
	}
	
	public String readLine() throws IOException {
		String answer=reader.readLine();
		System.out.println("FROM "+this.s.getInetAddress()+":"+this.s.getPort()+": "+answer);
		return answer;
	}
	
	public void close() throws IOException {
		reader.close();
		writer.close();
		s.close();
	}

	public Socket getSocket() {
		return s;
	}
	
}
