package pl.subfty.ldare;

import pl.subfty.ldare.screens.death.Death;
import pl.subfty.ldare.screens.game.Game;
import pl.subfty.ldare.screens.menu.Menu;
import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Native;

import com.badlogic.gdx.ApplicationListener;

public class Dare implements ApplicationListener {
	
  //SCREENS
	public static Game game;
	public static Menu menu;
	public static Death death;
	
	public Dare(Native nat){
		Sub.nat = nat;
	}
	
	@Override
	public void create() {		
		Sub.onCreate();
		
	  //SCREENS INIT
		menu = new Menu();
		game = new Game();
		death = new Death();
		
		Sub.setScreen(menu);
	}

	@Override
	public void dispose() {
		Sub.onExit();
	}

	@Override
	public void render() {		
		Sub.onRender();
	}

	@Override
	public void resize(int width, int height) {
		Sub.onResize(width, height);
	}

	@Override
	public void pause() {
		Sub.onPause();
	}

	@Override
	public void resume() {
		Sub.onResume();
	}
}
