package com.attendr.net;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.util.Log;

import com.attendr.app.EventData;
import com.attendr.app.PlaceData;
import com.attendr.app.PostData;
import com.attendr.app.ScheduleItem;
import com.attendr.app.UserData;
import com.attendr.net.Callbacks.CreateCommentCallback;
import com.attendr.net.Callbacks.GetCommentsCallback;
import com.attendr.net.Callbacks.RegisterCallback;

public class ServerAPI {
	String user_id, event_id;
	Activity current;

	public static ServerAPI getInstance(String uid, Activity current) {
		return new ServerAPI(uid, current);
	}

	private ServerAPI(String uid, Activity c) {
		this.user_id = uid;
		this.current = c;
	}

	public void setEventID(String eid) {
		this.event_id = eid;
	}

	/**
	 * Submit a user profile to the server.
	 * 
	 * @param facebookID
	 * @param profile
	 *            The profile to submit
	 * @param callback
	 *            The callback to execute when submission status is available.
	 */
	public void createUserProfile(String user_first, String user_last,
			String email, final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForCreateUser();
		String requestBody = Utils.serializeCreateUser(user_first, user_last,
				email);
		new PostTask(restUrl, requestBody, "Create User",
				new RestTaskCallback() {
					@Override
					public void onTaskComplete(String response) {
						UserData profile = Utils
								.parseResponseAsProfile(response);
						callback.setActivity(current);
						callback.onDataReceived(profile);
					}
				}).execute();
	}

