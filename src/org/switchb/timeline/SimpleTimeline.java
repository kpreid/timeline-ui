package org.switchb.timeline;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JMenu;

/**
 * A generic timeline class which stores a finite collection of events (rather
 * than doing anything interesting in the way of lazy calculation).
 */
public class SimpleTimeline extends BaseTimeline implements Serializable {
	
	TreeMap<Time, List<Event>> eventsByTime = new TreeMap<Time, List<Event>>();
	
	private Color color = Color.WHITE;

	@Override
    public List<Event> eventsInInterval(Time earliest, Time latest) {
		List<Event> events = new ArrayList<Event>();
		for (List<Event> simultaneous : eventsByTime.subMap(earliest, true, latest, true).values()) {
			events.addAll(simultaneous);
		}
	    return events;
    }

	public void add(Event event) {
		Time time = event.getTime();
		if (!eventsByTime.containsKey(time))
			eventsByTime.put(time, new ArrayList<Event>());
		eventsByTime.get(time).add(event);
		notifyListeners(time, time);
    }
	
	/**
	 * Remove the specified event from the collection of events which will be returned by eventsInInterval.
	 */
	public void remove(Event event) {
		final Time time = event.getTime();
		if (eventsByTime.containsKey(time))
			eventsByTime.get(time).remove(event);
	}

	/**
	 * Update the event index in the case that the event's time changed.
	 * 
	 * XXX review: should this be the *same method* as notifyEventTimeChange?
	 * 
	 * @param oldTime
	 *            The time at which the event *was* located. event.getTime()
	 *            should be the new time.
	 */
	public void timeChanged(Event event, Time oldTime) {
		if (!(    eventsByTime.containsKey(oldTime)
		       && eventsByTime.get(oldTime).contains(event) )) {
			/*
			 * Assuming the event is not actually in the timeline. XXX it would
			 * be more robust if we had events indexed by identity and we could
			 * check that -- as it is this will screw up if we are given a bad
			 * old-time.
			 */
			return;
		}
		
		final Time newTime = event.getTime();
		eventsByTime.get(oldTime).remove(event);
		if (!eventsByTime.containsKey(newTime))
			eventsByTime.put(newTime, new ArrayList<Event>());
		eventsByTime.get(newTime).add(event);
		notifyEventTimeChange(oldTime, newTime);
	}
	
	public JMenu makeMenu() {
		return null;
	}
	
	public Color getColor() {
		return color; 
	}
	
	public void setColor(Color color) {
	    this.color = color;
    }
}
