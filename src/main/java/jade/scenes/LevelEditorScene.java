package jade.scenes;

import jade.gameobjects.GameObject;
import jade.gameobjects.components.SpriteRenderer;
import jade.gameobjects.components.Transform;
import jade.renderer.Camera;
import jade.utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class LevelEditorScene extends Scene {


    public LevelEditorScene() {
        super();
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : gameObjects) {
            go.update(dt);
        }

        renderer.render();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage.png")));
        this.addGameObject(obj1);

        GameObject obj2 = new GameObject("Object 1", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage2.png")));
        this.addGameObject(obj2);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }
}
