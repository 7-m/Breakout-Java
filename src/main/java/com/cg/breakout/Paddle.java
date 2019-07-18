package com.cg.breakout;

public class Paddle
		extends Entity {
	public Paddle(double width, double height) {
		super(width, height);
		setX(0.5);

	}

	public Paddle(Paddle paddle) {
		super(paddle);
	}


	void moveLeft() {
		if (getX() > 0.0)
			setX(getX() - 0.02);

	}

	void moveRight() {
		if (getX()+getWidth() < 1.0)
			setX(getX() + 0.02);
	}

}
