package org.switchb.timeline.test;

import java.awt.Component;

import javax.swing.JLabel;

import org.switchb.timeline.Event;
import org.switchb.timeline.SimpleTimeline;
import org.switchb.timeline.Time;

@SuppressWarnings("serial")
public class TestTimeline extends SimpleTimeline {

	class TTEvent implements Event {
		Time time;
		String label;
		
		public TTEvent(Time when, String label) {
			time = when;
			this.label = label;
		}

		public Time getTime() {
			return time;
		}

		public Component makeComponent() {
			return new JLabel(label);
		}
		
	}
	
	public TestTimeline() {
		Time t0 = Time.msFromNow(0);
    	add(new TTEvent(t0, "start1"));
    	add(new TTEvent(t0, "start2"));
	    for (float t = 1; t <= 60; t *= 2) {
	    	add(new TTEvent(Time.msFromNow((int)t * 1000), "start + " + t));
	    }
    }
}
