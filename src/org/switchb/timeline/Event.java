package org.switchb.timeline;

import java.awt.Component;
import java.util.Date;

public interface Event {
	Component makeComponent();
	Time getTime();
}
