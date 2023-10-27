package jade.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.*;
import org.lwjgl.BufferUtils;

public class Shader {

    private final String filepath;
    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private boolean isBeingUsed;

    public Shader(String filePath) {
        this.filepath = filePath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitSource = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first and second patterns after #type 'patterns'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            setPattern(firstPattern, splitSource[1]);
            setPattern(secondPattern, splitSource[2]);
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open shader file: '" + filePath + "'";
        }
    }

    private void setPattern(String pattern, String patternSource) throws IOException {
        if (pattern.equals("vertex")) {
            vertexSource = patternSource;
        } else if (pattern.equals("fragment")) {
            fragmentSource = patternSource;
        } else {
            throw new IOException("Unexpected token '" + pattern + "'");
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        // Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU and compile
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU and compile
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    /**
     * Bind shader program
     */
    public void use() {
        if (!isBeingUsed) {
            glUseProgram(shaderProgramID);
            isBeingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        isBeingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadVec4f(String varName, Vector4f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float value) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varName, int value) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, value);
    }

    public void uploadIntArray(String varName, int[] array) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1iv(varLocation, array);
    }

    public void uploadTexture(String varName, int slot) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, slot);
    }
}
