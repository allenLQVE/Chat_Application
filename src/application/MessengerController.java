package application;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import application.Interface.MessageListener;
import application.Interface.MessageManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class MessengerController {
    // chat panel components
    @FXML
    private BorderPane chatPane;
    @FXML
    private Button returnBtn;
    @FXML
    private ListView<String> userList;
    @FXML
    private Label roomLabel;
    @FXML
    private TextArea msgTextDisplay;
    @FXML
    private TextField msgTextInput;
    @FXML
    private Button sendMsgBtn;

    // login panel components
    @FXML
    private BorderPane loginPane;
    @FXML
    private Button loginBtn;
    @FXML
    private Button newUserBtn;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField userNameInput;

    // main panel components
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<Button> roomList;
    @FXML
    private Button logOutBtn;

    private String userName;
    private String pwd;
    private User user;
    private Room currRoom;
    private MessageListener listener  = new MyMessageListener();
    private Dictionary<String, MessageManager> managers;

    private ArrayList<User> userDB;
    private ArrayList<Room> roomDB;
 
    /**
     * create user and room db for testing
     */
    @SuppressWarnings("unused")
    private void createDumyDB(){
        User user1 = new User("Allen", "1234");
        User user2 = new User("Bob", "1234");
        User user3 = new User("Cirby", "1234");

        userDB = new ArrayList<User>();
        roomDB = new ArrayList<Room>();
        for (int i = 0; i < 3; i++) {
            Room room = new Room("room " + i, i + 1000);
            room.addUser(user1);
            room.addUser(user2);
            room.addUser(user3);
            roomDB.add(room);
        }

        user1.setRooms(new ArrayList<Room>(roomDB));
        user2.setRooms(new ArrayList<Room>(roomDB));
        user3.setRooms(new ArrayList<Room>(roomDB));

        user2.removeRoom(roomDB.get(1));
        roomDB.get(1).removeUser(user2);

        userDB.add(user1);
        userDB.add(user2);
        userDB.add(user3);
    }

    /**
     * initialization
     */
    @FXML
    public void initialize() {
        // load database
        userDB = new ArrayList<User>();
        roomDB = new ArrayList<Room>();
        loadUserDB();
        loadRoomDB();
        
        loginPane.setVisible(true);

        // createDumyDB();
    }

    /**
     * load rooms from roomRecord
     */
    private void loadRoomDB() {
        ObjectInputStream inputStream = null;
        String fileName = "room.records";
        try {
            inputStream = new ObjectInputStream(new FileInputStream(fileName));

            while (true) {
                roomDB.add((Room) inputStream.readObject());
            }
        } catch (EOFException e) {
            // end of file, do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load users from userRecord
     */
    private void loadUserDB() {
        ObjectInputStream inputStream = null;
        String fileName = "user.records";
        try {
            inputStream = new ObjectInputStream(new FileInputStream(fileName));

            while (true) {
                userDB.add((User) inputStream.readObject());
            }
        } catch (EOFException e) {
            // end of file, do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save roomDB to a binary file
     */
    public void saveRoomDB() {
        ObjectOutputStream outputStream = null;
        String fileName = "room.records";
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

            for (Room room : roomDB) {
                outputStream.writeObject(room);
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save userDB to a binary file
     */
    public void saveUSERDB() {
        ObjectOutputStream outputStream = null;
        String fileName = "user.records";
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

            for (User user : userDB) {
                outputStream.writeObject(user);
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methods for Main Panel ---------------------------------------------------
    /**
     * Login user then connect the user to the server
     * 
     * @param event
     */
    @FXML
    void login(ActionEvent event) {
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        user = this.login(userName, pwd);

        // if login success, login user and connect to the server
        if(user == null){
            // if login failed, ask user to re-enter the password
            passwordInput.setText("");
            Alert alert = new Alert(AlertType.ERROR, "Incorrect Password!");
            alert.show();
        } else {
            try {
                this.connect();
            } catch (Exception e) {
                user = null;
                Alert alert = new Alert(AlertType.ERROR, "Server Connectino Failed!");
                alert.show();
                return;
            }
            
            this.showMainPane();
        }
    }

    /**
     * check if user in the database
     * 
     * @param userName
     * @param pwd
     * @return
     */
    private User login(String userName, String pwd) {
        for (User user : userDB) {
            if(user.getName().equals(userName) && user.checkPassword(pwd)){
                
                return user;
            }
        }
        return null;
    }

    /**
     * connect to the server
     * @throws Exception 
     */
    private void connect() throws Exception {
        // connect to mutiple rooms at once
        managers = new Hashtable<String, MessageManager>();
        for (int i = 0; i < user.getRooms().size(); i ++) {
            MessageManager manager = new SocketMessageManager("localhost");

            try {
                manager.connect(listener, user.getRooms().get(i).getPort());
            } catch (Exception e) {
                throw e;
            }
            
            managers.put(user.getRooms().get(i).getName(), manager);
        }
    }

    /**
     * connect to a room
     * @throws Exception 
     */
    private void connect(Room room) throws Exception {
        if(managers == null){
            managers = new Hashtable<String, MessageManager>();
        }
        
        MessageManager manager = new SocketMessageManager("localhost");

        try {
            manager.connect(listener, room.getPort());
        } catch (Exception e) {
            throw e;
        }
        managers.put(room.getName(), manager);
    }

    private class MyMessageListener implements MessageListener{
        @Override
        public void messageReceived(String sender, String msg, int send_port, int listen_port) {
            // if the currRoom is the room receving the msg
            if(currRoom != null && currRoom.getPort() == listen_port){
                currRoom.addContent(sender + ": " + msg);
                msgTextDisplay.appendText(sender + ": " + msg + "\n");
            } else {
                for (Room room : user.getRooms()) {
                    if(room.getPort() == listen_port){
                        room.addContent(sender + ": " + msg);
                    }
                }
            }
        }
        
    }

    /**
     * Display the view of a chat room
     * 
     * @param room
     */
    private void openChatRoom(Room room) {
        currRoom = room;
        
        // set up the chat panel
        List<String> userNames = new ArrayList<String>();
        for (User user : room.getUsers()) {
            userNames.add(user.getName());
        }

        userList.setItems(FXCollections.observableArrayList(userNames));
        roomLabel.setText(room.getName());
        for (String msg : room.getContents()) {
            msgTextDisplay.appendText(msg + "\n");
        }

        mainPane.setVisible(false);
        chatPane.setVisible(true);
    }

    /**
     * Add new chating room to the list
     * 
     * @param event
     */
    @FXML
    void addRoom(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Please enter the port for the room.");
            dialog.setTitle("Add Room");
            Optional<String> option = dialog.showAndWait();

            if (option.isEmpty()) {
                return;
            }
            
            int port = Integer.parseInt(option.get());

            Room newRoom = null;
            for (Room room : roomDB) {
                // check if the room exist
                if(room.getPort() == port){
                    newRoom = room;
                }
            }

            // if room already exist return
            if(user.getRooms().contains(newRoom)){
                return;
            }

            // get create a new room and save to DB
            if(newRoom == null){
                dialog = new TextInputDialog();
                dialog.setHeaderText("Please enter the name for the room.");
                dialog.setTitle("Add Room");
                option = dialog.showAndWait();

                String roomName = option.get();
                
                newRoom = new Room(roomName, port);
                roomDB.add(newRoom);
            }
            
            user.addRoom(newRoom);
            newRoom.addUser(user);
            try {
                connect(newRoom);
            } catch (Exception e) {
                user.removeRoom(newRoom);
                newRoom.removeUser(user);
                
                Alert alert = new Alert(AlertType.ERROR, "Failed to Connect to the room!");
                alert.show();
                return;
            }

            // update main panel
            showMainPane();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Invalid Input!");
            alert.show();
        } 
    }

    /**
     * remove user from room and room from user, if room is empty, remove room
     * 
     * @param event
     */
    @FXML
    void removeRoom(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Please enter the port or the name of the room.");
        dialog.setTitle("Remove Room");
        Optional<String> option = dialog.showAndWait();

        if(option.isEmpty()){
            return;
        }

        int port = -1;
        try {
            port = Integer.parseInt(option.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String name = option.get();

        Room roomToDelete = null;
        for (Room room : user.getRooms()) {
            if(room.getName().equals(name) || room.getPort() == port){
                roomToDelete = room;
            }
        }

        user.removeRoom(roomToDelete);
        roomToDelete.removeUser(user);
        managers.get(roomToDelete.getName()).disconnect(listener);

        if(roomToDelete.getUsers().size() == 0){
            roomDB.remove(roomToDelete);
        }

        // update main panel
        showMainPane();
    }

    /**
     * disconnect user from the server
     */
    public void disconnect() {
        if(user != null){
            for (Room room : user.getRooms()) {
                managers.get(room.getName()).disconnect(listener);
            }
        }
    }

    /**
     * log out the current user
     * @param event
     */
    @FXML
    void logOut(ActionEvent event) {
        userNameInput.clear();
        passwordInput.clear();

        disconnect();
        user = null;

        loginPane.setVisible(true);
        mainPane.setVisible(false);
    }

    // Methods for Chat Panel ---------------------------------------------------
    @FXML
    void returnToMain(ActionEvent event) {
        msgTextInput.clear();
        msgTextDisplay.clear();
        currRoom = null;

        chatPane.setVisible(false);
        mainPane.setVisible(true);
    }

    @FXML
    void sendMsg(ActionEvent event) {
        managers.get(currRoom.getName()).sendMessage(user.getName(), msgTextInput.getText(), 0, currRoom.getPort());
        msgTextInput.setText("");
    }

    // Methods for Login Panel ---------------------------------------------------
    /**
     * Display the main menu
     */
    private void showMainPane() {
        loginPane.setVisible(false);
        mainPane.setVisible(true);

        nameLabel.setText(user.getName());
        
        // add the rooms to the list view
        List<Button> roomBtns = new ArrayList<Button>();
        for (Room room : user.getRooms()) {
            Button roomBtn = new Button(room.toString());
            roomBtn.setStyle("-fx-border:none; -fx-background-color:transparent;");         
            roomBtn.setOnMouseClicked(e -> openChatRoom(room));
            roomBtn.setMaxWidth(Integer.MAX_VALUE);
            roomBtns.add(roomBtn);
        }

        roomList.setItems(FXCollections.observableArrayList(roomBtns));
    }
    
    /**
     * check if user exist by checking the name, if not create a new user by the password and username
     * 
     * @param event
     */
    @FXML
    void createNewUser(ActionEvent event) {
        userName = userNameInput.getText();
        pwd = passwordInput.getText();

        if(userName == "" || pwd == ""){
            Alert alert = new Alert(AlertType.ERROR, "Missing Information!");
            alert.show();
            return;
        }
        
        // if user name exist
        for (User user : userDB) {
            if(user.getName().equals(userName)){
                userNameInput.setText("");
                passwordInput.setText("");

                Alert alert = new Alert(AlertType.ERROR, "User Already Exist!");
                alert.show();
                return;
            }
        }
        
        // if user name not exist, create new user and log in user with the new name and password
        userDB.add(new User(userName, pwd));
        this.login(event);
    }
}