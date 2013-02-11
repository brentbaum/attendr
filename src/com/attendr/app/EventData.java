package com.attendr.app;

import com.attendr.net.Utils;

public class EventData {
	public String __v, _id, end, name, start, description, image;
	public Utils.CoordinateObject.LocationObject location;
	public String[] attendees;
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	public String getImageURL() {
		return this.image;
	}
	
}
