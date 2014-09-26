package pl.subfty.sub.rand;

import pl.subfty.sub.Sub;


public class RangedRandDelay {
	public float actValue,
				  rangeDelay,
				  extra=0;
	public boolean active;
	
	private float maxDelay,
				  minDelay;
	private boolean called;
	
	public RangedRandDelay(float maxDelay, float minDelay){
		this.maxDelay = maxDelay;
		this.minDelay = minDelay;
		restart();
	}
	
	public void restart(){
		rangeDelay = maxDelay;
		actValue = (maxDelay-minDelay)*Sub.rand.nextFloat()+minDelay;
		called = false;
		active=true;
	}
	
	public boolean tick(float delta){
		if(!active)
			return active;
		if(extra > 0){
			extra -= delta;
			return false;
		}
		rangeDelay -= delta;
		actValue -= delta;
		if(actValue < 0 && !called)
			return called = true;
		if(rangeDelay < 0)
			restart();
		
		return false;
	}
	
	public void w81sec(){
		called = false;
		rangeDelay = 1;
		actValue = 1;
		extra = 0;
	}
	public float getMaxDelay(){
		return maxDelay;
	}
	public float getMinDelay(){
		return minDelay;
	}
	public void setExtraW8(float extra){
		this.extra = extra;
	}
	public void setMaxMinDelay(float maxDelay, float minDelay){
		this.maxDelay = maxDelay;
		this.minDelay = minDelay;
	}
	public void setMaxMinDelay(float mminD){
		setMaxMinDelay(mminD, mminD);
	}
}
