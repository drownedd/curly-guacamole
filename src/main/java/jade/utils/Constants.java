package jade.utils;

public class Constants {


    public static enum SceneListing {
        LEVEL_EDITOR, LEVEL;
    }

    public static class Window {

        public static final int INITIAL_WIDTH = 1920, INITIAL_HEIGHT = 1080;

        public static final String TITLE = "Mario";
    }

    public static class Shaders {

        public static enum Fragments {
            DEFAULT("#version 330 core\n"
                    + "\n"
                    + "in vec4 fColor;\n"
                    + "\n"
                    + "out vec4 color;\n"
                    + "\n"
                    + "void main() {\n"
                    + "    color = fColor;\n"
                    + "}");

            private final String value;

            Fragments(String value) {
                this.value = value;
            }

            public String getValue() {
                return this.value;
            }
        }

        public static enum Vertices {
            DEFAULT("#version 330 core\n"
                    + "\n"
                    + "layout(location=0) in vec3 aPos;\n"
                    + "layout(location=1) in vec4 aColor;\n"
                    + "\n"
                    + "out vec4 fColor;\n"
                    + "\n"
                    + "void main() {\n"
                    + "    fColor = aColor;\n"
                    + "    gl_Position = vec4(aPos, 1.0);\n"
                    + "}");

            private final String value;

            Vertices(String value) {
                this.value = value;
            }

            public String getValue() {
                return this.value;
            }
        }

    }
}
