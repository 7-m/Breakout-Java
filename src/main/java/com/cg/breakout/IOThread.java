package com.cg.breakout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * OpenGL is initialized here. Windows are created here and the rendering logic is in this thread.
 */
public class IOThread
		extends Thread {
	public static final Logger            logger = Logger.getLogger("com.cg.breakout.IOThread");
	private final       EventBus          bus;
	private             GLFWErrorCallback errorCallback;
	private             long              window;
	private             GLFWKeyCallback   keyCallback;
	private             GameContext       gameCtx;
	private             boolean           renderRequired;


	public IOThread(EventBus bus, GameContext gameCtx) {
		super(IOThread.class.getName());
		this.gameCtx = gameCtx;
		this.bus = bus;
		bus.register(this);
		initOpenGl();
	}

	private void initOpenGl() {
		// create OpenGL context and initialize it here

		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);


		int WIDTH = 600;
		int HEIGHT = 600;

		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL3", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				// post the event on the message bus
				bus.post(new KeyPressEvent(key, action, mods));

			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);

		glfwSetWindowSizeCallback(window, (window1, width, height) -> {
			glViewport(0, 0, width, height);
		});


	}

	@Override
	public void run() {
		// make context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();


		while (!glfwWindowShouldClose(window)) {

			synchronized (this) {
				if (!renderRequired)
					glfwPollEvents();


				else {

					logger.info("Rendering on thread " + Thread.currentThread().getName());
					renderRequired = false;

					// rendering code here

					glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

					glMatrixMode(GL_PROJECTION);
					glLoadIdentity();
					glOrtho(0.0, 1.0, 0.0, 1.0, 1.0, -1.0);
					glMatrixMode(GL_MODELVIEW);


					glColor3b((byte) 100, (byte) 100, (byte) 0);


					// render paddle
					glRectd(gameCtx.paddle.getX(),
							gameCtx.getPaddle().getY(),
							gameCtx.getPaddle().getX() + gameCtx.getPaddle().getWidth(),
							gameCtx.getPaddle().getY() + gameCtx.getPaddle().getHeight());

					//render ball
					glRectd(gameCtx.getBall().getX(),
							gameCtx.getBall().getY(),
							gameCtx.getBall().getX() + gameCtx.getBall().getWidth(),
							gameCtx.getBall().getY() + gameCtx.getBall().getHeight());

					// render bricks
					for (Brick[] row : gameCtx.getBricks()) {
						for (Brick b : row) {
							if (b != null) {
								glRectd(b.getX(),
										b.getY(),
										b.getX() + b.getWidth(),
										b.getY() + b.getHeight());
							}
						}
					}

					glfwSwapBuffers(window);
				}
			}

		}
	}

	@Subscribe
	synchronized void drawUpdate(DrawEvent e) {

		// this method will be called on another thread, so wake up the right thread

		logger.info("Drawing message recieved on thread " + Thread.currentThread().getName());

		renderRequired = true;
		notify();


	}


}
