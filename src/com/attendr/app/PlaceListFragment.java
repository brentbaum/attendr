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

public class PlaceListFragment extends SherlockListFragment {
	ListView listView = null;
	String user_id, event_id;
	PlaceDataAdapter pAdapter;
	ServerAPI server;
	View view;
	public static ArrayList<PlaceData> eventList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user_id = Utils.getCurrentUserID(getSherlockActivity().getSharedPreferences(Utils.PREFS_NAME,0));
		server = ServerAPI.getInstance(user_id, this.getSherlockActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
		view = inflator.inflate(R.layout.place_list_fragment, container, false);
		listView = (ListView)view.findViewById(android.R.id.list);
		return view;
	}

	public void refreshPlaceList(double lat, double lon) {
		server = ServerAPI.getInstance(user_id, this.getSherlockActivity());
		server.getNearbyPlaces(lat, lon, new Callbacks.PlaceListCallback());
	}

	public void refreshListView(ArrayList<PlaceData> events) {
		eventList = events;
		pAdapter = new PlaceDataAdapter(this.getSherlockActivity(), user_id, android.R.layout.simple_list_item_1, events);
		listView.setAdapter(pAdapter);
		pAdapter.notifyDataSetChanged();
	}
}
