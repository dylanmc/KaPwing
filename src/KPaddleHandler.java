package com.mecodegoodsomeday.KaPwing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 2:03:49 PM
 * License: CC-SA
 */
public class KPaddleHandler extends KGenericHandler {
    KPoint m_mousePoint1, m_mousePoint2;

    public KPaddleHandler(KSpace s) {
        m_space = s;
        m_drag = true;
        m_move = false;
        m_mousePoint1 = new KPoint(0,0);
        m_mousePoint2 = new KPoint(0,0);
    }
    public boolean mousePressed(KEditable o, MouseEvent e) {
        m_mousePoint1.x = e.getX();
        m_mousePoint1.y = e.getY();
        // m_space.setDragLine(m_mousePoint1, m_mousePoint1);
        return true;
    }
    public boolean mouseReleased(KEditable o, MouseEvent e) {
        System.out.println("creating paddle");
        m_mousePoint2.x = e.getX();
        m_mousePoint2.y = e.getY();
        KPaddle newp = new KPaddle((int)m_mousePoint1.x, (int)m_mousePoint1.y,
                (int)m_mousePoint2.x, (int)m_mousePoint2.y);
        m_space.addObject(newp);
        m_space.clearDragLine();
        m_space.m_responders.pop(); // should we specify us, or is it implicit?
        return true;
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        return false;
    }
    public boolean mouseDragged(KEditable o, MouseEvent e) {
        m_mousePoint2.x = e.getX();
        m_mousePoint2.y = e.getY();

        m_space.setDragLine(m_mousePoint1, m_mousePoint2);
        return true;
    }
}
