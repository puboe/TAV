varying vec4 shadow_coord;

void main() {

	vec3 c_shadow_coord = (shadow_coord.xyz + vec3(1,1,1)) / 2.0;
    vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
    vec4 bitMsk = vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
    vec4 fract_result = fract(c_shadow_coord.z * bitSh);
    fract_result -= fract_result.xxyz * bitMsk;
    gl_FragColor = fract_result;
}
