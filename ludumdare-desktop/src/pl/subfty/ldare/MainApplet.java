package pl.subfty.ldare;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

public class MainApplet extends LwjglApplet{
    private static final long serialVersionUID = 1L;
    public MainApplet(){
        super((ApplicationListener) new Main(), false);
       // Main m = new Main();
    	//super(new MainApplet(), false);
    }
}