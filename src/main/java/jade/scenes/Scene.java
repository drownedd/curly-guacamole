package jade.scenes;

import jade.utils.Constants.SceneListing;

public abstract class Scene {

    protected Scene() {
    }

    public abstract void update(float dt);

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
