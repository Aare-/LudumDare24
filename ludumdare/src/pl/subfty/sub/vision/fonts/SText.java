package pl.subfty.sub.vision.fonts;

import pl.subfty.sub.vision.Art;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SText{
	public final static char NUMBERS[] = {'0','1','2','3','4','5','6','7','8','9', ' '};
	public int font;
	public boolean visible = true,
				   type=false; //false = text, true = number
	
	public float x=0,
				 y=0,
				 scale=1,
				 alpha=1,
				 fadeInA=1,
				 settedNumberF,
				 wrapWidth;
	private float prevCA;
	
	public Color c= new Color(Color.WHITE);
	public String text="";
	public char ctext[]=null;
	public int settedNumber;
	
	public TextBounds bounds = new TextBounds();
	public BitmapFont.HAlignment alignment;
	
	public SText(int font, float wrapWidth){
		this.font = font;
		alignment = HAlignment.LEFT;
		this.wrapWidth = wrapWidth;
	}
	
	public void setText(String text){
		type=false;
		this.text = text;
	}
	public void setNumber(int number, int maxDigit){
		settedNumber = number;
		type=true;
		int nCache;
		if(ctext == null){
			nCache = number;
			while(nCache > 0){
				nCache /= 10;
			}
			ctext = new char[maxDigit];
		}
		nCache = number;
		final int length = ctext.length;
		int digit=0;
		if(nCache == 0)
			ctext[length- digit++ -1] = NUMBERS[0];
		
		while(nCache > 0 && digit < maxDigit){
			ctext[length- digit++ -1] = NUMBERS[nCache%10];
			nCache /= 10;
		}
		
		while(digit < length)
			ctext[length - digit++ -1] =  NUMBERS[10];
	}
	public void setNumberF(float number, int maxDigit){
		settedNumberF=number;
		setNumber((int)number, maxDigit);
	}

	public TextBounds calcBounds(){
		Art.fonts[font].setScale(135.0f/Art.fonts[font].getLineHeight()*scale);
		bounds.set(Art.fonts[font].getBounds(text));
		return bounds;
	}
	
	public void draw(SpriteBatch batch){
		if(!visible)
			return;
		draw(batch, 1);
	}
	public TextBounds draw(SpriteBatch batch, float parentAlpha){
		if(!visible)
			return bounds;
		
		prevCA = c.a;
		c.a *= alpha*parentAlpha;
		Art.fonts[font].setColor(c);
		c.a = prevCA;
		
		Art.fonts[font].setScale(135.0f/Art.fonts[font].getLineHeight()*scale);
		if(type){
		  //DRAWING NUMBER
			bounds.set(Art.fonts[font].drawMultiLine(batch, ctext, x, y, wrapWidth, alignment));
			return bounds;
		}else{
		  //DRAWING TEXT
			bounds.set(Art.fonts[font].drawMultiLine(batch, text, x, y, wrapWidth, alignment, fadeInA, true));
			return bounds;
		}
	}
}
