package model;

import com.google.gson.*;

import java.util.HashMap;

public class Request {
    private User user;
    private String action;
    private String message;
    private String menu;
    private HashMap<String, String> parameters = new HashMap<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public static Request fromJson(String json) {
        return new Gson().fromJson(json, Request.class);
    }
}
