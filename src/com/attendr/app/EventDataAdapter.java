package com.attendr.app;

import java.util.ArrayList;

import com.attendr.app.R;
import com.attendr.net.Callbacks;
import com.attendr.net.ServerAPI;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDataAdapter extends ArrayAdapter<EventData> {
	private ArrayList<EventData> events;
	Context context;
	int EVENT_VIEW_ACTIVITY = 4;
	String user_id;
	ImageLoader imageLoader;
	DisplayImageOptions options;


	public EventDataAdapter(Context context, String uid, int textViewResourceId,
			ArrayList<EventData> events) {
		super(context, textViewResourceId, events);

		this.context = context;
		this.events = events;
		this.user_id = uid;
		this.options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.cacheInMemory()
			.cacheOnDisc()
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context).build();
		ImageLoader.getInstance().init(config);
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView description, eventName, numberOfAttendees, eventID;
		final ImageView eventPicture;
		View v = convertView;
		this.imageLoader = ImageLoader.getInstance();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.event_list_item, null);
		}

		EventData event = events.get(events.size()-position-1);
		if (event != null) {
			eventName = (TextView) v.findViewById(R.id.event_name);
			description = (TextView) v.findViewById(R.id.event_description);
			numberOfAttendees = (TextView)v.findViewById(R.id.number_of_attendees);
			eventID = (TextView)v.findViewById(R.id.event_id);
			eventPicture = (ImageView)v.findViewById(R.id.event_picture);

			if (eventName != null) {
				eventName.setText(event.getName());
			}

			if (description != null) {
				description.setText(event.getDescription());
			}

			if (numberOfAttendees != null) {
				numberOfAttendees.setText(event.attendees.length+" people at this event");
			}
			
			if (eventID != null) {
				eventID.setText(event._id);
			}
			
			if (eventPicture != null) {
				
				imageLoader.displayImage("http://startupweekend.org/wp-content/blogs.dir/1/files/2012/10/swnext-logo-300x256.png", eventPicture, this.options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingFailed(FailReason failReason) {
                        // TODO Auto-generated method stub
                    Log.v("Loading", failReason.toString());
                    super.onLoadingFailed(failReason);
                    }

                    @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    eventPicture.setImageBitmap(loadedImage);
                }
				});
			}
			v.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String event_id = (String)((TextView) v.findViewById(R.id.event_id)).getText();
					String event_name = (String)((TextView)v.findViewById(R.id.event_name)).getText();
					
					EventListActivity parent = (EventListActivity)v.getContext();
					
					parent.locationManager.removeUpdates(parent.listener);
					
					ServerAPI server = ServerAPI.getInstance(user_id, parent);
					server.setEventID(event_id);
					server.attendEvent(new Callbacks.CreateEventCallback());
					Intent intent = new Intent(parent.getApplicationContext(), EventActivity.class);
					intent.putExtra("event_id", event_id);
					intent.putExtra("event_name", event_name);
					parent.startActivity(intent);
				}
			});
			
			v.setVisibility(View.VISIBLE);
		}
		return v;
	}
}
