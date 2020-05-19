varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec2 u_resolution, u_pos[2];
uniform vec3 u_newColor[2];
uniform sampler2D u_sampler2D;
uniform float u_outRadius, u_inRadius, u_intensity;

void vignette() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0)*v_color;
	
	vec2 relativePosition = gl_FragCoord.xy/u_resolution-u_pos[0];
	float len = length(relativePosition);
	float vignette = smoothstep(u_outRadius, u_inRadius, len);
	color.rgb = mix(color.rgb, color.rgb*vignette, u_intensity);
	
	gl_FragColor = color;
}

void main() {
	vignette();
}