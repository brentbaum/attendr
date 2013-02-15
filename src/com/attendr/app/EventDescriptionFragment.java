package com.attendr.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.attendr.net.Callbacks;
import com.attendr.net.Callbacks.GetEventCallback;
import com.attendr.net.ServerAPI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class EventDescriptionFragment extends SherlockFragment {
	View view;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	String user_id, event_id;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflator = getLayoutInflater(savedInstanceState);
		this.view = inflator.inflate(R.layout.event_description_fragment, container, false);
		this.user_id = ((EventActivity)getSherlockActivity()).user_id;
		this.event_id = ((EventActivity)getSherlockActivity()).event_id;
		this.options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.cacheInMemory()
			.cacheOnDisc()
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getSherlockActivity()).build();
		ImageLoader.getInstance().init(config);
		this.imageLoader = ImageLoader.getInstance();
		
		ServerAPI server = ServerAPI.getInstance(user_id, getSherlockActivity());
		server.setEventID(event_id);
		server.getEventInformation(new GetEventCallback());
		
		return view;
	}
	public void displayEvent(EventData event) {
		TextView description, eventName, numberOfAttendees, eventID;
		final ImageView eventPicture;
		this.imageLoader = ImageLoader.getInstance();
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.event_description_fragment, null);
		}

		if (event != null) {
			eventName = (TextView) view.findViewById(R.id.event_name);
			description = (TextView) view.findViewById(R.id.event_description);
			numberOfAttendees = (TextView)view.findViewById(R.id.number_of_attendees);
			eventID = (TextView)view.findViewById(R.id.event_id);
			eventPicture = (ImageView)view.findViewById(R.id.event_picture);

			if (eventName != null) {
				eventName.setText(event.getName());
			}

			if (description != null) {
				String des = "\tThousands of computer geeks have come from all around the world to participate in the one hackathon to rule them all: Dev Week 2013 Hackathon."
						+" \n\nWith copious amounts of beer on hand, our adventurous programmer will embark on a journey that could truly only be likened in terms of epic "
						+"proportions to the Olympic trials. \n\nThis is it folks, this is it.";
				description.setText(des);
				//description.setText(event.getDescription());
			}

			if (numberOfAttendees != null) {
				numberOfAttendees.setText(event.attendees.length+" people at this event");
			}
			
			if (eventID != null) {
				eventID.setText(event._id);
			}
			
			if (eventPicture != null) {
				//eventPicture.setBackgroundColor(Color.LTGRAY);
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
                    Log.v("ImageLoader","Loading complete!");
                }
				});
				Log.v("ImageLoader","Image downloading!");
			}
		}
	}
}
