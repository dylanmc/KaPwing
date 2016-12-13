package com.mecodegoodsomeday.KaPwing;

public class KVector {
    float [] m_elt;
    public KVector() {
        m_elt = new float[2];
    }

    public KVector(float dx, float dy) {
        m_elt = new float[2];
        m_elt[0] = dx;
        m_elt[1] = dy;
    }
}
