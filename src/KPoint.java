package com.mecodegoodsomeday.KaPwing;

public class KPoint {
    float x;
    float y;
    public static KPoint ZeroPoint = new KPoint(0,0);

    public KPoint(float nx, float ny) {
        x = nx;
        y = ny;
    }

    public KPoint(int nx, int ny) {
        x = nx;
        y = ny;
    }
    public void copyFrom(KPoint p) {
        x = p.x;
        y = p.y;
    }

    public void addVectorByTime(KVector v, long deltat) {
        x = x + v.m_elt[0] * deltat;
        y = y + v.m_elt[1] * deltat;
    }

    public int getXint() {
        return (int) x;
    }
    public int getYint() {
        return (int) y;
    }
}
