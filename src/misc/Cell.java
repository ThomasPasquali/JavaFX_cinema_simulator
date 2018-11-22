package misc;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Cell extends Pane {
	
	private Rectangle r;
	private int row, col, number;
	private double width, height;
	
	public Cell(int row, int col, double width, double height, int number, String imgPath) {
		super(new Image(imgPath));
		this.row = row;
		this.col = col;
		this.width = width;
		this.height = height;
		this.number=number;
	}
	
	
	public int getNumber() {
		return number;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
