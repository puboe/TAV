varying vec4 v_color; 
varying vec2 v_texCoords;
varying vec4 normal;
varying vec4 v_position;
varying vec4 shadow_coordinates;

uniform vec4 light_position;
uniform vec4 light_color;

uniform sampler2D u_texture;
uniform sampler2D u_shadowMap;

uniform vec4 eye;
uniform vec4 specular_color;
uniform vec4 ambient_color;
uniform vec4 light_direction;
uniform float cosine_inner;
uniform float cosine_outter;

float unpack(const vec4 texture);

void main() {
    float visibility = 1.0;
    vec3 normalized_shadow = (shadow_coordinates.xyz + vec3(1,1,1)) / 2.0;
    float diffCoordMap = unpack(texture2D(u_shadowMap, normalized_shadow.xy)) - shadow_coordinates.z;
    if ( normalized_shadow.z > unpack(texture2D(u_shadowMap, normalized_shadow.xy)) ){
                visibility = 0.5;
    }
	vec4 l = normalize(v_position - light_position);
	float cosine_light = dot(l,normalize(light_direction));
	float spotlight = smoothstep(cosine_outter,cosine_inner,cosine_light);
   	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    float normal_dot_light = dot(normal, normalize(l));
    vec4 diffusal_irradiance = spotlight  * gl_FragColor  * light_color ;
    float material = 1.2;
    vec4 r = -1.0*l + 2.0 * normal_dot_light * normal;
    vec4 m_spec = vec4(0.00001,0.00001,0.00001,1);
    vec4 specular_irradiance = max(0.0, pow(dot(r, eye-v_position), material)) * m_spec * specular_color;
    vec4 m_ambient = vec4(0.0001,0.0001,0.0001,1);
    vec4 ambient_irradiance = m_ambient * ambient_color;
    gl_FragColor = vec4((diffusal_irradiance + specular_irradiance + ambient_irradiance).xyz * visibility,gl_FragColor.a);

}

float unpack(const vec4 texture) {
    const vec4 bitSh = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
    return(dot(texture, bitSh));
}