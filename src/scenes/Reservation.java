package scenes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import main.Main;
import misc.ActReservation;
import misc.ProtocolFormatter;

public class Reservation extends VBox{
	
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
	
	private ObservableList<ActReservation> allItems;
	
	@FXML
    private TextField fieldTitolo;

    @FXML
    private TextField fieldSedia;
	
	@FXML
	void initialize() {
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		fieldSedia.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String arg2) ->{
			String newVal =	arg2.replaceAll("[^\\d]", "");
			newVal = newVal.length()>3?newVal.substring(0, 3):newVal;
			fieldSedia.setText(newVal);
			list.setItems(getFilteredItems(fieldTitolo.getText(), newVal));	
		});
		
		fieldTitolo.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String arg2) ->{
			list.setItems(getFilteredItems(arg2, fieldSedia.getText()));
		});
	}
	
	private ObservableList<ActReservation> getFilteredItems(String title, String seat){
		title=title.isEmpty()?null:title;
		seat=seat.isEmpty()?null:seat;
		ObservableList<ActReservation> newItems=FXCollections.observableArrayList();
		for(ActReservation a : allItems)
			if((title==null||a.getTitle().toLowerCase().contains(title.toLowerCase()))&&
				(seat==null||(a.getSeat()+"").contains(seat)))
				newItems.add(a);
		return newItems;
	}
	
	public void refresh(){
		try {
			Main.client_unicast.write("GET_ACTIVE_RESERV " + Main.id);
			String ans=Main.client_unicast.readLine();
			if(ans.equals("NO_DATA")) {
				list.getItems().clear();
				Main.showInfoAlert("Non hai prenotazioni attive", null);
				return;
			}
			allItems = FXCollections.observableArrayList();
			String[] a=ans.split(" ");
			String[] sws=a[1].split("&");
			for (int i = 0; i < sws.length; i++) {
				String res=sws[i];
				String[] params=res.split("/");
				allItems.add(new ActReservation(
						Integer.parseInt(params[0]),
						Integer.parseInt(params[1]),
						ProtocolFormatter.decode(params[2]), 
						ProtocolFormatter.decode(params[3]), 
						LocalDate.parse(ProtocolFormatter.decode(params[4]), Main.sqlDateF), 
						LocalTime.parse(ProtocolFormatter.decode(params[5]), Main.timeF)));
			}
			list.setItems(allItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    void cancel() {
		List<ActReservation> l=list.getSelectionModel().getSelectedItems();
		if(l.size()<=0) return;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Are you sure?");
		alert.setHeaderText("Sicuro di voler annullare l"+(l.size()==1?'a':'e')+" seguent"+(l.size()==1?'e':'i')+" pronotazion"+(l.size()==1?'e':'i')+"?");
		StringJoiner sj=new StringJoiner(", ");
		for (ActReservation actReservation : l) 
			sj.add(actReservation.getId()+"");
		alert.setContentText(sj.toString());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			StringJoiner sjERR=new StringJoiner("\r\n");
			for (ActReservation actReservation : l) {
				try {
					Main.client_unicast.write("DELETE_RESERV "+actReservation.getId());
					String[] params = Main.client_unicast.readLine().split(" ");
					if(params[0].equals("ERROR"))
						sjERR.add(params[1]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(sjERR.toString().isEmpty())
				Main.showInfoAlert("Pronotazioni annullate con successo", "");
			else
				Main.showWarningAlert("Errori durantu l'annullamento:", sjERR.toString());
			refresh();
		}
	}
	
	@FXML
    void back() {
		Main.switchToIntro();
    }
	
}