package scenes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import main.Main;
import misc.Cell;
import misc.Show;

public class Home extends BorderPane{
	
	private boolean active;
	private List<Cell> cells;
	private static final int SEATS_PER_ROW=10;
	private Show show;
	
	public Home() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("home.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private GridPane grid;
	
	@FXML
    private Label lblTimestamp;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblHall;
	
	@FXML
    void book() throws IOException {
		StringJoiner sj=new StringJoiner("/");
		for (Cell cell : cells) 
			if(cell.getColor().equals("green")) {
				cell.setColor("red");
				sj.add(cell.getNumber()+"");
			}
		if(sj.toString().isEmpty()) 
			Main.showErrorAlert("Selezionare almeno un posto", "");
		else {
			Main.client_unicast.write("BOOK "+show.getId()+" "+sj.toString());
			String ans=Main.client_unicast.readLine();
			if(ans.equals("OK")) { Main.showInfoAlert("Prenotato con successo", ""); del();}
			else						  Main.showErrorAlert("Errore durante la prenotazione", ans.split(" ")[1]);
		}
    }

    @FXML
    void del() {
    	Main.switchToSelection();
    }

    private void initGrid(int tot) {
    	cells=new ArrayList<>(tot);
    	grid.getChildren().clear();
    	for (int i = 0, numero=1; numero <= tot ; i++) 	
			for (int j = 0; j < SEATS_PER_ROW && numero <= tot; j++) {
				Cell c=new Cell(i, j, numero, "gray");
				c.setOnMouseClicked(e->{
					if(c.getColor().equals("yellow")) Main.showWarningAlert("La poltrona è gia selezionata da un altro utente", "Selezionane un'altra");
					else if(c.getColor().equals("red")) Main.showErrorAlert("La poltrona è gia prenotata", "Selezionane un'altra");
					else{
						try {
							if(c.getColor().equals("gray")) {
								Main.client_unicast.write("RESERVE "+show.getId()+" "+c.getNumber());
								c.setColor("green");
							}else {
								Main.client_unicast.write("FREE "+show.getId()+" "+c.getNumber());
								c.setColor("gray");
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				cells.add(c);
				grid.add(c, j, i);
				numero++;
			}
    	
    	for(ColumnConstraints col : grid.getColumnConstraints()) {
			col.setHgrow(Priority.NEVER);
			col.setHalignment(HPos.CENTER);
			col.setPrefWidth(-1);
		}
    	
		for(RowConstraints row : grid.getRowConstraints()) {
			row.setVgrow(Priority.NEVER);
			row.setValignment(VPos.CENTER);
			row.setPrefHeight(-1);
		}
	}
    
	private void setOccupied(int[] occ) {
		for (int number : occ)
			getCell(number).setColor("red");
	}
	
	public Cell getCell(int number) {
		for (Cell cell : cells) 
			if(cell.getNumber()==number)
				return cell;
		return null;
	}

	public void setShow(Show show) throws IOException {
		this.show=show;
		initGrid(show.getTot());
		lblTitle.setText(show.getTitle());
		lblHall.setText(show.getHall());
		lblTimestamp.setText(Main.normalDateF.format(show.getDate())+"\r\n"+show.getFormattedTime());
		if(show.getFree()<show.getTot()) {
			Main.client_unicast.write("GET_OCCUPIED_SEATS_FOR "+show.getId());
			String[] s=Main.client_unicast.readLine().split(" ")[1].split("/");
			int[] occupiedSeats=new int[s.length];
			for (int i = 0; i < occupiedSeats.length; i++) 
				occupiedSeats[i]=Integer.parseInt(s[i]);
			setOccupied(occupiedSeats);
		}
	}
	
	/**
	 * Sets the color to red
	 * @param seat
	 */
	public void blockSeat(int seat) {
		getCell(seat).setColor("red");
	}
	
	/**
	 * Sets the color to yellow
	 * @param seat
	 */
	public void reserveSeat(int seat) {
		getCell(seat).setColor("yellow");
	}
	
	/**
	 * Sets the color to green
	 * @param seat
	 */
	public void freeSeat(int seat) {
		getCell(seat).setColor("gray");
	}
	
	public Show getShow() {
		return show;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
