package com.attendr.app;

public class UserData {
	public static final int TYPE_FACEBOOK = -2;
	public static final int TYPE_TWITTER = -3;
	private String __v;
	private String _id;
	private String facebook;
	private Name name;
	private String image;

	public UserData(String uid, String fName, String lName) {
		this._id = uid;
		name = new Name(fName, lName);
	}

	public String getID() {
		return _id;
	}
	
	public String getName() {
		return name.getName();
	}
	
	public void setFacebookId(String id) {
		facebook = id;
	}

	public String getFacebookId() {
		if(facebook!=null)
			return facebook;
		return "";
	}
	
	public String getImageURL() {
		return image;
	}
	
	public class Name {
		public String first;
		public String last;
		public Name(String firstName, String lastName) {
			first = firstName;
			last = lastName;
		}
		public String getName() {
			return first + last;
		}
	}
}
