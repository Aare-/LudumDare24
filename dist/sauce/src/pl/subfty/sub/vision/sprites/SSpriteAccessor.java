package pl.subfty.sub.vision.sprites;

import aurelienribon.tweenengine.TweenAccessor;

public class SSpriteAccessor implements TweenAccessor<SSprite>{

	public final static int ALPHA=0,
							SCALE_X=3,
							SCALE_Y=4,
							SCALE_XY=5,
							POSITION_XY=6,
							RADIUS_POS=7;
	
	@Override
	public int getValues(SSprite target, int type, float[] val) {
		switch(type){
		case ALPHA:
			val[0] = target.alpha;
			return 1;
		case SCALE_X:
			val[0] = target.getScaleX();
			return 1;
		case SCALE_Y:
			val[0] = target.getScaleY();
			return 1;
		case SCALE_XY:
			val[0] = target.getScaleX();
			val[1] = target.getScaleY();
			return 2;
		case POSITION_XY:
			val[0] = target.getX();
			val[1] = target.getY();
			return 2;
		}
		return 0;
	}

	@Override
	public void setValues(SSprite target, int type, float[] val) {
		switch(type){
		case ALPHA:
			target.alpha = val[0];
			break;
		case SCALE_X:
			target.setScale(val[0], target.getScaleY());
			break;
		case SCALE_Y:
			target.setScale(target.getScaleX(), val[0]);
			break;
		case SCALE_XY:
			target.setScale(val[0], val[1]);
			break;
		case POSITION_XY:
			target.setPosition(val[0], val[1]);
			break;
		}
	}
}
