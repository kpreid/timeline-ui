package org.switchb.timeline;

import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * Implements the listener-notification part of the Timeline interface.
 */
public abstract class BaseTimeline implements Timeline {
	Set<TimelineListener> listeners = new java.util.HashSet<TimelineListener>();

	public void addListener(TimelineListener listener) {
		listeners.add(listener);
	}

	/**
	 * Notify all listeners.
	 */
	protected void notifyListeners(final Time earliest, final Time latest) {
		for (final TimelineListener l : listeners) {
			// protect against exceptions and plan interference
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					boolean interested = l.timelineChanged(earliest, latest);
					if (!interested) {
						listeners.remove(l);
					}
				}
			});
		}
	}

	/**
	 * Convenience method for notifying of an event having changed its time.
	 */
	protected void notifyEventTimeChange(Time oldTime, Time newTime) {
		boolean forward = oldTime.compareTo(newTime) < 0;
		notifyListeners(forward ? oldTime : newTime, forward ? newTime
		        : oldTime);
	}

}
