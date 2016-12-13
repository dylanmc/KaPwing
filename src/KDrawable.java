package com.mecodegoodsomeday.KaPwing;

import java.awt.*;

public interface KDrawable extends KObject {
    public void draw(Graphics2D g);

    public boolean intersectsWithPoint(KPoint p);

    public void setState(int state);

    public KPoint getPos();
}
