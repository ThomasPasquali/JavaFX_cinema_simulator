package misc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import main.Main;

public class ActReservation {
	
	private static DateTimeFormatter timeF=DateTimeFormatter.ofPattern("HH:mm");
	private String title, hall;
	private LocalDate date;
	private LocalTime time;
	private int seat, id;
	
	public ActReservation(int id, int seat, String title, String hall, LocalDate date, LocalTime time) {
		this.id = id;
		this.seat = seat;
		this.title = title;
		this.hall = hall;
		this.date = date;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public String getHall() {
		return hall;
	}

	public int getId() {
		return id;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}
	
	public int getSeat() {
		return seat;
	}

	@Override
	public String toString() {
		return "ID: " + id + "\r\nTitolo: " + title + "\r\nSala: " + hall + "\r\nData: " + Main.normalDateF.format(date) + " " + timeF.format(time) + "\r\nSedia: " + seat;
	}
}