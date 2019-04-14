package com.cg.breakout;

public class Entity {
	private double width, height;
	private double x, y;

	public Entity(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	protected void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	protected void setWidth(double width) {
		this.width = width;
	}

	public double getX() {
		return x;
	}

	protected void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	protected void setY(double y) {
		this.y = y;
	}

	boolean collidesWith(Entity r) {

		return x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y;
	}
}
