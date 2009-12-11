package org.switchb.timeline;

import java.awt.Component;

public interface Event {
	Component makeComponent();
	Time getTime();
}
