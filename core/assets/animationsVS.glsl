attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec3 a_tangent;
attribute vec2 a_texCoord0;
varying vec2 v_texCoord;
varying vec4 v_color;

varying vec3 viewSpaceNormal;
varying vec3 viewSpacePos;
varying vec3 viewSpaceTangent;

uniform mat4 u_mvpMatrix;
uniform mat4 u_modelViewMatrix;
uniform mat4 u_normalMatrix;


//Firstly, we need to define loads of new attributes, one for each bone.
attribute vec2 a_boneWeight0;
attribute vec2 a_boneWeight1;
attribute vec2 a_boneWeight2;
attribute vec2 a_boneWeight3;
attribute vec2 a_boneWeight4;

//We also need to take the bonematrices
uniform mat4 u_bones[12];

void main() {
    v_color = vec4(1, 1, 1, 1);
    // Calculate skinning for each vertex
    mat4 skinning = mat4(0.0);
    skinning += (a_boneWeight0.y) * u_bones[int(a_boneWeight0.x)];
    skinning += (a_boneWeight1.y) * u_bones[int(a_boneWeight1.x)];
    skinning += (a_boneWeight2.y) * u_bones[int(a_boneWeight2.x)];
    skinning += (a_boneWeight3.y) * u_bones[int(a_boneWeight3.x)];
    skinning += (a_boneWeight4.y) * u_bones[int(a_boneWeight4.x)];

    //Include skinning into the modelspace position
    vec4 pos = skinning * vec4(a_position);

    // Rest of code is just like usual
    viewSpacePos = vec3((u_modelViewMatrix * pos).xyz);
    viewSpaceNormal = normalize(vec3(u_normalMatrix * skinning * vec4(a_normal, 0.0)).xyz); //viewspaceNormal
    viewSpaceTangent = vec3(u_normalMatrix * vec4(a_tangent,1.0));
    //gl_Position = u_mvpMatrix * pos;
    //v_texCoord = a_texCoord0;

    //viewSpacePos = vec3(u_modelViewMatrix * a_position);
    //viewSpaceNormal = vec3(u_normalMatrix * vec4(a_normal,1.0));
    //viewSpaceTangent = vec3(u_normalMatrix * vec4(a_tangent,1.0));
    gl_Position = u_mvpMatrix * pos;
    v_texCoord = a_texCoord0;

}