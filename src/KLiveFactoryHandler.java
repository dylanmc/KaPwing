package com.mecodegoodsomeday.KaPwing;

import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 8, 2004
 * Time: 11:14:35 AM
 */
public class KLiveFactoryHandler extends KGenericHandler {
    KPoint m_mousePoint;
    int m_mode;
    KLiveFactory m_bf;

    final static int NEW_FACTORY_MODE = 1;
    final static int MOVE_MODE = 2;

    public KLiveFactoryHandler(KSpace s) {
        m_space = s;
        m_drag = false;
        m_move = false;
        m_mousePoint = new KPoint(0,0);
    }
    public boolean mousePressed(KEditable o, MouseEvent e) {
        boolean ret = false;

        if (o == null) {
            m_mode = NEW_FACTORY_MODE;
        } else if (! (o instanceof KLiveFactory)) {
            return false;
        } else {
            m_mode = MOVE_MODE;
        }
        m_bf = (KLiveFactory) o;

        return ret;
    }

    public boolean mouseReleased(KEditable o, MouseEvent e) {
        if (m_mode == NEW_FACTORY_MODE) {
            m_mousePoint.x = e.getX();
            m_mousePoint.y = e.getY();
            KLiveFactory newbf = new KLiveFactory((int)m_mousePoint.x, (int)m_mousePoint.y);
            m_space.addObject(newbf);
            m_space.removeHandler(this);
        } else {
            int nx = e.getX();
            int ny = e.getY();
            m_bf.me.x = nx;
            m_bf.me.y = ny;
            m_bf.init (nx, ny, nx, ny);
        }
        return true;
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        return false;
    }

    public boolean mouseDragged(KEditable o, MouseEvent e) {      
        if (m_mode == MOVE_MODE) {
            int nx = e.getX();
            int ny = e.getY();
            m_bf.me.x = nx;
            m_bf.me.y = ny;
            m_bf.init (nx, ny, nx, ny);
        }
        return true;
    }

    public boolean wantMouseDragEvents() {
        return true;
    }
}
