attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec4 a_normal;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_normal;
varying vec4 v_position;
varying vec4 shadow_coord;
uniform mat4 depth_mvp;
uniform mat4 u_mvp;
uniform mat4 u_model;

void main()
{
    //v_color = vec4(1, 1, 1, 1);
    v_texCoords = a_texCoord0;
    v_normal = u_model * a_normal;
    v_normal = normalize(v_normal);
    v_position = u_model * a_position;
    gl_Position =  u_mvp * a_position;
    shadow_coord = depth_mvp * a_position;
}

