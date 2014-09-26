package pl.subfty.sub.vision.actors.scrollers;

import pl.subfty.sub.Sub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Logger;

public class Scrollable extends Group{
	private Logger l = new Logger("Scroll", Logger.DEBUG);
	public float interspace=0;
	
	protected int selected=0;
	protected float scrolled=0,
				    selectedX=0;
	public static boolean SCROLLING_BLOCK=false;
	
  //SCROLLING
	private Vector2 prev = new Vector2();
	private float xDelta[] = new float[1],
				  veloc,
				  target,
				  scrollSensitivity;
	private int filled=0,
				pos=0;
	private long time;
	private boolean jTouched=true,
					lr=false;
	
	public Scrollable(float scrollSensitivity){
		this.scrollSensitivity = scrollSensitivity;
	}

	public void setSelected(int what){
		setS(what);
		
		scrolled=0;
		for(int i=0; i<what; i++)
			scrolled -= children.get(i).width+interspace;
		target = selectedX = scrolled;
	}
	protected int setS(int s){
		return selected = s;
	}
	
	private float sum;
	public void draw(SpriteBatch batch, float parentAlpha){
	  //LOGIC YO!
		sum=0;
		final int size = children.size();
		for(int i=0; i<size; i++){
			children.get(i).x = scrolled+this.width/2-children.get(i).width/2+sum;
			children.get(i).y = (this.height-children.get(i).height)/2;
			sum += children.get(i).width+interspace;
		}
		
		Sub.toStageCoordinates(Gdx.input.getX(), Gdx.input.getY(), Sub.tmp);
		if(!Sub.BLOCK_INPUT || SCROLLING_BLOCK){
			if(Gdx.input.isTouched() &&
			   (Sub.tmp.y >= this.y && Sub.tmp.y <= this.y+this.height)){
				if(!jTouched){
					xDelta[(pos++)%xDelta.length] = (prev.x - Sub.tmp.x)/((System.currentTimeMillis()-time));
					filled++;
					
					if(filled>xDelta.length){
						final int size2 = xDelta.length;
						veloc=0;
						for(int i=0; i<size2; i++)
							veloc += xDelta[i];
						veloc /= size2;
						
						scrolled -= veloc*Sub.delta*1000;
					}
				}
				
				prev.set(Sub.tmp);
				time = System.currentTimeMillis();
				jTouched=false;
			}else{
				filled = 0;
				if(!jTouched){
				  //JUST RELEADES, FIND TARGET TO FALL BACK TO
					if(Math.abs(scrolled-selectedX)>scrollSensitivity){
						if(scrolled-selectedX > 0){
							if(selected>0)
								target = selectedX += children.get(setS(selected-1)+1).width+interspace;
						}else{
							if(selected < children.size()-1)
								target = selectedX -= children.get(setS(selected+1)-1).width+interspace;
						}
							
					}
				}
				jTouched=true;
				
				if(Math.abs(target-scrolled)>10){
					veloc = Math.max(Math.min((Math.abs((scrolled-target)/100.0f)), 7), 0.01f);
					veloc *= (scrolled-target < 0)?-1:1;
					
					if(scrolled<target)
						lr=false;
					else
						lr = true;
					scrolled -= veloc*Sub.delta*1000;
					if(scrolled>=target && !lr)
						scrolled=target;
					if(scrolled<=target && lr)
						scrolled=target;
				}else{
					scrolled=target;
				}
			}
		}
		
	  //DRAWING
		super.draw(batch, parentAlpha);
	}
}
