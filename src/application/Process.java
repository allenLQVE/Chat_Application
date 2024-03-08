package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Process {
	public static void main(String[] args) {
		
		Process me = new Process();
		
		me.test();
	}
	
	public void test() {
		
		Scanner keyboard = new Scanner(System.in);
		
		//multiple groups
		List<Room> groups = new ArrayList<Room>();
		
		Date now = new Date();
		Message chat;
		
		Room group1 = new Room();
		
		groups.add(group1);
		
		// add people in the group 
		group1.addObject("Alice");
		group1.addObject("Bob");
		group1.addObject("Christin");
		System.out.println("Group members:");
		System.out.println(group1.getObjects());
		System.out.println("Add a group members:");
		String name = keyboard.next();
		group1.addObject(name);
		System.out.println(group1.getObjects());
		System.out.println("Group members:");
		System.out.println(group1.getObjects());
		System.out.println();
		
		//add chats in the group
		chat = new Message("Hi!", "Bob", now.getTime());
		group1.addContent(chat);
		chat = new Message("How are you?", "Alice", now.getTime());
		group1.addContent(chat);
		for (int i = 0; i < group1.getContents().size(); i += 1) {
			System.out.println(group1.getContents().get(i).getObject());
			System.out.println(group1.getContents().get(i).getContent());
			System.out.println(group1.getContents().get(i).getTime());
		}
		
		keyboard.close();
	}
}
