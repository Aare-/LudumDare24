package pl.subfty.sub;

import java.util.Random;

import pl.subfty.sub.audio.SSound;
import pl.subfty.sub.interfaces.Native;
import pl.subfty.sub.utils.GameState;
import pl.subfty.sub.vision.Art;
import pl.subfty.sub.vision.SColor;
import pl.subfty.sub.vision.SColorAccessor;
import pl.subfty.sub.vision.actors.ClockAccessor;
import pl.subfty.sub.vision.actors.ClockShape;
import pl.subfty.sub.vision.actors.Ring;
import pl.subfty.sub.vision.actors.RingAccessor;
import pl.subfty.sub.vision.fonts.SText;
import pl.subfty.sub.vision.fonts.STextAccessor;
import pl.subfty.sub.vision.sprites.SSprite;
import pl.subfty.sub.vision.sprites.SSpriteAccessor;
import pl.subfty.sub.vision.stage.SScreen;
import pl.subfty.sub.vision.stage.SScreenAccessor;
import pl.subfty.sub.vision.stage.actors.SActor;
import pl.subfty.sub.vision.stage.actors.SActorAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Sub {
	public static Art art;
	public static SSound sound;
	public static Random rand = new Random();
	private static SpriteBatch batch;
	public static GameState gState;
	public static TweenManager tM = new TweenManager();
	public static Native nat;
	
	public static Stage stage;
	private static SScreen screen;
	
	public final static float STAGE_W=480f,
							  STAGE_H=320f,
							  ASPECT_RATIO = STAGE_W/STAGE_H;
	public static float SCREEN_WIDTH,
						SCREEN_HEIGHT,
						SCALE;
	
	public static boolean BLOCK_INPUT=false;
	
	public static Vector2 tmp = new Vector2();
	public static float delta;
	
  //GAME MAIN FLOW
	public static void onCreate(){
		batch = new SpriteBatch();
		stage = new Stage(STAGE_W, STAGE_H, false, batch);
		gState = new GameState();
		art = new Art();
		sound = new SSound();
		
		Gdx.input.setInputProcessor(stage);
		
	  //REGISTERING ACCESSORS
		Tween.registerAccessor(SSprite.class, new SSpriteAccessor());
		Tween.registerAccessor(SActor.class, new SActorAccessor());
		Tween.registerAccessor(ClockShape.class, new ClockAccessor());
		Tween.registerAccessor(SScreen.class, new SScreenAccessor());
		Tween.registerAccessor(SText.class, new STextAccessor());;
		Tween.registerAccessor(SColor.class, new SColorAccessor());
		Tween.registerAccessor(Ring.class, new RingAccessor());
		
	}
	public static void onExit(){
		gState.onExit();
		if(Sub.screen != null)
			Sub.screen.dispose();
	}
	public static void onPause(){
		gState.onExit();
		tM.pause();
		if(Sub.screen != null)
			Sub.screen.pause();
	}
	public static void onResume(){
		tM.resume();
		if(Sub.screen != null)
			Sub.screen.resume();
	}
	public static void onResize(int w, int h){
		float ratio = (float)w/(float)h;
		if(ratio > STAGE_W/STAGE_H)
			SCALE = STAGE_H/((float)h);
		else
			SCALE = STAGE_W/((float)w);
		
		Camera c = stage.getCamera(); 
		c.viewportWidth = SCREEN_WIDTH= w*SCALE;
		c.viewportHeight = SCREEN_HEIGHT = h*SCALE;
		c.position.set(STAGE_W/2,STAGE_H/2,0);
		if(Sub.screen != null)
			Sub.screen.resize(w,h);
	}
	public static void onRender(){
		delta = Math.min(Gdx.graphics.getRawDeltaTime(),0.25f);
		
		if(Sub.screen != null)
			Sub.screen.render(delta);
		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		tM.update(delta);
		stage.draw();
	}
	
  //SCREEN MANAGEMENT
	public static void setScreen(SScreen screen){
		if(Sub.screen != null){
			Sub.screen.cntnt.visible=false;
			Sub.screen.hide();
		}
		screen.cntnt.visible=true;
		Sub.screen = screen;
		
		Sub.screen.show();
	}
	
  //UTILS
	public static void vibrate(int time){
		Gdx.input.vibrate(time);
	}
	public static void toStageCoordinates(int x, int y, Vector2 out){
		stage.toStageCoordinates(x, y, out);
	}
}
