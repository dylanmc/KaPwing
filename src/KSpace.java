package com.mecodegoodsomeday.KaPwing;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Stack;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Oct 26, 2004
 * Time: 12:14:31 PM
 */

public class KSpace implements MouseListener, MouseMotionListener, KeyListener{
    long last_ts = 0;
    Vector m_balls = null;
    Vector m_collidables = null;
    Vector m_runnables = null;
    Vector m_drawables = null;

    KStack m_responders;

    int m_width;
    int m_height;

    int m_dx, m_dy; // mouse down
    int m_mx, m_my; // mouse move
    boolean m_drag = false;
    KPoint m_drag1, m_drag2;
    char m_cmd = 'x';

    boolean m_exit = false;

    KSoundManager m_soundManager;
    boolean m_liveTool = true; // whether the mouse-moved event can select something.
    KPoint m_mousePoint;
    KDrawable m_hotObj = null;
    boolean m_selectNote = false;
    boolean m_selectInstrument = false;
    int m_lastNote;
    int m_lastChannel;
    // let's show the top commands by default ...
    // KMainMenu m_mainMenu;
    KTopCommands m_topCommands;
    String m_msg = null;


    public KSpace() {
        last_ts = System.currentTimeMillis();
        m_balls = new Vector();
        m_collidables = new Vector();
        m_soundManager = new KSoundManager();
        m_runnables = new Vector();
        m_drawables = new Vector();
        m_mousePoint = new KPoint(0,0);
        // m_mainMenu = new KMainMenu(this);
        m_topCommands = new KTopCommands(this, null);
        m_topCommands.showMenu();
        m_responders = new KStack();
        KDefaultInputHandler defaultMouse = new KDefaultInputHandler(this);
        pushHandler(defaultMouse);
        KPaddle p = new KPaddle(200, 200, 300, 250);
        p.m_channel = 8;
        addObject(p);
        KBallFactory bf = new KBallFactory(new KPoint(250, 100), this, 250);
        addObject(bf);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void removeHandler(KEditHandler h) {
        if (m_responders.contains(h)) {
            m_responders.remove(h);
        }
    }
    public void pushHandler(KEditHandler h) {
        if (!m_responders.contains(h)) {
            m_responders.push(h);
        }
    }

    public void resetHandlerStack() {
        while (m_responders.size() > 1) {
            m_responders.pop();
        }
    }

    public void keyPressed(KeyEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.keyPressed(null, e))
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.keyReleased(null, e))
                break;
        }
    }

    public void mousePressed(MouseEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.mousePressed(null, e))
                break;
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.mouseReleased(null, e))
                break;
        }
    }
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.wantMouseDragEvents() && currentHandler.mouseDragged(null, e))
                break;
        }
    }
    public synchronized void mouseMoved(MouseEvent e) {
        for (Enumeration i = m_responders.elements(); i.hasMoreElements(); ) {
            KEditHandler currentHandler = (KEditHandler) i.nextElement();
            if (currentHandler.wantMouseMoveEvents() && currentHandler.mouseMoved(null, e))
                break;
        }
    }

    public void addObject(KObject o) {
        if (o instanceof KCollidable) {
            m_collidables.add(o);
        }
        if (o instanceof KDrawable) {
            m_drawables.add(o);
        }
        if (o instanceof KRunnable) {
            m_runnables.add(o);
        }
    }

    public void removeObject(KObject o) {
        if (o instanceof KCollidable) {
            m_collidables.remove(o);
        }
        if (o instanceof KDrawable) {
            m_drawables.remove(o);
        }
        if (o instanceof KRunnable) {
            m_runnables.remove(o);
        }
    }

    public void addBall(KBall b) {
        m_balls.add(b);
    }
    public void setDragLine(KPoint d1, KPoint d2) {
        m_drag1 = d1;
        m_drag2 = d2;
        m_drag = true;
    }
    public void clearDragLine() {
        m_drag = false;
        m_drag1 = m_drag2 = null;
    }
    public void runTimeStep(Graphics2D g) {
        long ts = System.currentTimeMillis();
        long deltat = ts - last_ts;
        last_ts = ts;
        // System.out.print("dt = " + deltat + " ");
        for (Enumeration e = m_runnables.elements(); e.hasMoreElements(); ) {
            KRunnable r = (KRunnable)e.nextElement();
            r.run(ts);
        }
        for (Enumeration e = m_drawables.elements(); e.hasMoreElements(); ) {
            KDrawable obj = (KDrawable) e.nextElement();
            obj.draw(g);
        }
        for (Enumeration e = m_balls.elements(); e.hasMoreElements(); ) {
            KBall ball = (KBall) e.nextElement();
            ball.updateVelocity(deltat);     // do gravityX first
            ball.updatePosition(deltat);
            if (ball.m_pos.y > m_height) {
                m_balls.remove(ball);
                KBall.free(ball);
                // manual memory mgmt goes here TODO
            } else {
                handleBallCollisions(ball);
                ball.draw(g);
            }
        }
        g.drawString("" + m_balls.size(), m_width - 20, 10);
        if (m_msg != null) {
            g.setPaint(Color.GRAY);
            g.drawString(m_msg, 200, m_height - 20);
        }
        if (m_drag) {
            g.setPaint(Color.BLACK);
            g.drawLine((int)m_drag1.x, (int)m_drag1.y, (int)m_drag2.x, (int)m_drag2.y);
        }
    }

    public void setMsg(String msg) {
        m_msg = msg;
    }

    // given the trajectory from b.m_oldPos to b.m_pos, compute whether there's a collision,
    // and if so, adjust b's position, and fire any events that it caused
    public void handleBallCollisions(KBall b) {
        for (Enumeration e = m_collidables.elements(); e.hasMoreElements(); ) {
            KCollidable obj = (KCollidable) e.nextElement();
            obj.handleCollision(b, this);
        }
    }

    public void mainLoop(ScreenManager screen) {
        // m_balls.add(new KBall(100,20));
        m_width = screen.getWidth();
        m_height = screen.getHeight();
        Window w = screen.getFullScreenWindow();
        w.addMouseListener(this);
        w.addMouseMotionListener(this);
        w.addKeyListener(this);

        while (!m_exit) {
            try {
                long start = System.currentTimeMillis();
                Graphics2D g = screen.getGraphics();
                // draw to g
                screen.clear();
                runTimeStep(g);
                g.dispose();
                screen.update();
                long endts = System.currentTimeMillis();
                long delta = endts - start;
                // System.out.print("drawtime = " + delta + " ");
                long sleepmillis = 20 - delta;
                if (sleepmillis > 0)
                    Thread.sleep(sleepmillis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    // ------------------- static geometric utility functions ----------------------

    // TODO: this is both wrong and slow.  Fixit!
    public static boolean rectOverlap(KPoint a1, KPoint a2, KPoint b1, KPoint b2) {
        float midax = (a1.x + a2.x)/2;
        float miday = (a1.y + a2.y)/2;
        float midbx = (b1.x + b2.x)/2;
        float midby = (b1.y + b2.y)/2;
        float dx = midax - midbx;
        float dy = miday - midby;
        float distSquared = (dx * dx) + (dy * dy);

        float aw = (a2.x - a1.x);
        float ah = (a2.y - a1.y);
        float bw = (b2.x - b1.x);
        float bh = (b2.y - b1.y);

        float aradius = (float) Math.sqrt(aw * aw + ah * ah) / 2;
        float bradius = (float) Math.sqrt(bw * bw + bh * bh) / 2;

        if (distSquared <= aradius + bradius) return true;
        return false;
    }

    // use sutherland's clipping algorithm on this one...
    public static boolean linesIntersect(KPoint a1, KPoint a2, KPoint b1, KPoint b2, KPoint i) {


        float denom = (b2.y - b1.y)*(a2.x - a1.x) - (b2.x - b1.x)*(a2.y - a1.y);
        if (denom == 0) return false;   // parallel lines
        float uanum = (b2.x - b1.x)*(a1.y - b1.y) - (b2.y - b1.y)*(a1.x - b1.x);
        float ubnum = (a2.x - a1.x)*(a1.y - b1.y) - (a2.y - a1.y)*(a1.x - b1.x);

        float ua = uanum / denom;
        float ub = ubnum / denom;

        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            i.x = a1.x + ua * (a2.x - a1.x);
            i.y = a1.y + ua * (a2.y - a1.y);    // whoops, had ub...symmetry be damned!
            return true;
        }

        return false;
    }

    public static float lineLength(KPoint a, KPoint b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    // returns absolute paddleAngle of a line segment in radians
    public static float lineAngle(KPoint a, KPoint b) {
        float dx = b.x - a.x;
        float len = lineLength(a,b);
        float rawAngle = (float) Math.acos(dx/len);
        int sign = sign((int)(a.y-b.y));  // note this is backwards - well, native coordinates are in quadrant 4
        float flip = 0;
        if (sign < 0) flip = 2 * (float) Math.PI;
        return flip + sign * rawAngle;
    }

    public static int sign(int a) {
        if (a > 0) return 1;
        return -1;
    }
    public static void truncateLine(KPoint a, KPoint b, float length) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        float l2 = length * length;
        float t2 = dx * dx + dy * dy;
        if (t2 > l2) {
            float ratio = length / (float)Math.sqrt(t2);
            b.x = a.x + ratio * dx;
            b.y = a.y + ratio * dy;
        }
    }
}
