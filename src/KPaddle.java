package com.mecodegoodsomeday.KaPwing;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Oct 26, 2004
 * Time: 12:12:32 PM
 */
public class KPaddle implements KCollidable, KDrawable, KEditable {
    int x1, y1, x2, y2;
    KPoint p1, p2, mp;
    KPoint i;
    float paddleAngle;
    float risePerUnitRun;
    int paddleNote;
    String label = null;
    static int s_channel = 0;
    static int s_lastNote = 60;
    int m_channel = 0;
    int m_state = 0;
    int m_altState = 0;
    boolean m_drag = true;
    boolean m_move = false;
    boolean m_vertical = false;
    boolean m_horizontal = false;
    static KPaddleModifier s_paddleModifierSingleton;
    KSound m_soundDevice;

    public KEditHandler getEditHandler() {
        return s_paddleModifierSingleton;    
    }

    public KPaddle(int nx1, int ny1, int nx2, int ny2) {
        p1 = new KPoint(0,0);
        p2 = new KPoint(0,0);
        i = new KPoint(0,0);
        mp = new KPoint(0,0);
        m_channel = s_channel;
        paddleNote = s_lastNote;
        init (nx1, ny1, nx2, ny2);
        m_soundDevice = KSoundManager.getSoundDevice(0);
    }

    // inits a paddle
    // returns true if pt 1 and pt 2 remain the same, false if we swapped 'em
    boolean init (int nx1, int ny1, int nx2, int ny2) {
        m_horizontal = false;
        m_vertical = false;
        risePerUnitRun = 0;
        boolean ret = true;
        if (nx1 <= nx2) {
            x1 = nx1;
            y1 = ny1;
            x2 = nx2;
            y2 = ny2;
        } else {
            x1 = nx2;
            y1 = ny2;
            x2 = nx1;
            y2 = ny1;
            ret = false;
        }
        if (x1 != x2) {
            risePerUnitRun = ((float)(y2 - y1) / (float)(x2 - x1));
        } else {
            m_vertical = true;
        }
        if (y1 == y2) {
            m_horizontal = true;
        }
        p1.x = x1;
        p1.y = y1;
        p2.x = x2;
        p2.y = y2;
        mp.x = (x2 + x1)/2;
        mp.y = (y2 + y1)/2;
        paddleAngle = KSpace.lineAngle(p1, p2);
        return ret;
    }
    public boolean intersectsWithPoint(KPoint p) {
        if (m_horizontal) {
            if ((Math.abs(p.y - y1) < 7)) return true;
        } else if (m_vertical) {
            if ((Math.abs(p.x - x1) < 7)) return true;
        } else if (p.x >= x1 -7 && p.x <= x2 + 7) {
            float testy = y1 + risePerUnitRun * (p.x - x1);
            // label = "testy = " + testy + ", y = " + p.y;
            if (Math.abs(testy - p.y) < 7) {
                return true;
            }
        }
        return false;
    }
    public void setState(int state) {
        m_state = state;
    }
    // check if KBall has collided with us by going from pt1 to pt2
    public void handleCollision(KBall o, KSpace s)
    {
         if (KSpace.linesIntersect(p1, p2, o.m_oldPos, o.m_pos, i) ) {
             // we have a collision at point i. Now do some trig to calculate the ball's new position...

             float ricoLength = KSpace.lineLength(i, o.m_pos);
             float ballAngle  = KSpace.lineAngle(o.m_oldPos, i);
             float ricoAngle  = (paddleAngle - ballAngle) + paddleAngle;
             if (ricoAngle < 0) {
                 ricoAngle += Math.PI * 2;
             }
             float cosRicoAngle = (float) Math.cos(ricoAngle);
             float sinRicoAngle = (float) Math.sin(ricoAngle);
             float newX = i.x + ricoLength * cosRicoAngle;
             float newY = i.y - (ricoLength * sinRicoAngle);   // note quadrant 4
             o.m_pos.x = newX;
             o.m_pos.y = newY;
             o.m_bouncePos.x = i.x;
             o.m_bouncePos.y = i.y;
             o.m_bounce = true;

             float velocityMagnitude = (float) Math.sqrt((o.m_v.m_elt[0] * o.m_v.m_elt[0]) +
                     (o.m_v.m_elt[1] * o.m_v.m_elt[1]));

             o.m_v.m_elt[0] = velocityMagnitude * cosRicoAngle;
             o.m_v.m_elt[1] = velocityMagnitude * sinRicoAngle * -1;
             // label = "ballAngle = " + Math.toDegrees(ballAngle) + ", ricoAngle = " + Math.toDegrees(ricoAngle);
             fireEvent(o, s);

         }
    }
    // make noise or whatever based on colliding with this ball
    public void fireEvent(KBall o, KSpace s)
    {
        s.m_soundManager.playNoteWithDuration(m_soundDevice, paddleNote, 400 /* duration */, m_channel, 93 /* velocity */);
    }

    public int paddleToNote() {
        return paddleNote;
    }

    public int ballToVelocity(KBall o) {
        return Math.min((int)(o.m_v.m_elt[0] * o.m_v.m_elt[1] * 50) % 256, 50);
    }

    public void draw(Graphics2D g) {
        if (label != null) g.drawString(label, 200, 30);
        switch (m_state) {
            case KaPwing.HOT:
                g.setPaint(Color.BLUE);
                g.drawLine(x1, y1, x2, y2);
                g.drawRect((int)mp.x, (int)mp.y, 2, 2);
                g.setPaint(Color.BLACK);

                break;
            default:
                g.setPaint(Color.BLACK);
                g.drawLine(x1, y1, x2, y2);
                // g.drawRect(x1, y1, 2, 2);
                // g.drawString("" + Math.toDegrees(paddleAngle) + ", " + label, x2, y2);
        }
    }

    public KPoint getPos() {
        return p1;
    }

}
