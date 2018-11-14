package scenes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import main.Main;
import misc.Client;

public class Selection extends VBox{
	
	public Selection() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("selection.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	void find() {
		LocalDate date = datePicker.getValue();
		try {
			Main.client.write(Main.dtf.format(date));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
