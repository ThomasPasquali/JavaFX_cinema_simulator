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

import misc.Client;
import misc.SHAEncryptor;

public class Login{

	private Stage s;
	private String username, ip;
	private int port;
	private int id;
	private Client client;
	
	public Login(String username, String ip, int port) {
		this.username=username;
		this.ip=ip;
		this.port=port;
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
        	fieldIP.setText(ip);
        	fieldPort.setText(port+"");
        	fieldUsername.setText(username);
        }
        
        @FXML
        void accedi() throws IOException {
    		if(!fieldIP.getText().matches("(\\d{1,3}.){3}\\d{1,3}")) {
    			Main.showErrorAlert("IP non corretto", ""); return;
    		}
        	try {
				client=new Client(fieldIP.getText(), Integer.parseInt(fieldPort.getText()));
				client.write(
						"LOGIN "+
						fieldUsername.getText()+" "+
						SHAEncryptor.get_SHA_1_SecurePassword(fieldPw.getText(), Main.salt));
				String answ=client.readLine();
				if(answ.startsWith("OK")) {
					username=fieldUsername.getText();
					id=Integer.parseInt(answ.split(" ")[1]);
					ip=fieldIP.getText();
					port=Integer.parseInt(fieldPort.getText());
					s.close();
					Main.startup();       
				}else {
					client.close();
					Main.showErrorAlert("Credenziali non valide", "");
				}
			} catch (IOException e) {
				//e.printStackTrace();
				if(client!=null) client.close();
				Main.showErrorAlert("Connessione rifiutata", "Controllare IP e PORTA");
			}
        }

        @FXML
        void registrati() {
        	//TODO
        	System.out.println(SHAEncryptor.get_SHA_1_SecurePassword(fieldPw.getText(), Main.salt));
        }
    }

	public String getUsername() {
		return username;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}
    
}
