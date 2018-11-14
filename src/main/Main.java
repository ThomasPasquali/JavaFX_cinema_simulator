package main;

import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import misc.Account;
import misc.Client;
import scenes.Home;
import scenes.Selection;
import windows.Login;

public class Main extends Application{
	
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static ClassLoader cl=null;
	
	private static Login login;
	
	private static Stage stage;
	
	private static Scene scene_home;
	private static Scene scene_selection;
	
	private static Home home;
	private static Selection selection;
	
	public static Client client;
	public static Account account;
	
	@Override
	public void start(Stage s) throws Exception {
		stage=s;
		cl=getClass().getClassLoader();
		login=new Login();
	}
	
	public static void startup() {
		account=login.getAccount();
		client=login.getClient();
		
		home=new Home();
		scene_home=new Scene(home);
		scene_home.getStylesheets().add(cl.getResource("biggerFont.css").toExternalForm());
		
		selection=new Selection();
		scene_selection=new Scene(selection);
		scene_selection.getStylesheets().add(cl.getResource("biggerFont.css").toExternalForm());
		
		stage.setTitle("Prenotazioni teatro");
		stage.setScene(scene_selection);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setOnCloseRequest(e->{
			try {
				client.write("QUIT");
				client.close();
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		stage.show();
	}
	
	public static void showErrorAlert(String header, String content) {
		Alert a=new Alert(AlertType.ERROR);
		a.setTitle("Error");
		a.setHeaderText(header);
		a.setContentText(content);
		a.showAndWait();
	}
	
	public static void showWarningAlert(String header, String content) {
		Alert a=new Alert(AlertType.WARNING);
		a.setTitle("Warning");
		a.setHeaderText(header);
		a.setContentText(content);
		a.showAndWait();
	}
	
	public static void showInfoAlert(String header, String content) {
		Alert a=new Alert(AlertType.INFORMATION);
		a.setTitle("Success");
		a.setHeaderText(header);
		a.setContentText(content);
		a.showAndWait();
	}
	
	public static void main(String[] args) {
		launch();
	}
}
