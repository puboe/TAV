varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_position;
varying vec4 v_normal;

uniform sampler2D u_texture;

uniform vec4 light_position;

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

    //Calculamos componente difusa
    vec4 idiff = max(0.0, (dot(v_normal.xyz, l))) * texture_color * light_intensity * light_color;
    idiff = clamp(idiff,0.0,1.0);



    //Calculamos la componente especular
    vec4 ispec = pow(max(dot(r,eye),0.0), u_shininess) * texture_color * light_intensity * light_color;
    ispec = clamp(ispec,0.0,1.0);

    gl_FragColor = vec4(idiff.xyz + ispec.xyz, 1);


     // set the specular term initially to black
            vec4 spec = vec4(0.0);

            vec3 n = normalize(m_normal * normal);

            float intensity = max(dot(n, l_dir), 0.0);

            // if the vertex is lit compute the specular term
            if (intensity > 0.0) {
                // compute position in camera space
                vec3 pos = vec3(m_viewModel * position);
                // compute eye vector and normalize it
                vec3 eye = normalize(-pos);
                // compute the half vector
                vec3 h = normalize(l_dir + eye);

                // compute the specular term into spec
                float intSpec = max(dot(h,n), 0.0);
                spec = specular * pow(intSpec, shininess);
            }
            // add the specular term
            DataOut.color = max(intensity *  diffuse + spec, ambient);
}


