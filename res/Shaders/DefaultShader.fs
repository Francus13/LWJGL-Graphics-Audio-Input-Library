#version 450

uniform vec4 color;
in vec2 uv;

uniform sampler2D texture;

void main(){
    gl_FragColor = color * texture(texture, uv);
}