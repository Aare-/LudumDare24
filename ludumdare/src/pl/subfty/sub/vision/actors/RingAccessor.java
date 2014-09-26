package pl.subfty.sub.vision.actors;

import aurelienribon.tweenengine.TweenAccessor;

public class RingAccessor implements TweenAccessor<Ring>{

	public final static int TIME=0,
							COLOR_A=1,
							ROTATION=2;
	
	@Override
	public int getValues(Ring arg0, int arg1, float[] arg2) {
		switch(arg1){
		case TIME:
			arg2[0] = arg0.time;
			return 1;
		case COLOR_A:
			arg2[0] = arg0.color.a;
			return 1;
		case ROTATION:
			arg2[0] = arg0.rotation;
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(Ring arg0, int arg1, float[] arg2) {
		switch(arg1){
		case TIME:
			arg0.time = arg2[0];
			break;
		case COLOR_A:
			arg0.color.a = arg2[0];
			break;
		case ROTATION:
			arg0.rotation = arg2[0];
			break;
		}
	}

}
