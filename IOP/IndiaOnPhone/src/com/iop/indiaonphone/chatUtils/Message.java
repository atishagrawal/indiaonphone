package com.iop.indiaonphone.chatUtils;

public class Message {
	private String fromName, message, imagePath;
	private boolean isSelf, isImage;

	public Message() {
	}

	public Message(String fromName, String message, String imagePath,
			boolean isSelf, boolean isImage) {
		this.fromName = fromName;
		this.message = message;
		this.isSelf = isSelf;
		this.imagePath = imagePath;
		this.isImage = isImage;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

}
