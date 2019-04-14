package com.cg.breakout;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback   keyCallback;

	private long window;


	public static void main(String[] args) {
		new Main().run();
	}

	private void init() {

		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		int WIDTH = 600;
		int HEIGHT = 600;

		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL3", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {

			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}


	private void loop() {
		GL.createCapabilities();
		System.out.println("----------------------------");
		System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
		System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
		System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));


		System.out.println("----------------------------");

		int left[] = new int[1], right[] = new int[1], top[] = new int[1], bottom[] = new int[1];



		new Thread(() -> {
			while (!glfwWindowShouldClose(window)) {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(-10.0, 10.0, -10.0, 10.0, 10.0, -10.0);
				glMatrixMode(GL_MODELVIEW);


				glColor3b((byte) 100, (byte) 100, (byte) 0);


				glRectd(0, 0, -5, -5);

				glfwSwapBuffers(window);

				glfwPollEvents();
			}
		}).run();
	}

	public void run() {
		System.out.println("Hello LWJGL3 " + Version.getVersion() + "!");

		try {
			init();
			loop();
			glfwDestroyWindow(window);
			keyCallback.close();
		} finally {
			glfwTerminate();
			errorCallback.close();
		}
	}

}