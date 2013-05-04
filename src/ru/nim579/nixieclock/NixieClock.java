//Class for Activity.Main
//Version 3.02. Last update: 18.11.2011.
//First developer: Ivanushkin Nikolay. Developer: also
//This is a main class. All logic relised here. Class use intents: main.xml, binary.xml, quatry.xml, trinary.xml.

package ru.nim579.nixieclock;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class NixieClock extends Activity {
    /** Called when the activity is first created. */
	private Timer timer1;
	public String hourMode;
	public boolean timerEn;
	public int in;
	
	//Image resources array
	Integer[] mImage = {R.drawable.s0, R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8, R.drawable.s9};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    //On create. Sets main content view
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
                
    }
    
    //Start application
    public void onStart(){
    	super.onStart();
        SharedPreferences prefsM = PreferenceManager
        		.getDefaultSharedPreferences(getBaseContext());
        
        boolean fsMode = prefsM.getBoolean("fullscreen", false);
        String ns_Mode = prefsM.getString("nsMode", this.getResources().getString(R.string.ns_v_d));
        hourMode = prefsM.getString("hou", this.getResources().getString(R.string.hou_v_d));
        timerEn = prefsM.getBoolean("timer_enable", false);
                
        String timerTo = prefsM.getString("timer_picker", "00:00");
        String[] tiToA = timerTo.split(":");
        int timerToM = 86400;
        if(prefsM.getBoolean("timer_to_end", false) == true){timerToM = Integer.parseInt(tiToA[0])*3600+Integer.parseInt(tiToA[1])*60;}
        
        //Fullscreen mode toggler
        if(fsMode == true){
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
        	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);}
        
        final int NumSys = Integer.parseInt(ns_Mode);
        final int t_length = NumSysLengh(NumSys);
        
        //Sets content view for numeral system.
        switch(t_length){
        case 2:
        	setContentView(R.layout.main);
        	break;
        case 3:
        	setContentView(R.layout.trinary);
        	break;
        case 4:
        	setContentView(R.layout.quatry);
        	break;
        default:
        	setContentView(R.layout.binary);
        }
        
        //Image view arrays
        final ImageView[] face_img_hou = {(ImageView)findViewById(R.id.bh1), (ImageView)findViewById(R.id.bh2), (ImageView)findViewById(R.id.bh3),
        		(ImageView)findViewById(R.id.bh4), (ImageView)findViewById(R.id.bh5), (ImageView)findViewById(R.id.bh6)};
        final ImageView[] face_img_min = {(ImageView)findViewById(R.id.bm1), (ImageView)findViewById(R.id.bm2), (ImageView)findViewById(R.id.bm3),
        		(ImageView)findViewById(R.id.bm4), (ImageView)findViewById(R.id.bm5), (ImageView)findViewById(R.id.bm6)};
        final ImageView[] face_img_sec = {(ImageView)findViewById(R.id.bs1), (ImageView)findViewById(R.id.bs2), (ImageView)findViewById(R.id.bs3),
        		(ImageView)findViewById(R.id.bs4), (ImageView)findViewById(R.id.bs5), (ImageView)findViewById(R.id.bs6)};         
        final ImageView[][] fin_img = {face_img_sec, face_img_min, face_img_hou};
        
        //Conversion from hours minutes and seconds to seconds. Variable for timer.
        final Date today = new Date();
    	final int daySec = 86400;
    	final int daySecR = timerToM;
    	in = 3600*today.getHours()+60*today.getMinutes()+today.getSeconds();
    	if(timerEn == true){in = daySecR - in; if(in <= 0){in += daySec;}}
    	                
        timer1 = new Timer();
        timer1.schedule(new TimerTask(){
        	
        	@Override
        	public void run() {
            //Logic of increment/decrement for time/timer
    		if(timerEn == true){--in;} else {++in;}
    		if(timerEn == true && in <= 0){in += daySec;}
    		//Resets time at the end of the day
    		in %= daySec;
    		//Conversion from seconds to normal time
			int hou = in/3600;
			int min = (in-hou*3600)/60;
			int sec = in-hou*3600-min*60;
			//Set 12-hour format if it turned on
			hou -= 12*(hou/13)*Integer.parseInt(hourMode);
			//Array of the current time based on arrays of numbers
			final int[][] fin_char = {NumSysArray(sec, NumSys), NumSysArray(min, NumSys), NumSysArray(hou, NumSys)};
			runOnUiThread(new Runnable()
			{
				public void run() {
					//Alert, then timer done
					if(timerEn == true && in == 0){Toast.makeText(getBaseContext(), getString(R.string.alert_timer_done), Toast.LENGTH_LONG).show();}
					for (int i = 0; i < t_length*3; i++) {
						//Printing time
						fin_img[i/t_length][i%t_length].setImageResource(mImage[fin_char[i/t_length][t_length-1-i%t_length]]);
					}
				}
			});	

        	}
        }, 0, 1000);
    }
    
    //Cancel timer, then activity stoped
	public void onStop(){
    	super.onStop();
  	
    	timer1.cancel();
    }
    
	//Converts number to the desired numeral system, divides that number by digits, and writes the array
    private static int[] NumSysArray(int n, int o){
    	int e = 1;
    	int oe = o;
		while(59 >= oe){
			oe *= o;
			++e;
		}
		int[] d = new int[e];
		while(n != 0){
			e--;
			d[e] = n % o;
			n /= o;
		}
    	return d;
    }
    
    //Specifies the length of a given numeral system
    private static int NumSysLengh(int o){
    	int e = 1;
    	int oe = o;
		while(59 >= oe){
			oe *= o;
			++e;
		}
		return e;
    }
        
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ab_m:
	        goAbout();
	        return true;
	    case R.id.set_m:
	        goMenu();
	        return true;
	    case R.id.exit_m:
	        finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	public void goAbout(){
		Intent i = new Intent(this,About.class);
		startActivity(i);
	}

	public void goMenu(){
		Intent i = new Intent(this,Prefs.class);
		startActivity(i);
	}
}
  
