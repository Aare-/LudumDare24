package pl.subfty.ldare;

import java.util.Map;

import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.interfaces.Native;

import com.badlogic.gdx.utils.Logger;

public class DNative implements Native{
	private Logger l = new Logger("DNative", Logger.DEBUG);
	
	@Override
	public void onDestroy() {
		
	}

	@Override
	public void lEvent(String id) {

	}

	@Override
	public void lEvent(String eventId, boolean timed) {

	}

	@Override
	public void lEvent(String eventId, Map<String, String> parameters) {

	}

	@Override
	public void lEvent(String eventId, Map<String, String> parameters,
			boolean timed) {

	}

	public boolean showAds() {
		return true;
	}
	public boolean sniTAUnlocked() {
		return true;
	}

  //NMATH
	public float sqrt(float val) {
		return (float)Math.sqrt(val);
	}
	public float sin(float angle) {
		return (float)Math.sin(angle);
	}
	public float cos(float angle) {
		return (float)Math.cos(angle);
	}
	public float floor(float v) {
		return (float)Math.floor(v);
	}
	public float ceil(float v) {
		return (float)Math.ceil(v);
	}
	public float toRadians(float angle) {
		return Native.PI/180.0f*angle;
	}
	public float toDegrees(float angle) {
		return 180.0f/Native.PI*angle;
	}



	@Override
	public void postOnWall(String nick, int points) {

	}

	@Override
	public void showSwarmDashboard() {

	}

	@Override
	public void uAchievement(int id) {
		
	}



	@Override
	public void swarmInit() {

	}

	@Override
	public void askQuestion(Callback call, String question) {

	}

	@Override
	public void goToFanPage() {

	}




	@Override
	public void askQuestion(Callback call, String question, String yes,
			String no) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToURL(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showInfo(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAnnoucement(String title, String content) {
		l.info(title+": "+content);
	}

	@Override
	public void submitScore(int leaderboard, int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ladScores(int leaderboard, Callback c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ladUserPageScores(int leaderboard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLeaderboard(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showBanner(String hook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideBanner(String hook) {
		// TODO Auto-generated method stub
		
	}

		
}
