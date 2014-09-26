package pl.subfty.ldare;

import java.util.Map;

import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.interfaces.Native;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.FloatMath;
import android.widget.Toast;

public class ANative implements Native{
	Handler uiThread;
	Activity act;
	
	public ANative(Activity act){
		super();
		this.act = act;
		
		/*Swarm.addNotificationDelegate(new SwarmNotificationDelegate() {
			public boolean gotNotification(SwarmNotification notif) {
				GYRO.notif.showAchiw(notif.getTitle(),notif.getMessage());
				return true;
			}
		});*/
	}
	
  //FLURRY (SORT OF)

	public void onDestroy(){
     	//FlurryAgent.onEndSession(MainActivity.context);
     }

	public void lEvent(String id) {
		//FlurryAgent.logEvent(id);
	}	
	public void lEvent(String eventId, boolean timed) {
		//FlurryAgent.logEvent(eventId, timed);
	}
	public void lEvent(String eventId, Map<String, String> parameters) {
		//FlurryAgent.logEvent(eventId, parameters);
	}
	public void lEvent(String eventId, Map<String, String> parameters,
			boolean timed) {
		//FlurryAgent.logEvent(eventId, parameters, timed);
	}
	
	public void showBanner(String hook) {
		/*ViewGroup viewGroup = (ViewGroup) act.findViewById(R.id.);
		View promoView = appCircle.getHook(act.getApplicationContext(), 
										   hook, 
										   Constants.MODE_PORTRAIT);
		if (promoView != null) 
			viewGroup.addView(promoView);*/
	}
	public void hideBanner(String hook) {
		// TODO Auto-generated method stub
		
	}
	
  //GAME STATE
	public boolean showAds() {
		return true;
	}
	public boolean sniTAUnlocked() {
		return true;
	}
	
  //SWARM
	public void showSwarmDashboard(){
           //Swarm.showDashboard();
	}
	public void uAchievement(final int id){
	    /*if (MainActivity.achievements != null) {
	        SwarmAchievement achievement = MainActivity.achievements.get(id);
	        if (achievement != null && achievement.unlocked == false)
	            achievement.unlock();
	    }*/
	}
	public void submitScore(int id, int score) {
		/*if (MainActivity.leaderboards[id] != null) 
			MainActivity.leaderboards[id].submitScore(score);*/
	}
	public void ladScores(final int id, final Callback c){
		/*if (MainActivity.leaderboards[id] != null) {
			MainActivity.leaderboards[id].getTopScores("", new GotScoresCB() {
				@Override
				public void gotScores(int arg0, List<SwarmLeaderboardScore> arg1) {
					if(arg1 == null)
						return;
					
					int i=0;
					for(i=0; i<10 && i<arg1.size(); i++)
						Leaderboard.worldScores[id][i] = new SScore(arg1.get(i).user.username,
																(int)arg1.get(i).score,
																arg1.get(i).rank);
					for(; i<10; i++){
						Leaderboard.worldScores[id][i] = new SScore("",0,0);
					}
					if(c != null)
						c.call(Callback.SUCCES);
				}
			});
		}*/
	}
	public void ladUserPageScores(final int id){
		/*if (MainActivity.leaderboards[id] != null) {
			MainActivity.leaderboards[id].getPageOfScoresForCurrentUser(SwarmLeaderboard.LEADERBOARD_DATE_ALL_TIME, new GotScoresCB() {
				@Override
				public void gotScores(int arg0, List<SwarmLeaderboardScore> arg1) {
					if(arg1 == null)
						return;
					
					Leaderboard.friendScores[id] = new SScore[arg1.size()];
					int i=0;
					for(i=0; i<arg1.size(); i++)
						Leaderboard.friendScores[id][i] = new SScore(arg1.get(i).user.username,
																 (int)arg1.get(i).score,
																 arg1.get(i).rank);
				}
			});
		}*/
	}
	public void swarmInit(){
		/*uiThread.post(new Runnable() {
					public void run() {
		if(!Swarm.isInitialized())
					 Swarm.init(act, MainActivity.SWARM_APP_ID, MainActivity.SWARM_KEY, 
							    MainActivity.mySwarmLoginListener);
		}});*/
	}
	public void showLeaderboard(int id) {
		/*if(MainActivity.leaderboards[id] != null)
			MainActivity.leaderboards[id].showLeaderboard();*/
	}	
	
  //FLOAT MATH
	public float sqrt(float val) {
		return FloatMath.sqrt(val);
	}
	public float sin(float angle) {
		return FloatMath.sin(angle);
	}
	public float cos(float angle) {
		return FloatMath.cos(angle);
	}
	public float floor(float v) {
		return FloatMath.floor(v);
	}
	public float ceil(float v) {
		return FloatMath.ceil(v);
		
	}
	public float toRadians(float angle) {
		return Native.PI/180.0f*angle;
	}
	public float toDegrees(float angle) {
		return 180.0f/Native.PI*angle;
	}

  //NATIVE UI
	public void askQuestion(final Callback call, final String question, final String yes, final String no){
		uiThread.post(new Runnable() {
            public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(act);
				builder.setMessage(question)
				       .setCancelable(false)
				       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // call.call(Callback.SUCCES);
				           }
				       })
				       .setNegativeButton(no, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                //call.call(Callback.FAILURE);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	public void showAnnoucement(final String title, final String content){
		uiThread.post(new Runnable() {
            public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(act);
				builder.setMessage(content)
				       .setCancelable(false)
				       .setTitle(title)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	public void askQuestion(final Callback call, final String question) {
		askQuestion(call, question, "Yes", "No");
	}
	public void goToURL(final String url){
		uiThread.post(new Runnable() {
            public void run() {
            	act.startActivity(
                		new Intent(Intent.ACTION_VIEW, 
    						  	   Uri.parse(url)));
            }
		});
	}
	public void showInfo(final String info){
		uiThread.post(new Runnable() {
            public void run() {
            	Toast.makeText(act, info, Toast.LENGTH_LONG)
            		 .show();
            }
		});
	}
	
  //FACEBOOK
	public void postOnWall(final String nick, final int points) {
		uiThread.post(new Runnable() {
            public void run() {
            /*	String link = "http://facebook.pl/submachinestudios",
    				   picture="http://su8.ncse.pl/subfty/gfx/fejs.jpg",
    				   caption="http://subfty.com",
    				   name="I just scored "+points+" points in GYRO!",
    				   description="GYRO is an easy to play, but hard to master arcade game with intuitive touch controls, addictive gameplay, cool minimalistic graphics and gazillions of points to collect! Get it now from Google Play and start spinning!",
    				   redirect="http://subfty.com";
            	
            	String s = "http://m.facebook.com/dialog/feed?"+
		            	 "app_id="+appID+"&"+
		            	 "link="+link+"&"+
		            	 "picture="+picture+"&"+
		            	 "name="+name+"&"+
		            	 "caption="+caption+"&"+
		            	 "description="+description+"&"+
		            	 "redirect_uri="+redirect+"&"+
		            	 "display=touch";

            	act.startActivity(
            		new Intent(Intent.ACTION_VIEW, 
						  	   Uri.parse(s)));*/
		    }
		});
	}
	public void goToFanPage() {
		uiThread.post(new Runnable() {
            public void run() {
            	act.startActivity(
            		new Intent(Intent.ACTION_VIEW, 
						  	   Uri.parse("http://m.facebook.com/submachinestudios")));
		    }
		});
	}
}
