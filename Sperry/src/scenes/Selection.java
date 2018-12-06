package scenes;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import main.Main;
import misc.ProtocolFormatter;
import misc.Show;

public class Selection extends BorderPane{
	
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
	private ScrollPane sp;
	
	@FXML
	void initialize() {
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        list.setOnMouseClicked((MouseEvent e)->{
			if(e.getClickCount()==2)
				try {
					Main.switchToHome(list.getSelectionModel().getSelectedItem());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		});
        list.prefWidthProperty().bind(sp.widthProperty());
        datePicker.setValue(LocalDate.now());
	}
	
	@FXML
	void find() {
		LocalDate date = datePicker.getValue();
		if(date==null) return;
		try {
			Main.client.write("GET_SHOWS "+Main.sqlDateF.format(date));
			String ans=Main.client.readLine();
			if(ans.equals("NO_DATA")) {
				list.getItems().clear();
				Main.showInfoAlert(
						"Non ci sono spettacoli il "+Main.normalDateF.format(datePicker.getValue()), 
						"Prova con un'altra data");
				return;
			}
			String[] a=ans.split(" ");
			String[] sws=a[1].split("&");
			list.getItems().clear();
			for (int i = 0; i < sws.length; i++) {
				String show=sws[i];
				String[] params=show.split("/");
				list.getItems().add(new Show(
						Integer.parseInt(params[0]),
						ProtocolFormatter.decode(params[1]), 
						LocalDate.parse(ProtocolFormatter.decode(params[2]), Main.sqlDateF), 
						LocalTime.parse(ProtocolFormatter.decode(params[3]), Main.timeF), 
						Integer.parseInt(params[4]), 
						Integer.parseInt(params[5]), 
						params[6]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
    void back() {
		Main.switchToIntro();
    }

}