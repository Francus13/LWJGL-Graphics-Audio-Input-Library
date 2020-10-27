#version 450

in vec4 color;
in vec2 uvCoords;

uniform sampler2D texture;

void main(){
    gl_FragColor = color * texture2D(texture, uvCoords);
}