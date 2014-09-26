package pl.subfty.sub.vision.stage;

import aurelienribon.tweenengine.TweenAccessor;

public class SScreenAccessor implements TweenAccessor<SScreen>{
	public final static int POS_X=0,
							POS_Y=1;

	@Override
	public int getValues(SScreen target, int type, float[] returnValues) {
		switch(type){
		case POS_X:
			returnValues[0] = target.cntnt.x;
			return 1;
		case POS_Y:
			returnValues[0] = target.cntnt.y;
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(SScreen target, int type, float[] newValues) {
		switch(type){
		case POS_X:
			target.cntnt.x=newValues[0];
			break;
		case POS_Y:
			target.cntnt.y=newValues[0];
			break;
		}
	}

}
