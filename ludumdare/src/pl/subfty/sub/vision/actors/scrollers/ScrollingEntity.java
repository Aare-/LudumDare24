package pl.subfty.sub.vision.actors.scrollers;

import pl.subfty.sub.Sub;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Logger;

public class ScrollingEntity extends SActor{
	private Logger l = new Logger("ScrollingEntity", Logger.DEBUG);
	private float damping,
				  max_speed;
	public Vector2 minScroll = new Vector2(),
				   maxScroll = new Vector2();
	
	private final static int TOUCH_ACCURACY=2;
	private int touchPosI=0,
				touchPosIFilled=0;
	public Vector2 scrolled = new Vector2();
	private Vector2 touchPos[] = new Vector2[TOUCH_ACCURACY];
	private boolean overS,
					scrollBorders=false;
	private Vector2 prevPos= new Vector2(),
					scrollSpeed= new Vector2();
	
	public ScrollingEntity(){
		this.damping = 1000;
		this.max_speed = 1000;
		scrollBorders=false;
		for(int i=0; i<TOUCH_ACCURACY; i++)
			touchPos[i] = new Vector2();
	}
	public ScrollingEntity(Vector2 minScroll, Vector2 maxScroll){
		this(minScroll, maxScroll, 10000, 4000.0f);
	}
	public ScrollingEntity(Vector2 minScroll, Vector2 maxScroll, float damping, float max_speed){
		this.minScroll.set(minScroll);
		this.maxScroll.set(maxScroll);
		this.damping = damping;
		this.max_speed = max_speed;
		scrollBorders=true;
		
		for(int i=0; i<TOUCH_ACCURACY; i++)
			touchPos[i] = new Vector2();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		Sub.toStageCoordinates(Gdx.input.getX(), Gdx.input.getY(), Sub.tmp);
		if(overS){
			if(!( Sub.tmp.x >= 0 && Sub.tmp.y >= 0 && Sub.tmp.x <= width && Sub.tmp.y < height))
				overS=false;

			touchPos[touchPosI++].set((Sub.tmp.x-prevPos.x)/(Sub.delta),
									  (Sub.tmp.y-prevPos.y)/(Sub.delta));
			
			touchPosI %= TOUCH_ACCURACY;
			prevPos.set(Sub.tmp);
			
			scrollSpeed.set(0, 0);
			if(touchPosIFilled >= TOUCH_ACCURACY){
				for(int i=0; i<touchPos.length; i++)
					scrollSpeed.add(touchPos[i]);
				scrollSpeed.x /= (float)TOUCH_ACCURACY;
				scrollSpeed.y /= (float)TOUCH_ACCURACY;
			}else
				touchPosIFilled++;
			
		}else{
			if(Math.abs(scrollSpeed.x)>0)
				if(scrollSpeed.x>0){
					scrollSpeed.x -= damping*Sub.delta;
					if(scrollSpeed.x<0)
						scrollSpeed.x = 0;
				}else{
					scrollSpeed.x += damping*Sub.delta;
					if(scrollSpeed.x > 0)
						scrollSpeed.x = 0;
				}
			if(Math.abs(scrollSpeed.y)>0)
				if(scrollSpeed.y>0){
					scrollSpeed.y -= damping*Sub.delta;
					if(scrollSpeed.y<0)
						scrollSpeed.y = 0;
				}else{
					scrollSpeed.y += damping*Sub.delta;
					if(scrollSpeed.y > 0)
						scrollSpeed.y = 0;
				}
			
			touchPosIFilled = 0;
		}
		
		if(scrollBorders)
			if(scrolled.y < minScroll.y){
				if(overS)
					scrollSpeed.y *= (1-Math.min(height/2, -scrolled.y)/(height/2));
				else
					scrollSpeed.y = Math.min(height/2, Math.abs(scrolled.y))/(height/2)*max_speed;
			}else if(scrolled.y >maxScroll.y){
				if(overS)
					scrollSpeed.y *= (1-Math.min(height/2, (scrolled.y-maxScroll.y))/(height/2));
				else
					scrollSpeed.y = -Math.min(height/2, Math.abs(scrolled.y-maxScroll.y))/(height/2)*max_speed;
			}
		
		scrollSpeed.x = Math.max(Math.min(scrollSpeed.x, max_speed), -max_speed);
		scrollSpeed.y = Math.max(Math.min(scrollSpeed.y, max_speed), -max_speed);
		setScrolled(scrolled.x+(scrollSpeed.x)*Sub.delta,
					scrolled.y+(scrollSpeed.y)*Sub.delta);
		
	}
	
	public void setScrolled(float x, float y){
		scrolled.set(x, y);
	}
	public void stop(){
		touchPosIFilled=0;
		overS=false;
		scrollSpeed.set(0, 0);
	}
	
  //TOUCH
	@Override
	public Actor hit(float x, float y) {
		if(x > 0 && x <= width &&
		   y > 0 && y <= height)
				return this;
		return null;
	}
	@Override
	public boolean touchDown(float x, float y, int pointer){
		overS=true;
		touchPosIFilled=0;
		touchPosI=0;
		Sub.toStageCoordinates(Gdx.input.getX(), Gdx.input.getY(), prevPos);
		return true;
	}
	@Override
	public boolean touchMoved(float x, float y){
			return true;
	}
	public void touchUp(float x, float y, int pointer){
		overS = false;
		//absDistance=0;
	}
}
