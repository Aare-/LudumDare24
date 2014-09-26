package pl.subfty.ldare.screens.game.actors.strips;

import pl.subfty.ldare.screens.game.actors.DNARibbon;
import pl.subfty.sub.Sub;
import pl.subfty.sub.audio.SSound;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Strip extends SActor
				   implements Callback{
  //SETTINGS
	public final static float STRIP_HEIGHT = 15;
	
	public final static int REGULAR=0,
							HEALER=1;
	private final static Color WHITE = new Color(1,1,1,1);
	
	SSprite sprite;
	public int aminoType,
			   type;
	public boolean active;
	
	public Strip(){

		sprite = new SSprite(Art.T_GME, 3);
		active = false;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!this.visible)
			return;

			sprite.setOrigin(0, 0);
			sprite.setPosition(this.x+(0.5f)*(DNARibbon.AMINO_WIDTH)-this.height/sprite.getWidth()*0.5f, this.y);
			sprite.setScale(this.height/sprite.getWidth(), 
							   this.height/sprite.getHeight());
			sprite.draw(batch, this.alpha*parentAlpha);

	}
	
	public void init(int amino_type, int row, int type){
		this.type = type;
		switch(type){
		default: 
			sprite.setColor(DNARibbon.AMI_C[this.aminoType = amino_type]);
			break;
		case 1:

				sprite.setColor(WHITE);
				sprite.getColor().a = 0.5f;
			break;
		}
		this.x = row*(DNARibbon.AMINO_WIDTH)+DNARibbon.MARGIN_LEFT+DNARibbon.AMINO_WIDTH*(4.0f/19.0f);
		this.y = -STRIP_HEIGHT;
		this.width = DNARibbon.AMINO_WIDTH-(DNARibbon.AMINO_WIDTH*(4.0f/19.0f)*2);
		this.height = STRIP_HEIGHT;
		this.visible=true;
		this.active=true;
		Sub.tM.killTarget(this);
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void call(int type) {
		switch(type){
		case HIT:
			this.visible=false;
			Sub.sound.playSound(SSound.HIT, 1);
			break;
		case MISS:
			Sub.sound.playSound(SSound.MISS, 1);
			this.visible=false;
			break;
		case KILL:
			this.visible = false;
			this.active=false;
			break;
		}
	}
}
