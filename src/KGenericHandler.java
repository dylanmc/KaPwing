package com.mecodegoodsomeday.KaPwing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 2:33:03 PM
 * License: CC-SA
 */
public class KGenericHandler implements KEditHandler {
    KSpace m_space;
    boolean m_drag = false;
    boolean m_move = false;

    public static boolean rightClick(MouseEvent e) {
        if ((e.getButton() == 3) || e.isControlDown()){
            return true;
        }
        return false;
    }
    public void resetHandler() {
        ;
    }
    public boolean keyReleased(KEditable o, KeyEvent e) {
        return false;
    }

    public boolean keyPressed(KEditable o, KeyEvent e) {
        return false;
    }

    public boolean mousePressed(KEditable o, MouseEvent e) {
        return false; // didn't handle
    }
    public boolean mouseReleased(KEditable o, MouseEvent e) {
        return false; // didn't handle
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        return false; // didn't handle
    }
    public boolean mouseDragged(KEditable o, MouseEvent e) {
        return false; // didn't handle
    }
    public boolean wantMouseDragEvents() {
        return m_drag;
    }
    public boolean wantMouseMoveEvents() {
        return m_move;
    }

}
