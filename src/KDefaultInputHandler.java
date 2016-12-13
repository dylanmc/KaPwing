package com.mecodegoodsomeday.KaPwing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 2:53:33 PM
 */

public class KDefaultInputHandler extends KGenericHandler {
    KSpace m_space;
    KPoint m_mousePoint = null;

    KObject m_hotObj = null;

    public boolean keyReleased(KEditable o, KeyEvent e) {
        return false;
    }

    public boolean keyPressed(KEditable o, KeyEvent e) {
        switch(e.getKeyChar()) {
            case 'e':
                m_space.m_topCommands.setEditMode();
                break;
            case 'f':
                m_space.m_topCommands.setFactoryMode();
                break;
            case 'p':
                m_space.m_topCommands.setPaddleMode();
                break;
            default:
        }
        return false;
    }

    public KDefaultInputHandler() {
        ; // throw an exception?? need space!!
    }
    public KDefaultInputHandler(KSpace s) {
        m_space = s;
        m_mousePoint = new KPoint(0,0);
        m_drag = false;
        m_move = true;
    }

    public boolean mousePressed(KEditable o, MouseEvent e) {
        return false;
    }

    public boolean mouseReleased(KEditable o, MouseEvent e) {
        // System.out.println("default mouse released");
        // System.out.println("DefaultMouseHandler mouse released..." + m_hotObj + o);
        if (m_hotObj != null && m_hotObj instanceof KMenuItem) {
            KMenuItem item = (KMenuItem) m_hotObj;
            item.m_parent.menuItemHit(item, true);
            return true;
        }
        return false;
    }

    // figured out if the mouse has moved over a menu item
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        m_mousePoint.x = ev.getX();
        m_mousePoint.y = ev.getY();
        boolean ret = false;
        m_hotObj = null;
        for (Enumeration e = m_space.m_drawables.elements(); e.hasMoreElements(); ) {
            KDrawable obj = (KDrawable) e.nextElement();
            if (!(obj instanceof KMenuItem)) {
                continue;
            }
            if (obj.intersectsWithPoint(m_mousePoint)) {
                obj.setState(KaPwing.HOT);
                m_hotObj = obj;
                ret = true;
            } else {
                obj.setState(0);
            }
        }
        return ret;
    }

    public boolean mouseDragged(KEditable o, MouseEvent e) {
        return false;
    }
}
