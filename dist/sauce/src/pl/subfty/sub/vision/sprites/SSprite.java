package pl.subfty.sub.vision.sprites;

import pl.subfty.sub.Sub;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class SSprite extends Sprite{
	public float alpha=1;
	public boolean visible = true;
	
	public SSprite(){
		
	}
	public SSprite(String sheet, int id){
		loadSprite(sheet, id);
	}
	
	public void loadRegion(AtlasRegion a){
		setRegion(a);
		setSize(Math.abs(getRegionWidth()), Math.abs(getRegionHeight()));
	}
	public void loadSprite(String sheet, int id){
		loadRegion(Sub.art.atlas.findRegion(sheet, id));
	}
}
