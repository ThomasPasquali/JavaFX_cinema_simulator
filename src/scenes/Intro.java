package scenes;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.Main;

public class Intro extends VBox{
	
	public Intro() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("intro.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @FXML
    private ImageView imgBook;

    @FXML
    private ImageView imgDel;

    @FXML
    void initialize() {
    	imgBook.setImage(new Image(getClass().getClassLoader().getResourceAsStream("hall.jpg")));
    	imgDel.setImage(new Image(getClass().getClassLoader().getResourceAsStream("del.png")));
    }
    
    @FXML
    void book() {
    	Main.switchToSelection();
    }

    @FXML
    void delete() {
    	//TODO
    }
}
