package com.mecodegoodsomeday.KaPwing;

import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 4:39:54 PM
 * License: CC-SA
 */

public class KBallFactoryHandler extends KGenericHandler {
    KPoint m_mousePoint;
    int m_mode;
    KBallFactory m_bf;
    KPoint mi = null;

    final static int NEW_FACTORY_MODE = 1;
    final static int MOVE_MODE = 2;
    // other things we want to control about ball-factories:
    final static int GRAVITY_MODE = 3;
    final static int INTERVAL_MODE = 4; // number of items in a cycle, duration of cycle, on/off/on...
    final static int MENU_MODE = 5;
    final static int FREQUENCY_MODE = 6;
    final static int VELOCITY_MODE = 7;

    final static int EDITING_GRAVITY = 11;
    final static int EDITING_VELOCITY = 12;

    final static float GRAV_CONST = 10000f;
    final static float VELOCITY_CONST = 3.5f;

    KBallFactoryMenu m_bfMenu = null;


    public KBallFactoryHandler(KSpace s) {
        m_space = s;
        m_drag = false;
        m_move = false;
        m_mousePoint = new KPoint(0,0);
        m_bfMenu = new KBallFactoryMenu(s);
        mi = new KPoint(0,0);
    }
    public boolean mousePressed(KEditable o, MouseEvent e) {
        mi.x = e.getX();
        mi.y = e.getY();
        if (o == null) {
            if (m_bf == null) {
                m_mode = NEW_FACTORY_MODE;
                // System.out.println("null object, no cbf in mouse pressed BFH");
                return true;
            } else {
                return false;
            }
        }
        if (! (o instanceof KBallFactory)) {
            return false;
        } else {
            m_bf = (KBallFactory) o;
            System.out.println("mouse pressed ball factory mode " + e);

            if (rightClick(e)) { // right-click summons menu
                m_bfMenu.setHotObj(m_bf);
                m_mode = MENU_MODE;
                m_bfMenu.showMenu(mi);
            } else {
                m_mode = MOVE_MODE;
            }
        }
        // System.out.println("mouse pressed, mode = " + m_mode);
        return true;
    }

    public boolean mouseReleased(KEditable o, MouseEvent e) {
        // System.out.println("mouse released, mode = " + m_mode);
        if (m_mode == NEW_FACTORY_MODE) {
            // System.out.println("creating factory");
            m_mousePoint.x = e.getX();
            m_mousePoint.y = e.getY();
            KBallFactory newbf = new KBallFactory(m_mousePoint, m_space, 1000);
            m_space.addObject(newbf);
            m_space.removeHandler(this);
            m_mode = 0;
        } else if (m_mode == MENU_MODE) {

        } else if (m_mode == MOVE_MODE) {
            m_bf.me.x = e.getX();
            m_bf.me.y = e.getY();
        }
        return true;
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        return false;
    }

    public boolean mouseDragged(KEditable o, MouseEvent e) {
        if (m_mode == MOVE_MODE) {
            m_bf.me.x = e.getX();
            m_bf.me.y = e.getY();
        }
        return true;
    }

    public boolean wantMouseDragEvents() {
        return (m_mode == MOVE_MODE);
    }

    //
    class KBallFactoryMenu extends KMenu {
        KBallFactory m_currFactory = null;
        KMenuItem m_lastSelected;
        KIntervalMenu m_intervalMenu;
        KFrequencyMenu m_freqMenu;
        KVectorSelector m_vectorSelector;
        KPoint m_p;

