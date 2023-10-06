package jade.input.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener kListener;

    private final boolean[] keyPressed;

    private KeyListener() {
        this.keyPressed = new boolean[350];
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int modifiers) {
        KeyListener kListener = KeyListener.get();
        switch (action) {
            case GLFW_PRESS:
                kListener.keyPressed[key] = true;
                break;
            case GLFW_RELEASE:
                kListener.keyPressed[key] = false;
                break;
        }
    }

    public static KeyListener get() {
        if (KeyListener.kListener == null) {
            KeyListener.kListener = new KeyListener();
        }

        return KeyListener.kListener;
    }

    public static boolean isKeyPressed(int keyCode) throws ArrayIndexOutOfBoundsException {
        return KeyListener.get().keyPressed[keyCode];
    }
}
