package abused_master.abusedlib.client.shaders;

import abused_master.abusedlib.AbusedLib;
//import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ShaderHelper {

    public static int ticksInGame;

    //Example - loadShaderProgram(new Identifier("MODID", "/shaders/testVS.vs"), new Identifier("MODID", "/shaders/testFS.fs"));
    public static int loadShaderProgram(Identifier vshID, Identifier fshID) {
        int vertexShader = createShader(vshID, GL20.GL_VERTEX_SHADER);
        int fragmentShader = createShader(fshID, GL20.GL_FRAGMENT_SHADER);
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        return program;
    }

    public static int loadVertexShaderProgram(Identifier vshID) {
        int vertexShader = createShader(vshID, GL20.GL_VERTEX_SHADER);
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glLinkProgram(program);

        return program;
    }

    public static int loadFragmentShaderProgram(Identifier fshID) {
        int fragmentShader = createShader(fshID, GL20.GL_FRAGMENT_SHADER);
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        return program;
    }

    public static int createShader(Identifier shaderFile, int shaderType) {
        int shader = GL20.glCreateShader(shaderType);

        if(shader == 0) {
            return 0;
        }

        try {
            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(shaderFile));
        }catch (Exception e) {
            e.printStackTrace();
        }

        GL20.glCompileShader(shader);

        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
        }

        return shader;
    }

    public static void useShader(int shader, Consumer<Integer> callback) {
        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0 && callback != null) {
            int ticks = ARBShaderObjects.glGetUniformLocationARB(shader, "ticks");
            ARBShaderObjects.glUniform1iARB(ticks, ticksInGame);

            callback.accept(shader);
        }
    }

    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        useShader(0);
    }

    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    /**
     * Read a shader file as a string
     * @param shaderFile - The identifier leading to the file
     * @return - The shader file as a string
     */
    public static String readFileAsString(Identifier shaderFile) {
        AbusedLib.LOGGER.info("Loading shader file " + shaderFile.toString());

        InputStream in = getShaderFile(shaderFile);
        String s = "";

        if (in != null){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                s = reader.lines().collect(Collectors.joining("\n"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                AbusedLib.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            } catch (IOException e) {
                e.printStackTrace();
                AbusedLib.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
            }
        }

        return s;
    }

    /**
     * Grab the shader file from the assets dir
     * @param shaderFile - The identifier leading to the file
     * @return - The InputStream for the shader file
     */
    public static InputStream getShaderFile(Identifier shaderFile) {
        if(MinecraftClient.getInstance().getResourceManager().containsResource(shaderFile)) {
            try {
                return MinecraftClient.getInstance().getResourceManager().getResource(shaderFile).getInputStream();
            } catch (IOException e) {
                AbusedLib.LOGGER.fatal("Unable to parse shader file! Source: " + shaderFile.toString(), e);
                return null;
            }
        }else {
            AbusedLib.LOGGER.fatal("Unable to find shader file! Source: " + shaderFile.toString());
            return null;
        }
    }
}
