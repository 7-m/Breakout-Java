package com.cg.breakout;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;

public class GameUpdateThread
		extends Thread {
	EventBus bus;
	private GameContext gctx;
	private boolean     close;

	public GameUpdateThread(EventBus bus, GameContext gctx) {
		super(GameUpdateThread.class.getName());
		this.gctx = gctx;
		this.bus = bus;
		bus.register(this);
	}

	@Override
	public void run() {
		while (!close) {
			try {
				sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			gctx.getBall().move();
			// detect collisons

			checkCollision();

			// ready to render new contents
			bus.post(new DrawEvent());

		}
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


		}

		// check right wall
		if (ball.getX() + ball.getWidth() >= 1.0) {
			// angle of normal of right wall is
			double nang = Math.PI;

			ball.getVelocity().reverse();
			double angleWith = nang - ball.getVelocity().getAngle();

			ball.getVelocity().rotateBy(2 * angleWith);
		}

		// check top wall
		if (ball.getY() + ball.getHeight() >= 1.0) {
			// angle normal of top wall is
			double nang = Math.PI * 3 / 2;

			ball.getVelocity().reverse();
			double angleWith = nang - ball.getVelocity().getAngle();

			ball.getVelocity().rotateBy(2 * angleWith);

		}

		// check paddle for bottom, any collsion is bounce for paddle

		if (ball.collidesWith(gctx.getPaddle())) {
			double nang = Math.PI / 2;

			ball.getVelocity().reverse();
			double angleWith = Math.abs(nang - ball.getVelocity().getAngle());

			if (ball.getX() < gctx.getPaddle().getX() + gctx.getPaddle().getWidth() / 2.0) // bounce left
				ball.getVelocity().rotateBy(angleWith - Math.random());
			else
				ball.getVelocity().rotateBy(-angleWith + Math.random());// randomness prevents ball from entering 90 deg bounce loop

		}

		boolean updated = false; // if multiple bricks are hit, then dont apply reflection calculations more than once
		Brick[][] bricks = gctx.getBricks();
		for (int i = 0; i < bricks.length; i++) {
			for (int j = 0; j < bricks[0].length; j++) {
				if (bricks[i][j] != null && ball.collidesWith(bricks[i][j])) {
					// determine overlap location
					// x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y;

					final double delta = 0.09;
					// ball hitting brick from bottom
					if (!updated) {
						if (ball.getY() + ball.getHeight() >= bricks[i][j].getY() && ball.getY() + ball.getHeight() <= bricks[i][j]
								.getY() + delta) {            // angle normal of top wall is
							double nang = Math.PI * 3 / 2;

							ball.getVelocity().reverse();
							double angleWith = nang - ball.getVelocity().getAngle();

							ball.getVelocity().rotateBy(2 * angleWith);
						}

						// ball hitting brick from left
						else if (ball.getX() + ball.getWidth() >= bricks[i][j].getX() && ball.getX() + ball.getWidth() <= bricks[i][j]
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
							double nang = Math.PI*3.0/2.0;

							ball.getVelocity().reverse();
							double angleWith = nang - ball.getVelocity().getAngle();

							ball.getVelocity().rotateBy(2 * angleWith);
						}
						updated = true;
					}


					bricks[i][j] = null;

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

		bus.post(new DrawEvent());

	}
}
