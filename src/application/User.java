package application;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    public static final Random RANDOM = new SecureRandom();

    private String name;
    private ArrayList<Room> rooms;
    private ArrayList<User> friends;
    private int hashedPassword;
    private byte[] salt;

    public User(String name, ArrayList<Room> rooms, ArrayList<User> friends, String password){
        this.name = name;
        this.rooms = rooms;
        this.friends = friends;

        // Simple salting and hashing the password may cause collision(Can be implementing with better methods)
        salt = new byte[16];
        RANDOM.nextBytes(salt);
        this.hashedPassword = (password + salt).hashCode();
    }

    public User(String name, String password){
        this(name, new ArrayList<Room>(), new ArrayList<User>(), password);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
    public void addRoom(Room room){
        if(!rooms.contains(room)){
            rooms.add(room);
        }
    }
    public void removeRoom(Room room){
        if(rooms.contains(room)){
            rooms.remove(room);
        }
    }

    public List<User> getFirends() {
        return friends;
    }
    public void setFirends(ArrayList<User> friends) {
        this.friends = friends;
    }
    public void addFirend(User friend){
        if(!friends.contains(friend)){
            friends.add(friend);
        }
    }
    public void removeFirend(User friend){
        if(friends.contains(friend)){
            friends.remove(friend);
        }
    }
    
    public int getHashedPassword() {
        return hashedPassword;
    }
    public boolean checkPassword(String password){
        return (password + salt).hashCode() == hashedPassword;
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() != this.getClass()){
            return false;
        }

        User user = (User) obj;
        return (name == user.getName() && hashedPassword == user.getHashedPassword());
    }

    @Override
    public String toString() {
        return "User: " + name + " Rooms: " + rooms + " Firends: " + friends;
    }
}
