package pl.subfty.ldare.screens.menu;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import pl.subfty.ldare.Dare;
import pl.subfty.sub.Sub;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.sprites.SSpriteAccessor;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.actors.SActorAccessor;

public class Menu extends SScreen{

	public boolean pressed=false,
				   ready = false;
	
	public Menu(){
		super();
	}
	
	@Override
	public void render(float delta) {
		if(!ready)
			return;
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			if(!pressed)
				Sub.setScreen(Dare.game);
			pressed=true;
		}else
			pressed=false;
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		cntnt.clear();
		this.loadScreenLayout("screens/menu.svg", cntnt, "menu");
		ready=false;
		SpriteActor title = (SpriteActor)cntnt.findActor("title");
		title.sprite.alpha = 0;
		
		SSprite start = ((SpriteActor)cntnt.findActor("enter_to_start")).sprite;
		SSprite player = ((SpriteActor)cntnt.findActor("player")).sprite;
		player.alpha=0;
		Sub.tM.killTarget(start);
		
		Timeline t = Timeline.createSequence();
		t.push(Timeline.createParallel()
				.push(Timeline.createParallel()
					   .push(Tween.from(player, SSpriteAccessor.POSITION_XY, 2)
							   .target(0, -100))
					   .push(Tween.to(player, SSpriteAccessor.ALPHA, 2)
							   .target(1)))
				.push(Timeline.createParallel()
					   .push(Tween.from(title.sprite, SSpriteAccessor.POSITION_XY, 2)
							 	  .targetRelative(0,100))
					  .push(Tween.to(title.sprite, SSpriteAccessor.ALPHA, 2)
							  	 .target(1))));

		
		start.alpha = 0.0f;
		t.push(Tween.to(start, SSpriteAccessor.ALPHA, 1f)
					.ease(Sine.IN)
					.target(0.8f));
		t.push(Tween.call(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				ready = true;
				SSprite start = ((SpriteActor)cntnt.findActor("enter_to_start")).sprite;
				Tween.to(start, SSpriteAccessor.ALPHA, 0.6f)
				 	 .target(0.4f)
				 	 .repeatYoyo(Tween.INFINITY, 0)
				 	 .start(Sub.tM);
			}
		}));
		t.start(Sub.tM);
		
		SSprite zoo = ((SpriteActor)cntnt.findActor("zoo")).sprite;
		Sub.tM.killTarget(zoo);
		Tween.to(zoo, SSpriteAccessor.POSITION_XY, 2)
			 .targetRelative(0, 4)
			 .repeatYoyo(Tween.INFINITY, 0)
			 .start(Sub.tM);
		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
