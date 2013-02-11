package com.attendr.app;

public class Comment {
	private String content;
	private String uid;

	public Comment(String content, String uid) {
		this.content = content;
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public String getUserID() {
		return uid;
	}
}
