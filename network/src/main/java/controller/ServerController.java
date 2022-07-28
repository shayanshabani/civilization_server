package controller;

import model.Maps;
import model.User;

import java.util.HashMap;

public class ServerController {
    private HashMap<Integer, String> userPorts = new HashMap<>();
    private Maps map = UsersController.getInstance().readFromJsonMap();;
    private static ServerController serverController;

    public static ServerController getInstance() {
        if (serverController == null)
            serverController = new ServerController();
        return serverController;
    }


    public HashMap<Integer, String> getUserPorts() {
        return userPorts;
    }

    public void setUserPorts(HashMap<Integer, String> userPorts) {
        this.userPorts = userPorts;
    }


    public void initGame() {
        UsersController.getInstance().setUsers(UsersController.getInstance().readFromJson());
        TechController.getInstance(UsersController.getInstance().readFromJsonGraph(), UsersController.getInstance().readFromJsonTech());
    }

    public Maps getMap() {
        return map;
    }

    public void setMap(Maps map) {
        this.map = map;
    }
}
