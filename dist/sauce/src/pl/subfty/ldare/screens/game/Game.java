package pl.subfty.ldare.screens.game;

import pl.subfty.ldare.Dare;
import pl.subfty.ldare.screens.game.actors.Amino;
import pl.subfty.ldare.screens.game.actors.DNARibbon;
import pl.subfty.ldare.screens.game.actors.EvolutionGuy;
import pl.subfty.ldare.screens.game.actors.Lazor;
import pl.subfty.ldare.screens.game.actors.strips.Strip;
import pl.subfty.ldare.screens.game.actors.strips.StripsOverlord;
import pl.subfty.sub.Sub;
import pl.subfty.sub.audio.SSound;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.actors.TextActor;
import pl.subfty.sub.vision.fonts.SText;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.SScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Game extends SScreen{

  //LOGIC ACTORS
	DNARibbon ribbon;
	EvolutionGuy eGuy;
	StripsOverlord sOverlord;
	Lazor lazors;
	
	Rectangle testa = new Rectangle(),
			  testb = new Rectangle();
	TextActor score;
	public int scoreVal=0;
	
	Array<Integer> aminoDel = new Array<Integer>(),
					aminoMut = new Array<Integer>();
	boolean killOrSpare[] = new boolean[27];
	
	SpriteActor lifea,
				lifeb;
	
	public Game(){
		super();
		ribbon = new DNARibbon(this);
		eGuy = new EvolutionGuy(this, ribbon);
		sOverlord = new StripsOverlord(ribbon,this);
		
		lazors = new Lazor(this, ribbon);
		
		score = new TextActor(new SText(Art.F_CIRCULA,100));
		score.text.wrapWidth = Sub.STAGE_W;
		score.text.alignment = HAlignment.CENTER;
		score.text.x = Sub.STAGE_W/2;
		score.text.y = 100;
		
		lifea = new SpriteActor(new SSprite(Art.T_GME, 2));
		lifeb = new SpriteActor(new SSprite(Art.T_GME, 2));
		
		this.loadScreenLayout("screens/game.svg", cntnt, "game");
		
	  //LOGIC
		cntnt.addActor(ribbon);
		cntnt.addActor(ribbon.scroller);
		cntnt.addActor(eGuy);
		cntnt.addActor(sOverlord);
		
	  //DRAWING
		cntnt.addActor(score);
		cntnt.addActor(eGuy.guy);
		cntnt.addActor(ribbon.ribbon);
		
		cntnt.addActor(sOverlord.strips);
		cntnt.addActor(lazors);
		
		
		cntnt.addActor(lifea);
		cntnt.addActor(lifeb);
		
	}
	
	@Override
	public void render(float delta) {
		
		score.text.x = -8;
		score.text.y = 240;
		score.text.alpha = 0.4f;
		score.text.scale = 0.3f;
		
	  //CALCULATING STRIPS HITS
		final int size = sOverlord.strips.getActors().size();
		for(int i=0; i<size; i++){
			Strip str = (Strip)sOverlord.strips.getActors().get(i);
			if(str.active)
				if(str.y+sOverlord.strips.getActors().get(i).height >= ribbon.y){
					compareStripToAminos(str, aminoMut, aminoDel);
					str.call(Callback.KILL);
				}
		}
		
		boolean killed=false,
				mutated=false;
		
		for(int i=0; i<killOrSpare.length; i++)
			killOrSpare[i] = true;
		for(int i=0; i<aminoMut.size; i++)
			killOrSpare[aminoMut.get(i)] = false;
		
		for(int i=0; i<aminoDel.size; i++)
			if(killOrSpare[aminoDel.get(i)]){
				ribbon.aminos[aminoDel.get(i)] = -1;
				killed = true;
			}
		aminoMut.sort();
		int v = -1;
		for(int i=0; i<aminoMut.size; i++)
			if(aminoMut.get(i) == v)
				aminoMut.removeIndex(i--);
			else
				v = aminoMut.get(i);
		for(int i=0; i<aminoMut.size; i++){
			ribbon.aminos[aminoMut.get(i)] += 1+2*Sub.rand.nextInt(1);
			ribbon.aminos[aminoMut.get(i)] %= 4;
			scoreVal++;
			score.text.setText(""+scoreVal);
			//score.text.setNumber(scoreVal, 10);
			mutated=true;
		}
			
		aminoMut.clear();
		aminoDel.clear();
		if(killed)
			Sub.sound.playSound(SSound.MISS);
		if(mutated)
			Sub.sound.playSound(SSound.HIT);
		
		
		int deathCounter = 0;
		for(int i=0; i<ribbon.aminos.length; i++){
			if(ribbon.aminos[i] == -1)
				deathCounter++;
		}
		
		lifea.sprite.setScale(-(10*(6-deathCounter))/lifea.sprite.getWidth(), 2/lifea.sprite.getHeight());
		lifea.sprite.setPosition(Sub.STAGE_W/2-24, Sub.STAGE_H-8);
		lifea.sprite.setColor(1, 1, 1, 0.2f);
		
		lifeb.sprite.setScale((10*(6-deathCounter))/lifea.sprite.getWidth(), 2/lifea.sprite.getHeight());
		lifeb.sprite.setPosition(Sub.STAGE_W/2+7, Sub.STAGE_H-8);
		lifeb.sprite.setColor(1, 1, 1, 0.2f);
		
		if(deathCounter > 5)
			Sub.setScreen(Dare.death);
		
		
		if(Gdx.input.isKeyPressed(Input.Keys.B)){
			if(!pressed){
				show();
			}
			pressed = true;
		}else
			pressed=false;
	}
	boolean pressed=false;

  //GAME LOGIC
	private void compareStripToAminos(Strip str, Array<Integer> mut, Array<Integer> del){
		testb.set(str.x+(DNARibbon.AMINO_WIDTH*(4.0f/19.0f)), 
				  0, 
				  DNARibbon.AMINO_WIDTH-(DNARibbon.AMINO_WIDTH*(4.0f/19.0f)*2), 
				  10);
		for(int i=0; i<ribbon.aminosG.getActors().size(); i++){
			Amino a = (Amino)ribbon.aminosG.getActors().get(i);
			testa.set(a.sprite.getX()+ribbon.aminosG.x+ribbon.ribbon.x+(DNARibbon.AMINO_WIDTH*(4.0f/19.0f)), 
					  0, 
					  DNARibbon.AMINO_WIDTH-(DNARibbon.AMINO_WIDTH*(4.0f/19.0f))*2, 
					  10);
			if(testa.overlaps(testb))
				if(ribbon.aminos[a.relatedAminoId] == str.aminoType)
					mut.add(a.relatedAminoId);
				else
					del.add(a.relatedAminoId);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}
	@Override
	public void show() {
		for(int i=0; i<restartPipeline.size; i++)
			restartPipeline.get(i).call(Callback.RESTART);
		scoreVal=0;
		score.text.setText(""+scoreVal);
		aminoDel.clear();
		aminoMut.clear();
	}
	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}
	@Override
	public void resume() {
		
	}
	@Override
	public void dispose() {
		
	}
}
