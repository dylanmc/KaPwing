package com.mecodegoodsomeday.KaPwing;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 4:58:48 PM
 */

// KEditModeHandler demuxes mouse events sent to it by KSpace to the individual Editable objects' own handlers
public class KEditModeHandler extends KGenericHandler {
    KPoint m_mousePoint = null;
    KEditable m_hotObj = null;
    KEditHandler m_currHandler = null;

    public KEditModeHandler(KSpace s) {
        m_space = s;
        m_move = true;
        m_drag = false; // this is dynamic, though
        m_mousePoint = new KPoint(0,0);
        KPaddle.s_paddleModifierSingleton = new KPaddleModifier(s, this);
        KBallFactory.s_factoryHandler = new KBallFactoryHandler(s);
        KLiveFactory.s_liveFactoryHandler = new KLiveFactoryHandler(s);
    }

    public boolean mousePressed(KEditable o, MouseEvent e) {
        if (m_currHandler != null) {
            m_currHandler.mousePressed(m_hotObj, e);
        }
        return false;
    }

    public boolean mouseReleased(KEditable o, MouseEvent e) {
        // System.out.println("KEditModeHandler mouse released..." + m_hotObj + o);
        if (m_currHandler != null) {
            m_currHandler.mouseReleased(m_hotObj, e);
        }

        return false;
    }
    public boolean mouseDragged(KEditable o, MouseEvent ev) {
        if (m_currHandler != null) {
            m_currHandler.mouseDragged(m_hotObj, ev);
        }

        return false;
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        m_mousePoint.x = ev.getX();
        m_mousePoint.y = ev.getY();
        KDrawable oldHotObj = (KDrawable)m_hotObj;
        m_hotObj = null;
        for (Enumeration e = m_space.m_drawables.elements(); e.hasMoreElements(); ) {
            KDrawable obj = (KDrawable) e.nextElement();
            // TODO: BSP tree if this bogs us down.
            if ((obj instanceof KLiveMenuItem) || (obj instanceof KEditable)) {
                // ok
            } else {
                continue;
            }
            if (obj.intersectsWithPoint(m_mousePoint)) {
                if (obj instanceof KEditable) {
                    obj.setState(KaPwing.HOT);
                    m_hotObj = (KEditable)obj;
                } else { // live menu item
                    m_hotObj = (KEditable)oldHotObj;    // don't want the menu item to be the hot obj
                    KLiveMenuItem lm = (KLiveMenuItem) obj;
                    obj.setState(KaPwing.HOT);
                    lm.m_parent.menuItemHit(lm, false);
                }
            } else {
                obj.setState(0);
            }
            if (m_hotObj != null && m_hotObj != oldHotObj) { // new hot object, so reset the handler
                KEditHandler h = m_hotObj.getEditHandler();
                m_currHandler = h;
                h.resetHandler();
            }
            if (m_hotObj == null && oldHotObj != null) {
                oldHotObj.setState(0);
            }
        }
        return false;
    }

    public boolean keyPressed(KEditable o, KeyEvent e) {
        if (m_hotObj != null) {
            KEditHandler h = m_hotObj.getEditHandler();
            return h.keyPressed(m_hotObj, e);
        }
        return false;
    }

    public boolean keyReleased(KEditable o, KeyEvent e) {
        if (e.getKeyCode() == 8) { // delete
            if (m_hotObj != null) {
                m_space.removeObject((KObject)m_hotObj);
                m_hotObj = null;
            }
            return true;
        } else {
            if (m_hotObj != null) {
                KEditHandler h = m_hotObj.getEditHandler();
                return h.keyReleased(m_hotObj, e);
            }
        }
        return false;
    }
    public boolean wantMouseDragEvents() {
        if (m_currHandler != null) {
            return m_currHandler.wantMouseDragEvents();
        }
        return m_drag;
    }
    public boolean wantMouseMoveEvents() {
        return true;
    }
}
