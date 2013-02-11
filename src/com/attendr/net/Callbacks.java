package com.attendr.net;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.attendr.app.*;

public class Callbacks {

	public static class RegisterCallback extends PostCallback {
		public String onDataReceived(UserData profile) {
			RegisterActivity a = (RegisterActivity) current;
			a.uid = profile.getID();
			Utils.saveUser(a.uid,
					current.getSharedPreferences(Utils.PREFS_NAME, 0));
			a.showSuccessFragment();
			a.isFinished(true);
			return ((RegisterActivity) current).uid;
		}
	}

	public static class EventListCallback extends PostCallback {
		@Override
		public String onEventListReceived(ArrayList<EventData> list) {
			Log.v("EventListActivity:","RefreshedEventList.");
			EventListActivity a = (EventListActivity)current;
			a.eventList = list;
			Log.v("list size:",""+list.size());
			if(list.size()==0 || list == null) {
				a.noEventsFound();
			}
			else
				a.refreshEventListFragment(list);
			return "success";
		}
	}
	
	public static class PlaceListCallback extends PostCallback {
		
		public void onPlaceListReceived(ArrayList<PlaceData> list) {
			EventListActivity a = (EventListActivity)current;
			PlaceListFragment frag = a.placeListFragment;
			frag.refreshListView(list);
		}
	}
	
	public static class CreateEventCallback extends PostCallback {

	}

	public static class GetCommentsCallback extends GetCallback {
		@Override
		public String onDataReceived(ArrayList<PostData> list) {
			EventPostViewActivity a = (EventPostViewActivity) current;
			if(list!=null)
				a.updateCommentListView(list);
			return "success";
		}
	}
	
	public static class GetPostCallback extends GetCallback {
		public String onDataReceived(PostData p) {
			EventPostViewActivity a = (EventPostViewActivity) current;
			a.post = p;
			TextView content = (TextView) a.findViewById(R.id.post_content);
			content.setText(a.post.getContent());

			// Fill user name TextView
			TextView name = (TextView) a.findViewById(R.id.user_name);
			name.setText(a.post.getUser().getName());

			// Fill the time posted TextView
			TextView time = (TextView) a.findViewById(R.id.time_posted);
			time.setText(a.post.getTime());
	
			// Set the popularity color
			View color = a.findViewById(R.id.color_square);
			color.setBackgroundResource(a.post.getColorSquare());
	
			TextView likes = (TextView) a.findViewById(R.id.number_likes);
			likes.setText("" + a.post.getScore());
			a.loadComments(a.post.getComments());
			return "success";
		}
	}

	public static class UpPlusLikeVoteCallback extends PostCallback {
		
		@Override
		public void onDataReceived(String s) {
			EventPostViewActivity a = (EventPostViewActivity) current;
			a.post.incrementScore();

			View color = a.findViewById(R.id.color_square);
			color.setBackgroundResource(a.post.getColorSquare());

			TextView likes = (TextView) a.findViewById(R.id.number_likes);
			likes.setText("" + a.post.getScore());
		}
	}

	public static class PostListCallback extends GetCallback {

		@Override
		public String onDataReceived(ArrayList<PostData> list) {
			EventActivity a = (EventActivity) current;
			EventContentFeedFragment cf = a.getContentFragment();

			if (list != null) {
				cf.refreshListView(list);
				return "success!";
			}
			return "failure...";
		}
	}

	public static class CreatePostCallback extends PostCallback {

		public String onDataReceived(PostData post) {
			Log.v("Received", "Post List Received.");
			EventActivity a = (EventActivity) current;
			a.addPostToList(post);
			return post.getID();
		}
	}
	
	public static class CreateCommentCallback extends PostCallback {
		
		public String onDataReceived(PostData post) {
			EventPostViewActivity a = (EventPostViewActivity)current;
			a.addCommentToList(post);
			return "success";
		}
	}
	
	public static class GetEventCallback extends GetCallback {
		
		public String onDataReceived(EventData event) {
			EventDescriptionFragment frag = ((EventActivity)current).getDescriptionFragment();
			frag.displayEvent(event);
			return "success";
		}
	}
	
	public static class GetScheduleCallback extends GetCallback {
		
		public String onScheduleReceived(ArrayList<ScheduleItem> schedule) {
			EventScheduleFragment frag = ((EventActivity)current).getScheduleFragment();
			frag.displaySchedule(schedule);
			return "success!";
		}
	}
}