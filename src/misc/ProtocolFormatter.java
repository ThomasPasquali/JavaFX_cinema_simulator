package misc;

import java.util.HashMap;

public class ProtocolFormatter {

	private static String[] codes= {"%20", "%21", "%22", "%23"};
	private static char[] chars= {' ', '&', '/', '%'};
	private static HashMap<Character, String> mapChars=initMapChars();
	private static HashMap<String, Character> mapCodes=initMapCodes();

	private static HashMap<Character, String> initMapChars() {
		HashMap<Character, String> map=new HashMap<>();
		for (int i = 0; i < chars.length; i++)
			map.put(chars[i], codes[i]);
		return map;
	}
	
	private static HashMap<String, Character> initMapCodes() {
		HashMap<String, Character> map=new HashMap<>();
		for (int i = 0; i < chars.length; i++)
			map.put(codes[i], chars[i]);
		return map;
	}
	
	public static String encode(String s) {
		for (Character c : mapChars.keySet()) 
			s=s.replaceAll(c+"", mapChars.get(c));
		return s;
	}
	
	public static String decode(String s) {
		for (String code : mapCodes.keySet()) 
			s=s.replaceAll(code, mapCodes.get(code)+"");
		return s;
	}
}
