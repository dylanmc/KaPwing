package com.mecodegoodsomeday.KaPwing;

import java.awt.geom.RoundRectangle2D;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 4, 2004
 * Time: 5:24:43 PM
 */

// This is kinda funny:  The function of a LiveMenuItem isn't all that different, but
// since it's a different class, it lets us do "instanceof" in the menu handling code
// which is where the differences are.    (see DefaultMouseHandler
public class KLiveMenuItem extends KMenuItem {
    static Color s_blue = new Color(100,100,255, 75);
    public KLiveMenuItem(KMenu parent, int x, int y, int w, int h, String label, int id) {
        super(parent, x, y, w, h, label);
        m_id = id;
    }
    /*
    public void draw (Graphics2D g) {
        g.setFont(new Font("Gill Sans", 0, 14));
        if (state == 0) {
            g.setPaint(s_neutral);
            // g.fillRect((int)me.x, (int)me.y-height+5, width, height-5);
            g.fill(m_backgroundRect);
            g.setPaint(new Color(30,30,30, 75));
            g.drawString(m_label, me.x + 3, me.y - 3);
        } else {
            g.setPaint(s_blue);
            // g.fillRect((int)me.x, (int)me.y-height+5, width, height-5);
            g.fill(m_backgroundRect);

            g.setPaint(Color.WHITE);
            g.drawString(m_label, me.x + 3, me.y - 3);
            g.setPaint(Color.BLACK);
        }
    } */
}
