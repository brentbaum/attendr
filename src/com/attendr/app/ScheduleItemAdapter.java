package com.attendr.app;

import java.util.ArrayList;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ScheduleItemAdapter extends ArrayAdapter<ScheduleItem> {
	private ArrayList<ScheduleItem> items;
	Context context;
	int POST_VIEW_ACTIVITY = 3;
	String user_id, event_id;
	ImageView profilePicture;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public ScheduleItemAdapter(Context context, String uid, String eid,
			int textViewResourceId, ArrayList<ScheduleItem> items) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;
		this.user_id = uid;
		this.event_id = eid;

		this.options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_for_empty_url)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.context).build();
		ImageLoader.getInstance().init(config);
		this.imageLoader = ImageLoader.getInstance();
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title, description;
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.schedule_feed_item, null);
		}
		
		ScheduleItem item = items.get(items.size() - position - 1);
		if(position>1) {
			ScheduleItem last = items.get(items.size() - position - 2);
		}
		
		
		if (item != null) { 
			title = (TextView) v.findViewById(R.id.event_title_label);

			if (title != null) {
				int length = item.getLengthInMinutes()-26;
				int height = (int) TypedValue.applyDimension(
					    TypedValue.COMPLEX_UNIT_DIP, length, parent.getResources()
					        .getDisplayMetrics());
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)title.getLayoutParams();
				params.height=height;
				title.setLayoutParams(params);
				item.getTitle();
				title.setText(item.getTitle());
			}

			//if (description != null) {
			//	description.setText(item.getDescription());
			//}

			/*v.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String pid = (String) ((TextView) v
							.findViewById(R.id.post_id)).getText();
					Activity parent = (Activity) v.getContext();
					Intent intent = new Intent(parent.getApplicationContext(),
							EventPostViewActivity.class);
					intent.putExtra("pid", pid);
					intent.putExtra("eid", event_id);
					parent.startActivity(intent);
				}
			});*/
			v.setVisibility(View.VISIBLE);
		}
		return v;
	}
}
