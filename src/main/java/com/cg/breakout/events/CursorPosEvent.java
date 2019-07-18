package com.cg.breakout.events;

public class CursorPosEvent {
	final private double xpos, ypos;

	public double getXpos() {
		return xpos;
	}

	public double getYpos() {
		return ypos;
	}

	public CursorPosEvent(double xpos, double ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}
}
