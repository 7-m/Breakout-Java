package com.cg.breakout;

import java.util.Arrays;

/**
 * Represents the current state of the game i.e play, pause or current bricks, paddle, ball etc
 */
public class GameContext {
	final Paddle paddle;
	final Ball   ball;
	Brick[][] bricks;
	final Brick[][] copyOfBricks;

	public GameContext(Brick[][] bricks, Paddle paddle, Ball ball) {
		this.copyOfBricks = makeCopy(bricks);
		this.bricks = bricks;
		this.paddle = paddle;
		this.ball = ball;
	}

	private Brick[][] makeCopy(Brick[][] bricks) {
		Brick[][] copy = new Brick[bricks.length][bricks[0].length];

		for (int i = 0; i < copy.length; i++) {
			copy[i] = Arrays.copyOf(bricks[i], bricks[i].length);
		}

		return copy;

	}

	public Brick[][] getBricks() {
		return bricks;
	}

	public Paddle getPaddle() {
		return paddle;
	}

	public Ball getBall() {
		return ball;
	}

	void reset() {
		this.bricks = makeCopy(copyOfBricks);
		//ball.getVelocity().reverse();
		ball.setX(paddle.getX());
		ball.setY(0.15);
	}


}
