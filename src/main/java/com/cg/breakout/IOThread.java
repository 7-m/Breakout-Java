package com.cg.breakout;

import com.cg.breakout.events.CursorPosEvent;
import com.cg.breakout.events.GameStateEvent;
import com.cg.breakout.events.KeyPressEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * OpenGL is initialized here. Windows are created here and the rendering logic is in this thread.
 */
public class IOThread
		extends Thread {
	public static final Logger            logger                   = Logger.getLogger("com.cg.breakout.IOThread");
	public static final int               REFRESH_PERIOD_IN_MILLIS = 30;
	private final       EventBus          bus;
	private             GLFWErrorCallback errorCallback;
	private             long              window;
	private             GLFWKeyCallback   keyCallback;
	volatile private    GameContext       gameCtx;
	volatile private    boolean           renderRequired;
	volatile private    GameState         gameState;
	//private GameStateEvent lastEvent;


	public IOThread(EventBus bus) {
		super(IOThread.class.getName());
		this.bus = bus;
		bus.register(this);
		initOpenGl();
		startTimerThread();
	}

	// resets the render flag to allow rendering
	private void startTimerThread() {
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(REFRESH_PERIOD_IN_MILLIS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (gameState != null) renderRequired = true;
			}
		}).start();
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

		glfwSetCursorPosCallback(window, (window1, xpos, ypos) -> bus.post(new CursorPosEvent(xpos / WIDTH, ypos / HEIGHT)));

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);

		glfwSetWindowSizeCallback(window, (window1, width, height) -> {
			glViewport(0, 0, width, height);
			glfwSetCursorPosCallback(window, (window2, xpos, ypos) -> bus.post(new CursorPosEvent(xpos / width, ypos / height)));
		});

		glfwSetWindowCloseCallback(window, window1 -> System.exit(0));


	}

	@Override
	public void run() {
		// make context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0, 1.0, 0.0, 1.0, 1.0, -1.0);
		glMatrixMode(GL_MODELVIEW);


		while (!glfwWindowShouldClose(window)) {

			synchronized (this) {
				if (!renderRequired)
					glfwPollEvents();


				else {


					//logger.info("Rendering on thread " + Thread.currentThread().getName());
					renderRequired = false;

					// rendering code here

					switch (gameState) {
						case PLAY:
							glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

							//glClearColor(0.8f,0.5f,1.0f,1.0f);




							glColor3d(0.0, 1.0, 0.0);


							// render paddle
							glRectd(gameCtx.paddle.getX(),
									gameCtx.getPaddle().getY(),
									gameCtx.getPaddle().getX() + gameCtx.getPaddle().getWidth(),
									gameCtx.getPaddle().getY() + gameCtx.getPaddle().getHeight());

							glColor3d(1.0, 0.0, 0.0);
							//render ball
							glRectd(gameCtx.getBall().getX(),
									gameCtx.getBall().getY(),
									gameCtx.getBall().getX() + gameCtx.getBall().getWidth(),
									gameCtx.getBall().getY() + gameCtx.getBall().getHeight());

							// render bricks
							for (Brick[] row : gameCtx.getBricks()) {
								for (Brick b : row) {
									if (b != null) {
										glColor3d(Math.random(), Math.random(), Math.random());
										glRectd(b.getX(),
												b.getY(),
												b.getX() + b.getWidth(),
												b.getY() + b.getHeight());
									}
								}
							}

							// render score

							drawString("SCORE\n", 0.3f, 0.9f, 1.0f, 1.0f, 1.0f);
							drawString(gameCtx.getScore()+"\n", 0.7f, 0.9f, 1.0f, 1.0f, 1.0f);
							break;

						case MENU:
							glClear(GL_COLOR_BUFFER_BIT);
							String text = "Press enter to play\n";
							drawString(text, 0.0f, 0.0f,1.0f,0.0f,0.0f);
							drawString("BREAKOUT\n", 0.0f,0.5f,0.0f,1.0f,0.0f);
							break;


						case END:
							glClear(GL_COLOR_BUFFER_BIT);
							drawString("FINAL SCORE\n"+gameCtx.getScore()+"\n",0.2f,0.5f,0.0f,1.0f,0.0f);
							break;

						case PAUSE:
							glClear(GL_COLOR_BUFFER_BIT);
							drawString("PAUSED\n",0.3f,0.5f,0.0f,1.0f,0.0f);
					}
					glfwSwapBuffers(window);
				}


			}
		}

	}

	private void drawString(String text, float x, float y, float red, float green, float blue) {
		ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);

		int quads = stb_easy_font_print(0,0, text, null, charBuffer);

		glEnableClientState(GL_VERTEX_ARRAY);
		glVertexPointer(2, GL_FLOAT, 16, charBuffer);

		glColor3f(red,green,blue); // Text color


		//glClear(GL_COLOR_BUFFER_BIT);

		float scaleFactor = 0.005f;

		glPushMatrix();
		glLoadIdentity();

		glTranslated(x, y+0.05f, 0.0f);
		// Zoom
		glScalef(scaleFactor, -scaleFactor, scaleFactor);
		// Scroll


		glDrawArrays(GL_QUADS, 0, quads * 4);

		glPopMatrix();


		glDisableClientState(GL_VERTEX_ARRAY);
	}


	@Subscribe
	synchronized void gameStateChangeListener(GameStateEvent event) {

		switch (event.getGameState()) {

			case PLAY:
				gameCtx = (GameContext) event.getExtras().get(GameThread.GAMECTX);
				gameState = event.getGameState();
				break;
			case PAUSE:
				gameState = event.getGameState();
				break;
			case MENU:
			case END:
				gameState = event.getGameState();
				break;

		}
		notify();

	}


}
