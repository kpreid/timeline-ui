package org.switchb.timeline;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public abstract class NowEvent implements Event {

	protected final Timeline timeline;
	protected final Timer timer;
	/**
     * The concrete time which the clock event is actually at.
     */
    protected Time now;


    /**
     * @param timeline
     */
    public NowEvent(Timeline timeline) {
        this.timeline = timeline;
		now = Time.msFromNow(0);
	    timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateNow();
			}
		});
		timer.start();
    }

	@Override
    public Time getTime() {
        return now;
    }
	
	/**
	 * Force an update of the 'now' time to the current instant.
	 */
	public void updateNow() {
		final Time was = now;
		now = Time.msFromNow(0);
		
		// XXX this event shouldn't(?) need to know this much about the type of timeline?
		((SimpleTimeline)NowEvent.this.timeline).timeChanged(NowEvent.this, was);
	}

}