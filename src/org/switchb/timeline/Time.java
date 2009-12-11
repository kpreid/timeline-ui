package org.switchb.timeline;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.text.DateFormatter;

/** 
 * An immutable wall-clock-time value.
 * 
 * For this prototype we do not consider issues of non-monotonic time.
 */
public class Time implements Comparable<Time>, Serializable {
	private final Date date;


	public static Time msFromNow(int offset) {
		Date d = new Date();
		d.setTime(d.getTime() + offset);
		return new Time(d);
	}

	public static Time fromDate(Date date) {
		Date d = new Date(date.getTime());
		return new Time(d);
	}
	
	private Time(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return (Date)date.clone();
	}

	public int compareTo(Time other) {
		return getDate().compareTo(other.getDate());
	}

	public String toString() {
		return DateFormat.getTimeInstance().format(getDate());
	}

	/**
	 * Exists for the convenience of TimelinePanel to produce a half-open interval.
	 * @return
	 */
	public Time nextAdjacentTime() {
	    return plus(1);
    }

	public long minus(Time other) {
	    return getDate().getTime() - other.getDate().getTime();
    }

	public Time plus(long offsetMillis) {
	    return new Time(new Date(getDate().getTime() + offsetMillis));
    }
}
