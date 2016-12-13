package com.mecodegoodsomeday.KaPwing;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 1, 2004
 * Time: 9:00:48 PM
 */
public class KMenuItem implements KDrawable {
    KPoint me;
    String m_label;
    int m_id;
    int width, height;
    int state = 0;
    KMenu m_parent;
    public boolean m_active = true;   // inactive menu items are really just labels.
    RoundRectangle2D.Float m_backgroundRect = null;
    static Color s_neutral = new Color(200,200,200, 75);
    static Color s_unselectedColor = new Color(30,30,30, 75);
    static Font s_menuFont = new Font("Gill Sans", 0, 14);
    TextLayout m_layout = null;
    static final int s_border = 3;
    int m_altState = 0;

    public KMenuItem(KMenu parent, int x, int y, int w, int h, String label, boolean active) {
        me = new KPoint(x, y);
        m_label = label;
        width = w;
        height = h;
        m_parent = parent;
        m_active = active;
        m_backgroundRect = new RoundRectangle2D.Float(me.x, me.y-height + 5,  w, h-5, 6.0f, 6.0f);
    }

    public KMenuItem(KMenu parent, int x, int y, int w, int h, String label) {
        me = new KPoint(x, y);
        m_label = label;
        width = w;
        height = h;
        m_parent = parent;
        m_backgroundRect = new RoundRectangle2D.Float(me.x, me.y-height + 5,  w, h-5, 6.0f, 6.0f);
    }
    public void moveItemTo(int x, int y) {
        me.x = x;
        me.y = y;
        m_backgroundRect = new RoundRectangle2D.Float(me.x, me.y-height + 5,  width, height-5, 6.0f, 6.0f);
    }

    public KPoint getPos() {
        return me;
    }

    public void draw (Graphics2D g) {
        g.setPaint(s_neutral);
        if (m_layout == null && m_label != null) {
            FontRenderContext frc = g.getFontRenderContext();

            m_layout = new TextLayout(m_label, s_menuFont, frc);
            Rectangle2D bounds = m_layout.getBounds();
            float nwidth = (float)bounds.getWidth();
            width = (int)nwidth + s_border + s_border;
            m_backgroundRect = new RoundRectangle2D.Float(me.x, me.y-height + 6,  width, height - 6, 6.0f, 6.0f);
            m_parent.updateRectangle((int)me.x, (int)(me.y-height-s_border), width, height);
        }
        // g.fillRect((int)me.x, (int)me.y-height+5, width, height-5);
        if (m_active && m_parent.m_currentItem == this) {
            g.fill(m_backgroundRect);
            g.setPaint(Color.GREEN);
            if (m_layout != null)
                m_layout.draw(g, me.x + 3, me.y - 3);
            // g.drawString(m_label, me.x + 3, me.y - 3);
        } else if (state == 0) {
            g.fill(m_backgroundRect);
            g.setPaint(Color.BLACK);
            if (m_layout != null)
                m_layout.draw(g, me.x + 3, me.y - 3);

            // g.drawString(m_label, me.x + 3, me.y - 3);
        }  else {
            g.fill(m_backgroundRect);
            g.setPaint(Color.WHITE);
            if (m_layout != null)
                m_layout.draw(g, me.x + 3, me.y - 3);
            // g.drawString(m_label, me.x + 3, me.y - 3);
        }
        if (m_altState == 2) {
            g.setPaint(Color.GRAY);
            g.fill(m_backgroundRect);
            if (m_layout != null)
                m_layout.draw(g, me.x + 3, me.y - 3);

            // g.drawString(m_label, me.x + 3, me.y - 3);
        }
    }

    public boolean intersectsWithPoint(KPoint p) {
        if (p.x > me.x && p.x < me.x + width) {
            if (p.y < me.y && p.y > me.y - height) {
                return true;
            }
        }
        return false;
    }

    public void setState (int s) {
        if (m_active) {
            state = s;
        }
    }

    public void setAltState(int s) {
        m_altState = s;
    }
}
