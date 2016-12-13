package com.mecodegoodsomeday.KaPwing;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 8, 2004
 * Time: 10:51:16 AM
 */

// this is a ball factory that produces balls upon incoming midi events (e.g., a "live" MIDI keyboard).
// also, it makes a noise each time a ball is produced.
// someday, when we have tone-balls, it should be able to produce tone balls, so that their bounces echo the original notes.
public class KLiveFactory extends KPaddle {
    KPoint me;
    static KLiveFactoryHandler s_liveFactoryHandler = null;

    public KLiveFactory(int nx, int ny) {
        super(nx, ny, nx, ny);
        me = new KPoint(nx, ny);
    }

    public KEditHandler getEditHandler() {
        return s_liveFactoryHandler;    
    }

    public void draw(Graphics2D g) {
        switch (m_state) {
            case KaPwing.HOT:
                g.setPaint(Color.ORANGE);
                g.drawLine((int)(me.x - 6), (int)(me.y - 6), (int)me.x, (int)me.y);
                g.drawLine((int)me.x, (int)me.y, (int)(me.x + 6), (int)(me.y - 6));
                break;
            default:
                g.setPaint(Color.GREEN);
                g.drawLine((int)(me.x - 6), (int)(me.y - 6), (int)me.x, (int)me.y);
                g.drawLine((int)me.x, (int)me.y, (int)(me.x + 6), (int)(me.y - 6));
        }
    }
    public boolean intersectsWithPoint(KPoint p) {
        if (p.x >= x1 -7 && p.x <= x1 + 7 && p.y >= y1 - 7 && p.y <= y1 + 7) {
            return true;
        }
        return false;
    }
    public void handleCollision(KBall o, KSpace s)
    {
        return;
    }
    public KPoint getPos() {
        return me;
    }

}
