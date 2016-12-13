package com.mecodegoodsomeday.KaPwing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 4, 2004
 * Time: 2:15:37 PM
 */

public class KPaddleModifier implements KEditHandler {

    KPoint mi;
    int m_editMode = 0;
    int ox1, ox2, oy1, oy2; // basis for moving
    boolean m_editP1 = false;
    KSpace m_space;
    KPaddle m_p;
    static KPaddleContextualMenu s_paddleContextual = null;
    KEditModeHandler m_emh;

    static final int MOVE_MODE = 1;
    static final int NOTE_MODE = 2;
    static final int CHAN_MODE = 3;
    static final int ANGLE_MODE = 4;
    static final int CHAN_LIVE_MODE = 5;
    static final int NOTE_LIVE_MODE = 6;
    static final int MIDI_OUT_MODE = 7;
    static final int MIDI_OUT_LIVE_MODE = 8;
    static final int MIDI_IN_MODE = 9;
    static final int MIDI_IN_LIVE_MODE = 10;
    static final int MENU_MODE = 11;
    KChannelMenu m_channelMenu;
    KNoteMenu    m_noteMenu;
    KMidiMenu    m_midiMenu;

    int m_handlerState = 0;
    static final int EDIT_STATE = 1;
    static final int DO_ACTION_STATE = 2;

    // ----------------- KEditHandler interface methods:
    public KPaddleModifier(KSpace s, KEditModeHandler emh) {
        mi = new KPoint (0,0);
        m_channelMenu = new KChannelMenu(s);
        m_noteMenu = new KNoteMenu(s, 72);
        m_midiMenu = new KMidiMenu(s);
        m_space = s;
        if (s_paddleContextual == null) {
            s_paddleContextual = new KPaddleContextualMenu(s);
        }
        m_emh = emh;
    }
    public void resetHandler() {
        m_handlerState = EDIT_STATE;
    }
    public boolean keyReleased(KEditable o, KeyEvent e) {
        if (! (o instanceof KPaddle)) return false;

        return false;
    }

    public boolean keyPressed(KEditable o, KeyEvent e) {
        if (! (o instanceof KPaddle)) return false;

        m_p = (KPaddle) o;
        return false; // gak - goto code -- need to let delete events flow outward
    }

    public boolean mousePressed(KEditable o, MouseEvent e) {
        boolean ret = false;

        if (! (o instanceof KPaddle)) return false;

        m_p = (KPaddle) o;
        mi.x = e.getX();
        mi.y = e.getY();
        /* We can be in two states at this point.
         * State 1) edit mode, plain click drags the item, alt-click summons a contextual menu
         * State 2) do menu action mode, an action has been selected in a contextual menu, and we're doing it
         */
        if (KGenericHandler.rightClick(e)) { // right-click
            m_p.m_altState = 1;
        } else {
            m_p.m_altState = 0;
        }

        if (m_handlerState == EDIT_STATE) {
            if (m_p.m_altState == 0) {
                // check whether we're adjusting an endpoint or moving the paddle
                if (KSpace.lineLength(mi, m_p.p2) < 8) {
                    m_editP1 = false;
                    m_editMode = ANGLE_MODE;
                    ox1 = m_p.x1; ox2 = m_p.x2; oy1 = m_p.y1; oy2 = m_p.y2;
                    ret = true;
                } else if (KSpace.lineLength(mi, m_p.mp) < 7) {
                    m_editP1 = false;
                    m_editMode = MOVE_MODE;
                    ox1 = m_p.x1; ox2 = m_p.x2; oy1 = m_p.y1; oy2 = m_p.y2;
                    ret = true;
                } else if (KSpace.lineLength(mi, m_p.p1) < 8) {
                    m_editP1 = true;
                    m_editMode = ANGLE_MODE;
                    ox1 = m_p.x1; ox2 = m_p.x2; oy1 = m_p.y1; oy2 = m_p.y2;
                    ret = true;
                }
            } else {
                m_editMode = MENU_MODE;
                s_paddleContextual.showMenu(this, mi);
                ret = true;
            }
        } else { // DO_ACTION_STATE
        }
        return ret; // didn't handle
    }

