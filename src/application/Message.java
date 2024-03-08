package application;

import java.util.Date;

public class Message {
	
	private String content;
	private String object;
	private long timestamp;
	Date now = new Date();
	
	Message(){
		this.content = "None";
		this.object = "None";
		this.timestamp = now.getTime();
	}
	
	Message(String content, String object, long timestamp){
		this.content = content;
		this.object = object;
		this.timestamp = timestamp;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getObject() {
		return object;
	}
	
	public void setObject(String object) {
		this.object = object;
	}
	
	public long getTime() {
		return timestamp;
	}
	
	public void setTime(long timestamp) {
		this.timestamp = timestamp;
	}	
	
	/*public void set(String content, String object, long timestamp) {
		this.content = content;
		this.object = object;
		this.timestamp = timestamp;
	}*/
}
