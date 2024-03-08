package application;

import java.util.ArrayList;

public class Room {
	
	private String name;
	private ArrayList<User> users;
	private ArrayList<Message> contents;
	
	Room(){
		this("Room",new ArrayList<User>(), new ArrayList<Message>());
	}
	
	Room(String name, ArrayList<User> users, ArrayList<Message> contents){
		this.name = name;
		this.users = users;
		this.contents = contents;
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
	
	public ArrayList<Message> getContents() {
		return contents;
	}
	public void setContents(ArrayList<Message> contents) {
		this.contents = contents;
	}
	public void addContent(Message content) {
		this.contents.add(content);
	}
	public void removeContent(Message content) {
		this.contents.remove(content);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()){
			return false;
		}

		Room room = (Room) obj;
		return room.getName() == name && room.getUsers().equals(users);
	}
	
	@Override
	public String toString() {
		return "Room - " + name + ": " + users;
	}
}
