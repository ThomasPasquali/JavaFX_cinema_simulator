package misc;

import java.io.IOException;

import main.Main;

public class BroadcastUpdater extends Thread{

	private Client client;
	
	public BroadcastUpdater(Client client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String answ=client.readLine();
				if(answ==null) {System.out.println("Chiusura updater"); return;}
				String[] params=answ.split(" ");
				if(params[0].equals("RESERVED")&&
						Main.getHome().getShow().getId()==Integer.parseInt(params[1])&&
						Main.getHome().isActive()) {
					Main.getHome().reserveSeat(Integer.parseInt(params[2]));
				}else if(params[0].equals("FREED")&&
						Main.getHome().getShow().getId()==Integer.parseInt(params[1])&&
						Main.getHome().isActive()) {
					Main.getHome().freeSeat(Integer.parseInt(params[2]));
				}else if(params[0].equals("BOOKED")&&
						Main.getHome().getShow().getId()==Integer.parseInt(params[1])&&
						Main.getHome().isActive()) {
					Main.getHome().blockSeat(Integer.parseInt(params[2]));
				}else if(params[0].equals("UNBOOKED")&&
						Main.getHome().getShow().getId()==Integer.parseInt(params[1])&&
						Main.getHome().isActive()) {
					Main.getHome().freeSeat(Integer.parseInt(params[2]));
					//TODO anche sul server
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
