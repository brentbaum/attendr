package com.attendr.app;

import com.attendr.net.Utils.CoordinateObject;

public class PlaceData {
		String formatted_address, icon, id, name, reference;
		double rating;
		String[] types;
		Geometry geometry;
	public class Geometry {
		CoordinateObject location;
	}
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return formatted_address;
	}
	
	public String getID() {
		return id;
	}
}
