package misc;

public class ProtocolFormatter {

	private static String[] codes= {"%20", "%21", "%22", "%23"};
	private static char[] chars= {' ', '&', '/', '%'};
	
	public static String encode(String s) {
		for (int i = chars.length-1; i >= 0 ; i--) 
			s=s.replaceAll(chars[i]+"", codes[i]);
		return s;
	}
	
	public static String decode(String s) {
		for (int i = 0; i < chars.length; i++)
			s=s.replaceAll(codes[i], chars[i]+"");
		return s;
	}
}
