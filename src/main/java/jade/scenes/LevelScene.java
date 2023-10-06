package jade.scenes;

import jade.Window;

public class LevelScene extends Scene {

    public LevelScene() {
        super();
        System.out.println("Inside level scene.");
        Window window = Window.get();

        window.r = 1;
        window.g = 1;
        window.b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
