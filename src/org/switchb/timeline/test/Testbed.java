package org.switchb.timeline.test;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import javax.swing.JFrame;

import org.switchb.timeline.Timeline;
import org.switchb.timeline.TimelineFrame;
import org.switchb.timeline.apps.CalendarTimeline;
import org.switchb.timeline.apps.ClockTimeline;
import org.switchb.timeline.apps.MemoTimeline;

public class Testbed {

	final static File calFile = new File("calendar.ser");
	final static File cal2File = new File("calendar2.ser");
	final static File memoFile = new File("memos.ser");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set up frame
		TimelineFrame frame = new TimelineFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up calendar, from file if possible
		final CalendarTimeline cal = loadOrCreate(calFile, new Callable<CalendarTimeline>() {
			public CalendarTimeline call() {
				CalendarTimeline newCal = new CalendarTimeline();
				newCal.setColor(Color.getHSBColor(0.13f, 0.25f, 1));
				return newCal;
			}
		});
		frame.addTimeline(cal);
		
		final CalendarTimeline cal2 = loadOrCreate(cal2File, new Callable<CalendarTimeline>() {
			public CalendarTimeline call() {
				CalendarTimeline newCal = new CalendarTimeline();
				newCal.setColor(Color.getHSBColor(0.40f, 0.25f, 1));
				return newCal;
			}
		});
		frame.addTimeline(cal2);
		
		// Memos, ditto
		final MemoTimeline memos = loadOrCreate(memoFile, new Callable<MemoTimeline>() {
			public MemoTimeline call() {
				MemoTimeline memos = new MemoTimeline();
				return memos;
			}
		});
		frame.addTimeline(memos);
		
		// Arrange to save calendar
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			/* NOTE: This is unsafe in two ways: if the serialization fails it will destroy the saved data, and it never saves except when the process exits. */
			
			public void run() {
				saveTimeline(calFile, cal);
				saveTimeline(cal2File, cal2);
				saveTimeline(memoFile, memos);
			}
		}));
		
		// Non-saved timelines
		frame.addTimeline(new TestTimeline());		
		frame.addTimeline(new ClockTimeline());
		
		// For testing timeline-change handling.
		//p.addTimeline(new BlinkTimeline());
		
		// show window
		frame.setSize(800, 400);
		frame.setVisible(true);
	}

	@SuppressWarnings("unchecked")
    private static <T> T loadOrCreate(File file, Callable<T> creator) {
		T result = null;
		try {
			result = (T)new ObjectInputStream(new FileInputStream(file)).readObject();
		} catch (FileNotFoundException e) {
			// do nothing;
		} catch (IOException e) {
			System.err.println("Error in loading data " + file + ":");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Error in loading data " + file + ":");
			e.printStackTrace();
		}
		if (result == null) {
			try {
	            result = creator.call();
            } catch (Exception e) {
	            // This shouldn't actually happen in this application, but Callable is declared to throw
            	throw new RuntimeException(e);
            }
		}
	    return result;
    }

	private static void saveTimeline(File file, Timeline object) {
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(object);
        } catch (FileNotFoundException e) {
        	System.err.println("Error in saving data " + file + ":");
            e.printStackTrace();
        } catch (IOException e) {
        	calFile.delete();
        	System.err.println("Error in saving data " + file + ":");
            e.printStackTrace();
        }
    }
}
