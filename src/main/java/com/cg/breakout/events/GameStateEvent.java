package com.cg.breakout.events;

import com.cg.breakout.GameState;

import java.util.Collections;
import java.util.Map;

public class GameStateEvent {
	private GameState                                   gameState;
	private Map<? extends CharSequence, ? super Object> extras = Collections.emptyMap();

	public GameStateEvent(GameState gameState, Map<? extends CharSequence, ? super Object> extras) {
		this.extras = Collections.unmodifiableMap(extras);
		this.gameState = gameState;
	}

	public GameStateEvent(GameState gameState) {
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Map getExtras() {
		return extras;
	}
}
