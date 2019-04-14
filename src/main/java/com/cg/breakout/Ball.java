package com.cg.breakout;

public class Ball
		extends Entity {
	/* velocity vectors*/
	Vector2D vel;

	public Ball(double width, double height) {
		super(width, height);
		setX(0.5);
		setY(0.5);
		vel = new Vector2D(0.001, 0.01);
	}

	public void move() {

		setX(getX() + vel.getX());
		setY(getY() + vel.getY());

	}


	Vector2D getVelocity() {
		return vel;
	}
}
