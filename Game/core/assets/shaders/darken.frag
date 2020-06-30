varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec3 u_newColor;
uniform sampler2D u_sampler2D;

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0)*v_color;
	color.rgb += u_newColor;
	gl_FragColor = color;
}