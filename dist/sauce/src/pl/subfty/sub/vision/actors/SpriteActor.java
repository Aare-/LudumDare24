package pl.subfty.sub.vision.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class SpriteActor extends SActor{

	public SSprite sprite;
	public boolean expiliteDisableBlending=false;
	
	
	public SpriteActor(String name, SSprite sprite){
		super(name);
		this.sprite = sprite;
	}
	
	public SpriteActor(SSprite sprite){
		this(null, sprite);
	}
	
	@Override
	public void draw(SpriteBatch sbatch, float parentAlpha){
		if(!this.visible)
			return;
	  //DRAW
		if(expiliteDisableBlending)
			sbatch.disableBlending();
		sprite.draw(sbatch, parentAlpha*sprite.alpha);
		if(expiliteDisableBlending)
			sbatch.enableBlending();
	}
	
	public void fillScreen(){
		sprite.setPosition(-(Sub.SCREEN_WIDTH-Sub.STAGE_W)/2,
						   -(Sub.SCREEN_HEIGHT-Sub.STAGE_H)/2);
		sprite.setScale(Sub.SCREEN_WIDTH/width, 
					    Sub.SCREEN_HEIGHT/height);
	}
	
	@Override
	public Actor hit(float x, float y) {
		return null;
	}
}
