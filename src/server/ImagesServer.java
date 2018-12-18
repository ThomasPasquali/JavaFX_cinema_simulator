package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImagesServer extends Thread{
	
	private String imgsPath;
	private HashMap<Integer, String> activeTokens_imgName;
	private ServerSocket server;
	private int port;
	
	public ImagesServer(int port, String imgsPath) {
		this.imgsPath = imgsPath;
		this.port = port;
		activeTokens_imgName=new HashMap<>();
		System.out.println("ImagesServer initialized");
	}
	
	@Override
	public void run() {
		try {
			server=new ServerSocket(port);
			System.out.println("ImagesServer running on port: "+port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		while(true) 
			try {
				new ImagesServerClient(server.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @param token
	 * @param imageFileName Ex. Image.jpg
	 * @return true if the token has been successfully added, false if the token is already active
	 */
	public boolean createToken(int token, String imageFileName) {
		if(activeTokens_imgName.containsKey(token))
			return false;
		activeTokens_imgName.put(token, imageFileName);
		System.out.println("Created token: "+token+" -> "+imageFileName);
		return true;
	}
	
	private class ImagesServerClient extends Thread{
		private Socket s;
		private DataInputStream is;
		private DataOutputStream os;
		public ImagesServerClient(Socket s) throws IOException {
			this.s = s;
			is = new DataInputStream(s.getInputStream());
			os = new DataOutputStream(s.getOutputStream());
			System.out.println("New connection to ImagesServer: "+s.getInetAddress()+":"+s.getPort());
		}
		@Override
		public void run() {
			//TODO read token, send image
			try {
				System.out.println("Listening for token...");
				
				int token = Integer.parseInt(is.readUTF());
				
				System.out.println("Got token: "+token);
				
				String imgName = activeTokens_imgName.get(token);
				if(imgName!=null) {
					File f=new File(imgsPath+imgName);
					if(f.exists()) {
						sendImageFound();
						sendImage(f);
					}else
						sendImageNotFound();
				}else
					sendImageNotFound();
				
				s.close();
				System.out.println("Closed connection with: "+s.getInetAddress()+":"+s.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void sendImage(File f) throws IOException {
			System.out.println("Sending image...");
			
	        ImageIO.write(ImageIO.read(f), getFileExtension(f), s.getOutputStream());
	        os.flush();
	        
	        System.out.println("Sent!");
		}
		
		private void sendImageFound() throws IOException {
			 os.writeUTF("1");
			 os.flush();
		}
		
		private void sendImageNotFound() throws IOException {
			 os.writeUTF("0");
			 os.flush();
		}
		
		private String getFileExtension(File f) {
			String[] s= f.getAbsolutePath().split("\\.");
			return s[s.length-1];
		}
		
	}

}