        public KBallFactoryMenu(KSpace s) {
            m_items = new Vector();
            int x = 0;
            int ypos = 0;
            m_items.add(new KMenuItem(this, x, ypos += 20, 75, 20,  "x"));
            m_items.add(new KMenuItem(this, x, ypos += 20, 75, 20,  "interval"));
            m_items.add(new KMenuItem(this, x, ypos += 20, 75, 20,  "frequency"));
            // m_items.add(new KMenuItem(this, x, ypos += 20, 75, 20,  "gravity"));
            m_items.add(new KMenuItem(this, x, ypos += 20, 75, 20,  "direction"));
            m_space = s;
            m_intervalMenu = new KIntervalMenu(s);
            m_freqMenu = new KFrequencyMenu(s);
            m_vectorSelector = new KVectorSelector(s);
            m_p = new KPoint (0,0);
        }
        public void setHotObj(KObject o) {
            KBallFactory p = (KBallFactory) o;
            m_currFactory = p;
        }
        public void menuItemHit(KMenuItem i, boolean mouseReleased) {
            m_currentItem = i;
            // do something good
            if (i != m_lastSelected) {
                m_lastSelected = i;
            }
            if (i.m_label.startsWith("x")) {
                System.out.println("closing factory popup menu");
                m_intervalMenu.hideMenu();
                m_freqMenu.hideMenu();
                m_vectorSelector.hide();
                hideMenu();
            } else if (i.m_label.startsWith("gravity")) {
                m_mode = GRAVITY_MODE;
                m_vectorSelector.setVectorMode(m_currFactory, EDITING_GRAVITY);
                m_vectorSelector.toggleView(m_currFactory.me);
            } else if (i.m_label.startsWith("direction")) {
                m_vectorSelector.setVectorMode(m_currFactory, EDITING_VELOCITY);
                m_mode = VELOCITY_MODE;
                m_vectorSelector.toggleView(m_currFactory.me);
            } else if (i.m_label.startsWith("interval")) {
                if (!m_intervalMenu.m_showing) { // toggle
                    m_mode = INTERVAL_MODE;
                    m_intervalMenu.setHotObj(m_currFactory);
                    m_p.copyFrom(m_currFactory.me);
                    m_p.y += 20;
                    m_p.x += 140;
                    m_intervalMenu.showMenu(m_p);
                } else {
                    m_intervalMenu.hideMenu();
                }
            } else if (i.m_label.startsWith("frequency")) {
                if (!m_freqMenu.m_showing) { // toggle
                    m_mode = FREQUENCY_MODE;
                    m_freqMenu.setHotObj(m_currFactory);
                    m_p.copyFrom(m_currFactory.me);
                    m_p.y += 40;
                    m_p.x += 140;
                    m_freqMenu.showMenu(m_p);
                } else {
                    m_freqMenu.hideMenu();
                }
            }
            /*
            if (mouseReleased) {
                hideMenu();
            } */
        }
        public void showMenu(KPoint p) {
            int ypos = p.getYint();

            for (Enumeration e = m_items.elements(); e.hasMoreElements(); ) {
                KMenuItem i = (KMenuItem)e.nextElement();
                i.moveItemTo(p.getXint() + 60, ypos);
                ypos += 20;
            }
            super.showMenu();
        }
        public void draw(Graphics2D g) {

        }
    }

    class KIntervalMenu extends KMenu {
        KBallFactory m_currFactory = null;
        KMenuItem m_lastSelected;
        KMenuItem[] m_itemArray;
        KMenuItem m_plusItem;
        KMenuItem m_minusItem;
        KPoint m_psav;

        public KIntervalMenu(KSpace s) {
            m_space = s;
            m_itemArray = new KMenuItem[KBallFactory.s_maxInterval];
            for (int i = 0; i < KBallFactory.s_maxInterval; i++) {
                m_itemArray[i] = new KMenuItem(this, 0,0,10, 20, null);
                m_itemArray[i].m_id = i;
            }
            m_plusItem = new KMenuItem(this, 0,0,10, 20, "+");
            m_plusItem.m_id = -1;
            m_minusItem = new KMenuItem(this, 0,0,10,20, "-");
            m_minusItem.m_id = -2;
            m_psav = new KPoint(0,0);
        }
        public void setHotObj(KObject o) {
            KBallFactory p = (KBallFactory) o;
            m_currFactory = p;
            System.out.println("setting hot obj " + p);
        }
        public void menuItemHit(KMenuItem i, boolean mouseReleased) {
            m_currentItem = i;
            // do something good
            if (i != m_lastSelected) {
                m_lastSelected = i;
            }
            if (i.m_id >= 0) {
                m_currFactory.m_pattern[i.m_id] = ! m_currFactory.m_pattern[i.m_id];
                int state = 0;
                if (m_currFactory.m_pattern[i.m_id]) {
                    state = 2;
                }
                i.setAltState(state);
            } else {
                // adjusting interval length
                if (i.m_id == -1) {
                    // growing
                    if (m_currFactory.m_pLength < KBallFactory.s_maxInterval - 1) {
                        hideMenu();
                        m_currFactory.m_pLength++;
                        showMenu(m_psav);
                    }
                } else {
                    // shrinking
                    if (m_currFactory.m_pLength > 1) {
                        hideMenu();
                        m_currFactory.m_pLength--;
                        showMenu(m_psav);
                    }
                }
            }
            /* if (mouseReleased) {
                hideMenu();
            } */
        }
        public void draw(Graphics2D g) {

        }
        public void showMenu(KPoint p) {
            m_psav.copyFrom(p);
            if (!m_showing) {
                m_showing = true;
                int xpos = p.getXint();
                m_minusItem.moveItemTo(xpos, p.getYint());
                xpos += 12;
                m_space.addObject(m_minusItem);
                m_plusItem.moveItemTo(xpos, p.getYint());
                m_space.addObject(m_plusItem);
                xpos += 12;

                for (int i = 0; i < m_currFactory.m_pLength; i++) {
                    KMenuItem e = m_itemArray[i];
                    int state = 0;
                    if (m_currFactory.m_pattern[i]) {
                        state = 2;
                    }
                    e.setAltState(state);
                    e.moveItemTo(xpos, p.getYint());
                    xpos += 12;
                    m_space.addObject(e);
                }
            }
            m_space.addObject(this);
        }

        public void hideMenu() {
            if (m_showing) {
                m_showing = false;
                for (int i = 0; i < m_currFactory.m_pLength; i++) {
                    KMenuItem e = m_itemArray[i];
                    m_space.removeObject(e);
                }
                m_space.removeObject(m_plusItem);
                m_space.removeObject(m_minusItem);
                m_space.removeObject(this);
            }

        }
    }

