package com.mecodegoodsomeday.KaPwing;

public interface KCollidable extends KObject {
	// check if KBall has collided with us by going from pt1 to pt2
	public void handleCollision(KBall o, KSpace s);
	// make noise or whatever based on colliding with this ball
	public void fireEvent(KBall o, KSpace s);
}
