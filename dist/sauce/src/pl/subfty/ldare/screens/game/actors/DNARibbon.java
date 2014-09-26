package pl.subfty.ldare.screens.game.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.SColor;
import pl.subfty.sub.vision.actors.scrollers.ScrollingEntity;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Logger;

public class DNARibbon extends SActor
					   implements Callback{
	private Logger l = new Logger("DNARibbon", Logger.DEBUG);
  //RIBBON SETTINGS
	public final static float RIBBON_WIDTH = 480,
							  AMINO_WIDTH=25,
							  AMINO_HEIGHT=55,
							  MARGIN_LEFT=0;
	public final static int GAME_COLUMNS = (int)(RIBBON_WIDTH/(AMINO_WIDTH));
	public final static SColor AMI_C[] = {new SColor(123.0f/255.0f, 227.0f/255.0f, 77.0f/255.0f, 1),
										   new SColor(217.0f/255.0f, 19.0f/255.0f, 19.0f/255.0f, 1),
										   new SColor(244.0f/255.0f, 96.0f/255.0f, 228.0f/255.0f, 1),
										   new SColor(237.0f/255.0f, 210.0f/255.0f, 62.0f/255.0f, 1)};
	public ScrollingEntity scroller;
	
	public int aminos[] = new int[27];
	
	public Group ribbon,
				 aminosG,
				 duplG;

	
	public DNARibbon(SScreen screen){
		screen.restartPipeline.add(this);
		
		this.width = RIBBON_WIDTH+AMINO_WIDTH*2;
		this.x = MARGIN_LEFT-AMINO_WIDTH;
		this.y = 250;
		
		
		ribbon = new Group();
		aminosG = new Group();
		duplG = new Group();
		
		scroller = new ScrollingEntity();
		scroller.x = 0;
		scroller.y = 0;
		scroller.width = Sub.STAGE_W;
		scroller.height = Sub.STAGE_H;
		
		ribbon.addActor(aminosG);
		ribbon.addActor(duplG);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		ribbon.x = this.x;
		ribbon.y = this.y;
		if(scroller.scrolled.x < 0)
			scroller.scrolled.x += (aminos.length*(AMINO_WIDTH));
		//scroller.scrolled.x = 0;
	  //UPDATING AMINOS
		final int size = aminosG.getActors().size();
		for(int i=0; i<size; i++){
			int pos = (i+(int)Math.ceil(-scroller.scrolled.x/(AMINO_WIDTH)))%aminos.length;
			if(pos < 0)
				pos += aminos.length;
			
			Amino s = (Amino)aminosG.getActors().get(i),
				  d = (Amino)duplG.getActors().get(i);
			s.relatedAminoId = pos;
			if(aminos[pos] == -1){
				s.sprite.setColor(Color.GRAY);
				d.sprite.setColor(Color.GRAY);
				d.sprite.alpha = 0.4f;
				s.sprite.alpha = 0.4f;
			}else{
				d.sprite.alpha = 1f;
				s.sprite.alpha = 1f;
				s.sprite.setColor(AMI_C[aminos[pos]]);
				d.sprite.setColor(AMI_C[(aminos[pos]+2)%4]);
			}
		}
		aminosG.x = scroller.scrolled.x%(AMINO_WIDTH);
		duplG.x = scroller.scrolled.x%(AMINO_WIDTH);
		
	  //MOVING RIBBON
	}

	@Override
	public void call(int type) {
		switch(type){
		case RESTART:
			scroller.stop();
			
		  //MAP DEFAULT EVOL GUY VALUES TO RANDOM POSITIONS
			int j=0,
				clr = Sub.rand.nextInt(2),
				span = Sub.rand.nextInt(2)+1;
			do{
				if(--span == 0){
					clr++;
					clr%=2;
					span = Sub.rand.nextInt(3)+1;
				}
				aminos[j] = clr+Sub.rand.nextInt(2)*2;
			}while(++j<aminos.length);
			if(aminos[aminos.length-1]%2 == aminos[0]%2){
				aminos[aminos.length-1]++;
				aminos[aminos.length-1] %= 4;
			}
			
			
		  //CREATING SPRITES
			for(int i=aminosG.getActors().size(); 
				    i<(this.width/(AMINO_WIDTH)+1); 
					i++){
				Amino amino = new Amino(new SSprite(Art.T_GME, 0));
				amino.sprite.setOrigin(0, 0);
				amino.sprite.setScale(AMINO_WIDTH/amino.sprite.getWidth(), 
									  AMINO_HEIGHT/amino.sprite.getHeight()*0.5f);
				amino.sprite.setPosition(AMINO_WIDTH*i,0);
				aminosG.addActor(amino);
				
				amino = new Amino(new SSprite(Art.T_GME, 0));
				amino.sprite.setOrigin(0, 0);
				amino.sprite.setScale(AMINO_WIDTH/amino.sprite.getWidth(), 
									  AMINO_HEIGHT/amino.sprite.getHeight()*0.5f);
				amino.sprite.setPosition(AMINO_WIDTH*i,AMINO_HEIGHT*0.5f);
				duplG.addActor(amino);
			}
			break;
		}
	}	
	
	@Override
	public Actor hit(float x, float y) {
		return null;
	}
}
