package com.corp.match.model;



public class ChatMessage {
    public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String sender;
    private String content;

    // getters and setters
}
