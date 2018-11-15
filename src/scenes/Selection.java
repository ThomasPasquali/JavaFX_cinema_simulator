package scenes;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import main.Main;
import misc.ProtocolFormatter;
import misc.Show;

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
	private ListView<Show> list; 
	
	@FXML
	void initialize() {
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        list.setOnMouseClicked((MouseEvent e)->{
			if(e.getClickCount()==2) 
				Main.switchToHome(list.getSelectionModel().getSelectedItem());
		});
	}
	
	@FXML
	void find() {
		LocalDate date = datePicker.getValue();
		try {
			 //Main.client.write("GET_SHOWS "+Main.sqlDateF.format(date));
			String[] a="sadds Titolo/2018-01-01/12:34/20/100/4&Titolo/2018-01-01/12:34/20/120/5".split(" ");//Main.client.readLine().split(" ");
			String[] sws=a[1].split("&");
			list.getItems().clear();
			for (int i = 0; i < sws.length; i++) {
				String show=sws[i];
				String[] params=show.split("/");
				list.getItems().add(new Show(
						ProtocolFormatter.decode(params[0]), 
						LocalDate.parse(params[1], Main.sqlDateF), 
						LocalTime.parse(params[2], Main.timeF), 
						Integer.parseInt(params[3]), 
						Integer.parseInt(params[4]), 
						Integer.parseInt(params[5])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}