    class KFrequencyMenu extends KMenu {
        KPoint m_psav;
        KBallFactory m_currFactory = null;
        KMenuItem m_lastSelected;

        public KFrequencyMenu(KSpace s) {
            m_items = new Vector();
            m_space = s;
            int period = 64;
            for (int i = 0; i < 7; i++) {
                KMenuItem e = new KMenuItem(this, 0,0,20, 20, "1/" + period);
                e.m_id = period;
                period = period >> 1;
                m_items.add(e);
            }
            m_psav = new KPoint(0,0);
        }

        public void setHotObj(KObject o) {
            KBallFactory p = (KBallFactory) o;
            m_currFactory = p;
            System.out.println("setting hot obj " + p);
        }
        public void showMenu(KPoint p) {
            m_psav.copyFrom(p);
            int y = p.getYint();
            for (Enumeration e = m_items.elements(); e.hasMoreElements(); )   {
                KMenuItem i = (KMenuItem) e.nextElement();
                i.moveItemTo(p.getXint(), y);
                y += 20;
            }

            super.showMenu();
        }

        public void menuItemHit(KMenuItem i, boolean mouseReleased) {
            i.setState(1);
            m_currentItem = i;
            if (i.m_id >= 0) {
                m_currFactory.m_interval = (1000 / i.m_id);
            }
        }
    }

    class KVectorSelector extends KGenericHandler implements KDrawable, KEditable {
        KPoint m_pos;
        int m_state;
        boolean m_showing = false;
        int radius = 50;
        int diameter = radius * 2;
        KPoint m_vector;
        KPoint m_deltavector;
        KPoint m_tmp;
        int m_editing;
        KBallFactory m_cbf;


        public KVectorSelector(KSpace s) {
            m_space = s;
            m_pos = new KPoint(0,0);
            m_vector = new KPoint (0,0);
            m_deltavector = new KPoint(0,0);
            m_drag = true;
            m_tmp = new KPoint(0,0);
        }

        public void setVectorMode(KBallFactory bf, int editing) {
            if (m_editing == EDITING_GRAVITY) {
                m_vector.x = bf.s_gx * GRAV_CONST;
                m_vector.y = bf.s_gy * GRAV_CONST;
            } else {
                m_vector.x = bf.m_vx * VELOCITY_CONST;
                m_vector.y = bf.m_vy * VELOCITY_CONST;
            }
            m_editing = editing;
            m_cbf = bf;
        }

        public KEditHandler getEditHandler() {
            return this;
        }


        public boolean mouseReleased(KEditable o, MouseEvent e) {
            adjustFactory();
            hide();
            return true;
        }

        public void adjustFactory() {
            float len = KSpace.lineLength(KPoint.ZeroPoint, m_vector);
            // float ratio = len / radius;
            m_deltavector.x = m_vector.x / radius;
            m_deltavector.y = m_vector.y / radius;
            // System.out.println("Yo! " + m_vector.x + ", " + m_vector.y);
            if (m_editing == EDITING_GRAVITY) {
                m_cbf.s_gx = (float)(m_deltavector.x / GRAV_CONST);
                m_cbf.s_gy = (float)(m_deltavector.y / GRAV_CONST);
            } else {
                m_cbf.m_vx = (float)(m_deltavector.x / VELOCITY_CONST);
                m_cbf.m_vy = (float)(m_deltavector.y / VELOCITY_CONST);
                // editing velocity
            }
        }

        public boolean mouseDragged(KEditable o, MouseEvent e) {
            // System.out.println("vector editor dragged");
            int x = e.getX();
            int y = e.getY();
            m_tmp.x = x;
            m_tmp.y = y;
            KSpace.truncateLine(m_pos, m_tmp, radius);
            m_vector.copyFrom(m_tmp);
            m_vector.x -= m_pos.x;
            m_vector.y -= m_pos.y;
            adjustFactory();
            return true;
        }
        public void toggleView(KPoint p) {
            if (!m_showing) {
                moveTo(p);
                show();
            } else {
                hide();
            }
        }
        public void show() {
            if (!m_showing) {
                m_showing = true;
                m_space.addObject(this);
                m_space.pushHandler(this);
            }
        }

        public void hide() {
            if (m_showing) {
                m_showing = false;
                m_space.removeObject(this);
                m_space.removeHandler(this);
            }
        }

        public void moveTo(KPoint p) {
            m_pos.copyFrom(p);
        }
        public KPoint getPos() {
            return m_pos;
        }

        public void draw (Graphics2D g) {
            g.setPaint(KMenuItem.s_neutral);
            g.fillOval((int)m_pos.x - radius, (int)m_pos.y - radius, diameter, diameter);
            g.setPaint(Color.black);
            g.drawRect((int)(m_pos.x + m_vector.x), (int)(m_pos.y + m_vector.y), 2, 2);
        }

        public void setState(int s) {
            m_state = s;
        }

        public boolean intersectsWithPoint (KPoint p) {
            return true;
        }
    }
}

