package com.cg.breakout;

import java.util.Arrays;

/**
 * Represents the current state of the game i.e play, pause or current bricks, paddle, ball etc
 */
public class GameContext {
	final Paddle    paddle;
	final Ball      ball;

	Brick[][] bricks;
	private int score;
	final private int maxscore;

	public GameContext(Brick[][] bricks, Paddle paddle, Ball ball, int maxscore) {

		this.bricks = bricks;
		this.paddle = paddle;
		this.ball = ball;
		this.maxscore = maxscore;
	}

	public GameContext(GameContext old) {
		this(makeCopy(old.getBricks()), new Paddle(old.paddle), new Ball(old.ball), old.maxscore);
		this.score=old.score;
	}

	public void incrementScore() {
		score++;
	}

	public int getMaxscore() {
		return maxscore;
	}

	static private Brick[][] makeCopy(Brick[][] bricks) {
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

	public int getScore() {
		return score;
	}




}
