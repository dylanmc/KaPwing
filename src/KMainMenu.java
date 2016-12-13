package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 11:12:56 AM
 */

public class KMainMenu extends KMenu {
    KTopCommands m_topCommands;
    KMenuItem m_item; // singleton special case

    public KMainMenu(KSpace s) {
        m_items = new Vector();
        m_space = s;
        m_item = new KMenuItem(this, 10, 20, 75, 20, "menu...");
        m_items.add(m_item);
        m_topCommands = new KTopCommands(s, this);
        showMenu();
    }
    public void menuItemHit(KMenuItem i, boolean mouseReleased) {
        m_currentItem = i;        
        hideMenu();
        m_topCommands.showMenu();
    }
    public void showMenu() {
        if (!m_showing) {
            m_showing = true;
            m_space.addObject(m_item);
        }
    }
    public void hideMenu() {
        if (m_showing) {
            m_showing = false;
            m_space.removeObject(m_item);
        }
    }
}

class KTopCommands extends KMenu {
    KPaddleHandler m_paddleHandler;
    KBallFactoryHandler m_ballFactoryHandler;
    KEditModeHandler m_editModeHandler;
    KMainMenu m_parent;
    KLiveFactoryHandler m_liveFactoryHandler;
    KMenuItem m_paddleItem, m_factoryItem, m_editItem;
    Vector m_otherItems;

    public KTopCommands (KSpace s, KMainMenu parent) {
        m_space = s;
        m_parent = parent;
        m_items = new Vector();
        m_otherItems = new Vector();
        int ypos = 10;
        m_otherItems.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "+"));
        ypos = 10;
        m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "-"));
        m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "menu", false));
        m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "add new", false));
        m_items.add(m_paddleItem = new KMenuItem(this, 20, ypos += 20, 75, 20,  "paddle"));
        m_items.add(m_factoryItem = new KMenuItem(this, 20, ypos += 20, 75, 20,  "ball factory"));
        // m_items.add(new KMenuItem(this, 20, ypos += 20, 75, 20,  "live factory"));
        // m_items.add(new KMenuItem(this, 20, ypos += 20, 75, 20,  "ball"));
        m_items.add(m_editItem = new KMenuItem(this, 10, ypos += 20, 75, 20,  "edit..."));
        m_items.add(new KMenuItem(this, 10, ypos += 20, 75, 20,  "quit"));
        m_paddleHandler = new KPaddleHandler(s);
        m_ballFactoryHandler = new KBallFactoryHandler(s);
        m_editModeHandler    = new KEditModeHandler(s);
        m_liveFactoryHandler = new KLiveFactoryHandler(s);
    }

    public void toggleMenu() {
        m_showing = true;
        hideMenu();
        Vector tmp = m_items;
        m_items = m_otherItems;
        m_otherItems = tmp;
        m_showing = false;
        showMenu();

        if (m_showing) {
            m_showing = false;
        } else {
            m_showing = true;
        }
    }

    public void setPaddleMode() {
        menuItemHit(m_paddleItem, false);
    }
    public void setFactoryMode() {
        menuItemHit(m_factoryItem, false);
    }
    public void setEditMode() {
        menuItemHit(m_editItem, false);
    }
    public void menuItemHit(KMenuItem i, boolean mouseReleased) {
        m_currentItem = i;
        // TODO:  need to handle switching modes before finishing the previous mode...popHandler calls...
        setMainMode(i.m_label);
    }

    public void setMainMode(String label) {
        m_space.resetHandlerStack();
        // clears all but the last one.
        if (label.startsWith("quit")) {
            System.exit(0);
        } else if (label.startsWith("-") || label.startsWith("+")) {
            toggleMenu();
        } else if (label.startsWith("paddle")) {
            m_space.setMsg("click and drag to make a paddle");
            m_space.pushHandler(m_paddleHandler);
            // hideMenu();
            // m_parent.showMenu();
        } else if (label.startsWith("ball factory")) {
            m_space.setMsg("click anywhere to make a ball factory");
            m_space.pushHandler(m_ballFactoryHandler);
            // hideMenu();
            // m_parent.showMenu();
        } else if (label.startsWith("live")) {
            m_space.pushHandler(m_liveFactoryHandler);
        } else if (label.startsWith("ball")) {
        } else if (label.startsWith("edit")) {
            m_space.setMsg("click and drag to move, right/ctrl-click to edit properties");
            m_space.pushHandler(m_editModeHandler);
        }
    }
}
