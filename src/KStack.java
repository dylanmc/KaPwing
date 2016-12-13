package com.mecodegoodsomeday.KaPwing;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: dylan
 * Date: Nov 3, 2004
 * Time: 4:06:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class KStack extends Vector {

    // just like a stack, but its elements() call returns the elements in stack-order
    public void push(Object o) {
        this.insertElementAt(o, 0);
    }

    public Object pop() {
        Object r = this.elementAt(0);
        this.removeElementAt(0);
        return r;
    }
}
