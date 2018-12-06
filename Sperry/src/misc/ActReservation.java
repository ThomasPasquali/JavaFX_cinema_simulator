package misc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ActReservation {
	
	private static DateTimeFormatter timeF=DateTimeFormatter.ofPattern("HH:mm");
	private String title, hall;
	private LocalDate date;
	private LocalTime time;
	private int resSeats;
	
	public ActReservation(int resSeats, String title, LocalDate date, LocalTime time, String hall) {
		this.resSeats=resSeats;
		this.title = title;
		this.date = date;
		this.time = time;
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
	
	public int getResSeats() {
		return resSeats;
	}

	@Override
	public String toString() {
		return "Titolo: " + title + "\r\nSala: " + hall + "\r\nData: " + date + " " + timeF.format(time) + "\r\nData: " + resSeats;
	}
}