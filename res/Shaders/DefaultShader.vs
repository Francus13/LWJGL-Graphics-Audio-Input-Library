#version 450

in vec2 position;
in vec2 textureCoords;

uniform mat4 projection;

uniform vec2 screenPos;
uniform vec2 size;

uniform vec2 textureSize;
uniform vec4 stpqCoords;

uniform float rotation;
uniform vec2 pivotCoords;

out vec2 uv;

void main(){
    if (rotation != 0) {
        vec2 pivot = screenPos + size / vec2(2, 2) + pivotCoords;
        float cosine = cos(rotation);
        float sine = sin(rotation);
        mat2 rotMat;
        rotMat[0] = vec2(cosine, -sine);
        rotMat[1] = vec2(sine, cosine);

        gl_Position = projection * vec4(rotMat * (position * size + screenPos - pivot) + pivot, 0, 1);
    }

    else {
        gl_Position = projection * vec4(position * size + screenPos, 0, 1);
    }

    //gl_Position determines where the point is placed on screen
    //uv determines point where the texture is sampled
    uv = (textureCoords * stpqCoords.pq + stpqCoords.st) / textureSize;
}