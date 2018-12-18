package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
	
	private Socket s;
	private BufferedReader reader;
	private PrintWriter writer;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public Client(String ip, int port) throws UnknownHostException, IOException {
		this(new Socket(ip, port));
	}
	
	public Client(Socket s) throws IOException {
		this.s=s;
		inputStream=s.getInputStream();
		outputStream=s.getOutputStream();
		reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer=new PrintWriter(s.getOutputStream(), true);
	}
	
	public void write(String s) throws IOException {
		writer.println(s);
		s=s.length()>30?s.substring(0, 20)+"[...]"+s.substring(s.length()-10, s.length()):s;
		System.out.println("TO "+this.s.getInetAddress()+":"+this.s.getPort()+": "+s);
	}
	
	public String readLine() throws IOException {
		String answer=reader.readLine();
		String s=answer.length()>30?answer.substring(0, 20)+"[...]"+answer.substring(answer.length()-10, answer.length()):answer;
		System.out.println("FROM "+this.s.getInetAddress()+":"+this.s.getPort()+": "+s);
		return answer;
	}
	
	public void close() throws IOException {
		reader.close();
		writer.close();
		s.close();
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public Socket getSocket() {
		return s;
	}
	
}
