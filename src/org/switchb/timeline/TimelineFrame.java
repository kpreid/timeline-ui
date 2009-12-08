package org.switchb.timeline;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.switchb.timeline.test.TestTimeline;

public class TimelineFrame extends JFrame {
	
	private TimelinePanel timelinePanel;
	private JMenuBar menuBar;

	public TimelineFrame() {
		setTitle("Timeline Data");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		timelinePanel = new TimelinePanel();
		setContentPane(timelinePanel);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// have a default size
		setSize(400, 400);
    }
	
	public void addTimeline(Timeline timeline) {
		timelinePanel.addTimeline(timeline);
		JMenu menu = timeline.makeMenu();
		if (menu != null) 
			menuBar.add(menu);
	}
	
	/*public void removeTimeline(Timeline timeline) {
		
	}*/
}