	/**
	 * Request a Post from the REST server.
	 * 
	 * @param userName
	 *            The user id for which the profile is to be requested.
	 * @param callback
	 *            Callback to execute when the profile is available.
	 */
	public void getPost(String pid, final GetCallback callback) {
		String restUrl = Utils.constructRestUrlForGetPost(user_id, event_id);
		String requestBody = Utils.serializeGetPost(pid);
		new PostTask(restUrl, requestBody, "Get Single Post",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						PostData post = Utils.parseResponseAsPost(response);
						callback.setActivity(current);
						callback.onDataReceived(post);
					}
				}).execute();
	}

	/**
	 * Request 20 most recent Posts from the REST server.
	 * 
	 * @param userName
	 *            The user id for which the posts are to be requested.
	 * @param callback
	 *            Callback to execute when the posts are available.
	 */
	public void getPostList(final GetCallback callback) {
		String restUrl = Utils
				.constructRestUrlForGetPostList(user_id, event_id);
		String requestBody = Utils.serializeGetPostListRequest(event_id);
		new PostTask(restUrl, requestBody, "Get Post List",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						ArrayList<PostData> temp = Utils
								.parseResponseAsPostList(response);
						ArrayList<PostData> posts = new ArrayList<PostData>();
						Iterator<PostData> i = temp.iterator();
						while (i.hasNext()) {
							posts.add(0, (PostData) i.next());
						}
						callback.setActivity(current);
						callback.onDataReceived(posts);
					}
				}).execute();
	}

	/**
	 * Get List of Comments for a given post
	 * 
	 * @param ids
	 *            List of ids of comments to get
	 * @param callback
	 *            The callback to execute when submission status is available
	 */
	public void getCommentList(String[] ids, final GetCommentsCallback callback) {
		String restUrl = Utils
				.constructRestUrlForGetPostList(user_id, event_id);
		String requestBody = Utils.serializeGetCommentsRequest(ids);
		new PostTask(restUrl, requestBody, "Get Comment List",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						ArrayList<PostData> temp = Utils
								.parseResponseAsPostList(response);
						ArrayList<PostData> posts = new ArrayList<PostData>();
						Iterator<PostData> i = temp.iterator();
						while (i.hasNext()) {
							posts.add(0, (PostData) i.next());
						}
						callback.setActivity(current);
						callback.onDataReceived(posts);
					}
				}).execute();
	}

	/**
	 * Submit a post to the server.
	 * 
	 * @param type
	 *            The type of content to submit
	 * @param content
	 *            The content to be submitted
	 * @param callback
	 *            The callback to execute when submission status is available.
	 */
	public void createPost(String type, String content,
			final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForCreatePost(user_id, event_id);
		String requestBody = Utils.serializeCreatePost(content, type);
		new PostTask(restUrl, requestBody, "Create Post",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						callback.setActivity(current);
						callback.onPostSuccess();
					}
				}).execute();
	}

	public void createComment(String content, String parent,
			final CreateCommentCallback callback) {
		String restUrl = Utils.constructRestUrlForCreatePost(user_id, event_id);
		String requestBody = Utils.serializeCreateComment(content, parent);
		new PostTask(restUrl, requestBody, "Create Comment",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						callback.setActivity(current);
						callback.onPostSuccess();
					}
				}).execute();
	}

	public void upPlusLikeVotePost(String pid, final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForUpPlusLikeVotePost(user_id);
		String requestBody = Utils.serializeUpPlusLikeVotePost(pid);
		new PostTask(restUrl, requestBody, "UpPlusLikeVote Post",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						callback.setActivity(current);
						callback.onDataReceived(response);
					}
				}).execute();
	}

	public void getNearbyEvents(double lat, double lon,
			final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForNearbyEvents(user_id);
		String requestBody = Utils.serializeEventRequest(lat, lon);
		new PostTask(restUrl, requestBody, "Get Nearby Events",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						ArrayList<EventData> list = Utils
								.parseResponseAsEventList(response);
						callback.setActivity(current);
						callback.onEventListReceived(list);
					}
				}).execute();
	}

	public void createEvent(double lat, double lon, String name,
			String description, final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForCreateEvent(user_id);
		String requestBody = Utils.serializeCreateEventRequest(lat, lon, name,
				description, "2013-01-13T22:05:57.000Z",
				"2013-02-13T22:05:57.000Z");
		new PostTask(restUrl, requestBody, "Create Event",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						callback.setActivity(current);
						callback.onDataReceived(response);
					}
				}).execute();
	}

	public void getEventInformation(final GetCallback callback) {
		String restUrl = Utils.constructRestUrlForGetEvent(user_id, event_id);
		new GetTask(restUrl, "Get Event Info", new RestTaskCallback() {
			public void onTaskComplete(String response) {
				callback.setActivity(current);
				EventData event = Utils.parseResponseAsEvent(response);
				callback.onDataReceived(event);
			}
		}).execute();
	}

	public void attendEvent(final PostCallback callback) {
		String restUrl = Utils
				.constructRestUrlForAttendEvent(user_id, event_id);
		String requestBody = Utils.serializeAttendEventRequest(event_id);
		new PostTask(restUrl, requestBody, "Attend Event",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						callback.setActivity(current);
						callback.onDataReceived(response);
					}
				}).execute();
	}

	public void createFacebookUserProfile(String token,
			final RegisterCallback callback) {
		String restUrl = Utils.constructRestUrlForCreateFBUser();
		String requestBody = Utils.serializeCreateFBUser(token);
		new PostTask(restUrl, requestBody, "Create User",
				new RestTaskCallback() {
					@Override
					public void onTaskComplete(String response) {
						UserData profile = Utils
								.parseResponseAsProfile(response);
						callback.setActivity(current);
						callback.onDataReceived(profile);
					}
				}).execute();
	}

	public void getNearbyPlaces(double lat, double lon,
			final PostCallback callback) {
		String restUrl = Utils.constructRestUrlForNearbyPlaces(user_id);
		String requestBody = Utils.serializeGetNearbyPlacesRequest(lat, lon);
		Log.v("Reqest:", requestBody);
		new PostTask(restUrl, requestBody, "Get Nearby Places",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						ArrayList<PlaceData> list = Utils
								.parseResponseAsPlaceList(response);
						callback.setActivity(current);
						callback.onPlaceListReceived(list);
					}
				}).execute();
	}
	
	public void getSchedule(final GetCallback callback) {
		String restUrl = Utils.constructRestUrlForGetSchedule(user_id, event_id);
		String response = "[{  \"title\": \"Prep Pitches\",  \"description\": \"Just don't panic\",  \"start\": \"2013-02-03T18:00:00Z\",  \"end\": \"2013-02-03T19:00:00Z\"},{  \"title\": \"Pitch to the Judges\",  \"description\": \"Pitches be cray!\",  \"start\": \"2013-02-03T19:00:00Z\",  \"end\": \"2013-02-03T20:30:00Z\"}]";
		ArrayList<ScheduleItem> list = Utils
				.parseResponseAsSchedule(response);
		callback.setActivity(current);
		callback.onScheduleReceived(list);
		/*new GetTask(restUrl, "Get Schedule",
				new RestTaskCallback() {
					public void onTaskComplete(String response) {
						ArrayList<ScheduleItem> list = Utils
								.parseResponseAsSchedule(response);
						callback.setActivity(current);
						callback.onScheduleReceived(list);
					}
				}).execute();*/
	}
}