varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec2 u_resolution, u_pos;
uniform sampler2D u_sampler2D;
uniform float u_outRadius, u_inRadius, u_intensity;

void main()
{
	vec4 color = texture2D(u_sampler2D, v_texCoord0)*v_color;
	
	vec2 relativePosition = gl_FragCoord.xy/u_resolution-u_pos;
	relativePosition.x *= u_resolution.x/u_resolution.y;
	float len = length(relativePosition);
	float vignette = smoothstep(u_outRadius, u_inRadius, len);
	color.rgb = mix(color.rgb, color.rgb*vignette, u_intensity);
	
	gl_FragColor = color;
}