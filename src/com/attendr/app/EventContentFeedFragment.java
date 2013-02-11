package com.attendr.app;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockListFragment;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
import com.attendr.net.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventContentFeedFragment extends SherlockListFragment {
	ListView listView = null;
	String user_id, event_id;
	PostDataAdapter pAdapter;
	ServerAPI server;
	View view;
	public static ArrayList<PostData> postList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user_id = Utils.getCurrentUserID(getSherlockActivity().getSharedPreferences(Utils.PREFS_NAME,0));
		event_id = ((EventActivity)getSherlockActivity()).getEventID();
		
		server = ServerAPI.getInstance(user_id, this.getSherlockActivity());
		server.setEventID(event_id);
		
		refreshPostList();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
		view = inflator.inflate(R.layout.content_feed_activity, container, false);
		listView = (ListView)view.findViewById(android.R.id.list);
		return view;
	}
	
	public void refreshPostList() {
    	server.getPostList(new Callbacks.PostListCallback());
	}

	public void refreshListView(ArrayList<PostData> list) {
		pAdapter = new PostDataAdapter(this.getSherlockActivity(), user_id, event_id, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(pAdapter);
		pAdapter.notifyDataSetChanged();
	}
	public void addPostToList(PostData post) {
		refreshPostList();
	}
}
