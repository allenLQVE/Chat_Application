package application;

import java.util.ArrayList;

public class Room {
	
	private String name;
	private int port;
	private ArrayList<User> users;
	private ArrayList<String> contents;
	
	Room(String name, int port){
		this(name, new ArrayList<User>(), new ArrayList<String>(), port);
	}
	
	Room(String name, ArrayList<User> users, ArrayList<String> contents, int port){
		this.name = name;
		this.users = users;
		this.contents = contents;
		this.port = port;
	}

	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	public void addUser(User user) {
		this.users.add(user);
	}
	public void removeUser(User user) {
		this.users.remove(user);
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
		return room.getPort() == port;
	}
	
	@Override
	public String toString() {
		return "Room - " + name + ": " + users;
	}
}
