package scenes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import misc.Cell;

public class Home extends BorderPane{
	
	private List<Cell> cells;
	
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
	void initialize() {
		int rows=10, cols=10;
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
			
    	cells=new ArrayList<>(rows*cols);
    	for (int i = 0, numero=1; i < rows; i++) 	
			for (int j = 0; j < cols; j++) {
				Cell c=new Cell(i, j, numero, "gray");
				c.setOnMouseClicked(e->{
					System.out.println(c.getNumber());
					c.setColor(c.getColor().equals("green")?"red":"green");
				});
				cells.add(c);
				grid.add(c, j, i);
				numero++;
			}
		
	}
	
	@FXML
    void book() {

    }

    @FXML
    void del() {

    }
	
}
