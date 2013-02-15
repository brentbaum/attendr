package com.attendr.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
import com.attendr.net.Utils;
import com.attendr.net.Utils.TabInfo;

public class EventListActivity extends SherlockFragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
	public ArrayList<EventData> eventList;
	private ListView listView;
	private String user_id;
	EventDataAdapter pAdapter = null;
	LocationManager locationManager;
	Geocoder geocoder;
	String TAG = "Location:";
	private String event_id;
	private String event_name;
	private double lat, lon = -1000;
	public static ProgressDialog finding;
	public static LocationListener listener;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	public HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private PagerAdapter mPagerAdapter;
	public static EventListFragment eventListFragment;
	public static PlaceListFragment placeListFragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		user_id = Utils.getCurrentUserID(getSharedPreferences(Utils.PREFS_NAME,0));

		setContentView(R.layout.event_list_activity);
		
		ActionBar actionbar = getSupportActionBar();
		actionbar.show();
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		
		LayoutInflater inflater = LayoutInflater.from(this);
	    View customView = inflater.inflate(R.layout.actionbar_title, null);
		actionbar.setCustomView(customView);
	    actionbar.setDisplayShowCustomEnabled(true);
		
		((TextView)findViewById(R.id.actionbar_title_custom)).setText("Attendr");
		
		listView = (ListView) findViewById(R.id.event_listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> par, View v, int position, long id) {
				event_id = (String)((TextView) v.findViewById(R.id.event_id)).getText();
				event_name = (String)((TextView)v.findViewById(R.id.event_name)).getText();
				
				Activity parent = (Activity)v.getContext();
				
				locationManager.removeUpdates(listener);
				
				ServerAPI server = ServerAPI.getInstance(user_id, parent);
				server.setEventID(event_id);
				server.attendEvent(new Callbacks.CreateEventCallback());
				Intent intent = new Intent(getApplicationContext(), EventActivity.class);
				intent.putExtra("event_id", event_id);
				intent.putExtra("event_name", event_name);
				startActivity(intent);
			}
		});
		
		finding = ProgressDialog.show(this, "", "Finding your location",true);
		finding.show();
		
		listener = new EventListLocationListener();
	    locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
	    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    if(location != null) {
	    	Log.d("Location:", location.toString());
	    	listener.onLocationChanged(location);
	    }
	    else
	    	Log.e("Location:","Location unable to be determined");
	    
	    setContentView(R.layout.tabs_viewpager_layout);
		// Initialize the TabHost
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
		// Initialize ViewPager
		this.initializeViewPager();
	    //ServerAPI server = ServerAPI.getInstance(this.getSharedPreferences(Utils.PREFS_NAME, 0).getString("user_id", "no_id"), this);
		//server.createEvent(37.505661, -122.248394,  "Developer Week Hackathon!", "We're hacking at Developer week.", new Callbacks.CreateEventCallback());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		pAdapter.imageLoader.stop();
	}
	private void initializeViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, EventListFragment.class.getName()));
		//postList = cf.postList;
		fragments.add(Fragment.instantiate(this, PlaceListFragment.class.getName()));
		this.mPagerAdapter  = new PagerAdapter(getSupportFragmentManager(), fragments);
		eventListFragment = (EventListFragment)mPagerAdapter.getItem(0);
		eventListFragment.setRetainInstance(true);
		placeListFragment = (PlaceListFragment)mPagerAdapter.getItem(1);
		placeListFragment.setRetainInstance(true);

		this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
    }

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        Utils.TabInfo tabInfo = null;
        EventListActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Events").setIndicator("Events"), ( tabInfo = new TabInfo("Events", EventListFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        EventListActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Places").setIndicator("Places"), ( tabInfo = new TabInfo("Places", EventListFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Content Feed");
        //
        mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Add Tab content to the Tabhost
	 **/
	public static void AddTab(EventListActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new Utils.TabFactory(activity));
        tabHost.addTab(tabSpec);
	}

	public void onTabChanged(String tag) {
		//TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
    }
	public class EventListLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) { //<9>
			finding.hide();
		    String text = String.format("Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location.getLatitude(), 
		                  location.getLongitude(), location.getAltitude(), location.getBearing());
		    Log.v("Location:", text);
		    lat = (double)(location.getLatitude());
		    lon = (double)(location.getLongitude());
		    if(eventListFragment!=null)
		    	eventListFragment.refreshEventList(lat, lon);
		    if(placeListFragment!=null)
				placeListFragment.refreshPlaceList(lat, lon);
		} 

		@Override
		public void onProviderDisabled(String arg0) {
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getSupportMenuInflater().inflate(R.menu.event_list_menu, menu);
		return true;
	}
	public void createEvent() {
		if(lat != -1000) {
			ServerAPI server = ServerAPI.getInstance(this.getSharedPreferences(Utils.PREFS_NAME, 0).getString("user_id", "no_id"), this);
			server.createEvent(lat, lon,  "Startup Weekend NEXT", "Learning to make a company with the help of some awesome mentors.", new Callbacks.CreateEventCallback());
			Log.v("Refreshing","Refreshing");
			placeListFragment.refreshPlaceList(lat, lon);
			eventListFragment.refreshEventList(lat, lon);
		}
	}
	
	public void onRefreshClick(MenuItem item) {
	    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    if(location != null) {
	    	listener.onLocationChanged(location);
	    }
	}
	
	public void onCreateClick(MenuItem item) {
		createEvent();
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		this.mTabHost.setCurrentTab(position);
	}
	
	public void refreshEventListFragment(ArrayList<EventData> events) {
		eventListFragment.refreshListView(events);
	}
	
	public void noEventsFound() {
	}
}