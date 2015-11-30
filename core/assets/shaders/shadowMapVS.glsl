attribute vec4 a_position;
uniform mat4 u_worldView;
varying vec4 shadow_coord;
void main()
{
    gl_Position = u_worldView * a_position;
    shadow_coord = u_worldView * a_position;
}