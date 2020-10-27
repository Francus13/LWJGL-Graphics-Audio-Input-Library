package graphics;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46C.*;

public class Shader {
    private int program;
    private int vs;
    private int fs;

    public Shader(String fileName){
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, createShader(fileName + ".vs"));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, createShader(fileName + ".fs"));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "uv");

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    private String createShader(String fileName){
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader(new File("res/Shaders/" + fileName)));
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void setUniform(String name, float x, float y){
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform2f(location, x, y);
    }

    public void setUniform(String name, float x, float y, float z, float w){
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform4f(location, x, y, z, w);
    }

    public void setUniform(String name, Color c){
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform4f(location, c.r(), c.g(), c.b(), c.a());
    }

    public void setUniform(String name, Matrix4x4 m){
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        m.getBuffer(buffer);
        if (location != -1)
            glUniformMatrix4fv(location, false, buffer);
    }

    public void bind() {glUseProgram(program);}
    public void unbind() {glUseProgram(0);}
}
