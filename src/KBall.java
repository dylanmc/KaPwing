package com.mecodegoodsomeday.KaPwing;

import java.awt.*;
import java.util.Vector;

public class KBall implements KDrawable {
	KPoint m_pos;
    KPoint m_oldPos;
    KPoint m_bouncePos;
    Rectangle m_shape;
	KVector m_v;    // expressed in pixels per millisecond
    float gravityY = 0.0004f;
    float gravityX = 0f;
    boolean m_bounce = false;
    static Rectangle s_rect = new Rectangle(3,3);
    static Vector s_freeBalls = new Vector();

    public static void free(KBall b) {
        s_freeBalls.insertElementAt(b, 0);
    }
    public static KBall alloc(int x, int y, float gx, float gy, float vx, float vy) {
        KBall ret = null;
        if (s_freeBalls.size() > 0) {
            ret = (KBall) s_freeBalls.remove(0);
            ret.m_pos.x = x;
            ret.m_pos.y = y;
            ret.m_oldPos.x = x;
            ret.m_oldPos.y = y;
            ret.m_shape = s_rect;
            ret.m_v.m_elt[0] = vx;
            ret.m_v.m_elt[1] = vy;
            ret.gravityX = gx;
            ret.gravityY = gy;
            // ret.m_bouncePos = new KPoint (0,0);
        } else {
            ret = new KBall(x, y, gx, gy, vx, vy);
        }
        return ret;
    }

    public void setState(int s) {
    }

    public KPoint getPos() {
        return m_pos;
    }

    public boolean intersectsWithPoint(KPoint p) {
        return false;
    }

    public KBall(int x, int y) {
        m_pos = new KPoint(x, y);
        m_oldPos = new KPoint(x, y);
        m_shape = s_rect;
        m_v = new KVector(0.0f,0.0f);
        m_bouncePos = new KPoint (0,0);
    }

    public KBall(int x, int y, float gx, float gy, float vx, float vy) {
        m_pos = new KPoint(x, y);
        m_oldPos = new KPoint(x, y);
        m_shape = s_rect;
        m_v = new KVector(vx, vy);
        gravityX = gx;
        gravityY = gy;
        m_bouncePos = new KPoint (0,0);

    }

	public void draw(Graphics2D g) {
        // m_shape.setLocation(m_pos.getXint(), m_pos.getYint());
        //g.draw(m_shape);
        g.setColor(Color.BLACK);
        if (!m_bounce) {
            g.drawLine((int)m_oldPos.x,  (int)m_oldPos.y, (int)m_pos.x, (int)m_pos.y);
        } else {
            g.drawLine((int)m_oldPos.x,  (int)m_oldPos.y, (int)m_bouncePos.x, (int)m_bouncePos.y);
            g.setPaint(Color.WHITE);
            g.drawLine((int)m_bouncePos.x,  (int)m_bouncePos.y, (int)m_pos.x, (int)m_pos.y);
            m_bounce = false;
            g.setPaint(Color.BLACK);
            //g.drawLine((int)m_bouncePos.x, (int)m_bouncePos.y,
            //        (int)(m_bouncePos.x + m_v.m_elt[0]), (int)(m_bouncePos.y + m_v.m_elt[1]));
        }
        // g.drawString(m_pos.x + "," + m_pos.y, m_pos.x, m_pos.y);
	}

    public void updateVelocity(long deltat)
    {
        m_v.m_elt[1] += (gravityY * deltat);
        m_v.m_elt[0] += (gravityX * deltat);
    }

    public void updatePosition(long deltat)
    {
        m_oldPos.copyFrom(m_pos);
        m_pos.addVectorByTime(m_v, deltat);
    }
}
