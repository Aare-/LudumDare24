package pl.subfty.sub.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Logger;

public class GameState {
	private Logger l = new Logger("GameState", Logger.DEBUG);
	
	private Preferences pref;

	private String testPref= "test0";
	
	public boolean bData[] = new boolean[0];
	private final static boolean DEF_B_VALUES[] = {};
		
	public GameState(){
		pref = Gdx.app.getPreferences(testPref+"data");
		
	  //LOADING BOOLEAN DATA
		for(int i=0; i<bData.length; i++)
			bData[i] = pref.getBoolean("B_"+i, DEF_B_VALUES[i]);
		
	
	}

	public void onExit(){
	  //SAVING BOOLEAN DATA
		for(int i=0; i<bData.length; i++)
			pref.putBoolean("B_"+i, bData[i]);
		
		pref.flush();
	}
}
