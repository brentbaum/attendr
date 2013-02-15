package com.attendr.app;

import java.util.ArrayList;

import com.attendr.app.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDataAdapter extends ArrayAdapter<PostData> {
	private ArrayList<PostData> posts;
	Context context;
	int POST_VIEW_ACTIVITY = 3;
	String user_id, event_id;
	ImageView profilePicture;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public PostDataAdapter(Context context, String uid, String eid,
			int textViewResourceId, ArrayList<PostData> posts) {
		super(context, textViewResourceId, posts);

		this.context = context;
		this.posts = posts;
		this.user_id = uid;
		this.event_id = eid;

		this.options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_for_empty_url)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(3)).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.context).build();
		ImageLoader.getInstance().init(config);
		this.imageLoader = ImageLoader.getInstance();
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView content, username, numberOfComments, pid, numberOfLikes;
		View colorBar;
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.new_feed_item, null);
		}

		PostData post = posts.get(posts.size() - position - 1);
		if (post != null && post.getUser()!=null) {
			username = (TextView) v.findViewById(R.id.username);
			content = (TextView) v.findViewById(R.id.content);
			numberOfComments = (TextView) v
					.findViewById(R.id.number_of_comments);
			colorBar = (View) v.findViewById(R.id.color_bar);
			pid = (TextView) v.findViewById(R.id.post_id);
			profilePicture = (ImageView) v
					.findViewById(R.id.selection_profile_pic);
			numberOfLikes = (TextView)v.findViewById(R.id.number_of_likes);
			
			post.getUser().getName();
			
			if (username != null) {
				username.setText(post.getUser().getName());
			}

			if (content != null) {
				content.setText(post.getContent());
			}

			if (numberOfComments != null) {
				numberOfComments.setText(post.getComments().length
						+ " comments");
			}

			if (colorBar != null) {
				colorBar.setBackgroundResource(post.getColorBar());
			}

			if (pid != null) {
				pid.setText(post.getID());
			}
			
			numberOfLikes.setText("" + post.getScore());

			profilePicture.setBackgroundColor(Color.LTGRAY);
			if (post.getUser().getImageURL() != null)
				imageLoader.displayImage(post.getUser().getImageURL(),
						profilePicture, this.options,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingFailed(FailReason failReason) {
								super.onLoadingFailed(failReason);
							}

							@Override
							public void onLoadingComplete(Bitmap loadedImage) {
								profilePicture.setImageBitmap(loadedImage);
							}
						});

			v.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String pid = (String) ((TextView) v
							.findViewById(R.id.post_id)).getText();
					EventActivity parent = (EventActivity) v.getContext();
					Intent intent = new Intent(parent.getApplicationContext(),
							EventPostViewActivity.class);
					intent.putExtra("pid", pid);
					intent.putExtra("eid", event_id);
					intent.putExtra("event_name", parent.event_name);
					parent.startActivity(intent);
				}
			});
			v.setVisibility(View.VISIBLE);
		}
		return v;
	}
}
