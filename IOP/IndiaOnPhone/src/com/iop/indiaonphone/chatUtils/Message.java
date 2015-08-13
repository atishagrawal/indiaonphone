package com.iop.indiaonphone.chatUtils;

public class Message {
	private String fromName, message, image;
	private boolean isSelf, isImage;

	public Message() {
	}

	public Message(String fromName, String message, String image,
			boolean isSelf, boolean isImage) {
		this.fromName = fromName;
		this.message = message;
		this.isSelf = isSelf;
		this.image = image;
		this.isImage = isImage;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
