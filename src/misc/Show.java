package misc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Show {
	
	private static DateTimeFormatter timeF=DateTimeFormatter.ofPattern("HH:mm");
	private String title, hall;
	private LocalDate date;
	private LocalTime time;
	private int free, tot, id;
	
	public Show(int id, String title, LocalDate date, LocalTime time, int free, int tot, String hall) {
		this.id=id;
		this.title = title;
		this.date = date;
		this.time = time;
		this.free = free;
		this.tot = tot;
		this.hall = hall;
	}

	public String getTitle() {
		return title;
	}

	public String getHall() {
		return hall;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	public int getFree() {
		return free;
	}

	public int getTot() {
		return tot;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Titolo: " + title + "\r\nSala: " + hall + "\r\nData: " + date + " " + timeF.format(time) + "\r\nPosti:" + free+ "/" + tot;
	}
}
