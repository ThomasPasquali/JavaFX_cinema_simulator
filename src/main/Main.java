package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;

import org.ini4j.Ini;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import misc.Client;
import misc.Show;

import scenes.Home;
import scenes.Intro;
import scenes.Selection;

import windows.Login;

public class Main extends Application{
	
	public static DateTimeFormatter sqlDateF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter normalDateF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static DateTimeFormatter timeF = DateTimeFormatter.ofPattern("HH:mm:ss");

	public static ClassLoader cl=null;
	
	private static Login login;
	
	private static Stage stage;
	
	private static Scene scene_home;
	private static Scene scene_selection;
	private static Scene scene_intro;
	
	private static Home home;
	private static Selection selection;
	private static Intro intro;
	
	public static Client client;
	public static String username;
	public static int id;
	
	private static String ip;
	private static int port;
	
	public static byte[] salt;
	
	public static final double MAX_HEIGHT=700;
	
	@Override
	public void start(Stage s) throws Exception {
		stage=s;
		
		cl=getClass().getClassLoader();
		
		salt=loadSalt();
		
		loadIniFile();
		login=new Login(username, ip, port);
	}
	
	public static void startup() throws IOException {
		username=login.getUsername();
		id=login.getId();
		client=login.getClient();
		ip=login.getIp();
		port=login.getPort();
		saveIniFile();
		
		home=new Home();
		scene_home=new Scene(home);
		scene_home.getStylesheets().addAll(
				cl.getResource("biggerFont.css").toExternalForm(),
				cl.getResource("home.css").toExternalForm());
		
		selection=new Selection();
		scene_selection=new Scene(selection);
		scene_selection.getStylesheets().addAll(
				cl.getResource("biggerFont.css").toExternalForm(),
				cl.getResource("selection.css").toExternalForm());
		
		intro=new Intro();
		scene_intro=new Scene(intro);
		scene_intro.getStylesheets().add(cl.getResource("intro.css").toExternalForm());
		
		stage.setTitle("Prenotazioni teatro");
		stage.setScene(scene_intro);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setMaxHeight(MAX_HEIGHT);
		stage.sizeToScene();
		stage.setOnCloseRequest(e->{
			try {
				client.write("QUIT "+id);
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
	
	private static byte[] loadSalt() throws IOException {
		InputStream in = cl.getResourceAsStream("salt.txt"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String[] bytesS=reader.readLine().split(" ");
		byte[] bytes=new byte[16];
		for (int i = 0; i < bytes.length; i++) 
			bytes[i]=Byte.parseByte(bytesS[i]);
		return bytes;
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
	
	public static void switchToHome(Show show) throws IOException {
		home.setShow(show);
		stage.setScene(scene_home);
		stage.sizeToScene();
	}

	public static void switchToSelection() {
		stage.setScene(scene_selection);
		stage.sizeToScene();
	}
	
	public static void switchToIntro() {
		stage.setScene(scene_intro);
		stage.sizeToScene();
	}
	
	private void loadIniFile() {
		Ini i=null;
		try {
			i=new Ini(new FileReader("Cinema.ini"));
		} catch (IOException e) {
			File f=new File("Cinema.ini");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				saveDefaultIniFile();
				loadIniFile();
			}
			e.printStackTrace();
		}			
		ip=i.get("dbInfo").get("ip");
		try {
			port=Integer.parseInt(i.get("dbInfo").get("port"));
		} catch (NumberFormatException e) {
			port=3306;
		}
		username=i.get("dbInfo").get("username");
	}
	
	private static void saveIniFile(){
		try {
			Ini i=new Ini(new FileReader("Cinema.ini"));
			i.put("dbInfo", "ip", ip);
			i.put("dbInfo", "port", port);
			i.put("dbInfo", "username", username);
			i.store(new FileWriter("Cinema.ini"));
		} catch (IOException e) {
			System.out.println("error writing ini file");
			e.printStackTrace();
		}
	}
	
	private void saveDefaultIniFile() {
		try {
			Ini i=new Ini(new FileReader("Cinema.ini"));
			i.put("dbInfo", "ip", "127.0.0.1");
			i.put("dbInfo", "port", "1234");
			i.put("dbInfo", "username", "");
			i.store(new FileWriter("Cinema.ini"));
		} catch (IOException e) {
			System.out.println("error writing def ini file");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch();
	}
}