package pl.subfty.sub.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SSound {
	
	private final static String SOUND_PATCH = "sounds/";
  //DYNAMIC SOUNDS
	public static final int LAZOR=0,
							HIT=1,
							MISS=2;
	private Sound sounds[] = new Sound[22];
	private long loopingIds[] = new long[22];
	private boolean looping[] = new boolean[22];
	private float volume[] = new float[22];
	
	public SSound(){
	  //LOADING STATIC SOUNDS
		loadSound(LAZOR,"laser");
		loadSound(HIT,"collected");
		loadSound(MISS,"missed");
	}
	private void loadSound(int id, String name){
		sounds[id] = Gdx.audio.newSound(Gdx.files.internal(SOUND_PATCH+name+".wav"));
		looping[id] = false;
	}
	
	public void playSound(int id){
		playSound(id, 1);
	}
	public void playSound(int id, float volume){
			sounds[id].stop();
			sounds[id].play(volume);
	}
	public void loopSound(int id){
		loopSound(id, 1);
	}
	public void loopSound(int id, float volume){
		sounds[id].stop();
		sounds[id].stop();
		this.volume[id] = volume;
		loopingIds[id] = sounds[id].loop(volume);
		
		looping[id] = true;
	}
	public boolean isLooping(int id){
		return looping[id];
	}
	public void setVoumeforLoopingS(int id, float volume){
		if(!looping[id])
			return;
		this.volume[id] = volume;
		sounds[id].setVolume(loopingIds[id], volume);
	}
	public void stop(int id){
		sounds[id].stop();
		looping[id]=false;
	}
	public void stopAll(){
		for(int i=0; i<sounds.length; i++)
			stop(i);
	}

	public void stopAllLooping(){
		for(int i=0; i<sounds.length; i++){
			if(looping[i]){
				sounds[i].stop();
				looping[i]=false;
			}
		}
			
	}
	public void killAllLooping(){
		for(int i=0; i<sounds.length; i++)
			if(looping[i]){
				sounds[i].stop();
				looping[i]=false;
			}
	}
	public void resumeAllLooping(){
		for(int i=0; i<sounds.length; i++)
			if(looping[i])
				sounds[i].loop(volume[i]);
	}
}
