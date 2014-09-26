package pl.subfty.sub.vision;

import pl.subfty.sub.vision.fonts.BFonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Art{
	public TextureAtlas atlas;
	
  //------EDIT------
	private final static String TEXTURES[] = {"textures/game/game"};
	
  //TEXTURES PREFIXES
	public final static String T_GME="gme";
		
	public Art(){
		atlas = new TextureAtlas();
		
		for(int i=0; i<TEXTURES.length; i++){
			TextureAtlas ta = new TextureAtlas(Gdx.files.internal(TEXTURES[i]));
			for(AtlasRegion a : ta.getRegions())
				atlas.addRegion(a.name, a).index = a.index;
		}
		loadFonts();
	}
	
  //LOADING FONTS
	public final static int F_CIRCULA=0;
	private static final String FONTS_SRC = "fonts/",
								F_SRC[] = {"font"};
	public static BFonts fonts[] = new BFonts[F_SRC.length];
	public void loadFonts(){		
		for(int i=0; i<F_SRC.length; i++){
			fonts[i] = new BFonts(Gdx.files.internal(FONTS_SRC+F_SRC[i]+".fnt"), 
					              atlas.findRegion(F_SRC[i], 0),
					              false);
			fonts[i].setFixedWidthGlyphs("0123456789");
		}
	}
}
