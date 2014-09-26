package pl.subfty.sub.vision;

import com.badlogic.gdx.graphics.Color;

public class SColor extends Color{
	
	public SColor(){
		super();
	}
	public SColor(Color c){
		super(c);
	}
	public SColor(float r, float g, float b, float a){
		super(r,g,b,a);
	}
	
	public void interp(Color c, float value){
		r = r*(1-value)+c.r*value;
		g = g*(1-value)+c.g*value;
		b = b*(1-value)+c.b*value;
	}
}
