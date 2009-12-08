package org.switchb.timeline;

import java.util.HashMap;

public interface TimelineListener {
	/**
	 * Notify the listener that one or more events in the interval [earliest, latest] have changed.
	 * @param earliest
	 * @param latest
	 * @return Whether this listener is interested in further notifications. 
	 */
	boolean timelineChanged(Time earliest, Time latest);
}
