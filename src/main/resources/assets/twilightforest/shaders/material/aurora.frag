#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl
#include frex:shaders/api/world.glsl
#include twilightforest:shaders/lib/noise.glsl

void frx_materialFragment() {
        float time = frx_renderSeconds();
        vec3 ripple = vec3(tf_rippleFractialNoise(128.0f, frx_cameraPos + frx_vertex.xyz + time, 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
        ripple = tf_hsv2rgb(vec3(ripple.x, ripple.y * 0.5F, min(ripple.z + 0.4F, 0.9F)));
        frx_fragColor = frx_fragColor * vec4(ripple.x, ripple.y, ripple.z, 1);

        // make these fragments fully lit
        // re-add once the structure has been redesigned
//        frx_fragEmissive = 1.0;
//        frx_fragEnableAo = true;
//        frx_fragEnableDiffuse = true;
}