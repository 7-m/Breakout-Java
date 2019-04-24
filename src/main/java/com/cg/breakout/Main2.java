package com.cg.breakout;

import com.cg.breakout.events.GameStateEvent;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

public class Main2 {
	GameContext gctx;
	public static void main(String[] args) {
		Main2 main = new Main2();
		main.init();
	}

	private void init() {

		EventBus ev = new AsyncEventBus(Executors.newWorkStealingPool());
		final Paddle paddle = new Paddle(0.1, 0.02);
		final Ball ball = new Ball(0.03, 0.03);
		int ncols=20;
		int nrow=10;
		final Brick[][] bricks = new Brick[nrow][ncols];

		final double width = 1.0 / ncols;
		final double height = 0.6 / nrow;
		for (int i = 0; i < nrow; i++) {
			for (int j = 0; j < ncols; j++) {
				bricks[i][j] = new Brick(width, height, j*width,(0.9-height*i));
			}
		}
		GameContext gtx = new GameContext(bricks, paddle, ball);

		IOThread rt = new IOThread(ev);
		rt.start();

		GameThread gt = new GameThread(ev,gtx);
		gt.start();

		ev.post(new GameStateEvent(GameState.MENU));


	}
}
