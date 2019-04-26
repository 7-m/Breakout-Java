package com.cg.breakout;


import com.cg.breakout.events.CursorPosEvent;
import com.cg.breakout.events.GameStateEvent;
import com.cg.breakout.events.KeyPressEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;


public class GameUpdateThread {
	EventBus bus;
	private GameContext gctx;
	private boolean     stop;

	public GameUpdateThread(EventBus bus, GameContext gctx) {
		this.gctx = gctx;
		this.bus = bus;
		bus.register(this);
	}


	/**
	 * Runs a single game loop and updates the gameContext.
	 */
	public void runLoop() {


		gctx.getBall().move();
		// detect collisons

		checkCollision();

		// ready to render new contents


	}

	private void checkCollision() {

		// check collison with walls
		// use laws of reflection to bounce the ball off

		// check left wall
		final Ball ball = gctx.getBall();
		if (ball.getX() <= 0.0) {
			// angle o normal of left wall is
			double nang = 0.0;

			ball.getVelocity().reverse();
			double angleWith = nang - ball.getVelocity().getAngle();


			ball.getVelocity().rotateBy(2 * angleWith);


		} else

			// check right wall
			if (ball.getX() + ball.getWidth() >= 1.0) {
				// angle of normal of right wall is
				double nang = Math.PI;

				ball.getVelocity().reverse();
				double angleWith = nang - ball.getVelocity().getAngle();

				ball.getVelocity().rotateBy(2 * angleWith);
			} else

				// check top wall
				if (ball.getY() + ball.getHeight() >= 1.0) {
					// angle normal of top wall is
					double nang = Math.PI * 3 / 2;

					ball.getVelocity().reverse();
					double angleWith = nang - ball.getVelocity().getAngle();

					ball.getVelocity().rotateBy(2 * angleWith);

				} else

					// check for bottom hit


					// check paddle for bottom, any collsion is bounce for paddle

					if (ball.collidesWith(gctx.getPaddle())) {


						// reflection is more if distance of ball from paddle is more

						double paddleoff =
								(gctx.getPaddle().getX() + gctx.getPaddle().getWidth() / 2.0) - ball.getX(); // offset from centre of paddle
						paddleoff = paddleoff / (gctx.getPaddle().getWidth() / 2.0);


						ball.getVelocity().rotateTo(Math.PI / 2.0);
						ball.getVelocity().rotateBy(paddleoff * Math.PI / 4);


					} else if (ball.getY() < 0.0) {
						bus.post(new GameStateEvent(GameState.END));

					} else {
						// check for collsions with bricks

						Brick[][] bricks = gctx.getBricks();

						OUTER:
						for (int i = 0; i < bricks.length; i++) {
							for (int j = 0; j < bricks[0].length; j++) {
								if (bricks[i][j] != null && ball.collidesWith(bricks[i][j])) {
									// determine overlap location
									// x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y;

									final double delta = 0.09;
									// ball hitting brick from bottom

									if (ball.getY() + ball.getHeight() >= bricks[i][j].getY() && ball.getY() + ball
											.getHeight() <= bricks[i][j]
											.getY() + delta) {            // angle normal of top wall is
										double nang = Math.PI * 3 / 2;

										ball.getVelocity().reverse();
										double angleWith = nang - ball.getVelocity().getAngle();

										ball.getVelocity().rotateBy(2 * angleWith);
									}

									// ball hitting brick from left
									else if (ball.getX() + ball.getWidth() >= bricks[i][j].getX() && ball.getX() + ball
											.getWidth() <= bricks[i][j]
											.getX() + delta) {            // angle normal of top wall is
										double nang = Math.PI;

										ball.getVelocity().reverse();
										double angleWith = nang - ball.getVelocity().getAngle();

										ball.getVelocity().rotateBy(2 * angleWith);
									}

									// ball hitting brick from rigth
									else if (ball.getX() <= bricks[i][j].getX() + bricks[i][j].getWidth() && ball.getX() >= bricks[i][j]
											.getX() + bricks[i][j].getWidth() - delta) {            // angle normal of top wall is
										double nang = 0;

										ball.getVelocity().reverse();
										double angleWith = nang - ball.getVelocity().getAngle();

										ball.getVelocity().rotateBy(2 * angleWith);
									}

									// ball hitting brick from top
									else if (ball.getY() <= bricks[i][j].getY() + bricks[i][j].getHeight() && ball.getY() >= bricks[i][j]
											.getY() + bricks[i][j].getHeight() - delta) {            // angle normal of top wall is
										double nang = Math.PI * 3.0 / 2.0;

										ball.getVelocity().reverse();
										double angleWith = nang - ball.getVelocity().getAngle();

										ball.getVelocity().rotateBy(2 * angleWith);
									}


									gctx.incrementScore();
									bricks[i][j] = null;

									// increase ball speed
									gctx.getBall().increaseVelocity();

									break OUTER; // only update 1 brick

								}
							}
						}
					}
	}


	@Subscribe
	void keyEventListener(KeyPressEvent e) {
		if (e.getKey() == GLFW.GLFW_KEY_LEFT)
			gctx.getPaddle().moveLeft();

		else if (e.getKey() == GLFW.GLFW_KEY_RIGHT)
			gctx.getPaddle().moveRight();


	}

	@Subscribe
	void cursorEventListener(CursorPosEvent e) {
		gctx.getPaddle().setX(e.getXpos() - gctx.getPaddle().getWidth()/2);


	}
}
