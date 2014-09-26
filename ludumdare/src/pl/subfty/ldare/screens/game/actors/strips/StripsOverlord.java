package pl.subfty.ldare.screens.game.actors.strips;

import pl.subfty.ldare.screens.game.actors.DNARibbon;
import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.rand.RangedRandDelay;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.actors.SActor;
import pl.subfty.sub.vision.stage.actors.SActorAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Logger;

public class StripsOverlord extends SActor
							implements Callback{
	private Logger l = new Logger("STRIP", Logger.DEBUG);
	public Group strips;
	RangedRandDelay rStrips;
	DNARibbon ribbon;
	
	float diff=1;
	
	public StripsOverlord(DNARibbon ribbon, SScreen screen){
		strips = new Group();
		rStrips = new RangedRandDelay(3f, 1.8f);
		screen.restartPipeline.add(this);
		this.ribbon = ribbon;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		diff -= Sub.delta*0.01f*0.5f;
		if(diff< 0)
			diff=0;
		if(rStrips.tick(Sub.delta)){
			int pos,
				cbreak=0,
				span = (Sub.rand.nextInt(3)+1),
				start = 6+(Sub.rand.nextInt(DNARibbon.GAME_COLUMNS-span-12));
			if(diff > 0.90f)
				span = 1;
			else if(diff > 0.75f)
				span = (Sub.rand.nextInt(2)+1);
			
			boolean passed;
			do{
				pos = Sub.rand.nextInt(ribbon.aminos.length);
				passed=true;
				for(int i=pos; i<span+pos; i++)
					if(ribbon.aminos[i%ribbon.aminos.length]<0)
						passed=false;
					
				if(cbreak++ > 10){
					span--;
					cbreak=0;
				}
				if(span < 1)
					span = 1;
			}while(!passed);
			
			for(int i=0; i<span; i++){
				Strip s = obtain();
				int clor = (ribbon.aminos[(pos+i)%ribbon.aminos.length]+2*Sub.rand.nextInt(2))%4;
				if(clor == -1)
					clor = Sub.rand.nextInt(4);
				s.init(clor, 
					   start+i, 
					   Strip.REGULAR);
				
				Tween.to(s,SActorAccessor.POS_Y,4+3*diff)
					 .targetRelative(Sub.SCREEN_HEIGHT)
					 .ease(Linear.INOUT)
					 .start(Sub.tM);
			}
		}
	}

  //POOL CLASSES
	private Strip obtain(){
		int i;
		final int size = strips.getActors().size();
		for(i=0; i<size; i++)
			if(!strips.getActors().get(i).visible)
				break;
		if(i == size){
			Strip s = new Strip();
			strips.addActor(s);
			return s;
		}
		return (Strip)strips.getActors().get(i);
	}

	@Override
	public void call(int type) {
		switch(type){
		case RESTART:
			final int size = strips.getActors().size();
			for(int i=0; i<size; i++)
				((Strip)strips.getActors().get(i)).call(KILL);
			rStrips.restart();
			diff=1;
			break;
		}
	}
	@Override
	public Actor hit(float x, float y) {
		return null;
	}
}
