package misc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Account {
	
	private String username, password;
	private Float schei;
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
		schei=10f;
	}
	
	public Account(String line) {
		String[] s=line.split("[;\\s]");
		username=s[0];
		password=s[1];
		schei=Float.parseFloat(s[2]);
	}
	
	public Float getSchei() {
		return schei;
	}

	public void setSchei(Float schei) {
		this.schei = schei;
	}

	public String getUsername() {
		return username;
	}

	public boolean matchPassword(String pw) {
		return password.equals(pw);
	}
	
	public String getCSVformat() {
		return username+";"+password+";"+schei;
	}
	
	public static List<Account> loadAccounts(File f) {
		List<Account> lista=new ArrayList<>();
		try{
			List <String> linee = Files.readAllLines(Paths.get(f.getAbsolutePath()), StandardCharsets.UTF_8);
			for(String linea : linee)
				lista.add(new Account(linea));
		}catch(IOException e){
			e.printStackTrace();
			lista=null;
		}
		return lista;
	}
	
	public static boolean storeAccounts(File f, List<Account> accounts){
		List<String> lines= new ArrayList<String>(accounts.size());
		for (Account account : accounts) 
			lines.add(account.getCSVformat());
			
		try {
			Files.write(Paths.get(f.getAbsolutePath()), lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
