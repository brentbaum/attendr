package com.attendr.app;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.attendr.app.EventComposeDialogFragment.ComposeDialogListener;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
import com.attendr.net.Utils;
import com.attendr.net.Utils.TabInfo;

/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager.
 * @author mwho
 */
public class EventActivity extends SherlockFragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, ComposeDialogListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, Utils.TabInfo> mapTabInfo = new HashMap<String, Utils.TabInfo>();
	private PagerAdapter mPagerAdapter;
	public static EventContentFeedFragment contentFragment;
	public static EventDescriptionFragment descriptionFragment;
	public static EventScheduleFragment scheduleFragment;
	public String user_id, event_id, event_name;
	public UserData currentUser;
	EventComposeDialogFragment editNameDialog; 
	public int currentPosition = 0;
	
	/** (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		user_id = Utils.getCurrentUserID(getSharedPreferences(Utils.PREFS_NAME,0));
		event_id = getIntent().getStringExtra("event_id");
		Log.v("Event ID:", event_id);
		event_name = getIntent().getStringExtra("event_name");
		getSupportActionBar().show();
		getSupportActionBar().setTitle(event_name);
		
		// Inflate the layout
		setContentView(R.layout.tabs_viewpager_layout);
		// Initialize the TabHost
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
            if(mTabHost.getCurrentTab()==0)
            	contentFragment.refreshPostList();
        }
		// Initialize ViewPager
		this.initializeViewPager();
	}
	
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}
	
	public EventContentFeedFragment getContentFragment() {
		return contentFragment;
	}
	
	public EventDescriptionFragment getDescriptionFragment() {
		return descriptionFragment;
	}
	
	public EventScheduleFragment getScheduleFragment() {
		return scheduleFragment;
	}
	
	public void addPostToList(PostData post) {
		contentFragment.addPostToList(post);
	}
	
	public String getEventID() {
		return event_id;
	}

    /**
     * Initialize ViewPager
     */
    private void initializeViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		
		fragments.add(Fragment.instantiate(this, EventContentFeedFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, EventDescriptionFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, EventScheduleFragment.class.getName()));
		
		this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		
		contentFragment = (EventContentFeedFragment)mPagerAdapter.getItem(0);
		contentFragment.setRetainInstance(true);
		descriptionFragment = (EventDescriptionFragment)mPagerAdapter.getItem(1);
		descriptionFragment.setRetainInstance(true);
		scheduleFragment = (EventScheduleFragment)mPagerAdapter.getItem(2);
		scheduleFragment.setRetainInstance(true);

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
        EventActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Content Feed").setIndicator("Content Feed"), ( tabInfo = new TabInfo("Content Feed", EventContentFeedFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        EventActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Description").setIndicator("Description"), ( tabInfo = new TabInfo("Description", EventDescriptionFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        EventActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Schedule").setIndicator("Schedule"), ( tabInfo = new TabInfo("Schedule", EventScheduleFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Content Feed");
        //
        mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Add Tab content to the Tabhost
	 **/
	public static void AddTab(EventActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new Utils.TabFactory(activity));
        tabHost.addTab(tabSpec);
	}

	public void onTabChanged(String tag) {
		//TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
    }

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		currentPosition = position;
	}

	@Override
	public void onPageSelected(int position) {
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
	
	public boolean composeDialog(MenuItem item) {
		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
	    DialogFragment newFragment = new EventComposeDialogFragment();
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	    newFragment.show(fm, "dialog");
	    return true;
	}
	
	 @Override
	    public void onFinishEditDialog(String inputText, String type) {
	    	if(type=="text") {
	    		ServerAPI server = ServerAPI.getInstance(user_id, this.getParent());
	    		server.setEventID(event_id);
	        	server.createPost(type, inputText, new Callbacks.CreatePostCallback());
	        	contentFragment.refreshPostList();
	    	}
	    	getWindow().setSoftInputMode(
	    		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    }
	
	@Override
	public void onBackPressed() {
		ConfirmLeaveDialogFragment dialog = new ConfirmLeaveDialogFragment();
		dialog.show(getSupportFragmentManager(), "Show/Confirm Dialog");
	}
	
	public void onLeaveEventClick(MenuItem item) {
		ConfirmLeaveDialogFragment dialog = new ConfirmLeaveDialogFragment();
		dialog.show(getSupportFragmentManager(), "Show/Confirm Dialog");
	}
	
	public void onRefreshClick(MenuItem item) {
		//if(mTabHost.getCurrentTab()==0)
		contentFragment.refreshPostList();
		scheduleFragment.refreshScheduleList();
	}
	
	public static class ConfirmLeaveDialogFragment extends SherlockDialogFragment {
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
			builder.setMessage("Really leave event?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getSherlockActivity().getApplicationContext(), EventListActivity.class);
						getSherlockActivity().startActivity(intent);
					}})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
			});
			return builder.create();
		}
	}
}

