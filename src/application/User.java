package application;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class: User
 * 
 * A class for user, records the user's password for login. Have the rooms the user in and friends the user have.
 */
public class User {
    public static final Random RANDOM = new SecureRandom();

    private String name;
    private ArrayList<Room> rooms;
    private int hashedPassword;
    private byte[] salt;

    public User(String name, ArrayList<Room> rooms, String password){
        this.name = name;
        this.rooms = rooms;

        // Simple salting and hashing the password may cause collision(Can be implementing with better methods)
        salt = new byte[16];
        RANDOM.nextBytes(salt);
        this.hashedPassword = (password + salt).hashCode();
    }

    public User(String name, String password){
        this(name, new ArrayList<Room>(), password);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Room> getRooms() {
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
    
    public int getHashedPassword() {
        return hashedPassword;
    }
    
    /**
     * check if the input password equals to the password
     * 
     * @param password
     *          the password that getting checked
     * @return return true if the password equals hashedPassword after adding salting and hashing
     */
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
        return "User: " + name;
    }
}
