package com.attendr.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;


import android.graphics.Color;
import android.util.Log;

public class PostData {
	private String type;
	private String content;
	private uData user;
	private String event;
	private String _id;
	private String __v;
	private String posted;
	private int score;
	private String parent;
	private String[] comments;

	public PostData(String content, UserData u) {
		this.content = content;
		this.user = new uData();
	}
	
	public String[] getComments() {
		return comments;
	}

	public int getScore() {
		return score;
	}

	public int incrementScore() {
		this.score +=1;
		return 1;
	}
	
	public int getColorBar() {
		if(this.score<3)
			return R.drawable.color_bar_blank;
		if(this.score < 5)
			return R.drawable.color_bar_blue;
		if(this.score < 10) 
			return R.drawable.color_bar_green;
		return R.drawable.color_bar_red;
	}
	public int getColorSquare() {
		if(score < 3)
			return R.drawable.color_square_blank;
		if(score < 5)
			return R.drawable.color_square_blue;
		if(score < 10)
			return R.drawable.color_square_green;
		return R.drawable.color_square_red;
	}
	
	public int getColorRibbon() {
		if(score < 3)
			return R.drawable.color_square_blank;
		if(score < 5)
			return R.drawable.color_square_blue;
		if(score < 10)
			return R.drawable.color_square_green;
		return R.drawable.color_square_red;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public uData getUser() {
		return user;
	}
	
	public String getID() {
		return _id;
	}
	
	public class uData {
		String _id;
		String profileID;
		String image;
		uName name;
		

		public String getName() {
			return name.first+" "+name.last;
		}
		
		public String getProfilePictureID() {
			return profileID;
		}

		public String getID() {
			return _id;
		}
		public class uName {
			String first;
			String last;
		}
		public String getImageURL() {
			return image;
		}
	}

	public String getType() {
		return type;
	}

	public String getTime() {
        final String pattern = "yyyy-MM-dd'T'hh:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = null;
        try
        {
            d = sdf.parse(this.posted);
            System.out.println(d);
        } 
        catch (ParseException e)
        {
            e.printStackTrace();
        }
		Log.v("Current Time",System.currentTimeMillis()+"");
		String label = "";
		long differenceSeconds = (d.getTime()-System.currentTimeMillis())/1000;
		Log.v("Posted:", differenceSeconds+" seconds ago");
		if(differenceSeconds/(60)<1) //then same minute
			label = "less than a minute ago";
		else if(differenceSeconds/(60*60)<1) //then same hour
			label = (int)(differenceSeconds/60)+" minutes ago";
		else if(differenceSeconds/(60*60*24)<1)//then same day
			label = (int)(differenceSeconds/3600)+" hours ago";
		else 
			label = (int)(differenceSeconds/(60*60*24))+" days ago";
		return label;
	}
}
