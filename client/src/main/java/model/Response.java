package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class Response {

    private String statusCode;
    private String message;
    private HashMap<String, String> parameters = new HashMap<>();
    private User user;
    private Tile tile;
    private Maps maps;

    public Response(){

    }

    private ArrayList<String> notifications = new ArrayList<>();

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response fromJson(String json) {
        return new Gson().fromJson(json, Response.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public void setTile(Tile tile){
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public Maps getMaps() {
        return maps;
    }

    public void setMaps(Maps maps) {
        this.maps = maps;
    }
}
