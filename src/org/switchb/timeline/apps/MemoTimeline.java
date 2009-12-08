package org.switchb.timeline.apps;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;

import org.switchb.timeline.BaseTimeline;
import org.switchb.timeline.Event;
import org.switchb.timeline.NowEvent;
import org.switchb.timeline.SimpleTimeline;
import org.switchb.timeline.Time;
import org.switchb.timeline.Timeline;
import org.switchb.timeline.TimelineListener;

/**
 * Timeline which stores dated memos (lines of text) and provides a field to enter them.
 */
public class MemoTimeline implements Timeline {
	SimpleTimeline memos = new SimpleTimeline();
	
	transient SimpleTimeline nowContainer = new SimpleTimeline();
	{
		nowContainer.add(new MemoInputEvent());
	}
	
	public MemoTimeline() {
	    // TODO Auto-generated constructor stub
    }
	
	@Override
	public List<Event> eventsInInterval(Time earliest, Time latest) {
	    List<Event> result = new ArrayList<Event>();
	    result.addAll(memos.eventsInInterval(earliest, latest));
	    result.addAll(nowContainer.eventsInInterval(earliest, latest));
	    return result;
	}
	
	@Override
	public void addListener(TimelineListener listener) {
	    memos.addListener(listener);
	    nowContainer.addListener(listener);
	}

	@Override
    public Color getColor() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public JMenu makeMenu() {
	    // TODO Auto-generated method stub
	    return null;
    }
	
	private class MemoEvent implements Event, Serializable {

		private final Time time;
		private final String memo;

		public MemoEvent(Time time, String memo) {
			// Immutable -- so there are no setters
	        this.time = time;
	        this.memo = memo;
        }
		
		@Override
        public Time getTime() {
	        return time;
        }

		@Override
        public Component makeComponent() {
	        JTextField field = new JTextField(memo);
	        field.setEditable(false);
	        return field;
        }
		
	}

	private class MemoInputEvent extends NowEvent {

		public MemoInputEvent() {
	        super(nowContainer);
        }

		@Override
        public Component makeComponent() {
	        final JTextField field = new JTextField(40);
	        field.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					memos.add(new MemoEvent(Time.msFromNow(0), field.getText()));
					field.setText("");
					updateNow();
				}
			});
			return field;
        }
		
	}
}
