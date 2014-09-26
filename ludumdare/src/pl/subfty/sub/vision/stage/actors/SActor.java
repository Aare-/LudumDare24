package pl.subfty.sub.vision.stage.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class SActor extends Actor{
	public float alpha=1;
	
	public SActor(String name){
		super(name);
	}
	public SActor(){
		super(null);
	}
}
