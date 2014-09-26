package pl.subfty.sub.vision.fonts;

import aurelienribon.tweenengine.TweenAccessor;

public class STextAccessor implements TweenAccessor<SText>{
	public final static int SCALE=0,
							POSITION_XY=1,
							ALPHA=2,
							FALPHA=3,
							NUM=4,
							CALPHA=5,
							FADE_IN=6;
	
	@Override
	public int getValues(SText arg0, int arg1, float[] arg2) {
		switch (arg1) {
		case SCALE:
			arg2[0] = arg0.scale;
			return 1;
		case POSITION_XY:
			arg2[0] = arg0.x;
			arg2[1] = arg0.y;
			return 2;
		case ALPHA:
			arg2[0] = arg0.alpha;
			return 1;
		case NUM:
			arg2[0] = arg0.settedNumberF;
			return 1;
		case CALPHA:
			arg2[0] = arg0.c.a;
			return 1;
		case FADE_IN:
			arg2[0] = arg0.fadeInA;
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(SText arg0, int arg1, float[] arg2) {
		switch (arg1) {
		case SCALE:
			arg0.scale = arg2[0];
			break;
		case POSITION_XY:
			arg0.x = arg2[0];
			arg0.y = arg2[1];
			break;
		case ALPHA:
			arg0.alpha = arg2[0];
			break;
		case NUM:
			arg0.setNumberF(arg2[0], 15);
			break;
		case CALPHA:
			arg0.c.a = arg2[0];
			break;
		case FADE_IN:
			arg0.fadeInA = arg2[0];
			break;
		}
	}

}
