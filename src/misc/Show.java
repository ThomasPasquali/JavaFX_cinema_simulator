package misc;

import java.time.LocalDate;
import java.time.LocalTime;

import main.Main;

public class Show {
	
	private String title;
	private LocalDate date;
	private LocalTime time;
	private int free, tot, hall;
	
	public Show(String title, LocalDate date, LocalTime time, int free, int tot, int hall) {
		this.title = title;
		this.date = date;
		this.time = time;
		this.free = free;
		this.tot = tot;
		this.hall = hall;
	}
	
	@Override
	public String toString() {
		return title+" Time: "+Main.timeF.format(time)+" Hall: "+hall+" Free seats: "+free+"/"+tot;
	}
}
