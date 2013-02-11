package com.attendr.net;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

import com.attendr.app.EventData;
import com.attendr.app.PlaceData;
import com.attendr.app.PostData;
import com.attendr.app.ScheduleItem;
import com.attendr.app.UserData;
import com.attendr.net.Utils.CoordinateObject.LocationObject;
import com.google.gson.Gson;

public class Utils {

	public static final String PREFS_NAME = "attendr_prefs_file";
	public static final String GUEST_ID = "50f892be946d2b441d000006";
	public static final String server_url = "http://caelondia.student.rit.edu";
	public static final int COMPOSE_DIALOG = 2;
	public static final int HOUR_HEIGHT = 60;

	public static String constructRestUrlForCreateUser() {
		return server_url+"/user/create/";
	}
	
	public static String constructRestUrlForCreateFBUser() {
		return server_url+"/user/fblogin/";
	}

	public static String constructRestUrlForGetPost(String user_id, String event_id) {
		return server_url+"/post/get/?user="+user_id+"&event_id="+event_id;
	}

	public static String constructRestUrlForCreatePost(String user_id, String event_id) {
		return server_url+"/post/create/?user="+user_id+"&event_id="+event_id;
	}

	public static String constructRestUrlForGetPostList(String user_id, String event_id) {
		return server_url+"/post/get/?user="+user_id+"&event_id="+event_id;
	}

	public static String constructRestUrlForUpPlusLikeVotePost(String user_id) {
		return server_url+"/post/upvote/?user="+user_id;
	}

	public static String constructRestUrlForNearbyEvents(String user_id) {
		return server_url+"/event/nearby/?user="+user_id;
	}

	public static String constructRestUrlForCreateEvent(String user_id) {
		return server_url+"/event/create/?user="+user_id;
	}
	
	public static String constructRestUrlForGetEvent(String user_id, String event_id) {
		return server_url+"/event/"+event_id+"?user="+user_id;
	}
	
	public static String constructRestUrlForAttendEvent(String user_id, String event_id) {
		return server_url+"/event/"+event_id+"/attend/?user="+user_id;
	}
	
	public static String constructRestUrlForGetSchedule(String user_id, String event_id) {
		return server_url+"/event/"+event_id+"/schedule/?user="+user_id;
	}
	
	//takes same data as events nearby
	public static String constructRestUrlForNearbyPlaces(String user_id) {
		return server_url+"/place/nearby/?"+user_id;
	}

