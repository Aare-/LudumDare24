package pl.subfty.sub.vision.actors;

import aurelienribon.tweenengine.TweenAccessor;

public class ClockAccessor implements TweenAccessor<ClockShape> {

	public final static int POS_X = 0, POS_Y = 1, POS_XY = 2, RADIUS = 3,
			TIME = 4, ALPHA = 5, BORDER_A = 6, ROTATION = 7;

	@Override
	public int getValues(ClockShape target, int type, float[] val) {
		switch (type) {
		case POS_X:
			val[0] = target.x;
			return 1;
		case POS_Y:
			val[0] = target.y;
			return 1;
		case POS_XY:
			val[0] = target.x;
			val[1] = target.y;
			return 2;
		case RADIUS:
			val[0] = target.radius;
			return 1;
		case TIME:
			val[0] = target.time;
			return 1;
		case ALPHA:
			val[0] = target.alpha;
			return 1;
		case BORDER_A:
			val[0] = target.borderMixColorA;
			return 1;
		case ROTATION:
			val[0] = target.rotation;
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(ClockShape target, int type, float[] val) {
		switch (type) {
		case POS_X:
			target.x = val[0];
			break;
		case POS_Y:
			target.y = val[0];
			break;
		case POS_XY:
			target.x = val[0];
			target.y = val[1];
			break;
		case RADIUS:
			target.radius = val[0];
			break;
		case TIME:
			target.setTime(val[0]);
			break;
		case ALPHA:
			target.alpha = val[0];
			break;
		case BORDER_A:
			target.borderMixColorA = val[0];
			break;
		case ROTATION:
			target.rotation = val[0];
			break;
		}
	}

}
