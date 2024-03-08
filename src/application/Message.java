package application;

import java.util.Date;

public class Message {
	
	private String content;
	private User user;
	private long timestamp;
	Date now = new Date();
	
	Message(){
		this("None", null);
	}
	
	Message(String content, User user){
		this.content = content;
		this.user = user;
		this.timestamp = now.getTime();
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
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

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()){
			return false;
		}

		Message msg = (Message) obj;
		return content == msg.getContent() && user.equals(msg.getUser()) && timestamp == msg.timestamp;
	}

	@Override
	public String toString() {
		return user + " [" + timestamp + "]: " + content; 
	}
}
