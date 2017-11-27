/**
 * @file ChristmasBootReceiver.java
 * @brief sets up the class BroadcastReceiver
 * 
 * 
 */
 
package com.cs3560.largejollyhottamales.christmas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChristmasBootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED))
			ChristmasAlarm.setEnabledAlarms(context);
	}

}