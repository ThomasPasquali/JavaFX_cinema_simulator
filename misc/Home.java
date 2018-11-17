package scenes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import clientMain.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ObservableValue;
import javafx.event.Event; 
import javafx.scene.input.MouseButton; 
import javafx.scene.input.MouseEvent;
import misc.Cell;
import server.ServerTombola;

public class Home extends BorderPane {

	private static final int MAX_NUMS=5;
	private List<Cell> cells;
	private int numbersLeft=MAX_NUMS;
	private float bid;
	private boolean validBid=false;
	
	public Home() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("home.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
		} catch (IOException exc) {
            exc.printStackTrace();
        }
	}
	
	private static List<Cell> getHighlightedCells(List<Cell> l){
		List<Cell> list=new ArrayList<>();
		for (Cell cell : l)
			if(cell.isHighlighted())
				list.add(cell);
		return list;
	}
	
	@FXML
	private Button btnGioca;

    @FXML
    private Label lblSoldi;
    
    @FXML
    private Label lblNumbersLeft;

    @FXML
    private GridPane grid;
    
    @FXML
    private TextField fieldBid;

    @FXML
    void initialize() {
    	lblSoldi.setText("Soldi: "+Main.account.getSchei());
    	lblNumbersLeft.setText("Numeri rimasti: "+numbersLeft);
    	int rows=9, cols=10;
    	double cellWidth=grid.getPrefWidth()/cols, cellHeight=grid.getPrefHeight()/rows;
    	cells=new ArrayList<>(rows*cols);
    	for (int i = 0; i < rows; i++) 	
			for (int j = 1; j <= cols; j++) {
				Cell c=new Cell(i, j, cellWidth, cellHeight);
				c.setOnMouseClicked(e->{
					
					if(c.isHighlighted())
						numbersLeft++;
					else
						numbersLeft--;
					
					if(numbersLeft>MAX_NUMS) {
						numbersLeft=MAX_NUMS;
						e.consume();
						return;
					}
					if(numbersLeft<0) {
						numbersLeft=0;
						e.consume();
						return;
					}
					btnGioca.setDisable(numbersLeft!=0||!validBid);

					lblNumbersLeft.setText("Numeri rimasti: "+numbersLeft);
					c.setHighlighted(!c.isHighlighted());		
				});
				cells.add(c);
				grid.add(c, j, i);
			}
    	fieldBid.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String newValue)->{
			/*String s=newValue.replaceAll("^[,.]|[^\\d,.]", "");
			s=s.replaceAll("[,.]+", "\\.");
    		fieldBid.setText(s.replaceAll("(\\d*)(\\.)(\\d{0,2})", "$1\\.$3"));*/
    		String s;
			s=newValue.replaceAll("^[,.]|[^\\d.,]", "");
			s=s.replaceAll("[,.]{2,}?", ".");
			s=s.replaceAll("(\\d+)[.,]+(\\d+)(.*)","$1.$2");
			s=s.replaceAll("(.*)(-\\.)(.*)", "$1-$3");
			String[] digits=s.split("\\.");
			if(digits.length>1)
				s=digits[0]+"."+(digits[1].length()>2?digits[1].substring(0, 2):digits[1]);
			fieldBid.setText(s);
    		try {
				bid=Float.parseFloat(s);
				validBid=true;
				btnGioca.setDisable(numbersLeft!=0||!validBid);
			} catch (Exception e2) {
				validBid=false;
			}
		});
    	fieldBid.setOnAction(e->{if(!btnGioca.isDisabled())gioca();});
    }
    
    @FXML
    void gioca() {
    	if(bid>Main.account.getSchei()) {
    		Main.showErrorAlert("Non hai abbastanza scheiii!", "");
    		return;
    	}
    	if(bid==0) {
    		Main.showErrorAlert("Devi puntare qualcosa!", "");
    		return;
    	}
    	List<Cell> highlightedCells=Home.getHighlightedCells(cells);
    	StringJoiner request = new StringJoiner(" ");
    	int[] numeriProvati=new int[MAX_NUMS];
    	
    	request.add("PLAY");
    	int index=0;
    	for (Cell cell : highlightedCells) {
    		request.add(cell.getNumber()+"");
    		numeriProvati[index]=cell.getNumber();
    		index++;
    	}
    		
    	request.add(bid+"");
    	try {
    		Main.client.write(request.toString());
    		String[] answer=Main.client.readLine().split(" ");
    		float vincita=Float.parseFloat(answer[answer.length-1]);
    		Main.account.setSchei(Main.account.getSchei()+vincita);
    		lblSoldi.setText(Main.account.getSchei()+"");
    		
    		StringJoiner sjNumeriEstratti=new StringJoiner(", ");
    		StringJoiner sjNumeriCorretti=new StringJoiner(", ");
    		int[] numeriEstratti=new int[ServerTombola.NUM_ESTR];
    		
    		for (int i = 0; i < answer.length-1; i++) {
    			sjNumeriEstratti.add(answer[i]+"");
    			numeriEstratti[i]=Integer.parseInt(answer[i]);
    		}
    			
    		for (int i = 0; i < numeriProvati.length; i++) 
				for (int j = 0; j < numeriEstratti.length; j++) 
					if(numeriProvati[i]==numeriEstratti[j])
						sjNumeriCorretti.add(numeriProvati[i]+"");
    		
    		Main.showInfoAlert((vincita<0?"HAI PERSO!":"HAI VINTO!")+"\r\nNumeri estratti: "+sjNumeriEstratti.toString()
    				+ (vincita>=0?"\r\nNumeri corretti: "+sjNumeriCorretti.toString():""),
    				(vincita>=0?"Hai vinto: ":"Hai perso: ")+Math.abs(vincita));
    		
    		for (Cell cell : cells)
    			if(cell.isHighlighted())
	    			Event.fireEvent(cell, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
	    	                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
	    	                true, true, true, true, true, true, null));
    	} catch (IOException e) {Main.showErrorAlert("Si è verificato un errore!", e.toString());}
    	
    }

}
