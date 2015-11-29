attribute vec4 a_position;
uniform mat4 u_mvp;
varying vec4 Shadowcoord;

void main()
{
    gl_Position =  u_mvp * a_position;
    Shadowcoord = gl_Position;

}