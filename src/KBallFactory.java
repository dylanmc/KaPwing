package com.mecodegoodsomeday.KaPwing;

import java.awt.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 1, 2004
 * Time: 2:30:36 PM
 * License: CC-SA
 */
public class KBallFactory implements KDrawable, KRunnable, KEditable {
    KPoint me;
    float m_vx = 0f;
    float m_vy = 0f;
    static float s_gx = 0.00000f;
    static float s_gy = 0.0002f;
    long m_nextTime;
    long m_interval;
    KSpace m_space;
    int m_state = 0;
    boolean[] m_pattern = null;
    int m_pLength;
    int m_pIndex;
    static KBallFactoryHandler s_factoryHandler;
    public static int s_maxInterval = 64;

    public KBallFactory(KPoint loc, KSpace s, long interval) {
        me = new KPoint(loc.x, loc.y);
        m_space = s;
        m_interval = interval;
        m_nextTime = System.currentTimeMillis();
        m_pattern = new boolean[s_maxInterval];
        m_pIndex = 0;
        m_pattern[0] = true;
        m_pattern[1] = true;
        m_pLength = 4;
    }

    public KEditHandler getEditHandler() {
        return s_factoryHandler;
    }

    public void draw(Graphics2D g) {
        switch (m_state) {
            case KaPwing.HOT:
                g.setPaint(Color.BLUE);
                g.drawLine((int)(me.x - 6), (int)(me.y - 6), (int)me.x, (int)me.y);
                g.drawLine((int)me.x, (int)me.y, (int)(me.x + 6), (int)(me.y - 6));
                g.setPaint(Color.BLACK);
                break;
            default:
                g.setPaint(Color.BLACK);
                g.drawLine((int)(me.x - 6), (int)(me.y - 6), (int)me.x, (int)me.y);
                g.drawLine((int)me.x, (int)me.y, (int)(me.x + 6), (int)(me.y - 6));
        }
    }
    public KPoint getPos() {
        return me;
    }
    public boolean intersectsWithPoint(KPoint p) {
        if (KSpace.lineLength(p, me) < 6) return true;
        return false;
    }
    public void setState(int state) {
        m_state = state;
    }

    public void run(long now) {
        if (now >= m_nextTime) {
            if (m_pattern[m_pIndex]) {
                injectBall();
            }
            m_pIndex = (m_pIndex + 1) % m_pLength;
            m_nextTime = now + m_interval;
        }
    }

    public void injectBall() {
        m_space.addBall(KBall.alloc((int)me.x, (int)me.y, s_gx, s_gy, m_vx, m_vy));
    }
}
