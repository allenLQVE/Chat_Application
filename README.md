# Chat Application

This is an application that allows users to chat with other people who are connecting to the server in different chat rooms. \
It connects users and servers through web sockets and uses broadcasts to send messages with multicast sockets. \
The GUI is built with Java FX and stores the user information in Java binary files.

### How To Run
The application is separated into the server side and the client side. A server needs to be hosted every time the application is running. 
The server can be run with the Server, and the client can be run with the Main.

### Issue
Each chating room needs to take a unique port, otherwise, the message will be sent to all the users who are listening to the port.

### Acknowledge
This application is inspired by DietalMessenger in Paul Dietal and Harvey Dietal's "[Java How to Program](https://deitel.com/java-how-to-program-11-e-early-objects-version/)".
