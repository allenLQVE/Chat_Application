package application;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Class: User
 * 
 * A class for user, records the user's password for login. Have the rooms the user in and friends the user have.
 */
public class User implements Serializable {
    // serialVersionUID should be increase every time the structure of the class is changed
    private static final long serialVersionUID = 1;

    private String name;
    private ArrayList<Room> rooms;
    private String password;

    public User(String name, ArrayList<Room> rooms, String password){
        this.name = name;
        this.rooms = rooms;
        this.password = password;
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
    
    /**
     * check if the input password equals to the password
     * 
     * @param password
     *          the password that getting checked
     * @return return true if the password equals
     */
    public boolean checkPassword(String password){
        return password.equals(this.password);
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() != this.getClass()){
            return false;
        }

        User user = (User) obj;
        return (name == user.getName());
    }

    @Override
    public String toString() {
        return "User: " + name;
    }
}
