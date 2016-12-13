package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;
import java.util.Enumeration;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 1, 2004
 * Time: 9:13:31 PM
 */
public class KChannelMenu extends KMenu {
    KPaddle m_currPaddle = null;
    KMenuItem m_lastSelected = null;

    public void draw(Graphics2D g) {
        
    }
    public KChannelMenu(KSpace s) {
        m_items = new Vector();
        int j = 12;
        for (int i = 0; i < 16; i++) {
            float theta = (float) (Math.PI / 8) * j;
            j = (j + 1) % 16;
            int dx = (int)(60 * Math.cos(theta));
            int dy = (int)(60 * Math.sin(theta));
            KMenuItem item = new KLiveMenuItem(this, 200 + dx, 200 + dy, 20, 20, "" + (i + 1), i);
            m_items.add(item);
        }
        m_space = s;
    }
    public void setHotObj(KObject o) {
        KPaddle p = (KPaddle) o;
        m_currPaddle = p;
    }
    public void menuItemHit(KMenuItem i, boolean mouseReleased) {
        m_currentItem = i;        
        // do something good
        m_currPaddle.m_channel = i.m_id;
        if (i != m_lastSelected) {
            m_currPaddle.fireEvent(null, m_space);
            m_lastSelected = i;
        }
        if (mouseReleased) {
            hideMenu();
        }
    }
    public void showMenu(KPoint p) {
        int j = 12;
        for (int i = 0; i < 16; i++) {
            float theta = (float) (Math.PI / 8) * j;
            j = (j + 1) % 16;
            int x = (int)(p.x + (60 * Math.cos(theta)));
            int y = (int)(p.y + 60 * Math.sin(theta));
            KMenuItem item = (KMenuItem) m_items.elementAt(i);
            item.moveItemTo(x, y);
        }
        super.showMenu();
    }
}
