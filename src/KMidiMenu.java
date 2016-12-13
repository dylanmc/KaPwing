package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 6, 2004
 * Time: 3:07:27 PM
 * License: CC-SA
 */
public class KMidiMenu extends KMenu {
    KSoundManager m_soundManager;
    KPaddle m_currPaddle;
    int m_numItems;
    KMenuItem m_lastSelected;

    public KMidiMenu(KSpace s) {
        m_items = new Vector();
        m_numItems = KSoundManager.s_kSounds.size();

        for (int i = 0; i < m_numItems;  i++) {
            KSound ks = (KSound) KSoundManager.s_kSounds.elementAt(i);
            KMenuItem item = new KLiveMenuItem(this, 0, 0, 100, 18, ks.m_name, i);
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
        m_currPaddle.m_soundDevice = (KSound) KSoundManager.s_kSounds.elementAt(i.m_id);
        if (i != m_lastSelected) {
            m_currPaddle.fireEvent(null, m_space);
            m_lastSelected = i;
        }
        if (mouseReleased) {
            hideMenu();
        }
    }
    public void showMenu(KPoint p) {
        int height = 20;
        int halfNum = m_numItems / 2;
        int yoff = halfNum * height;

        for (int i = 0; i < m_numItems; i++) {
            int x = (int)p.x - 30;
            int y = (int)(p.y - yoff + (i * height));
            KMenuItem item = (KMenuItem) m_items.elementAt(i);
            item.moveItemTo(x, y);
        }
        super.showMenu();
    }
}
