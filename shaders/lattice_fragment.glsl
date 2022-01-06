#version 330 core

out vec4 FragColor;
uniform vec4 cellColor;

void main() {
	FragColor = cellColor;
}