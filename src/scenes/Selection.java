package scenes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import main.Main;
import misc.ImageReceiever;
import misc.ProtocolFormatter;
import misc.Show;
import misc.TreeShow;

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
    private TreeView<TreeShow> treeView;
	
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
        datePicker.setDayCellFactory(datePicker -> new DateCell () {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if(empty || date.compareTo(LocalDate.now()) < 0) {
                	setDisable(true);
                	setStyle("-fx-background-color: #f74040");
                }
            }
        });
        datePicker.setValue(LocalDate.now());
        treeView.setShowRoot(false);
        treeView.setOnMouseClicked(e->{
        	if(e.getClickCount()>=2) {
        		TreeShow show = treeView.getSelectionModel().getSelectedItem().getValue();
        		if(show.getId()!=null)
        			try {
						Main.client_unicast.write("GET_SHOW_FROM_ID "+show.getId());
						String ans=Main.client_unicast.readLine();
						if(ans.equals("NO_DATA"))
							return;
						String[] a=ans.split(" ");
						String[] sws=a[1].split("&");
						String[] params=sws[0].split("/");
						Main.switchToHome(new Show(
								Integer.parseInt(params[0]),
								ProtocolFormatter.decode(params[1]), 
								LocalDate.parse(ProtocolFormatter.decode(params[2]), Main.sqlDateF), 
								LocalTime.parse(ProtocolFormatter.decode(params[3]), Main.timeF), 
								Integer.parseInt(params[4]), 
								Integer.parseInt(params[5]), 
								params[6]));
					} catch (Exception e2) {
						// TODO: handle exception
					}
        	}
        });
	}
	
	public void refresh() {
		  try {
			  try {
				  //TODO richiesta di tutte le immagini
				  String imgName="Trevo.jpg";
				  Main.client_unicast.write("GET_IMAGE_FOR "+imgName);
				  String ans=Main.client_unicast.readLine();
				  String[] a=ans.split(" ");
				  BufferedImage img = ImageReceiever.receieve(Main.getServerIp(), Main.getServerPort()+1, Integer.parseInt(a[1]));
				  ImageIO.write(img, Main.getExtension(imgName), new File("wowo.jpg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			Main.client_unicast.write("GET_ALL_SHOWS");
			String ans=Main.client_unicast.readLine();
			treeView.setRoot(null);
			if(ans.equals("NO_DATA"))
				return;
			String[] a=ans.split(" ");
			String[] sws=a[1].split("&");
			List<TreeShow> shows=new ArrayList<>(sws.length);
			for (int i = 0; i < sws.length; i++) {
				String show=sws[i];
				String[] params=show.split("/");
				shows.add(new TreeShow(
						Integer.parseInt(params[0]),
						ProtocolFormatter.decode(params[1]), 
						LocalDate.parse(ProtocolFormatter.decode(params[2]), Main.sqlDateF), 
						LocalTime.parse(ProtocolFormatter.decode(params[3]), Main.timeF)));
			}
			
			TreeItem<TreeShow> root=new TreeItem<>(new TreeShow());
			root.setExpanded(true);
			treeView.setRoot(root);
			
			ObservableList<TreeItem<TreeShow>> titoli=FXCollections.observableArrayList();
			for (TreeShow show : shows) {
				int i=indexOf(show.getTitle(), titoli);
				if(i>=0)
					titoli.get(i).getChildren().add(generateItem(show));
				else {
					TreeItem<TreeShow> titolo=new TreeItem<>(new TreeShow(show.getTitle()));
					titolo.getChildren().add(generateItem(show));
					titoli.add(titolo);
				}
			}
			root.getChildren().addAll(titoli);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TreeItem<TreeShow> generateItem(TreeShow show){
		return new TreeItem<>(show);
	}
	
	private int indexOf(String title, ObservableList<TreeItem<TreeShow>> titols) {
		int index=0;
		for (TreeItem<TreeShow> i : titols) {
			if(i.getValue().getTitle().equalsIgnoreCase(title))
				return index;
			index++;
		}
		return -1;
	}
	
	@FXML
	void find() {
		LocalDate date = datePicker.getValue();
		if(date==null) return;
		try {
			Main.client_unicast.write("GET_SHOWS "+Main.sqlDateF.format(date));
			String ans=Main.client_unicast.readLine();
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