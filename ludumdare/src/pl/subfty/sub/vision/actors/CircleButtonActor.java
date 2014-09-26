package pl.subfty.sub.vision.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class CircleButtonActor extends SActor{
	
	public SSprite sprite;
	private final static Vector2 hitTest = new Vector2();
	
	public CircleButtonActor(String name, int index){
		super();
		
		this.touchable=true;
		sprite = new SSprite();
		setSprite(name, index);
	}
	
	public void setSprite(String name, int index){
		this.sprite.loadRegion(Sub.art.atlas.findRegion(name, index));
		this.width = sprite.getWidth()*sprite.getScaleX();
		this.height = sprite.getHeight()*sprite.getScaleY();
		sprite.setOrigin(0,0);
	}
	
	@Override
	public void draw(SpriteBatch sbatch, float parentAlpha){
	  //LOGIC
		sprite.setPosition(x-width*scaleX/2, 
						   y-height*scaleY/2);
	  //DRAWING
		sprite.setScale(scaleX, scaleY);
		sprite.draw(sbatch, parentAlpha*sprite.alpha*alpha);
	}

	@Override
	public Actor hit(float x, float y) {
		if(hitTest.set(x, y).len()<=width/2 && 
		   !Sub.BLOCK_INPUT){
			return this;
		}
		return null;
	}
}
