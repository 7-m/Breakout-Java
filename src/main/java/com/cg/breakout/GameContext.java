package com.cg.breakout;

/**
 * Represents the current state of the game i.e play, pause or current bricks, paddle, ball etc
 */
public class GameContext {
	final Brick[][] bricks;
	final Paddle    paddle;
	final Ball ball;

	public GameContext(Brick[][] bricks, Paddle paddle, Ball ball) {
		this.bricks = bricks;
		this.paddle = paddle;
		this.ball = ball;
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


}
