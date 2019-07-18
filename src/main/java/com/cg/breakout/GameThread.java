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
	public static final String    GAMECTX          = "gamectx";
	private final GameContext gctxCopy;
	volatile            GameState currentGameState = GameState.MENU;
	EventBus    eventBus;
	GameContext gctx;
	private boolean paused;

	public GameThread(EventBus eventBus, GameContext gctx) {
		this.gctx = gctx;
		this.eventBus = eventBus;
		this.gctxCopy = new GameContext(gctx);
	}

	@Subscribe
	void keyboardEnterListener(KeyPressEvent e) {
		// change game state to play and notify on bus
		switch (currentGameState) {
			case MENU:
				if (e.getKey() == GLFW.GLFW_KEY_ENTER && e.getAction() == GLFW.GLFW_PRESS) {
					currentGameState = GameState.PLAY;
					Map<String, Object> extra = new HashMap<>();
					extra.put(GAMECTX, gctx);
					eventBus.post(new GameStateEvent(GameState.PLAY, extra));
					synchronized (this) {
						notify();
					}
				}

				if (e.getKey() == GLFW.GLFW_KEY_ESCAPE && e.getAction() == GLFW.GLFW_PRESS) {
					System.exit(0);
				}

				break;

			case PLAY:
				if (e.getKey() == GLFW.GLFW_KEY_ESCAPE && e.getAction() == GLFW.GLFW_PRESS) {
					currentGameState = GameState.PAUSE;
					paused=true;
					eventBus.post(new GameStateEvent(GameState.PAUSE));
				}
				break;

			case PAUSE:
				if (e.getKey() == GLFW.GLFW_KEY_ESCAPE && e.getAction() == GLFW.GLFW_PRESS) {
					synchronized (this) {
						paused = false;
						currentGameState = GameState.PLAY;

						Map<String, Object> extra = new HashMap<>();
						extra.put(GAMECTX, gctx);
						eventBus.post(new GameStateEvent(GameState.PLAY, extra));

						notify();
					}

				}
				break;
		}

	}

	@Subscribe
	void gameStateListener(GameStateEvent e) {
		currentGameState = e.getGameState();
	}

	@Override
	public void run() {
		eventBus.register(this);
		GameUpdateThread game = new GameUpdateThread(eventBus, gctx);
		while (true) {


			switch (currentGameState) {
				case PLAY: // run the game on this thread
					try {
						sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					game.runLoop();
					break;

				case MENU:
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {


						}
					}
					break;

				case END:


					synchronized (this) {
						try {
							gctx = new GameContext(gctxCopy);
							game = new GameUpdateThread(eventBus, gctx);
							sleep(3*1000);
							currentGameState = GameState.MENU;
							eventBus.post(new GameStateEvent(GameState.MENU));
						} catch (InterruptedException e) {


						}
					}
					break;

				case PAUSE:
					synchronized (this) {
						try {
							eventBus.post(new GameStateEvent(GameState.PAUSE)); // dont update any thing
							while (paused) {
								wait(); //todo: spurious waking
							}
						} catch (InterruptedException e) {


						}
					}

			}
		}
	}
}
