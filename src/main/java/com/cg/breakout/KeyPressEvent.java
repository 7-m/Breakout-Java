package com.cg.breakout;

/**
 * Event representing a key press.
 */
public class KeyPressEvent {

	// key, int scancode, int action, int mods
	private int key;
	private int action;
	private int mods;

	public KeyPressEvent(int key, int action, int mods) {
		this.key = key;
		this.action = action;
		this.mods = mods;
	}

	public int getKey() {
		return key;
	}

	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}
}
