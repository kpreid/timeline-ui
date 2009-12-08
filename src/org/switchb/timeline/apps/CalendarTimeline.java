package org.switchb.timeline.apps;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.switchb.timeline.Event;
import org.switchb.timeline.SimpleTimeline;
import org.switchb.timeline.Time;

public class CalendarTimeline extends SimpleTimeline {
	public CalendarTimeline() {
		add(new CalendarEvent("[This calendar created]", Time.msFromNow(0)));
    }
	public JMenu makeMenu() {
		JMenu menu = new JMenu("Calendar");

		menu.add(new JMenuItem(new AddAction()));

		return menu;
	}

	public class AddAction extends AbstractAction {
		public AddAction() {
			super("Add Event");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// panel containing data for event
			JComponent settings = new Box(BoxLayout.PAGE_AXIS);
			JTextField textC = new JTextField(50);
			settings.add(textC);
			SpinnerDateModel dateM = new SpinnerDateModel(new Date(), null,
			                                              null, Calendar.MINUTE);
			settings.add(new JSpinner(dateM));
			
			// get data
			int result = 
				JOptionPane.showConfirmDialog(e.getSource() instanceof Component
				                              	? (Component)e.getSource()
				                              	: null,
				                              settings,
				                              "New Event",
				                              JOptionPane.OK_CANCEL_OPTION,
				                              JOptionPane.QUESTION_MESSAGE);
			
			// If user didn't cancel, add event
			if (result == JOptionPane.OK_OPTION) {
				CalendarTimeline.this.add(new CalendarEvent(textC.getText(),
				                                            Time.fromDate(dateM.getDate())));
				System.out.println("added");
			}
		}
	}

	private class CalendarEvent implements Event, Serializable {
		
		private String text;
		private Time time;

	    public CalendarEvent(String text, Time time) {
			// Not using setters because they don't expect an uninitialized object
	    	this.text = text;
	    	this.time = time;
        }

	    @Override
	    public Component makeComponent() {
		    return new JLabel(text);
	    }

		@Override
	    public Time getTime() {
		    return time;
	    }

		public void setTime(Time newTime) {
			if (newTime == null) 
				throw new NullPointerException("Attempted to set a calendar event's time to null");
			final Time oldTime = time;
	        time = newTime;
			timeChanged(this, oldTime);
        }

		public void setText(String text) {
	        this.text = text;
	        // XXX update components
        }

		public String getText() {
	        return text;
        }

    }

}
