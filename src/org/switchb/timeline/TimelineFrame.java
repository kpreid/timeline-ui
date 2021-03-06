package org.switchb.timeline;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class TimelineFrame extends JFrame {
	
	private TimelinePanel timelinePanel;
	private JMenuBar menuBar;

	public TimelineFrame() {
		setTitle("Timeline Data");

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
		if (menu != null) { 
			// TODO: Should let the timeline do this rather than tinkering with what it gave us
			menu.setBackground(timeline.getColor());

			menuBar.add(menu);
		}		
	}
	
	/*public void removeTimeline(Timeline timeline) {
		
	}*/
}
