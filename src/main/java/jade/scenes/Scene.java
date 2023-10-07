package jade.scenes;

import jade.renderer.Camera;
import jade.utils.Constants.SceneListing;

public abstract class Scene {

    protected Camera camera;

    protected Scene() {
    }

    public abstract void update(float dt);

    public void init() {

    }

    public final static Scene getScene(SceneListing scene) {
        switch (scene) {
            case LEVEL_EDITOR:
                return new LevelEditorScene();
            case LEVEL:
                return new LevelScene();
        }
        throw new IllegalArgumentException("Unknown scene '" + scene + "'");
    }

}
