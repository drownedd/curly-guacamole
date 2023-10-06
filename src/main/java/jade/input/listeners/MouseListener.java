package jade.input.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener mListener;

    private final boolean[] mouseButtonPressed;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean isDragging;

    private MouseListener() {
        this.mouseButtonPressed = new boolean[3];
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        MouseListener mListener = MouseListener.get();
        mListener.lastX = mListener.xPos;
        mListener.lastY = mListener.yPos;
        mListener.xPos = xPos;
        mListener.yPos = yPos;
        mListener.isDragging =
            mListener.mouseButtonPressed[0]
            || mListener.mouseButtonPressed[1]
            || mListener.mouseButtonPressed[2];
    }

    public static MouseListener get() {
        if (MouseListener.mListener == null) {
            MouseListener.mListener = new MouseListener();
        }

        return MouseListener.mListener;
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifiers) {
        if (button >= mListener.mouseButtonPressed.length) {
            return;
        }

        MouseListener mListener = MouseListener.get();
        switch (action) {
            case GLFW_PRESS:
                mListener.mouseButtonPressed[button] = true;
                break;
            case GLFW_RELEASE:
                mListener.mouseButtonPressed[button] = false;
                mListener.isDragging = false;
                break;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        MouseListener mListener = MouseListener.get();
        mListener.scrollX = xOffset;
        mListener.scrollY = yOffset;
    }

    public static void endFrame() {
        MouseListener mListener = MouseListener.get();
        mListener.scrollX = 0.0;
        mListener.scrollY = 0.0;
        mListener.lastX = mListener.xPos;
        mListener.lastY = mListener.yPos;
    }

    public static float getScrollX() {
        return (float) (MouseListener.get().scrollX);
    }

    public static float getScrollY() {
        return (float) (MouseListener.get().scrollY);
    }

    public static float getX() {
        return (float) (MouseListener.get().xPos);
    }

    public static float getY() {
        return (float) (MouseListener.get().yPos);
    }

    public static float getDx() {
        return (float) (MouseListener.get().lastX - MouseListener.get().xPos);
    }

    public static float getDy() {
        return (float) (MouseListener.get().lastY - MouseListener.get().yPos);
    }

    public static boolean isDragging() {
        return MouseListener.get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button >= MouseListener.get().mouseButtonPressed.length) {
            return false;
        }
        return MouseListener.get().mouseButtonPressed[button];
    }
}
