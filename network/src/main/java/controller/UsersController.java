package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import view.ClientHandler;

import java.io.FileWriter;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UsersController {
    //arraylist of registered users;
    private ArrayList<User> users;

    private static UsersController usersController;

    private UsersController() {
        users = new ArrayList<>();
    }

    public static UsersController getInstance() {
        if (usersController == null)
            usersController = new UsersController();
        return usersController;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    //add user
    public void addUser(User user) {
        users.add(user);
    }
    //if same username exists among the users or not
    public boolean sameUsernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }
    //if same nickname exists among the users or not
    public boolean sameNicknameExists(String nickname) {
        for (User user : users) {
            if (user.getNickname().equals(nickname))
                return true;
        }
        return false;
    }
    //get user by username
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }
    // read the information of users from .json file
    public ArrayList<User> readFromJson() {
        try {
            String usersJson = new String(Files.readAllBytes(Paths.get("usersInformation.json")));
            ArrayList<User> usersFromJson = new Gson().fromJson(usersJson, new TypeToken<List<User>>(){}.getType());
            if (usersFromJson != null)
                return usersFromJson;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // write the information of users to .json file
    public void writeToJson() {
        try {
            FileWriter WriterToJson = new FileWriter("usersInformation.json");
            if (users.size() > 0)
                WriterToJson.write(new Gson().toJson(users));
            WriterToJson.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // read the graph and prerequisites
    public int[][] readFromJsonGraph() {
        try {
            String all = new String(Files.readAllBytes(Paths.get("TechFile/graph.json")));
            return new Gson().fromJson(all, int[][].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // read all technologies
    public ArrayList<Technology> readFromJsonTech() {
        try {
            String all = new String(Files.readAllBytes(Paths.get("TechFile/tech.json")));
            return new Gson().fromJson(all, new TypeToken<List<Technology>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // write graph to json
    public void writeToJson(int[][] graph) {
        try {
            FileWriter fileWriter = new FileWriter("TechFile/graph.json");
            fileWriter.write(new Gson().toJson(graph));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // write technologies to json
    public void writeToJson(ArrayList<Technology> technologies) {
        try {
            FileWriter fileWriter = new FileWriter("TechFile/tech.json");
            fileWriter.write(new Gson().toJson(technologies));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToJson(Maps map) {
        try {
            FileWriter fileWriter = new FileWriter("MapFile/map1.json");
            fileWriter.write(new Gson().toJson(map));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Maps readFromJsonMap() {
        try {
            String all = new String(Files.readAllBytes(Paths.get("MapFile/map1.json")));
            return new Gson().fromJson(all, Maps.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUserColor(String color , User user, HashMap<String, Boolean> availableColors){
        user.setColor(color);
        availableColors.replace(color , false);
    }


    public Response changePassword(Request request) {
        Response response = new Response();
        String oldPassword = request.getParameters().get("old password");
        String newPassword = request.getParameters().get("new password");
        String username = request.getParameters().get("username");
        User user = getUserByUsername(username);
        if (user.getPassword().equals(oldPassword)) {
            if (!oldPassword.equals(newPassword)) {
                user.setPassword(newPassword);
                response.setMessage("password changed successfully!");
                response.setStatusCode("200");
                HashMap<String, String> parameters = new HashMap<>();
                //parameters.put("user", user);
                response.setParameters(parameters);
                return response;
            }
            else {
                //old and new are the same;
                response.setStatusCode("401");
                response.setMessage("please enter a new password");
            }
        }
        else {
            //old password is not correct;
            response.setStatusCode("404");
            response.setMessage("current password is invalid");
        }
        return response;
    }

    public synchronized Response changeNickname(Request request) {
        Response response = new Response();
        String newNickname = request.getParameters().get("new nickname");
        String username = request.getParameters().get("username");
        User user = getUserByUsername(username);
        if (!sameNicknameExists(newNickname)) {
            user.setNickname(newNickname);
            response.setStatusCode("200");
            response.setMessage("nickname changed successfully!");
        }
        else {
            response.setStatusCode("401");
            response.setMessage("user with nickname " + newNickname + " already exists");
        }
        return response;
    }

    public synchronized Response signup(Request request) {
        Response response = new Response();
        String username = request.getParameters().get("username");
        String nickname = request.getParameters().get("nickname");
        String password = request.getParameters().get("password");
        if (!sameUsernameExists(username)) {
            if (!sameNicknameExists(nickname)) {
                User user = new User(username, nickname, password);
                addUser(user);
                response.setMessage("user created successfully!");
            }
            else
                response.setMessage("user with this nickname " + nickname + " already exists");

        }
        else
            response.setMessage("user with this username " + username + " already exists");

        return response;
    }

    public synchronized Response login(Request request) {
        Response response = new Response();
        String username = request.getParameters().get("username");
        String password = request.getParameters().get("password");
        User user;
        if ((user = getUserByUsername(username)) != null) {
            if (user.getPassword().equals(password)) {
                response.setStatusCode("200");
                response.setMessage("user logged in successfully!");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                response.setParameters(parameters);
                return response;
            }
        }
        response.setStatusCode("404");
        response.setMessage("Username and password didn't match!");

        return response;
    }

    public synchronized Response startGame(Request request) {
        Response response = new Response();
        String input = request.getParameters().get("input");
        String[] usernames = findUsernames(input, response);
        response.setStatusCode("404");
        if (usernames != null && usernames.length >= 2 && checkIfUsernamesAreValid(usernames, response) && ifAllUsernamesAreUnique(usernames, response)) {
            response.setMessage("usernames are valid!");
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("usernames", new Gson().toJson(usernames));
            response.setParameters(parameters);
            response.setStatusCode("200");
        }
        return response;
    }

    private String[] findUsernames(String input, Response response) {
        String[] all = input.split("--");
        int size = all.length - 1;
        String[] usernames = new String[size];
        if (size >= 2) {
            // handle double dash ,,, order doesn't matter
            String[] splitPlayerAndUsername;
            int indexOfPlayer;
            String subStringForNumber;
            for (int i = 1; i <= size; i++) {
                splitPlayerAndUsername = all[i].split("\\s+");
                if (splitPlayerAndUsername[0].startsWith("player")) {
                    subStringForNumber = splitPlayerAndUsername[0].substring(6);
                    if (Pattern.matches("[0-9]+", subStringForNumber)) {
                        if (assignUsernames(size, usernames, splitPlayerAndUsername, subStringForNumber, response))
                            return null;
                    }
                    else {
                        response.setMessage("invalid number for player , only number after <player>");
                        return null;
                    }
                }
                else {
                    response.setMessage("invalid command for players , each should start with key word <player>");
                    return null;
                }
            }
        }

        else {
            // handle one dash input ,,, order doesn't matter
            all = input.split("-");
            size = all.length - 1;
            usernames = new String[size];
            if (size >= 2) {
                String[] splitPlayerAndUsername;
                int indexOfPlayer;
                String subStringForNumber;
                for (int i = 1; i <= size; i++) {
                    splitPlayerAndUsername = all[i].split("\\s+");
                    if (splitPlayerAndUsername[0].startsWith("p")) {
                        subStringForNumber = splitPlayerAndUsername[0].substring(1);
                        if (Pattern.matches("[0-9]+", subStringForNumber)) {
                            if (assignUsernames(size, usernames, splitPlayerAndUsername, subStringForNumber, response))
                                return null;
                        }
                        else {
                            response.setMessage("invalid number for player , only number after <p>");
                            return null;
                        }
                    }
                    else {
                        response.setMessage("invalid command for players , each should start with key word <p>");
                        return null;
                    }
                }
            }
            else
                response.setMessage("invalid command");
        }
        return usernames;
    }

    private boolean assignUsernames(int size, String[] usernames, String[] splitPlayerAndUsername, String subStringForNumber, Response response) {
        int indexOfPlayer;
        indexOfPlayer = Integer.parseInt(subStringForNumber);
        if (indexOfPlayer <= size) {
            if (usernames[indexOfPlayer - 1] == null)
                usernames[indexOfPlayer - 1] = splitPlayerAndUsername[1].trim();
            else {
                response.setMessage("identical number for players");
                return true;
            }
        }
        else {
            response.setMessage("given numbers are not proper");
            return true;
        }
        return false;
    }

    //all usernames should be valid
    private boolean checkIfUsernamesAreValid(String[] usernames, Response response) {

        for (String s : usernames) {
            if (s == null) {
                response.setMessage("error occurred with given numbers");
                return false;
            }
        }

        for (String username : usernames) {
            if (!sameUsernameExists(username)) {
                response.setMessage("some usernames are not valid");
                return false;
            }
        }
        return true;
    }
    // all usernames should be unique
    private boolean ifAllUsernamesAreUnique(String[] usernames, Response response) {
        for (int i = 0; i < usernames.length - 1; i++) {
            for (int j = i + 1; j < usernames.length; j++) {
                if (usernames[i].equals(usernames[j])) {
                    response.setMessage("a username has been entered more than one time");
                    return false;
                }
            }
        }
        return true;
    }

}
