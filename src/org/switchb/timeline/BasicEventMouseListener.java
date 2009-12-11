package org.switchb.timeline;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

/**
 * A MouseListener implementing behaviors common to many timeline objects.
 * Currently, this is: having a popup menu.
 */
public class BasicEventMouseListener implements MouseListener {

	private JPopupMenu popupMenu;

	/**
	 * @param popupMenu
	 *            The menu which will be shown upon a popup-trigger mouse event
	 *            on the component.
	 */
	public BasicEventMouseListener(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	/**
	 * Generically handle any type of mouse event.
	 */
	private void evaluateForPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		evaluateForPopup(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		evaluateForPopup(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		evaluateForPopup(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		evaluateForPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		evaluateForPopup(e);
	}
}
