package view;

import controller.*;
import model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Receiver {
    private Maps map;

    private LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
    private ArrayList<User> players;
    private LinkedList<ClientHandler> clientHandlersPlayers = new LinkedList<>();
    private TechController techController;

    public Receiver() {
        ServerController.getInstance().initGame();
        map = ServerController.getInstance().getMap();
        GameController.getInstance().assignNeighbor(map);
        TechController.getInstance(UsersController.getInstance().readFromJsonGraph(), UsersController.getInstance().readFromJsonTech());
    }

    public void run(int SERVER_PORT) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("a new client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Maps getMap() {
        return map;
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public LinkedList<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void setClientHandlers(LinkedList<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    public void kickClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        clientHandler.interrupt();
    }

    private void readFromJson() {
        ArrayList<User> users = UsersController.getInstance().readFromJson();
        UsersController.getInstance().setUsers(users);
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public LinkedList<ClientHandler> getClientHandlersPlayers() {
        return clientHandlersPlayers;
    }

    public void setClientHandlersPlayers(LinkedList<ClientHandler> clientHandlersPlayers) {
        this.clientHandlersPlayers = clientHandlersPlayers;
    }

    public TechController getTechController() {
        return techController;
    }

    public void setTechController(TechController techController) {
        this.techController = techController;
    }
}
