package jade.scenes;

import static jade.utils.Constants.Shaders.DEFAULT_SHADER_FILE_PATH;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import jade.renderer.Camera;
import jade.renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jade.renderer.Texture;
import jade.utils.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {

    private final float[] vertexArray = {
            // Position and color of the vertices
            // (x, y , z, r, g, b, a)
            // POS            // RGBA                 // UV coords
            100f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1, 1, // Bottom right  0
            100f, 100f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1, 0, // Top right      1
            0.0f, 100f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0, 0, // Top left      2
            0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0, 1, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elementArray = {
            0, 1, 2, // Top right triangle
            0, 2, 3 // Bottom left triangle
    };

    private Shader defaultShader;

    private Texture testTexture;

    private int vaoID;

    public LevelEditorScene() {
        super();
    }

    @Override
    public void update(float dt) {
//        camera.position.x -= dt * 50.0f;
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEXT_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader(DEFAULT_SHADER_FILE_PATH);
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/test.png");

        int vboID, eboID;
        // VAO: Vertex Array Object
        // VBO: Vertex Buffer Object
        // EBO: Element Buffer Object
        // Generate VAO, VBO, and EBO buffer objects
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO, upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers (offsets)
        int positionsSize = 3, colorSize = 4, uvSize = 2;
        int vertexSizeInBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes,
                positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeInBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }
}
