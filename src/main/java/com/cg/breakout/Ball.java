package com.cg.breakout;

public class Ball
		extends Entity {
	/* velocity vectors*/
	Vector2D vel;

	public Ball(double width, double height, double x, double y) {
		super(width, height);
		setX(x);
		setY(y);
		vel = new Vector2D(0.01, 0.01);
	}

	public Ball(Ball ball) {
		super(ball);
		this.vel = new Vector2D(ball.getVelocity());
	}

	public void move() {

		setX(getX() + vel.getX());
		setY(getY() + vel.getY());

	}


	Vector2D getVelocity() {
		return vel;
	}

	public void increaseVelocity() {
		if (vel.getLength() < 0.05){ // after years of advanced  researchs
			vel.multiply(1.03);
		}
	}
}
