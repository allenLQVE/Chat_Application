package application;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	private List<String> objects;
	
	private List<Message> contents;
	
	Room(){
		this.objects = new ArrayList<String>();
		this.contents = new ArrayList<Message>();
	}
	
	Room(List<String> objects, List<Message> contents){
		this.objects = objects;
		this.contents = contents;
	}
	
	public List<String> getObjects() {
		return objects;
	}
	public void setObjects(List<String> objects) {
		this.objects = objects;
	}
	
	public void addObject(String object) {
		this.objects.add(object);
	}
	
	public void removeObject(String object) {
		this.objects.remove(object);
	}
	
	public List<Message> getContents() {
		return contents;
	}
	
	public void setContents(List<Message> contents) {
		this.contents = contents;
	}
	
	public void addContent(Message content) {
		this.contents.add(content);
	}
	
	public void removeContent(Message content) {
		this.contents.remove(content);
	}
	
}
