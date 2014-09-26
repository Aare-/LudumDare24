package pl.subfty.ldare.screens.death;

import pl.subfty.ldare.Dare;
import pl.subfty.sub.Sub;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.actors.TextActor;
import pl.subfty.sub.vision.fonts.SText;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.SScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class Death extends SScreen{

	SpriteActor background;
	TextActor score;
	
	public Death(){
		super();
		background = new SpriteActor(new SSprite(Art.T_GME, 8));
		
		score = new TextActor(new SText(Art.F_CIRCULA,100));
		score.text.wrapWidth = Sub.STAGE_W;
		score.text.alignment = HAlignment.CENTER;
		score.text.x = Sub.STAGE_W/2;
		score.text.y = 100;
		
		cntnt.addActor(background);
		cntnt.addActor(score);
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			Dare.menu.pressed=true;
			Sub.setScreen(Dare.menu);
		}
		score.text.x = 0;
		score.text.y = 120;
		score.text.scale = 0.3f;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
		background.sprite.setPosition(0, 0);
		background.sprite.setScale(Sub.STAGE_W/background.sprite.getWidth(), Sub.STAGE_H/background.sprite.getHeight());
		score.text.setText(""+Dare.game.scoreVal);
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
