package com.attendr.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.attendr.app.EventComposeDialogFragment.ComposeDialogListener;
import com.attendr.net.Callbacks;
import com.attendr.net.Callbacks.GetCommentsCallback;
import com.attendr.net.ServerAPI;
import com.attendr.net.Utils;

public class EventPostViewActivity extends SherlockFragmentActivity implements ComposeDialogListener{
	public String user_id, event_id, post_id;
	public PostData post;
	public ListView commentListView;
	public PostDataAdapter pAdapter;
	public ArrayList<PostData> commentList;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post_view_activity);
		commentListView = (ListView)findViewById(R.id.comment_list);

		post_id = getIntent().getExtras().getString("pid");
		event_id = getIntent().getExtras().getString("eid");
		user_id = Utils.getCurrentUserID(getSharedPreferences(Utils.PREFS_NAME,0));

		loadPostData(post_id);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent upIntent = new Intent(this, EventActivity.class);
	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	                // This activity is not part of the application's task, so create a new task
	                // with a synthesized back stack.
	                TaskStackBuilder.create(this)
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {
	                // This activity is part of the application's task, so simply
	                // navigate up to the hierarchical parent activity.
	                onBackPressed();
	            }
	            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent result = new Intent(getApplicationContext(), EventActivity.class);
    	setResult(Activity.RESULT_OK, result);
    	getWindow().setSoftInputMode(
    		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		result.putExtra("event_id", event_id);
		startActivity(result);
        finish();
	}
	
	public void loadPostData(String pid) {
		ServerAPI server = ServerAPI.getInstance(user_id, this);
		server.setEventID(event_id);
		server.getPost(pid, new Callbacks.GetPostCallback());
	}
	
	public void loadComments(String[] array) {
		if(array.length>0) {
			ServerAPI server = ServerAPI.getInstance(Utils.getCurrentUserID(getSharedPreferences(Utils.PREFS_NAME,0)), this);
			server.setEventID(event_id);
			server.getCommentList(array, new GetCommentsCallback());
		}
	}

	public void updateCommentListView(ArrayList<PostData> list) {
		commentList = list;
		pAdapter = new PostDataAdapter(this, user_id, event_id, android.R.layout.simple_list_item_1, list);
		commentListView.setAdapter(pAdapter);
		pAdapter.notifyDataSetChanged();
	}

	//TODO limit each user to only one like.
	public void onLikeClick(View v) {
		ServerAPI server = ServerAPI.getInstance(Utils.getCurrentUserID(getSharedPreferences(Utils.PREFS_NAME,0)), this);
		server.setEventID(event_id);
		server.upPlusLikeVotePost(post_id, new Callbacks.UpPlusLikeVoteCallback());
	}

	//TODO Replies
	public void onReplyClick(View v) {
		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
	    EventComposeDialogFragment newFragment = new EventComposeDialogFragment();
	    newFragment.setResponseEnabled(true);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	    newFragment.show(fm, "dialog");
	}
	
	@Override
    public void onFinishEditDialog(String inputText, String type) {
    	if(type=="text") {
    		ServerAPI server = ServerAPI.getInstance(user_id, this.getParent());
    		server.setEventID(event_id);
        	server.createComment(inputText, post.getID(), new Callbacks.CreateCommentCallback());
    	}
    	getWindow().setSoftInputMode(
    		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

	public void addCommentToList(PostData p) {
		commentList.add(p);
		updateCommentListView(commentList);
		loadComments(post.getComments());
	}
}
