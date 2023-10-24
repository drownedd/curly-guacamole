package jade.scenes;

import jade.gameobjects.GameObject;
import jade.renderer.Camera;
import jade.renderer.Renderer;
import jade.utils.Constants.SceneListing;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected final Renderer renderer;
    protected Camera camera;
    private boolean isRunning;

    protected final List<GameObject> gameObjects;

    protected Scene() {
        this.gameObjects = new ArrayList<>();
        this.renderer = new Renderer();
    }

    public abstract void update(float dt);

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
            renderer.add(go);
        }
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

    public Camera getCamera() {
        return camera;
    }

}
