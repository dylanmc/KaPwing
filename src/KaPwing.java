package com.mecodegoodsomeday.KaPwing;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Oct 26, 2004
 * Time: 12:37:03 PM
 * License: CC-SA
 */
public class KaPwing {
    private ScreenManager m_screen;
    private KSpace m_space;

    // drawable "states"
    public final static int HOT = 1;

    public static void main(String[] args) {
        KaPwing app = new KaPwing();
        app.run();
    }

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(896, 600, 32, 0),
        new DisplayMode(1280, 854, 24, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0)
    };

    public void run() {
        m_screen = new ScreenManager();
        try {
            DisplayMode displayMode =
                m_screen.findFirstCompatibleMode(POSSIBLE_MODES);
            m_screen.setFullScreen(displayMode);
            m_space = new KSpace();
            m_space.mainLoop(m_screen);
        }
        finally {
            m_screen.restoreScreen();
        }

    }
}
