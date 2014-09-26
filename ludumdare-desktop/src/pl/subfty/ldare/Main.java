package pl.subfty.ldare;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ludumdare";
		cfg.useGL20 = false;
		float scale = 1.5f;
		cfg.resizable=false;
		cfg.width = (int)(480*scale);
		cfg.height = (int)(320*scale);
		
		new LwjglApplication(new Dare(new DNative()), cfg);
	}
}
