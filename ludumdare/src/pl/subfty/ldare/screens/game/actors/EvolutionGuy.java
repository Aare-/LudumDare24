package pl.subfty.ldare.screens.game.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.SColor;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class EvolutionGuy extends SActor
						  implements Callback{
	private Logger l = new Logger("Evloution", Logger.DEBUG);
	
	public Group guy,
				 torso,
				 leftArm,
				 rightArm,
				 head,
				 legs;
	
	private final static SColor SKIN_WHITE = new SColor(201.0f/255.0f, 199.0f/255.0f, 173.0f/255.0f, 1),
								SKIN_BLACK = new SColor(118.0f/255.0f, 96.0f/255.0f, 61.0f/255.0f, 1),
								HAIR_COLOR_FROM = new SColor(236.0f/255.0f,189.0f/255.0f,27.0f/255.0f,1),
								HAIR_COLOR_TO = new SColor(90.0f/255.0f,73.0f/255.0f,14f/255.0f,1);
	
	private SColor skin_tone = new SColor(),
				   hair = new SColor();
	
	private final static int P_SKIN_TONE=0,
							 P_JOHNY_L=1,
							 P_EYE_NUM=2,
							 P_HANDS_LEN=3,
							 P_HAIR_COL=4,
							 P_HEAD_S=5,
							 P_BODY_H=6,
							 P_LEGS=7,
							 P_HAIR=8;
			
	Array<Integer> takeValues = new Array<Integer>();
	private final float defValues[] = {0.15f, 0.3f, 0.5f, 0.3f, 0.1f, 0.4f, 0.6f, 0.3f, 0.5f};
	float properties[] = new float[9];
	int initGenomV[] = new int[27];
	
	private final static int LEFT_LEG=0,
							 RIGHT_LEG=1,
							 JOHNNY=2,
							 BODY=3,
							 LEFT_ARM=4,
							 LEFT_ARM_2=5,
							 RIGHT_ARM=6,
							 RIGHT_ARM_2=7,
							 HAIR=8,
						 	 HEAD=9,
					 		 EYE_1=10,
							 EYE_2=11,
							 EYE_3=12;
	SpriteActor sprites[] = new SpriteActor[13];
	DNARibbon ribbon;

	
	public EvolutionGuy(SScreen game, DNARibbon ribbon){
		for(int i=0; i<sprites.length; i++)
			sprites[i] = new SpriteActor(new SSprite(Art.T_GME, 2));
		
		for(int i=0; i<initGenomV.length; i++)
			takeValues.add(i);
		
		guy = new Group();
		torso = new Group();
		leftArm = new Group();
		rightArm = new Group();
		head = new Group();
		legs = new Group();
		
		torso.addActor(leftArm);
		torso.addActor(rightArm);
		
		guy.addActor(legs);
		guy.addActor(torso);
		guy.addActor(head);
		
	  //ADDING SPRITES
		leftArm.addActor(sprites[LEFT_ARM]);
		leftArm.addActor(sprites[LEFT_ARM_2]);
		
		rightArm.addActor(sprites[RIGHT_ARM]);
		rightArm.addActor(sprites[RIGHT_ARM_2]);
		
		torso.addActor(sprites[BODY]);
		
		legs.addActor(sprites[LEFT_LEG]);
		legs.addActor(sprites[RIGHT_LEG]);
		legs.addActor(sprites[JOHNNY]);
		
		head.addActor(sprites[HAIR]);
		head.addActor(sprites[HEAD]);
		head.addActor(sprites[EYE_1]);
		head.addActor(sprites[EYE_2]);
		head.addActor(sprites[EYE_3]);
		
		this.ribbon = ribbon;
		
		game.restartPipeline.add(this);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		skin_tone.set(SKIN_WHITE);
		skin_tone.interp(SKIN_BLACK, getValue(P_SKIN_TONE));
		
		hair.set(HAIR_COLOR_FROM);
		hair.interp(HAIR_COLOR_TO, getValue(P_HAIR_COL));
		
		for(int i=0; i<sprites.length; i++)
			sprites[i].sprite.setColor(skin_tone);
	
		guy.y = 55;
		guy.x = Sub.STAGE_W/2-8;
		
		legs.y = getValue(P_LEGS)*19;
		head.y = getValue(P_LEGS)*19+10+(40+getValue(P_BODY_H)*45);
		torso.y = getValue(P_LEGS)*19+10;
		
		SSprite s = sprites[LEFT_LEG].sprite;
	  //LEFT LEG
		s.setScale(15/s.getWidth(), (15+14*getValue(P_LEGS))/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f-20,
					  -(+19*getValue(P_LEGS)));
		
	  //RIGHT LEG
		s = sprites[RIGHT_LEG].sprite;
		s.setScale(15/s.getWidth(), (15+14*getValue(P_LEGS))/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f+20,
					  -(+19*getValue(P_LEGS)));
		
	  //JOHNY
		s = sprites[JOHNNY].sprite;
		s.setScale(5/s.getWidth(), (3+getValue(P_JOHNY_L)*10)/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
					  10-s.getScaleY()*s.getHeight());
		
	  //TORSO
		s = sprites[BODY].sprite;
		s.setScale(55/s.getWidth(), (40+getValue(P_BODY_H)*45)/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
					  0);
		
	  //HEAD
		s = sprites[HEAD].sprite;
		s.setScale((35+10*getValue(P_HEAD_S))/s.getWidth(), (40)/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
					  0);
		
	  //HAIR
		s = sprites[HAIR].sprite;
		s.setScale(((35+10*getValue(P_HEAD_S))*(1.1f+getValue(P_HAIR)*0.10f))/s.getWidth(), 
				   (40*(1.1f+getValue(P_HAIR)*0.10f)-2)/s.getHeight());
		s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
					  2);
		s.setColor(hair);
		
	  //ARMS
		s = sprites[LEFT_ARM].sprite;
		s.setScale((-20-getValue(P_HANDS_LEN)*25)/s.getWidth(), 
				   (10)/s.getHeight());
		s.setPosition(-27,
				      (40+getValue(P_BODY_H)*45)-12);
		
		s = sprites[RIGHT_ARM].sprite;
		s.setScale((20+getValue(P_HANDS_LEN)*25)/s.getWidth(), 
				   (10)/s.getHeight());
		s.setPosition(27,
				      (40+getValue(P_BODY_H)*45)-12);
		
	  //EYES
		if(getValue(P_EYE_NUM) < 0.33f){
			s = sprites[EYE_1].sprite;
			s.setColor(Color.BLACK);
			float eye_w = (((0.33f-getValue(P_EYE_NUM))/0.33f)*3+5);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
		
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
						  25-s.getScaleY()*s.getHeight()*0.5f);
			
			sprites[EYE_1].sprite.alpha=1;
			sprites[EYE_2].sprite.alpha=0;
			sprites[EYE_3].sprite.alpha=0;
		}else if(getValue(P_EYE_NUM) < 0.66f){
			float v = (getValue(P_EYE_NUM)-0.33f)/0.33f;
			float eye_w = (v*2+4);
			s = sprites[EYE_1].sprite;
			s.setColor(Color.BLACK);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f-3-v*10,
						  25-s.getScaleY()*s.getHeight()*0.5f);
			
			s = sprites[EYE_2].sprite;
			s.setColor(Color.BLACK);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f+3+v*10,
						  25-s.getScaleY()*s.getHeight()*0.5f);
			
			sprites[EYE_1].sprite.alpha=1;
			sprites[EYE_2].sprite.alpha=1;
			sprites[EYE_3].sprite.alpha=0;
		}else{
			float v = (getValue(P_EYE_NUM)-0.66f)/0.33f;
			float eye_w = (v*2+4);
			s = sprites[EYE_1].sprite;
			s.setColor(Color.BLACK);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f-3-10,
						  25-s.getScaleY()*s.getHeight()*0.5f-7*v);
			
			s = sprites[EYE_2].sprite;
			s.setColor(Color.BLACK);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f+3+10,
						  25-s.getScaleY()*s.getHeight()*0.5f-7*v);
			
			s = sprites[EYE_3].sprite;
			s.setColor(Color.BLACK);
			s.setScale(eye_w/s.getWidth(), eye_w/s.getHeight());
			s.setPosition(-s.getScaleX()*s.getWidth()*0.5f,
						  25-s.getScaleY()*s.getHeight()*0.5f+v*7);
			
			sprites[EYE_1].sprite.alpha=1;
			sprites[EYE_2].sprite.alpha=1;
			sprites[EYE_3].sprite.alpha=1;
		}
		
		//rites[LEFT_LEG]
	}
	
	//0-64
	private float getValue(int property){
		int val = getValueFrom4Arr(ribbon.aminos, property)
				  -getValueFrom4Arr(initGenomV, property);
		while(val < 0)
			val += 64;
		val %= 64;
		return (defValues[property]+val/64.0f)%1.0f;
	}
	
	@Override
	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public void call(int type) {
		switch(type){
		case RESTART:
			takeValues.shuffle();
			for(int i=0; i<properties.length; i++)
				properties[i] = defValues[i];
			for(int i=0; i<initGenomV.length; i++)
				initGenomV[i] = ribbon.aminos[i];
			break;
		}
	}
	
	private int getValueFrom4Arr(int arr[], int pos){
		int val=0;
		for(int i=1; i<=3; i++)
			val += arr[takeValues.get(pos*3+i-1)]*Math.pow(4, i);
		
		return Math.max(val,0);
	}
}
