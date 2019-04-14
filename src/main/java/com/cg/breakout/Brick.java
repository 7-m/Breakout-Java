package com.cg.breakout;

public class Brick extends Entity {
	public Brick(double width, double height, double x, double y) {
		super(width, height);
		setX(x);
		setY(y);
	}
}
