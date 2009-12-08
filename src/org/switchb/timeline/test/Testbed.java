package org.switchb.timeline.test;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.swing.*;

import org.switchb.timeline.Timeline;
import org.switchb.timeline.TimelineFrame;
import org.switchb.timeline.TimelinePanel;
import org.switchb.timeline.apps.CalendarTimeline;
import org.switchb.timeline.apps.ClockTimeline;
import org.switchb.timeline.apps.MemoTimeline;

public class Testbed {

	static File calFile = new File("calendar.ser");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set up frame
		TimelineFrame frame = new TimelineFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addTimeline(new TestTimeline());
		
		// Set up calendar, from file
		CalendarTimeline cal = null;
		try {
            cal = (CalendarTimeline)new ObjectInputStream(new FileInputStream(calFile)).readObject();
        } catch (FileNotFoundException e) {
	            // do nothing;
            } catch (IOException e) {
	            System.err.println("Error in loading calendar data:");
	            e.printStackTrace();
            } catch (ClassNotFoundException e) {
	            System.err.println("Error in loading calendar data:");
	            e.printStackTrace();
            }
		if (cal == null) {
			cal = new CalendarTimeline();
			cal.setColor(Color.getHSBColor(0.13f, 0.25f, 1));
		}
		frame.addTimeline(cal);
		
		// Arrange to save calendar
		final CalendarTimeline calFinal = cal; // sigh
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			/* NOTE: This is unsafe in two ways: if the serialization fails it will destroy the saved data, and it never saves except when the process exits. */
			
			public void run() {
				try {
	                new ObjectOutputStream(new FileOutputStream(calFile)).writeObject(calFinal);
                } catch (FileNotFoundException e) {
                	System.err.println("Error in saving calendar data:");
	                e.printStackTrace();
                } catch (IOException e) {
                	calFile.delete();
                	System.err.println("Error in saving calendar data:");
	                e.printStackTrace();
                }
			}
		}));
		
		frame.addTimeline(new ClockTimeline());
		frame.addTimeline(new MemoTimeline());
		
		// For testing timeline-change handling.
		//p.addTimeline(new BlinkTimeline());
		
		// show window
		frame.setSize(800, 400);
		frame.setVisible(true);
	}

}
