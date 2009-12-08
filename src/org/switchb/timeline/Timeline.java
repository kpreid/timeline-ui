package org.switchb.timeline;

import java.awt.Color;
import java.util.List;

import javax.swing.JMenu;

public interface Timeline {
	/**
	 * Return the events currently in the timeline within the specified
	 * interval.
	 * 
	 * @param earliest
	 *            The earliest time for which an event may be included in the
	 *            list.
	 * @param latest
	 *            The latest time for which an event may be included in the
	 *            list.
	 * @return
	 */
	List<Event> eventsInInterval(Time earliest, Time latest);

	/**
	 * Add a listener to be notified whenever the results of eventsInInterval
	 * may have changed.
	 */
	void addListener(TimelineListener listener);

	/**
	 * Returns either null, if this timeline has no menu UI, or a fresh JMenu
	 * containing menu items for interacting with this timeline.
	 */
	JMenu makeMenu();

	/**
	 * A color to use to help distinguish this timeline.
	 */
	Color getColor();
}
