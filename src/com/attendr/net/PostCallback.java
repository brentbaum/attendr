package com.attendr.net;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.attendr.app.EventData;
import com.attendr.app.PlaceData;
import com.attendr.app.PostData;
import com.attendr.app.UserData;

/**
 * 
 * Class definition for a callback to be invoked when the response for the data 
 * submission is available.
 * 
 */
public abstract class PostCallback{
	Activity current;
	
    /**
     * Called when a POST success response is received. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public void onPostSuccess() {
	}

	public String onDataReceived(UserData profile) {
		return null;
	}
	
	public String onDataReceived(PostData post) {
		return null;
	}
	
	public String onEventListReceived(ArrayList<EventData> list) {
		return null;
	}
	
	public String onEventReceived(EventData event) {
		return null;
	}

	public void onDataReceived(String response) {}
	
	public void setActivity(Activity a) {
		this.current = a;
	}

	public void onPlaceListReceived(ArrayList<PlaceData> places) {
		
	}
}