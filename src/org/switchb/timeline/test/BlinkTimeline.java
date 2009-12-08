package org.switchb.timeline.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.Timer;

import org.switchb.timeline.BaseTimeline;
import org.switchb.timeline.Event;
import org.switchb.timeline.Time;

public class BlinkTimeline extends BaseTimeline {
	private static final int INTERVAL = 2000;

	class BTEvent implements Event {
		Time time;
		
		public BTEvent(Time when) {
			time = when;
		}

		public Time getTime() {
			return time;
		}

		public Component makeComponent() {
			JLabel l = new JLabel("*blink*\nasdf");
			l.setPreferredSize(new java.awt.Dimension(100, 100));
			return l;
		}
		
	}
	
	BTEvent event = new BTEvent(Time.msFromNow(-1000));
	
	public BlinkTimeline() {
	    new Timer(INTERVAL, new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				notifyListeners(event.getTime(), event.getTime());
				
			}
		}).start();
    }
	
	private boolean showAt(Time now) {
		return now.getDate().getTime() / INTERVAL % 2 == 0;
	}

	public List<Event> eventsInInterval(Time earliest, Time latest) {
		List<Event> events = new ArrayList<Event>();

		if (showAt(Time.msFromNow(0))) {
			events.add(event);
		}

		return events;
	}
	
	public JMenu makeMenu() {
		return null;
	}
	
	public Color getColor() {
		return Color.BLACK;
	}

}
