package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 5, 2004
 * Time: 9:02:32 AM
 * License: CC-SA
 */

public class KNoteMenu extends KMenu{
    KPaddle m_currPaddle = null;
    int m_range, halfRange;
    KMenuItem m_lastSelected;

    public KNoteMenu(KSpace s, int range) {
        m_items = new Vector();
        m_range = range;
        halfRange = range / 2;

        for (int i = 0; i < range; i++) {
            int dx = i * 7;
            int dy = 0;
            KMenuItem item = new KLiveMenuItem(this, 200 + dx, 200 + dy, 5, 20, null, 60 - halfRange + i);
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
        // System.out.println("notemenu item hit");
        // do something good
        m_currPaddle.paddleNote = i.m_id;
        if (i != m_lastSelected) {
            m_currPaddle.fireEvent(null, m_space);
            m_lastSelected = i;
        }
        if (mouseReleased) {
            hideMenu();
        }
    }
    public void showMenu(KPoint p) {
        for (int i = 0; i < m_range; i++) {
            int j = i - halfRange;
            int x = (int)(p.x + (j * 7));
            int y = (int)p.y;
            KMenuItem item = (KMenuItem) m_items.elementAt(i);
            item.moveItemTo(x, y);
        }
        super.showMenu();
    }
    public void draw(Graphics2D g) {

    }
}
