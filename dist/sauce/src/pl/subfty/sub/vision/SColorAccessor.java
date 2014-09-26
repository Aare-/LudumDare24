package pl.subfty.sub.vision;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Color;

public class SColorAccessor implements TweenAccessor<Color>{
	public final static int RGB = 0;
	@Override
	public int getValues(Color color, int arg1, float[] arg2) {
		switch(arg1){
		case RGB:
			arg2[0] = color.r;
			arg2[1] = color.g;
			arg2[2] = color.b;
			return 3;
		}
		return 0;
	}

	@Override
	public void setValues(Color color, int arg1, float[] arg2) {
		switch(arg1){
		case RGB:
			color.set(arg2[0], arg2[1], arg2[2], 1);
			break;
		}
	}
}
