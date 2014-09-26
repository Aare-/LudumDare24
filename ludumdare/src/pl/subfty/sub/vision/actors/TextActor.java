package pl.subfty.sub.vision.actors;

import pl.subfty.sub.vision.fonts.SText;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends SActor{

	public SText text;
	
	public TextActor(SText text){
		this.text = text;
	}

	@Override
	public void draw(SpriteBatch sbatch, float parentAlpha){
		if(!visible)
			return;
		text.draw(sbatch);
	}
	
	@Override
	public Actor hit(float x, float y) {
		return null;
	}

}
