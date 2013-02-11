package com.attendr.net;

import java.util.ArrayList;

import android.app.Activity;

import com.attendr.app.EventData;
import com.attendr.app.PostData;
import com.attendr.app.ScheduleItem;
import com.attendr.app.UserData;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public class GetCallback{
	Activity current;
    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     * 
     * @param profile The {@code Profile} that was received from the server.
     */
    public void onDataReceived(UserData profile) { return; }
    public String onDataReceived(ArrayList<PostData> list) { return null; }
    public String onDataReceived(PostData post) { return null; }
    public String onDataReceived(EventData event) { return null; }
    public String onScheduleReceived(ArrayList<ScheduleItem> schedule) { return null; }
	public void setActivity(Activity a) {
		this.current = a;
	}

    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}