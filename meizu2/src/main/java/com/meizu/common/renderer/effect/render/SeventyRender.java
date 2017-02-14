package com.meizu.common.renderer.effect.render;

import com.meizu.common.renderer.effect.GLCanvas;

public class SeventyRender extends PixelsRender {
    private static final String FRAG = "precision mediump float; \nuniform sampler2D sTexture; \nuniform float uAlpha;\nvarying vec2 vTexCoord; \nvec3 GammaCorrection(vec3 color, float gamma) \n{ \n   return vec3(pow(color.r, 1.0 / gamma), pow(color.g, 1.0 / gamma), pow(color.b, 1.0 / gamma)); \n} \nfloat SingleChannelGammaCorrection(float value, float gamma) \n{ \n  return pow(value, 1.0 / gamma); \n} \nvec3 LevelsControlInputRange(vec3 color, float minInput, float maxInput) \n{ \n  return min(max(color - vec3(minInput), vec3(0.0)) / (vec3(maxInput) - vec3(minInput)), vec3(1.0)); \n} \nfloat SingleChannelControlInputRange(float value, float minInput, float maxInput) \n{ \n   return min(max(value - minInput, 0.0) / (maxInput - minInput), 1.0); \n} \nvec3 LevelsControlInput(vec3 color, float minInput, float gamma, float maxInput) \n{ \n   return GammaCorrection(LevelsControlInputRange(color, minInput, maxInput), gamma); \n} \nfloat SingleChannelControlInput(float value, float minInput, float gamma, float maxInput) \n{ \n   return SingleChannelGammaCorrection(SingleChannelControlInputRange(value, minInput, maxInput), gamma); \n} \nvec3 LevelsControlOutputRange(vec3 color, float minOutput, float maxOutput) \n{ \n   return mix(vec3(minOutput), vec3(maxOutput), color); \n} \nfloat SingleLevelsControlOutputRange(float value, float minOutput, float maxOutput) \n{ \n   return mix(minOutput, maxOutput, value); \n} \nvec3 LevelsControl(vec3 color, float minInput, float gamma, float maxInput, float minOutput, float maxOutput) \n{ \n   vec3 tmpColor = LevelsControlInput(color, minInput, gamma, maxInput); \n   return LevelsControlOutputRange(tmpColor, minOutput, maxOutput); \n} \nfloat SingleChannelLevelControl(float value, float minInput, float gamma, float maxInput, float minOutput, float maxOutput) \n{ \n   float tmpValue = SingleChannelControlInput(value, minInput, gamma, maxInput); \n   return SingleLevelsControlOutputRange(tmpValue, minOutput, maxOutput); \n} \nvoid main() \n{ \n   vec4 color = texture2D(sTexture, vTexCoord); \n   color.rgb = LevelsControl(color.rgb, 30.0 / 255.0, 0.75, 211.0 / 255.0, 73.0 / 255.0, 236.0 / 255.0); \n   color.r = SingleChannelLevelControl(color.r, 0.0, 1.38, 236.0 / 255.0, 30.0 / 255.0, 231.0/255.0); \n    color.g = SingleChannelLevelControl(color.g, 0.0, 0.79, 236.0/255.0, 46.0/255.0, 221.0/255.0); \n    color.b = SingleChannelLevelControl(color.b, 5.0/255.0, 0.96, 221.0/255.0, 3.0/255.0, 241.0/255.0); \n    gl_FragColor.rgb = color.rgb; \n   gl_FragColor.a *= uAlpha; \n} \n";

    public SeventyRender(GLCanvas gLCanvas) {
        super(gLCanvas);
        this.mKey = Render.SEVENTY;
    }

    public String getFragmentShader() {
        return FRAG;
    }
}
