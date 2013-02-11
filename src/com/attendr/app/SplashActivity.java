package com.attendr.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.attendr.net.Utils;

public class SplashActivity extends SherlockActivity{
	public static LocationManager locationManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startNextActivity();
	}
	
	public void startNextActivity() {
		SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
		if(settings.getString("user_id", "no_id").equals("no_id")) {
			Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
			startActivity(intent);
		}
		else {
			Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
			startActivity(intent);
		}
	}
}
