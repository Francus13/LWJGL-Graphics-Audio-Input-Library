package graphicsLibrary;

import java.nio.FloatBuffer;

public class Matrix4x4 {
    private final float[][] m;

    public Matrix4x4(){
        m = new float[4][4]; //Initialized to 0
        m[3][3] = 1; //Controls the zoom level
    }

    public void getBuffer(FloatBuffer buffer){
        buffer.put(m[0][0]).put(m[0][1]).put(m[0][2]).put(m[0][3]);
        buffer.put(m[1][0]).put(m[1][1]).put(m[1][2]).put(m[1][3]);
        buffer.put(m[2][0]).put(m[2][1]).put(m[2][2]).put(m[2][3]);
        buffer.put(m[3][0]).put(m[3][1]).put(m[3][2]).put(m[3][3]);
        buffer.flip();
    }

    public void ortho(float left, float right, float top, float bottom){
        float width = right - left;
        float height = top - bottom;

        //Makes a transformation matrix that maps the (0, 0) to (width, height) coordinate system to the (-1, -1) to (1, 1) coordinate system that is used by the shader
        m[0][0] = 2 / width;
        m[1][1] = 2 / height;
        m[3][0] = -(right + left) / width;
        m[3][1] = -(bottom + top) / height;
    }
}
