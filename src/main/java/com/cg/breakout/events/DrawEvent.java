package com.cg.breakout.events;

import com.cg.breakout.GameContext;

public class DrawEvent {
	private final GameContext gctx;

	public DrawEvent(GameContext gctx) {
		this.gctx = gctx;
	}

	public GameContext getGameContext() {
		return gctx;
	}
}
