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

public class PlaceDataAdapter extends ArrayAdapter<PlaceData> {
	private ArrayList<PlaceData> places;
	Context context;
	int EVENT_VIEW_ACTIVITY = 4;
	String user_id;
	ImageLoader imageLoader;
	DisplayImageOptions options;


	public PlaceDataAdapter(Context context, String uid, int textViewResourceId,
			ArrayList<PlaceData> places) {
		super(context, textViewResourceId, places);

		this.context = context;
		this.places = places;
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
		TextView description, placeName, numberOfAttendees, placeID;
		final ImageView placePicture;
		View v = convertView;
		this.imageLoader = ImageLoader.getInstance();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.place_list_item, null);
		}

		PlaceData place = places.get(places.size()-position-1);
		if (place != null) {
			placeName = (TextView) v.findViewById(R.id.place_name);
			description = (TextView) v.findViewById(R.id.place_description);
			placeID = (TextView)v.findViewById(R.id.place_id);
			placePicture = (ImageView)v.findViewById(R.id.place_picture);

			if (placeName != null) {
				placeName.setText(place.getName());
			}

			if (description != null) {
				description.setText(place.getAddress());
			}
			
			if (placeID != null) {
				placeID.setText(place.id);
			}
			
			if (placePicture != null) {
				//placePicture.setBackgroundColor(Color.LTGRAY);
				imageLoader.displayImage(place.icon, placePicture, this.options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingFailed(FailReason failReason) {
                        // TODO Auto-generated method stub
                    Log.v("Loading", failReason.toString());
                    super.onLoadingFailed(failReason);
                    }

                    @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    placePicture.setImageBitmap(loadedImage);
                    Log.v("ImageLoader","Loading complete!");
                }
				});
				Log.v("ImageLoader","Image downloading!");
			}
			/*v.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String place_id = (String)((TextView) v.findViewById(R.id.place_id)).getText();
					String place_name = (String)((TextView)v.findViewById(R.id.place_name)).getText();
					
					PlaceListActivity parent = (PlaceListActivity)v.getContext();
					
					parent.locationManager.removeUpdates(parent.listener);
					
					ServerAPI server = ServerAPI.getInstance(user_id, parent);
					server.setPlaceID(place_id);
					server.attendPlace(new Callbacks.CreatePlaceCallback());
					Intent intent = new Intent(parent.getApplicationContext(), PlaceActivity.class);
					intent.putExtra("place_id", place_id);
					intent.putExtra("place_name", place_name);
					parent.startActivity(intent);
				}
			});*/
			
			v.setVisibility(View.VISIBLE);
		}
		return v;
	}
}
