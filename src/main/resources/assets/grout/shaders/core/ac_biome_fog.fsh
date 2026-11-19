#version 150

// Vanilla's fog (see its fog.glsl) drawn over a finished picture, working out each pixel's distance from the depth texture.

uniform sampler2D Sampler0;
uniform mat4 ClipToCamera;
uniform float BiomeFogStart;
uniform float BiomeFogEnd;
uniform float Cylinder;
uniform vec3 BiomeFogColor;
uniform float Strength;

in vec2 screenUv;

out vec4 fragColor;

void main() {
    float depth = texture(Sampler0, screenUv).r;
    vec4 position = ClipToCamera * vec4(screenUv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    position /= position.w;
    float away = Cylinder > 0.5 ? max(length(position.xz), abs(position.y)) : length(position.xyz);
    float fog = away < BiomeFogEnd ? smoothstep(BiomeFogStart, BiomeFogEnd, away) : 1.0;
    fragColor = vec4(BiomeFogColor, fog * Strength);
}
