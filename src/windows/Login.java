package windows;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import misc.Account;
import misc.Client;

public class Login{

	private Stage s;
	private Account account;
	private Client client;
	
	public Login() {
		s=new Stage();
		s.setTitle("Login");
		Scene scene=new Scene(new Root());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("biggerFont.css").toExternalForm());
		s.setScene(scene);
		s.setResizable(false);
		s.show();
	}
	
    private class Root extends VBox{
    	public Root() {
    		try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
                loader.setController(this);
                loader.setRoot(this);
                loader.load();
    		} catch (IOException exc) {
                exc.printStackTrace();
            }
		}
    	@FXML
        private TextField fieldIP;

        @FXML
        private TextField fieldPort;

        @FXML
        private TextField fieldUsername;

        @FXML
        private PasswordField fieldPw;

        @FXML
        void initialize() {
        	fieldPort.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String newValue)->{
        		String s=newValue.replaceAll("\\D", "");
        		fieldPort.setText(s.substring(0, s.length()>5?5:s.length()));
			});
        	fieldIP.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String newValue)->{
        		fieldIP.setText(newValue.replaceAll("[^\\d.]", ""));
        	});
        }
        
        @FXML
        void accedi() {
        	//TODO
        	try {
        		if(!fieldIP.getText().matches("(\\d{1,3}.){3}\\d{1,3}")) {Main.showErrorAlert("IP non corretto", ""); return;}
				client=new Client(fieldIP.getText(), Integer.parseInt(fieldPort.getText()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	s.close();
        	Main.startup();       	
        }

        @FXML
        void registrati() {
        	//TODO
        }
    }

	public Account getAccount() {
		return account;
	}

	public Client getClient() {
		return client;
	}
    
}
