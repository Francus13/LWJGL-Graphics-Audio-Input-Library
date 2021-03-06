package graphics;

import java.nio.FloatBuffer;

public class Matrix4x4 {
    private float[][] m = new float[4][4];

    public Matrix4x4(){
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;
    }

    public void getBuffer(FloatBuffer buffer){
        buffer.put(m[0][0]).put(m[0][1]).put(m[0][2]).put(m[0][3]);
        buffer.put(m[1][0]).put(m[1][1]).put(m[1][2]).put(m[1][3]);
        buffer.put(m[2][0]).put(m[2][1]).put(m[2][2]).put(m[2][3]);
        buffer.put(m[3][0]).put(m[3][1]).put(m[3][2]).put(m[3][3]);
        buffer.flip();
    }

    public static Matrix4x4 ortho(float left, float right, float top, float bottom){
        Matrix4x4 matrix = new Matrix4x4();

        float width = right - left;
        float height = top - bottom;

        matrix.m[0][0] = 2f / width;
        matrix.m[1][1] = 2f / height;
        matrix.m[3][0] = -(right + left) / width;
        matrix.m[3][1] = -(bottom + top) / height;

        return matrix;
    }
}
