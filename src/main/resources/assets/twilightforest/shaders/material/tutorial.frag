#include frex:shaders/api/fragment.glsl
#include frex:shaders/lib/math.glsl
#include frex:shaders/lib/noise/noise3d.glsl
#include frex:shaders/api/world.glsl

// holds our noise coordinates from the vertex shader
in vec3 pos;

// The name of this method is special - Canvas will call it for each fragment associated with your shader
void frx_startFragment(inout frx_FragmentData fragData) {
    // modify appearance where stone texture is lighter in color

    // get a time value we can use for animation
    float time = frx_renderSeconds();

    // mix 'em up!
//    vec4 highlight = mix(, vec4(0.7, 1.0, 1.0, 1.0), snoise(pos));

    // call animated noise function with noise coordinates scaled and shifted
//    float blend_weight = time * 2.0;

    // mix with the stone texture colors
    fragData.spriteColor = vec4(0.0, 0.0, 0.0, 1.0);
    fragData.vertexColor = vec4(0.0, 0.0, 0.0, 1.0);

    // make these fragments fully lit
    fragData.emissivity = 1.0;
//    fragData.ao = false;
//    fragData.diffuse = false;
}