    public boolean mouseReleased(KEditable o, MouseEvent e) {
        if (!(o instanceof KPaddle)) return false;

        boolean ret = false;

        // System.out.println("KPaddle mouse released  Mode = " + m_editMode + ", state = " + m_handlerState);
        if (m_editMode == MENU_MODE) {
            if (m_handlerState == DO_ACTION_STATE) {
                s_paddleContextual.hideMenu();
                m_emh.m_currHandler = null; // relinquish
            } else {
                // edit state: menuitem actions handle this...
                // but first, swallow the first release
                m_handlerState = DO_ACTION_STATE;
            }
            m_editMode = 0;
        }
        m_p.label = "";
        return ret;
    }
    public boolean mouseMoved(KEditable o, MouseEvent ev) {
        return false; // didn't handle
    }
    public boolean mouseDragged(KEditable o, MouseEvent e) {
        //System.out.println("KPaddle mouse dragged!  Mode = " + m_editMode + ", state = " + m_handlerState);
        switch (m_editMode) {
            case MOVE_MODE:
                int dx = (int) (e.getX() - mi.x);
                int dy = (int) (e.getY() - mi.y);
                m_p.label = "move: (" + dx + ", " + dy + ")";
                m_p.init (ox1 + dx, oy1 + dy, ox2 + dx, oy2 + dy);
                return true;
            case CHAN_MODE:
                break;
            case ANGLE_MODE:
                boolean no_swap = false;
                if (m_editP1) {
                    no_swap = m_p.init(e.getX(), e.getY(), m_p.x2,  m_p.y2);
                } else {
                    no_swap = m_p.init(m_p.x1,  m_p.y1, e.getX(), e.getY());
                }
                if (!no_swap) {
                    m_editP1 = !m_editP1;
                }
                return true;
            default:

        }
        return false; // didn't handle
    }
    public boolean wantMouseDragEvents() {
        return true;
    }
    public boolean wantMouseMoveEvents() {
        return false;
    }


    class KPaddleContextualMenu extends KMenu {
        KPaddleModifier m_pm;
        public KPaddleContextualMenu (KSpace s) {
            m_space = s;
            m_items = new Vector();
            int ypos = 10;
            m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "x"));
            m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "note"));
            m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "midi out device"));
            m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "midi out channel"));
        }
        public void showMenu(KPaddleModifier pm, KPoint p) {
            m_pm = pm;
            int ypos = p.getYint();

            for (Enumeration e = m_items.elements(); e.hasMoreElements(); ) {
                KMenuItem i = (KMenuItem)e.nextElement();
                i.moveItemTo(p.getXint(), ypos);
                ypos += 20;
            }
            super.showMenu();
        }
        public void menuItemHit(KMenuItem i, boolean mouseReleased) {
            m_pm.m_handlerState = DO_ACTION_STATE;
            if (i.m_label.startsWith("x")) {
                hideMenu();
            } else if (i.m_label.startsWith("note")) {
                m_pm.m_editMode = NOTE_MODE;
                m_pm.m_noteMenu.setHotObj(m_p);
                m_pm.m_noteMenu.showMenu(m_p.p1);
                hideMenu();
                // m_space.pushHandler(m_pm);
                m_pm.m_p.label = "select a note";
            }  else if (i.m_label.endsWith("device")) {
                m_editMode = MIDI_OUT_MODE;
                m_midiMenu.setHotObj(m_p);
                m_midiMenu.showMenu(m_p.p1);
                hideMenu();
                m_p.label = "configure midi device";
            } else if (i.m_label.endsWith("channel")) {
                m_editMode = CHAN_MODE;
                m_channelMenu.setHotObj(m_p);
                m_channelMenu.showMenu(m_p.p2);
                hideMenu();
                // m_space.pushHandler(this);
                m_p.label = "select a channel";
            } else {
                m_p.label = "unknown menu item?";
            }
        }
    }
}

