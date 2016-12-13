package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;
import java.util.Enumeration;
import java.awt.geom.RoundRectangle2D;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 1, 2004
 * Time: 9:10:55 PM
 */
public class KMenu implements KDrawable {
    // subclass constructors must fill these in:
    Vector m_items;
    boolean m_showing = false;
    KSpace m_space;
    int m_minx, m_maxw, m_miny, m_maxh;
    RoundRectangle2D.Float m_backgroundRect = null;
    boolean m_doUpdateRect = false;
    KMenuItem m_currentItem = null;

    public KMenu() {
        m_minx = 1000;
        m_miny = 5000;
        m_maxw = -1;
        m_maxh = -1;
    }

    public void menuItemHit(KMenuItem o, boolean mouseReleased) {
        ; // override to do cool stuff.
    }

    public void updateRectangle(int x, int y, int w, int h) {
        if (m_doUpdateRect) {
            if (m_minx != x) {
                // here we have an indented item...
                w += Math.abs(x - m_minx);
            }
        }
        m_minx = Math.min(x, m_minx);
        m_miny = Math.min(y, m_miny);
        m_maxw = Math.max(w, m_maxw);
        m_maxh += h;
        m_doUpdateRect = true;
    }

    public void showMenu() {
        if (!m_showing) {
            m_showing = true;
            for (Enumeration e = m_items.elements(); e.hasMoreElements(); ) {
                KMenuItem i = (KMenuItem) e.nextElement();
                m_space.addObject(i);
            }
            m_space.addObject(this);
        }
    }
    public void hideMenu() {
        if (m_showing) {
            m_showing = false;
            for (Enumeration e = m_items.elements(); e.hasMoreElements(); ) {
                KMenuItem i = (KMenuItem) e.nextElement();
                m_space.removeObject(i);
            }
            m_space.removeObject(this);
        }

    }
    public KPoint getPos() {
        return null;
    }
    public void draw (Graphics2D g) {
        if (m_doUpdateRect) {
            System.out.println("UR: " + m_minx +", "+m_miny +", dims" + m_maxw + ", "+ m_maxh);
            m_backgroundRect = new RoundRectangle2D.Float(m_minx - 6, m_miny,  m_maxw + 12, m_maxh + 12, 6.0f, 6.0f);
            m_doUpdateRect = false;
        }
        /* if (m_backgroundRect != null) {
            g.setPaint(KMenuItem.s_neutral);
            g.fill(m_backgroundRect);
        }
        */
    }
    public boolean intersectsWithPoint(KPoint p) {
        return false;
    }

    public void setState(int state) {

    }


}
