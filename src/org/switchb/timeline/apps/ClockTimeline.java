package org.switchb.timeline.apps;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.Timer;

import org.switchb.timeline.NowEvent;
import org.switchb.timeline.SimpleTimeline;
import org.switchb.timeline.Time;
import org.switchb.timeline.Timeline;
import org.switchb.timeline.TimelineListener;

public class ClockTimeline extends SimpleTimeline implements Timeline {

	private final ClockEvent event = new ClockEvent(this);
	
	public ClockTimeline() {
		add(event);
    }
	
	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	@Override
	public JMenu makeMenu() {
		return null;
	}
	
	private class ClockEvent extends NowEvent {
		public ClockEvent(ClockTimeline timeline) {
	        super(timeline);
        }

		@Override
	    public Component makeComponent() {
		    return new JLabel("Ñ");
	    }

	}
}
