package misc;

import java.net.URL;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;

public class Cell extends StackPane {
	
	private static HashMap<String, URL> map_imgs=loadImgs();
	private int row, col, number;
	private AnchorPane p;
	private String color;
	
	public Cell(int row, int col, int number, String color) {
		this.row = row;
		this.col = col;
		this.number=number;
		this.color=color;
		
		Image img=new Image(map_imgs.get(color).toString());
		setPrefSize(img.getWidth(), img.getHeight());
		
		p=new AnchorPane();
		p.setStyle("-fx-background-image: url('"+map_imgs.get(color).toString()+"')");
		
		Text t=new Text(number+"");
		t.setStroke(Paint.valueOf("White"));
		t.setFont(Font.font(30));
		t.setFill(Paint.valueOf("White"));
		
		getChildren().addAll(p, t);
	}
	
	public void setColor(String color) {
		p.setStyle("-fx-background-image: url('"+map_imgs.get(color).toString()+"')");
		this.color=color;
	}
	
	private static HashMap<String, URL> loadImgs() {
		HashMap<String, URL> map=new HashMap<>();
		map.put("gray", Main.cl.getResource("gray.png"));
		map.put("green", Main.cl.getResource("green.png"));
		map.put("red", Main.cl.getResource("red.png"));
		return map;
	}
	
	public String getColor() {
		return color;
	}

	public int getNumber() {
		return number;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}

}