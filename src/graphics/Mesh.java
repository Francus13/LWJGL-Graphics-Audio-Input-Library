package graphics;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46C.*;

import java.nio.FloatBuffer;

public class Mesh {

    private final int vID;
    private final int vao;

    public Mesh(){

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vID);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void render(){
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public void bind(){
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, vID);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vID);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    }

    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private static FloatBuffer createBuffer(){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(12);
        buffer.put(new float[] {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1});
        buffer.flip();
        return buffer;
    }
}
