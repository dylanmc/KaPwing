package com.mecodegoodsomeday.KaPwing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 9:57:19 PM
 */
public interface KEditHandler {
    public void resetHandler();
    public boolean keyReleased(KEditable o, KeyEvent e);
    public boolean keyPressed(KEditable o, KeyEvent e);
    public boolean mousePressed(KEditable o, MouseEvent e);
    public boolean mouseReleased(KEditable o, MouseEvent e);
    public boolean mouseMoved(KEditable o, MouseEvent ev) ;
    public boolean mouseDragged(KEditable o, MouseEvent e);
    public boolean wantMouseDragEvents() ;
    public boolean wantMouseMoveEvents() ;
}
