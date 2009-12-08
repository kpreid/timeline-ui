package org.switchb.timeline;

import java.awt.*;
import javax.swing.*;

import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class TimelinePanel extends JPanel {
	
	/**
	 * All timelines (data sources) for this timeline panel. 
	 */
	Set<Timeline> timelines = new java.util.HashSet<Timeline>();
	
	/**
	 *  Listing of all events *actually in the display* by time
	 */
	NavigableMap<Time, List<EventRecord>> timeIndex = new TreeMap<Time,List<EventRecord>>();
	
	/**
	 *  Listing of the times which events are recorded as being at in the timeIndex (which may be out of date).
	 */
	Map<Event, EventRecord> eventRecords = new HashMap<Event, EventRecord>();

	// UI components
	JScrollPane scrollC;
	JPanel contentC;
	JPanel fillerC; // filler for bottom end of grid bag to let it stretch

	/**
	 * The GridBagLayout gridy coordinate to use for an event added at the bottom.
	 */
	private int nextRowIndex = 0;
	
	/**
	 * The time which the middle of the view corresponded to when saveScroll()
	 * was last called. If null, either saveScroll() has not been done or the
	 * position was not convertible to a time.
	 */
	private Time savedTime = null;

	/**
	 * The offset from the top of the component for the savedTime. XXX improve
	 * this explanation.
	 * 
	 * If -1, then savedTime and savedOffset are invalid.
	 */
	private int savedOffset = -1;
	
	/**
	 * The current time when the scroll position was saved.
	 */
	private Time savedNow = null;

	private GridBagLayout layout = new GridBagLayout();

	private Map<Integer, Time> rowToTimeIndex = new HashMap<Integer, Time>();
	
	public TimelinePanel() {
		
		setFocusable(true);
		
		contentC = new JPanel();
		contentC.setLayout(layout);

		scrollC = new JScrollPane(contentC);
		scrollC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		setLayout(new BorderLayout());
		add(scrollC, BorderLayout.CENTER);
		
		fillerC = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.weightx = 1e50;
		c.weighty = 1e50;
		contentC.add(fillerC, c);
	}
	
	/**
	 * Add a timeline which this panel will start displaying.
	 */
	public void addTimeline(Timeline t) {
		timelines.add(t);
		
		t.addListener(new TPListener(t));
		
		for (Event event : t.eventsInInterval(Time.msFromNow(-100000000), Time.msFromNow(1000000000))) { // XXX stub for testing
			addEvent(t, event);
		}
	}
	
	private void addEvent(Timeline timeline, Event event) {
		EventRecord record = new EventRecord(timeline, event, event.makeComponent());
		Time time = event.getTime();
		if (!timeIndex.containsKey(time))
			timeIndex.put(time, new ArrayList<EventRecord>());
		timeIndex.get(time).add(record);
		eventRecords.put(event, record);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);

		// add timestamp
		c.gridx = 0;
		c.gridy = nextRowIndex;
		c.anchor = GridBagConstraints.EAST;
		contentC.add(record.getLabel(), c);
		
		// add custom component
		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		contentC.add(record.getComponent(), c);
		
		nextRowIndex++;
		
		renumber();		
	}
	
	/**
	 * Reassign gridys for all events.
	 */
	private void renumber() {
		// XXX this is O(n).
		
		int gridy = 0;
		rowToTimeIndex.clear();

		for (List<EventRecord> records : timeIndex.values()) {
			boolean first = true;
			for (EventRecord record : records) {
				for (Component component : (new Component[] {record.getComponent(), record.getLabel()})) {
					final GridBagConstraints eventConstr = layout.getConstraints(component);
					eventConstr.gridy = gridy;
					layout.setConstraints(component, eventConstr);
				}
				record.setLabelVisible(first);
				first = false;
				rowToTimeIndex.put(gridy, record.getTime());
				
				gridy++;
			}
		}

		final GridBagConstraints fillerConstr = layout.getConstraints(fillerC);
		fillerConstr.gridy = gridy;
		layout.setConstraints(fillerC, fillerConstr);
		
		revalidate();
		
		
	}
	
	private class EventRecord {

		private final Component component;
		private final Event event;
		private final Timeline timeline;
		private final JLabel label;
		
		/**
		 * The time which this timeline thinks the event is at, as opposed to the time its getTime() reports.
		 */
		private Time indexedTime;
		
		/**
		 * True until this record is removed();
		 */
		private boolean alive = false;
		
		private boolean labelVisible = true;

		public EventRecord(Timeline timeline, Event event,
                Component component) {
	        this.event = event;
			this.component = component;
	        this.timeline = timeline;
	        indexedTime = event.getTime();
	        label = new JLabel();
	        label.setBackground(timeline.getColor());
	        label.setOpaque(true);
	        updateLabel();
	        alive = true;
        }
		
		private void updateLabel() {
			label.setText(indexedTime.toString());
			if (labelVisible) {
				label.setForeground(Color.BLACK);
			} else {
				label.setForeground(timeline.getColor());
			}
		}

		public void setLabelVisible(boolean newVis) {
	        // TODO Auto-generated method stub
			labelVisible = newVis;
			updateLabel();
        }

		public Time getTime() {
	        return indexedTime;
        }

		public Component getLabel() {
	        return label;
        }

		public Component getComponent() {
	        return component;
        }

		public Event getEvent() {
	        return event;
        }

		public Timeline getTimeline() {
	        return timeline;
        }

		public void updateTime() {
			assert alive;
			
			Time newTime = event.getTime();
			
			timeIndex.get(indexedTime).remove(this);
			
			indexedTime = newTime;

			if (!timeIndex.containsKey(newTime)) // XXX repeated code, extract
				timeIndex.put(newTime, new ArrayList<EventRecord>());
			timeIndex.get(indexedTime).add(this);

			updateLabel();
			
			renumber();

		}

		/**
		 * The inverse of this operation is TimelinePanel.addEvent...
		 */
		public void remove() {
			assert alive;
			alive = false;
			
			try {
				// XXX review: this is done within timelineChanged which does its own save/restore, and we don't have nesting support. 
				//saveScroll();
				
				// remove from indexes
				timeIndex.get(indexedTime).remove(this);
				eventRecords.remove(event);

				// remove UI components
				component.getParent().remove(component);
				label.getParent().remove(label);

				// force layout update
				TimelinePanel.this.revalidate();

				// force redrawing
				// You'd think this would be unnecessary...
				TimelinePanel.this.repaint();
			} finally {
				//restoreScroll();
			}
        }
	}

	public class TPListener implements TimelineListener {
	    
		private final Timeline timeline;

		public TPListener(Timeline timeline) {
	        this.timeline = timeline;
        }

		public boolean timelineChanged(Time earliest, Time latest) {
			System.err.println("Handling change notification from " + timeline + ": " + earliest + " -to- " + latest);
			try {
				saveScroll();

				// Save copy of current events so we can check what to remove
				HashMap<Time, List<EventRecord>> myEvents = new HashMap<Time, List<EventRecord>>(timeIndex.subMap(earliest, latest.nextAdjacentTime()));

				List<Event> timelineEvents = timeline.eventsInInterval(earliest, latest);
				Set<Event> tev = new HashSet<Event>(timelineEvents); 

				for (List<EventRecord> l : myEvents.values()) {
					for (EventRecord er : new ArrayList<EventRecord>(l)) { // must copy to avoid deleting-while-iterating
						if (er.getTimeline() == timeline && !tev.contains(er.getEvent())) {
							System.err.println("Removing event not in timeline events: " + er.getEvent() + " -- tev is " + timelineEvents);
							er.remove();
						}
					}
				}

				// add events not previously seen, and update times of events
				for (Event e : timelineEvents) {
					System.err.println("\tfrom timeline: " + e);
					if (!eventRecords.containsKey(e)) {
						System.err.println("\t\tactually adding");
						addEvent(timeline, e);
					} else if (eventRecords.get(e).getTime() != e.getTime()) {
						System.err.println("\t\tupdating time");
						eventRecords.get(e).updateTime();
					}
				}

				//System.err.println("handling update " + earliest + " " + latest);

			} finally {
				restoreScroll();
			}

			return true; // XXX arrange so that this will return false when appropriate
	    }

    }

	private int offsetForSaveScroll() {
		return getHeight() / 2;
	}
	
	/**
	 * Remember the current scroll position *in terms of timeline events* for later restoration after a layout change.
	 */
	private void saveScroll() {
		assert savedOffset == -1;
		final Point pointInView = SwingUtilities.convertPoint(scrollC.getViewport(), 0, offsetForSaveScroll(), contentC);
	    savedTime = positionToTime(pointInView.y);
	    savedOffset = pointInView.y - timeToPosition(savedTime);
	    savedNow = Time.msFromNow(0);
	    //System.err.println("saving " + pointInView.y + " => " + savedTime + "(" + timeToPosition(savedTime) + ") + " + savedOffset);
    }

	private void restoreScroll() {
		contentC.validate(); // force immediate layout update so we have accurate information
		
	    //System.err.println("restoring " + (timeToPosition(savedTime) + savedOffset) + " <= " + savedTime + "(" + timeToPosition(savedTime) + ") + " + savedOffset);
	    scrollC.getViewport().setViewPosition(new Point(
	       scrollC.getViewport().getViewPosition().x,
	       timeToPosition(savedTime.plus(savedNow.minus(savedTime))) - offsetForSaveScroll() + savedOffset));
	    
	    savedOffset = -1;
    }

	/**
	 * Return the Y coordinate of the top edge of the bottommost event occurring
	 * at or before the specified time.
	 * 
	 * @param time
	 *            The time to find the location of. If null, then 0 is returned.
	 */
	private int timeToPosition(Time time) {
		if (time == null) {
			return 0;
		} else {
			//final SortedMap<Time, List<EventRecord>> thenOrEarlier = timeIndex.headMap(time.nextAdjacentTime());
			//List<EventRecord> records = thenOrEarlier.get(thenOrEarlier.lastKey());
			time = timeIndex.floorKey(time);
			while (time == null || timeIndex.get(time).size() == 0) {
				time = timeIndex.lowerKey(time);
			}
			List<EventRecord> records = timeIndex.get(time);
			return records.get(records.size() - 1).getLabel().getLocation().y;
		}
    }

	/**
	 * Return the time of the event containing the specified Y coordinate.
	 */
	private Time positionToTime(int y) {
		Point gridCell = layout.location(0, y);
		//System.err.println("\tptt " + gridCell + "=> " + rowToTimeIndex.get(gridCell.y));
		return rowToTimeIndex.get(gridCell.y);
    }

	
}
