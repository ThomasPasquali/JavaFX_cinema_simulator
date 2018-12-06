package scenes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import main.Main;
import misc.ActReservation;
import misc.ProtocolFormatter;

public class Reservation extends BorderPane{
	
	public Reservation() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("reservation.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private ListView<ActReservation> list;
	
	@FXML
	private ScrollPane sp;
	
	@FXML
	void initialize() {
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		try {
			Main.client.write("GET_ACT_RESERV" + Main.id);
			String ans=Main.client.readLine();
			if(ans.equals("NO_RESERVATION")) {
				list.getItems().clear();
				Main.showInfoAlert("Non hai prenotazioni attive", null);
				return;
			}
			String[] a=ans.split(" ");
			String[] sws=a[1].split("&");
			list.getItems().clear();
			for (int i = 0; i < sws.length; i++) {
				String res=sws[i];
				String[] params=res.split("/");
				list.getItems().add(new ActReservation(
						Integer.parseInt(params[0]),
						ProtocolFormatter.decode(params[1]), 
						LocalDate.parse(ProtocolFormatter.decode(params[2]), Main.sqlDateF), 
						LocalTime.parse(ProtocolFormatter.decode(params[3]), Main.timeF), 
						params[6]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        list.setOnMouseClicked((MouseEvent e)->{
			if(e.getClickCount()==2)
				try {
					Main.client.write("DELETE_RESERVATION");
					Main.showInfoAlert("Prenotazione annullata", null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		});
        list.prefWidthProperty().bind(sp.widthProperty());
	}
	
	@FXML
    void back() {
		Main.switchToIntro();
    }

}