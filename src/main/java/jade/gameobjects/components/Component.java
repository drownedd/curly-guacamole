package jade.gameobjects.components;

import jade.gameobjects.GameObject;

public abstract class Component {

    public GameObject gameObject;

    public abstract void update(float dt);

    public void start() {

    }
}
