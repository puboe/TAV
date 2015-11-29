varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_position;
varying vec4 v_normal;
uniform sampler2D u_texture;
uniform vec4 light_position;
uniform float cone_angel;
uniform vec4 cone_direction;
uniform float light_intensity;
uniform float u_shininess;
uniform vec4 light_color;

void main() {

    vec4 texture_color = texture2D(u_texture, v_texCoords);
    //Direccion de la luz Vector L de las ppt
    vec3 l = normalize(light_position.xyz - v_position.xyz);
    //Calculamos la direccion a la camara
    vec3 eye = normalize(-v_position.xyz);
    //Calculamos vector R de la ppt
    vec3 r = normalize(-reflect(l,v_normal.xyz));

    float lightToSurfaceAngle = dot(l, cone_direction.xyz);


    float attenuation = 0.0;
    if(lightToSurfaceAngle > cone_angel){
        attenuation = 1.0;
    }

    //Calculamos componente difusa
    vec4 idiff = max(0.0, (dot(v_normal.xyz, normalize(cone_direction.xyz)))) * texture_color * light_intensity * light_color;
    idiff = clamp(idiff,0.0,1.0);

    //Calculamos la componente especular
    vec4 ispec = pow(max(dot(r,l),0.0), u_shininess) * texture_color * light_intensity * light_color;
    ispec = clamp(ispec,0.0,1.0);

    gl_FragColor = vec4(idiff.xyz * attenuation + ispec.xyz * attenuation, 1);
}
