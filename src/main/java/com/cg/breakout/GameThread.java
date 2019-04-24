package com.cg.breakout;

import com.cg.breakout.events.GameStateEvent;
import com.cg.breakout.events.KeyPressEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class GameThread
		extends Thread {
	public static final String GAMECTX = "gamectx";
	volatile GameState gameState = GameState.MENU;
	EventBus    eventBus;
	GameContext gctx;

	public GameThread(EventBus eventBus, GameContext gctx) {
		this.gctx = gctx;
		this.eventBus = eventBus;
	}

	@Subscribe
	void keyboardEnterListener(KeyPressEvent e) {
		// change game state to play and notify on bus
		if (e.getKey() == GLFW.GLFW_KEY_ENTER) {
			gameState = GameState.PLAY;
			Map<String, Object> extra = new HashMap<>();
			extra.put(GAMECTX, gctx);
			eventBus.post(new GameStateEvent(GameState.PLAY,extra));
		}

	}

	@Override
	public void run() {
		eventBus.register(this);
		GameUpdateThread game = new GameUpdateThread(eventBus, gctx);
		while (!gameState.equals(GameState.EXIT)) {
			try {
				sleep(30); // frame rate of the app is 30 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// GameState oldState = gameState;

			switch (gameState) {
				case PLAY: // run the game on this thread
					game.runLoop();
					break;

				case PAUSE:

					break;

				case MENU:

			}
		}
	}
}
