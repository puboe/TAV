varying vec4 v_color; 
varying vec2 v_texCoords;
varying vec4 v_position;
varying vec4 v_normal;

uniform sampler2D u_texture;

uniform vec4 light_position;

uniform float light_intensity;

uniform vec3 light_color;

void main() {
    vec4 texture_color = texture2D(u_texture, v_texCoords);
    vec4 position = v_position;
    vec4 direction = normalize(light_position - v_position);

    vec4 light =  dot(v_normal, direction) * texture_color * light_intensity;
    vec3 diff = light.xyz * light_color;

    gl_FragColor = vec4(diff, 1);
}


