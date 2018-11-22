package misc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import main.Main;

public class Show {
	
	private static DateTimeFormatter timeF=DateTimeFormatter.ofPattern("HH:mm");
	private String title, hall;
	private LocalDate date;
	private LocalTime time;
	private int free, tot;
	
	public Show(String title, LocalDate date, LocalTime time, int free, int tot, String hall) {
		this.title = title;
		this.date = date;
		this.time = time;
		this.free = free;
		this.tot = tot;
		this.hall = hall;
	}
	
	@Override
	public String toString() {
		return title+" Time: "+timeF.format(time)+" Hall: "+hall+" Free seats: "+free+"/"+tot;
	}
}
