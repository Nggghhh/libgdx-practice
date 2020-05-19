varying vec4 v_color;
varying vec2 v_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_resolution, u_pos;
uniform sampler2D u_sampler2D;

float circleShape(vec2 position, float radius) {
	return step(radius, length(position));
}

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0)*v_color;
	color.rgb -= vec3(200.0/255.0, 200.0/255.0, 200.0/255.0);
	
	gl_FragColor = color;
}