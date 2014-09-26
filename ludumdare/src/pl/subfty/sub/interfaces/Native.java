package pl.subfty.sub.interfaces;

import java.util.Map;

public interface Native {	
  //FLURRY
	public void onDestroy();
	public void lEvent(String id);
	public void lEvent(String eventId, boolean timed);
	public void lEvent(String eventId, Map<String, String> parameters);
	public void lEvent(String eventId, Map<String, String> parameters, boolean timed);
	
	//public final static String ;
	public void showBanner(String hook);
	public void hideBanner(String hook);
	
  //FACEBOOK
	public void postOnWall(String nick, int points);
	public void goToFanPage();
	
  //SWARM
	  //ACHIEVEMENTS
		//public final static int ;
	public void showSwarmDashboard();
	public void uAchievement(final int id);
	public void submitScore(int leaderboard, int score);
	public void ladScores(int leaderboard, final Callback c);
	public void ladUserPageScores(int leaderboard);
	public void swarmInit();
	public void showLeaderboard(int id);
	
  //GAME STATES
	public boolean showAds();
	public boolean sniTAUnlocked();
	
  //NATIVE UI
	public void askQuestion(final Callback call, final String question, final String yes, final String no);
	public void askQuestion(final Callback call, final String question);
	public void goToURL(final String url);
	public void showInfo(final String info);
	public void showAnnoucement(final String title, final String content);
	
  //MATH
	public static float PI= 3.141592653589793f,
						PI2 = PI/2,
						PI4 = PI/4,
						E = 2.718281828459045f;
	
	public float sqrt(float val);
	public float sin(float angle);
	public float cos(float angle);
	public float floor(float v);
	public float ceil(float v);
	public float toRadians(float angle);
	public float toDegrees(float angle);
}
