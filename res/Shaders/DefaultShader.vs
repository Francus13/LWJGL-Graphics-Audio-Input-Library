#version 450

in vec2 position;
in vec2 textureCoords;

uniform vec4 matColor;
uniform mat4 projection;
uniform vec2 pixelScale;
uniform vec2 screenPos;

out vec4 color;
out vec2 uvCoords;

void main(){
    color = matColor;

    gl_Position = projection * vec4(position.xy * pixelScale + screenPos, 0, 1);
    uvCoords = textureCoords;
}