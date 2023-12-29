package projects;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;  

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

	private long window;

    boolean go_left = false;
    boolean go_right = false;
    boolean go_up = false;
    boolean go_down = false;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1440, 900, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
            
            if ( key == GLFW_KEY_A && action == GLFW_PRESS )
				go_left = true;
            if ( key == GLFW_KEY_A && action == GLFW_RELEASE )
				go_left = false;

            if ( key == GLFW_KEY_D && action == GLFW_PRESS )
				go_right = true;
            if ( key == GLFW_KEY_D && action == GLFW_RELEASE )
				go_right = false;
            
            if ( key == GLFW_KEY_W && action == GLFW_RELEASE )
				go_up = true;
            if ( key == GLFW_KEY_W && action == GLFW_PRESS )
				go_up = false;
            
            if ( key == GLFW_KEY_S && action == GLFW_PRESS )
				go_down = true;
            if ( key == GLFW_KEY_S && action == GLFW_RELEASE )
				go_down = false;


		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.1f, 0.1f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
        float player_points[] = {0.0f, 0.05f,
								 -0.05f, -0.05f,
								 0.05f, -0.05f};

		while ( !glfwWindowShouldClose(window) ) {

            glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glColor3i(0, 255, 255);

            glBegin(GL_POLYGON);
            glVertex2f(player_points[0], player_points[1]);
            glVertex2f(player_points[2], player_points[3]);
            glVertex2f(player_points[4], player_points[5]);
			glEnd();

			glfwSwapBuffers(window);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}