#version 330

layout (std140) uniform Matrices {
    mat4 m_pvm;
    mat4 m_viewModel;
    mat3 m_normal;
};

layout (std140) uniform Materials {
    vec4 diffuse;
    vec4 ambient;
    vec4 specular;
    float shininess;
};

layout (std140) uniform Lights {
    vec3 l_dir;    // camera space
};

in vec4 position;   // local space
in vec3 normal;     // local space

// the data to be sent to the fragment shader
out Data {
    vec4 color;
} DataOut;

void main () {

    // set the specular term initially to black
    vec4 spec = vec4(0.0);

    v_normal = u_model * a_normal;
    v_normal = normalize(v_normal);

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

    gl_Position = m_pvm * position;
}