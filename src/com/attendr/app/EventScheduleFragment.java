package com.attendr.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
import com.attendr.net.Utils;

public class EventScheduleFragment extends SherlockFragment {
	ListView listView, hourListView = null;
	String user_id, event_id;
	ScheduleItemAdapter sAdapter;
	ArrayAdapter<String> tAdapter;
	ServerAPI server;
	View view;
	public static ArrayList<ScheduleItem> itemList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user_id = Utils.getCurrentUserID(getSherlockActivity().getSharedPreferences(Utils.PREFS_NAME,0));
		event_id = ((EventActivity)getSherlockActivity()).getEventID();
		
		server = ServerAPI.getInstance(user_id, this.getSherlockActivity());
		server.setEventID(event_id);
				
		//refreshScheduleList();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
		view = inflator.inflate(R.layout.event_schedule_fragment, container, false);
		hourListView = (ListView)view.findViewById(R.id.hour_line_list);
		listView = (ListView)view.findViewById(android.R.id.list);
		
		ArrayList<String> t = new ArrayList<String>();
		for(int x = 6; x < 23; x++) {
			t.add(x%12+1 + ":00");
		}
			tAdapter = new ScheduleTimeAdapter(this.getSherlockActivity(), android.R.layout.simple_list_item_1, t);
			hourListView.setAdapter(tAdapter);
			tAdapter.notifyDataSetChanged();
			
		return view;
	}
	
	public void refreshScheduleList() {
    	//server.getSchedule(new Callbacks.GetScheduleCallback());
	}
	
	public void displaySchedule(ArrayList<ScheduleItem> schedule) {
		ArrayList<ArrayList<ScheduleItem>> list = buildSchedule(schedule);
		for(ArrayList<ScheduleItem> l : list)
			for(ScheduleItem item : l)
				break;
	}
	
	public ArrayList<ArrayList<ScheduleItem>> buildSchedule(ArrayList<ScheduleItem> schedule) {
		ArrayList<ArrayList<ScheduleItem>> levels = new ArrayList<ArrayList<ScheduleItem>>();
		int index = 0;
		while(schedule.size()>0 && index < 3) {
			for(ScheduleItem s1 : schedule) {
				levels.get(index).add(s1);
				for(ScheduleItem s2 : levels.get(index))
					if(s1.overlaps(s2)) {
						levels.get(index).remove(s1);
						break;
					}
			}
			index++;
		}
		return levels;
	}
}
