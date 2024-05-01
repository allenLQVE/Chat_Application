package application;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
	// serialVersionUID should be increase every time the structure of the class is changed
	private static final long serialVersionUID = 2;

	private String name;
	private int port;
	private ArrayList<String> userNames;
	private ArrayList<String> contents;
	
	public Room(String name, int port){
		this(name, new ArrayList<String>(), new ArrayList<String>(), port);
	}
	
	public Room(String name, ArrayList<String> userNames, ArrayList<String> contents, int port){
		this.name = name;
		this.userNames = userNames;
		this.contents = contents;
		this.port = port;
	}

	public ArrayList<String> getUserNames() {
		return userNames;
	}
	public void setUserNames(ArrayList<String> userNames) {
		this.userNames = userNames;
	}
	public void addUser(User user) {
		this.userNames.add(user.getName());
	}
	public void removeUser(User user) {
		this.userNames.remove(user.getName());
	}
	
	public ArrayList<String> getContents() {
		return contents;
	}
	public void setContents(ArrayList<String> contents) {
		this.contents = contents;
	}
	public void addContent(String content) {
		this.contents.add(content);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()){
			return false;
		}

		Room room = (Room) obj;
		return (room.getPort() == port) || (room.getName().equals(name));
	}
	
	@Override
	public String toString() {
		return "Room - " + name + ": " + port;
	}
}
