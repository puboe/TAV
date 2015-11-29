varying vec4 Shadowcoord;

void main() {
    const vec4 packFactors = vec4(255.0 * 255.0 * 255.0, 255.0 * 255.0, 255.0, 1);
    const vec4 bitMask     = vec4(0.0,1.0/255.0,1.0/255.0,1.0/255.0);

    vec3 normalizedShadowCoord = (Shadowcoord.xyz + vec3(1,1,1)) / 2.0;

    vec4 packedValue = vec4(fract(normalizedShadowCoord.z*packFactors));
    packedValue -= packedValue.xxyz * bitMask;
    gl_FragColor = packedValue;
}