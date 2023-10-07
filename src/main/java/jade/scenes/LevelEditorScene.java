package jade.scenes;

import static jade.utils.Constants.Shaders.DEFAULT_SHADER_FILE_PATH;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
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

import jade.renderer.Shader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
        // Position and color of the vertices
        // (x, y , z, r, g, b, a)
        0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // Bottom right  0
        0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, // Top right      1
        -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Top left      2
        -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        0, 1, 2, // Top right triangle
        0, 2, 3 // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    public LevelEditorScene() {
        super();
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
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
        defaultShader = new Shader(DEFAULT_SHADER_FILE_PATH);
        defaultShader.compile();

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
        int positionsSize = 3, colorSize = 4, floatSizeInBytes = 4;
        int vertexSizeInBytes = (positionsSize + colorSize) * floatSizeInBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes,
            positionsSize * floatSizeInBytes);
        glEnableVertexAttribArray(1);
    }
}
