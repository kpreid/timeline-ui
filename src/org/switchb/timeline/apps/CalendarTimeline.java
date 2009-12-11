package org.switchb.timeline.apps;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.switchb.timeline.BasicEventMouseListener;
import org.switchb.timeline.Event;
import org.switchb.timeline.SimpleTimeline;
import org.switchb.timeline.Time;

@SuppressWarnings("serial")
public class CalendarTimeline extends SimpleTimeline {
	
	public CalendarTimeline() {
		add(new CalendarEvent("[This calendar created]", Time.msFromNow(0)));
    }
	
	public JMenu makeMenu() {
		JMenu menu = new JMenu("Calendar");

		menu.add(new JMenuItem(new AddAction()));

		return menu;
	}
	
	/**
	 * Calendar events -- have a time and date and are editable.
	 */
	private class CalendarEvent implements Event, Serializable {
		
		private String text;
		private Time time;
		
		/**
		 * Components to be updated with our current text.
		 */
		transient private WeakHashMap<JTextField, Void> components; 

	    public CalendarEvent(String text, Time time) {
	    	if (text == null)
	    		throw new NullPointerException("Text may not be null");
	    	if (time == null)
	    		throw new NullPointerException("Time may not be null");

    		// Not using setters because they don't expect an uninitialized object
	    	this.text = text;
	    	this.time = time;
        }

	    @Override
	    public Component makeComponent() {
		    final JTextField textC = new JTextField(text);
			textC.setEditable(false); /*
									 * Not editable because a lot of editable
									 * components would be visual and tab-order
									 * clutter. The dialog is used instead.
									 */
		    
		    // Remember the component so we can update it when our text changes
		    getComponents().put(textC, null);
		    
		    JPopupMenu myMenu = new JPopupMenu();
		    myMenu.add(new JMenuItem(new EditAction(this)));
		    myMenu.add(new JMenuItem(new DeleteAction(this)));
		    
		    textC.addMouseListener(new BasicEventMouseListener(myMenu));
		    
		    return textC;
	    }

		/**
		 * Ensure that the transient components table is available whether we were
		 * constructed or unserialized.
		 */
		private WeakHashMap<JTextField, Void> getComponents() {
	        if (components == null)
	        	components = new WeakHashMap<JTextField, Void>();
	        return components;
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
	    	if (text == null)
	    		throw new NullPointerException("Text may not be null");
	    	this.text = text;
	        
	    	// TODO: Should this be deferred?
	    	for (JTextField c : getComponents().keySet()) {
	    		c.setText(text);
	    	}
        }

		public String getText() {
	        return text;
        }
		
		/**
		 * Display a window to edit the event.
		 * 
		 * @param parentComponent
		 *            As in the JOptionPane parentComponent parameter.
		 * 
		 * @return a boolean indicating whether the user saved or cancelled.
		 */
		private boolean edit(Component parentComponent) {

			// container holding edit controls for event
			JComponent settings = new Box(BoxLayout.PAGE_AXIS);
			JTextField textC = new JTextField(getText(), 50);
			settings.add(textC);
			SpinnerDateModel dateM = new SpinnerDateModel(getTime().getDate(), null,
			                                              null, Calendar.MINUTE);
			settings.add(new JSpinner(dateM));
			
			// get data
			int result = 
				JOptionPane.showConfirmDialog(parentComponent,
				                              settings,
				                              "New Event",
				                              JOptionPane.OK_CANCEL_OPTION,
				                              JOptionPane.QUESTION_MESSAGE);
			
			// If user didn't cancel, save changes to event
			if (result == JOptionPane.OK_OPTION) {
				setText(textC.getText());
				setTime(Time.fromDate(dateM.getDate()));
				System.out.println("added");
				return true;
			} else {
				return false;
			}
		}
    }

	/**
	 * Command to create a new event.
	 */
	public class AddAction extends AbstractAction {
		public AddAction() {
			super("Add Event");
		}

		@Override
		public void actionPerformed(ActionEvent userAction) {
			CalendarEvent newEvent = new CalendarEvent("", Time.msFromNow(0));
			if (newEvent.edit(userAction.getSource() instanceof Component 
			                    ? (Component)userAction.getSource()
			                    : null)) {
				CalendarTimeline.this.add(newEvent);
			}
		}
	}

	/**
	 * Command to edit an existing event.
	 */
	public class EditAction extends AbstractAction {
		private final CalendarEvent event;

		public EditAction(CalendarEvent event) {
			super("Edit Event");
			this.event = event;
		}

		@Override
		public void actionPerformed(ActionEvent userAction) {
			System.err.println(userAction.getSource());
			event.edit(userAction.getSource() instanceof Component 
	                    ? (Component)userAction.getSource()
	                    : null);
		}
	}


	/**
	 * Command to delete an existing event.
	 */
	public class DeleteAction extends AbstractAction {
		private final CalendarEvent event;

		public DeleteAction(CalendarEvent event) {
			super("Delete Event");
			this.event = event;
		}

		@Override
		public void actionPerformed(ActionEvent userAction) {
			CalendarTimeline.this.remove(event);
		}
	}

}
