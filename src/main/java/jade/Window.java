package jade;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import jade.input.listeners.KeyListener;
import jade.input.listeners.MouseListener;
import jade.utils.Constants;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

/**
 * Singleton class game window.
 */
public class Window {

    private static Window window;
    private final String title;
    private int width, height;
    private float r, g, b, a;
    /**
     * Address memory where the window is stored
     */
    private long glfwWindow;


    private Window() {
        this.width = Constants.Window.INITIAL_WIDTH;
        this.height = Constants.Window.INITIAL_HEIGHT;
        this.title = Constants.Window.TITLE;
        this.r = Constants.Window.R;
        this.g = Constants.Window.G;
        this.b = Constants.Window.B;
        this.a = Constants.Window.A;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        this.init();
        this.loop();

        // Free the memory
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        // Terminate GLFW and the free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        // Setup user-input callbacks
        glfwSetKeyCallback(this.glfwWindow, KeyListener::keyCallback);
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.glfwWindow);

        // Enable VSync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glfwWindow);

        // This is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context
        // that is managed externally. LWJGL detects the context that is current in the current
        // thread, creates the GLCapabilities instance and makes the OpenGL bindings available
        // for use.
        GL.createCapabilities();
    }

    private void loop() {
        while (!glfwWindowShouldClose(this.glfwWindow)) {
            glfwPollEvents();

            glClearColor(this.r, this.g, this.b, this.a);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(this.glfwWindow);
        }
    }

}
