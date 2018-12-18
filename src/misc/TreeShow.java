package misc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import main.Main;

public class TreeShow{
	
	private static DateTimeFormatter timeF=DateTimeFormatter.ofPattern("HH:mm");
	private String title;
	private LocalDate date;
	private LocalTime time;
	private Integer id;
	
	public TreeShow() {title ="";}
	
	public TreeShow(String title) {
		this.title = title;
		id=null;
		date=null;
		time=null;
	}

	public TreeShow(int id, String title, LocalDate date, LocalTime time) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.time = time;
	}
	
	public String getTitle() {
		return title;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}
	
	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		if(getDate()!=null&&getTime()!=null)
			return Main.normalDateF.format(getDate())+" "+timeF.format(getTime());
		return title;
	}
}