	public static UserData parseResponseAsProfile(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, UserData.class);
	}
	
	public static PostData parseResponseAsPost(String response) {
		Gson gson = new Gson();
		Log.v("Response", response);
		PostData[] post = gson.fromJson(response, PostData[].class);
		if(post.length==0)
			return null;
		return post[0];
	}
	
	public static ArrayList<PostData> parseResponseAsPostList(String json) {
		ArrayList<PostData> posts = new ArrayList<PostData>();
		
		Gson gson = new Gson();
		PostData[] data = gson.fromJson(json, PostData[].class);
		if(data != null)
			for (PostData p : data) {
				if(p!=null)
					posts.add(0,p);
			}
		return posts;
	}
	
	public static ArrayList<EventData> parseResponseAsEventList(String json) {
		ArrayList<EventData> posts = new ArrayList<EventData>();
		
		Gson gson = new Gson();
		EventData[] data = gson.fromJson(json, EventData[].class);
		
		for (EventData e : data) {
			if(e!=null)
				posts.add(0,e);
		}
		return posts;
	}
	
	public static EventData parseResponseAsEvent(String response) {
		Gson gson = new Gson();
		Log.v("Response", response);
		EventData post = gson.fromJson(response, EventData.class);
		return post;
	}
	
	public static ArrayList<PlaceData> parseResponseAsPlaceList(String response) {
		ArrayList<PlaceData> places = new ArrayList<PlaceData>();
		
		Gson gson = new Gson();
		GetPlaceObject data = gson.fromJson(response, GetPlaceObject.class);
		if(data != null)
			for (PlaceData p : data.results) {
				if(p!=null)
					places.add(0,p);
			}
		return places;
	}
	
	public static ArrayList<ScheduleItem> parseResponseAsSchedule(String response) {
		ArrayList<ScheduleItem> schedule = new ArrayList<ScheduleItem>();
		
		Gson gson = new Gson();
		ScheduleItem[] data = gson.fromJson(response, ScheduleItem[].class);
		if(data != null)
			for (ScheduleItem s : data) {
				if(s!=null)
					schedule.add(0,s);
			}		
		return schedule;
	}
	
	public static String serializeCreatePost(String content, String type) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CreatePostObject(content, type));
		return requestBody;
	}
	
	public static String serializeGetPost(String pid) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.PostObject(pid));
		return requestBody;
	}
	
	public static String serializeGetPostListRequest(String event_id) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.EventIDObject(event_id));
		return requestBody;
	}
	
	public static String serializeUpPlusLikeVotePost(String pid) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.PostObject(pid));
		return requestBody;
	}

	public static String serializeCreateUser(
			String user_first, String user_last, String facebookID) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CreateUserObject(user_first, user_last, facebookID));
		return requestBody;
	}
	
	public static String serializeEventRequest(double lat, double lon) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CoordinateObject(lat, lon));
		return requestBody;
	}
	
	public static String serializeCreateEventRequest(double lon, double lat, String name, String description, String start, String end) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CreateEventObject(lon, lat, name, description, start, end));
		return requestBody;
	}
	
	public static String serializeGetEventRequest(String event_id) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.EventIDObject(event_id));
		return requestBody;
	}
	
	public static String serializeGetCommentsRequest(String[] ids) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.GetCommentsObject(ids));
		return requestBody;
	}
	
	public static String serializeAttendEventRequest(String event_id) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.EventIDObject(event_id));
		return requestBody;
	}
	
	public static String serializeCreateComment(String content, String parent) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CreateCommentObject(content,parent));
		return requestBody;
	}
	
	public static String serializeCreateFBUser(String token) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new Utils.CreateFBUserObject(token));
		return requestBody;
	}
	
	public static String serializeGetNearbyPlacesRequest(double lat, double lon) {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new GetPlacesObject(lat, lon, 1000));
		return requestBody;
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static void saveUser(String uid, SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("user_id", uid);
		editor.commit();
		editor.apply();
	}
	
	public static Date parseStringAsDate(String str) {
		final String pattern = "yyyy-MM-dd'T'hh:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = null;
        try
        {
            d = sdf.parse(str);
            System.out.println(d);
        } 
        catch(ParseException e) {
        	e.printStackTrace();
        }
        return d;
	}
	
	public static String getCurrentUserID(SharedPreferences settings) {
		return settings.getString("user_id", Utils.GUEST_ID);
	}
	
	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	public static class CreateUserObject {
		public UserName name;
		public String email;
		public CreateUserObject(String fn, String ln, String email) {
			name = new UserName(fn, ln);
			this.email = email;
		}
		public class UserName {
			String first;
			String last;
			public UserName(String firstName, String lastName) {
				this.first = firstName;
				this.last = lastName;
			}
		}
	}
	
	public static class CreateFBUserObject {
		public String access_token;
		public CreateFBUserObject(String a) {
			access_token = a;
		}
	}
	
	public static class CreatePostObject {
		String content;
		String type;
		public CreatePostObject(String c, String t) {
			this.content = c;
			this.type = t;
		}
	}
	
	public static class CreateCommentObject {
		String content;
		String responding;
		String type;
		public CreateCommentObject(String c, String r) {
			this.content = c;
			this.responding = r;
			this.type = "text";
		}
	}
	
	public static class GetPlaceObject {
		String[] html_attributions;
		PlaceData[] results;
	}
	
	public static class GetPlacesObject {
		int radius;
		LocationObject loc;
		public GetPlacesObject(double lat, double lon, int radius) {
			this.radius = radius;
			loc = new LocationObject(lat, lon);
		}
	}
	public static class PostObject {
		String _id;
		public PostObject(String _id) {
			this._id = _id;
		}
	}
	
	public static class CreateEventObject {
		CoordinateObject.LocationObject loc;
		String name, start, end, description;
		public CreateEventObject(double d, double e, String name, String description, String start, String end) {
			this.loc = new CoordinateObject.LocationObject(d, e);
			this.name = name;
			this.description = description;
			this.start = start;
			this.end = end;
		}
	}
	
	public static class CoordinateObject {
		LocationObject loc;
		public CoordinateObject(double lat, double lon) {
			loc = new LocationObject(lat, lon);
		}

		public static class LocationObject {
			public double lat;
			public double lon;
			
			public LocationObject(double lat, double lon) {
				this.lat = lat;
				this.lon = lon;
			}
		}
	}

	public static class EventIDObject {
		String event_id;
		public EventIDObject(String event_id) {
			this.event_id = event_id;
		}
	}

	public static class GetCommentsObject {
		String[] _id;
		public GetCommentsObject(String[] id_array) {
			_id = id_array;
		}
	}
	
	public static class TabInfo {
		 public String tag;
       private Class<?> clss;
       private Bundle args;
       private Fragment fragment;
       public TabInfo(String tag, Class<?> clazz, Bundle args) {
      	 this.tag = tag;
      	 this.clss = clazz;
      	 this.args = args;
       }
	}
	
	/**
	 * A simple factory that returns dummy views to the Tabhost
	 * @author mwho
	 */
	public static class TabFactory implements TabContentFactory {

		private final Context mContext;

	    /**
	     * @param context
	     */
	    public TabFactory(Context context) {
	        mContext = context;
	    }

	    /** (non-Javadoc)
	     * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	     */
	    public View createTabContent(String tag) {
	        View v = new View(mContext);
	        v.setMinimumWidth(0);
	        v.setMinimumHeight(0);
	        return v;
	    }
	}
}
