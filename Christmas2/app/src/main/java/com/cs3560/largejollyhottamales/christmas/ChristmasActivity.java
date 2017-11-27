/**
 * @file ChristmasActivity.java 
 * @brief sets up the Christmas Activty class
 * 
 * 
 */
 
package com.cs3560.largejollyhottamales.christmas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import java.util.Locale;

public class ChristmasActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        isItChristmas(); // set the answer now
        setLocalAlarm(); // so it updates this screen while the user is watching on Christmas
        
        // will cancel and re-schedule all alarms when the user opens the app
        ChristmasAlarm.setEnabledAlarms(this);
    }
    /// @brief finds out if it is Christmas
    /// @param none
    /// @return none
    public void isItChristmas() {
    	((TextView) findViewById(R.id.answer)).setText(Christmas.answer(Christmas.isIt(), Locale.getDefault()));
    }
    
    final Handler handler = new Handler();
    final Runnable updater = new Runnable() {
    	public void run() {
    		isItChristmas();
    	}
    };
    /// @brief sets the local alarm
    /// @param none
    /// @return none
    public void setLocalAlarm() {
    	Thread alarm = new Thread() {
    		public void run() {
    			long untilChristmas = Christmas.time() - System.currentTimeMillis();
    			
    			try {
    				sleep(untilChristmas);
    			} catch(InterruptedException e) {
    				// well, I never
    			}
    			handler.post(updater);
    		};
    	};
    	alarm.start();
    }
	/// @brief updates the CountdownActivity class
	/// @param View view
	/// @return none
    public void goToCountdown (View view){
		Intent intent = new Intent (this, CountdownActivity.class);
		startActivity(intent);
	}
	/// @brief updates the SupportActivity class
	/// @param View view
	/// @return none
	public void goToSupport (View view){
		Intent intent = new Intent (this, SupportActivity.class);
		startActivity(intent);
	}

    @Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
		super.onCreateOptionsMenu(menu); 
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) { 
		case R.id.preferences:
			startActivity(new Intent(this, ChristmasPreferences.class));
			break;
		}
		return true;
	}
}