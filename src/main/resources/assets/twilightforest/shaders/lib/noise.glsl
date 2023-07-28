#include frex:shaders/lib/noise/noise3d.glsl

float tf_fractalNoise(float size, vec3 pos) {
    float result = ((snoise(vec3(
    (pos.x + (2 * size)) / size,
    (pos.y + (2 * size)) / size,
    (pos.z + (2 * size)) / size))
    + 1.0f) * 0.5f);

    return result + ((snoise(vec3(
    (pos.x + (1 * size)) / size,
    (pos.y + (1 * size)) / size,
    (pos.z + (1 * size)) / size))
    + 1.0f) * 0.5f);
}

float tf_rippleFractialNoise(float size, vec3 pos, float minimum, float maximum, float frequency) {
    float i = maximum - minimum;
    return abs((mod((tf_fractalNoise(size, pos) * frequency), (2 * i))) - i) + minimum;
}

vec3 tf_hsv2rgb(vec3 c){
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 tf_rgb2hsv(vec3 c){
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}