package misc;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ImageReceiever {
	
	public static BufferedImage receieve(String inetAddr, int port, int token) throws Exception{
		System.out.println("Connecting to: "+inetAddr+":"+port);
		
		Socket s=new Socket(inetAddr, port);
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		DataInputStream in = new DataInputStream(is);
		DataOutputStream out = new DataOutputStream(os);
		
		out.writeUTF(token+"");
		
		System.out.println("Submitted the token");
		
		boolean invalidImage = in.readUTF().equals("0");
		
		if(invalidImage) {
        	System.out.println("Got image not found error");
        	s.close();
        	return null;
        }

        System.out.println("Receiveing image...");
        
        BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(is));
        
        System.out.println("Image received!");

        s.close();
        
        return img;
	}
	
}
