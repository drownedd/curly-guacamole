package jade.scenes;

import jade.Window;
import jade.input.listeners.KeyListener;
import jade.utils.Constants.SceneListing;
import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene;
    private float timeToChangeScene = 2;

    public LevelEditorScene() {
        super();
        System.out.println("Inside level editor scene.");
    }

    @Override
    public void update(float dt) {
        System.out.println((1.0f / dt) + " FPS");

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0.0f) {
            timeToChangeScene -= dt;

            Window window = Window.get();
            window.r -= dt * 5.0f;
            window.g -= dt * 5.0f;
            window.b -= dt * 5.0f;
            window.a -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(SceneListing.LEVEL);
        }
    }
}
