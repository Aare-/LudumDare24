package pl.subfty.ldare.screens.game.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.audio.SSound;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.SColor;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.actors.SActor;
import pl.subfty.sub.vision.stage.actors.SActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class Lazor extends SActor
				   implements Callback{
	private Logger l = new Logger("Lazor", Logger.DEBUG);
	SpriteActor beams[] = new SpriteActor[1];
	SpriteActor lazorFlash;
	boolean keyPressed=false;
	DNARibbon ribbon;
	
	Array<Float> targets;
	int used=0;
	
	Rectangle testa = new Rectangle(),
			  testb = new Rectangle();
	
	public Lazor(SScreen screen, DNARibbon ribbon){
		lazorFlash = new SpriteActor(new SSprite(Art.T_GME,2));
		lazorFlash.alpha=0;
		
		this.ribbon = ribbon;
		screen.restartPipeline.add(this);
		for(int i=0; i<beams.length; i++){
			beams[i] = new SpriteActor(new SSprite(Art.T_GME, 2));
			beams[i].sprite.setColor(new SColor(1,1,1,1));
			beams[i].sprite.setPosition(0,0);
		}
		targets = new Array<Float>();
		for(int i=0; i<beams.length; i++){
			targets.add(Sub.STAGE_W/(beams.length+1)*(i+1));
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0; i<beams.length; i++){
			if(!Sub.tM.containsTarget(beams[i], SActorAccessor.POS_X))
				startBeamAnim(i);
			
			beams[i].sprite.alpha = 0.3f;
			beams[i].sprite.setScale(2/beams[i].sprite.getWidth(), Sub.STAGE_H/beams[i].sprite.getHeight());
			beams[i].sprite.setPosition(beams[i].x%DNARibbon.RIBBON_WIDTH, 0);
			beams[i].sprite.draw(batch, beams[i].sprite.alpha*parentAlpha);
		}
		
		lazorFlash.fillScreen();
		lazorFlash.sprite.setPosition(0,0);
		lazorFlash.sprite.setScale(Sub.STAGE_W/lazorFlash.sprite.getWidth(),Sub.STAGE_H/lazorFlash.sprite.getHeight());
		lazorFlash.sprite.draw(batch, lazorFlash.alpha);
			
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			if(!keyPressed)
				shoot();
			keyPressed=true;
		}else
			keyPressed=false;
	}

	private void shoot(){
		Sub.sound.playSound(SSound.LAZOR, 1);
		for(int j=0; j<beams.length; j++){
			testa.set(beams[j].sprite.getX(), 0, beams[j].sprite.getWidth()*beams[j].sprite.getScaleX(), 10);
			for(int i=0; i<ribbon.aminosG.getActors().size(); i++){
				Amino actor = (Amino)ribbon.aminosG.getActors().get(i);
				testb.set(actor.sprite.getX()+ribbon.aminosG.x+ribbon.ribbon.x, 0, DNARibbon.AMINO_WIDTH, 10);
				if(testa.overlaps(testb))
					if(ribbon.aminos[actor.relatedAminoId]!= -1)
						ribbon.aminos[actor.relatedAminoId] = (ribbon.aminos[actor.relatedAminoId]+2)%4;
				Sub.tM.killTarget(lazorFlash,SActorAccessor.ALPHA);
				lazorFlash.alpha = 0.8f;
				Tween.to(lazorFlash, SActorAccessor.ALPHA, 0.4f)
					 .target(0)
					 .start(Sub.tM);
			}	
		}
	}
	boolean target[]={false,true};
	private void startBeamAnim(final int id){
		target[id] = !target[id];
		Sub.tM.killTarget(beams[id], SActorAccessor.POS_X);
		Tween.to(beams[id], SActorAccessor.POS_X, 6+2*Sub.rand.nextFloat())
			 .target(Sub.STAGE_W/2+
					 ((target[id])?
					 	(-Sub.STAGE_W*0.1f*Sub.rand.nextFloat())://(-Sub.STAGE_W*0.1f-Sub.STAGE_W*0.35f*Sub.rand.nextFloat()):
					 	(Sub.STAGE_W*0.1f*Sub.rand.nextFloat())))//(Sub.STAGE_W*0.1f+Sub.STAGE_W*0.35f*Sub.rand.nextFloat())))
			 .ease(Sine.OUT)
			 .start(Sub.tM);
		Tween.call(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				startBeamAnim(id);
			}
		}).delay(2+2*Sub.rand.nextFloat())
		.start(Sub.tM);
		used++;
		if(used >= targets.size){
			targets.shuffle();
			used = 0;
		}
	}
	
	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void call(int type) {
		switch(type){
		case RESTART:
			for(int i=0; i<beams.length; i++){
				beams[i].x = Sub.STAGE_W/2;
				beams[i].visible=false;
				Sub.tM.killTarget(beams[i]);
			}
			break;
		}
	}
}
