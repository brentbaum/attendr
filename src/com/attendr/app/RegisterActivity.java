package com.attendr.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;


//TODO: modify Register Activity to start MainActivity properly in all cases
public class RegisterActivity extends SherlockFragmentActivity {
	public String uid;
	public static ProgressDialog loading;
	private static final int SPLASH = 0;
	private static final int EMAIL = 1;
	private static final int LOGGEDIN = 2;
	private static final int FRAGMENT_COUNT = LOGGEDIN + 1;
	private boolean finished = false;
	private SherlockFragment[] fragments = new SherlockFragment[FRAGMENT_COUNT];
	private FragmentManager fm;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.register_main);
		
		fm = getSupportFragmentManager();
		fragments[SPLASH] = (SherlockFragment) fm.findFragmentById(R.id.registerSplashFragment);
		fragments[LOGGEDIN] = (SherlockFragment) fm.findFragmentById(R.id.userLoggedInFragment);
		fragments[EMAIL] = (SherlockFragment) fm.findFragmentById(R.id.registerEmailFragment);
		
		FragmentTransaction transaction = fm.beginTransaction();
		for(int x = 0; x < fragments.length; x++)
			transaction.hide(fragments[x]);
		transaction.commit();
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
	    fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    for (int i = 0; i < fragments.length; i++) {
	        if (i == fragmentIndex) {
	            transaction.show(fragments[i]);
	        } else {
	            transaction.hide(fragments[i]);
	        }
	    }
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    isResumed = false;
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			for(int x = 0; x < backStackSize; x++)
				manager.popBackStack();
			if(state.isOpened())
				showFragment(LOGGEDIN, false);
			else if(state.isClosed())
				showFragment(SPLASH, false);
		}
	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();
		
		if(session != null && session.isOpened())
			showFragment(LOGGEDIN, false);
		else
			showFragment(SPLASH, false);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  uiHelper.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
    
	public void onFacebookClick(View v) {
		 Session.openActiveSession(this, true, new Session.StatusCallback() {
			    // callback when session changes state
			    @Override
			    public void call(final Session session, SessionState state, Exception exception) {
			    	if (session.isOpened()) {
			    		// make request to the /me API
			    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

			    		  // callback after Graph API response with user object
			    		  @Override
			    		  public void onCompleted(GraphUser user, Response response) {
			    			  if(user!=null) {
			    				  createFBUser(session.getAccessToken());
			    			  }
			    		  }
			    		});
			    	}
			    }
		});
	}
	
	public void onTwitterClick(View v) {
		//Access Twitter API, authorize, pass twitter token to server
		createUser("Twitter","User", "none");
	}
	
	public void onRegisterClick(View layout) {
		showFragment(EMAIL, true);
	}
	
	@Override
	public void onBackPressed() {
		showFragment(0, false);
	}
	
	public void onFinishRegisterClick(View layout) {
		final EditText mEditText = ((EditText)findViewById(R.id.register_input_name));
		String name = mEditText.getText().toString();
		if(name.length()==0)
			mEditText.setHint("Must be a valid name");
		String email = ((EditText)findViewById(R.id.register_input_email)).getText().toString();
		String fname = name.substring(0,name.indexOf(' '));
		String lname = name.substring(name.indexOf(' '), name.length());
		createUser(fname, lname, email);
	}
	
	public void createUser(String fName, String lName, String email) {
		ServerAPI server = ServerAPI.getInstance(uid, this);
		server.createUserProfile(fName, lName, email, new Callbacks.RegisterCallback());
	}
	
	public void createFBUser(String id) {
		ServerAPI server = ServerAPI.getInstance(uid, this);
		server.createFacebookUserProfile(id, new Callbacks.RegisterCallback());
	}
	
	public void onFinishClick(View v) {
		if(finished)
			startEventListActivity();
		else {
			loading = ProgressDialog.show(this, "", "Loading, please wait....",true);
			loading.show();
		}
	}
	
	public void showSuccessFragment() {
		if(loading!=null)
			loading.dismiss();
		showFragment(LOGGEDIN, false);
	}
	
	public void startEventListActivity() {
       	Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
		startActivity(intent);
    	finish(); 
	}
	
	public void isFinished(boolean bool) {
		finished = bool;
	}
}
