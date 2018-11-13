package misc;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Cell extends StackPane {
	
	private Rectangle r;
	private int row, col, number;
	private double width, height;
	private boolean highlighted;
	
	public Cell(int row, int col, double width, double height) {
		this.row = row;
		this.col = col;
		this.width = width;
		this.height = height;
		number=row*10+col;
		
		r=new Rectangle(this.width, this.height);
		r.setStroke(Color.BLACK);
		r.setFill(null);
		
		setHighlighted(false);
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		getChildren().clear();
		if(!highlighted) 
			getChildren().addAll(r, new Text(number+""));
		else 
			getChildren().addAll(r, new Circle(width/2, height/2, width/2-2, Color.RED), new Text(number+""));
	}
	
	public boolean isHighlighted() {
		return highlighted;
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
