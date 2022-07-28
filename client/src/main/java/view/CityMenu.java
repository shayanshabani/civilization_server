package view;


import controller.NetworkController;
import view.enums.Colors;
import model.*;
import view.enums.RegexEnums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityMenu {

    private Matcher matcher;

    public void run(Scanner scanner, String username) {
        String cityInput;
        System.out.println("welcome to city menu dear " + username);
        while (true) {
            cityInput = scanner.nextLine();
            // get out
            if (cityInput.equals("city exit")) {
                System.out.println("back to play game");
                return;
            }
            // show player cities
            else if (cityInput.equals("user cities")) {
                Request request = new Request();
                request.setMenu("city menu");
                request.setAction("user cities");
                Response response = NetworkController.getInstance().sendRequest(request);
                for (String notification : response.getNotifications()) {
                    System.out.println(notification);
                }
                int max = Integer.parseInt(response.getParameters().get("max"));
                while (true) {
                    cityInput = scanner.nextLine();
                    if (cityInput.equals("city exit")) {
                        System.out.println("back to play game");
                        return;
                    } else if (Pattern.matches("[0-9]+", cityInput)) {
                        int index = Integer.parseInt(cityInput);
                           if (index >= 1 && index <= max) {
                               cityPage(index - 1, username, scanner);
                           } else
                               System.out.println("invalid number");
                    } else
                        System.out.println("invalid command");
                }
            }
            // show all cities in the game
            else
                System.out.println("invalid command");
        }
    }

    public void cityPage(int indexOfCity, String username, Scanner scanner) {
        String cityInput;
        System.out.println("you are in the city page");
        while (true) {
            cityInput = scanner.nextLine();
            if (cityInput.trim().equals("city exit")) {
                System.out.println("back to city menu");
                return;
            }
            else if (cityInput.trim().equals("buy tile")) {

                Request request = new Request();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("index of city", String.valueOf(indexOfCity));
                request.setMenu("city menu");
                request.setAction("nearby tiles information");
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                int max = Integer.parseInt(response.getParameters().get("max"));
                for (String notification : response.getNotifications()) {
                    System.out.println(notification);
                }
                int index;

                String buyTileInput;
                boolean buying = true;
                while (buying) {
                    buyTileInput = scanner.nextLine();
                    if (buyTileInput.equals("buy tile exit"))
                        buying = false;
                    else if (Pattern.matches("[\\d+]", buyTileInput)) {
                        index = Integer.parseInt(buyTileInput);
                        if (index >= 1 && index <= max) {
                            // add the tile to the city and user
                            buyTile(username, indexOfCity, index, false);
                        }
                        else
                            System.out.println("invalid number");
                    }
                    else if ((matcher = RegexEnums.getMatcher(buyTileInput, RegexEnums.BUY_TILE1)) != null ||
                            (matcher = RegexEnums.getMatcher(buyTileInput, RegexEnums.BUY_TILE2)) != null) {
                        index = Integer.parseInt(matcher.group("index"));
                        if (index >= 1 && index <= max) {
                            buyTile(username, indexOfCity, index, true);
                        }
                        else
                            System.out.println("invalid number");
                    }
                    else
                        System.out.println("invalid command");
                }
            }
            else if (cityInput.trim().equals("new production")) {

                setProduction(indexOfCity, username, scanner);
            }
            else if (cityInput.trim().equals("resume production")) {

            }
            else if (cityInput.trim().equals("terminate current production")) {

            }
            else if (cityInput.trim().equals("set citizens")){
                alternativeFunction(scanner, indexOfCity, username);
            }
            else if (cityInput.trim().equals("city information")) {
                //cityInformation(city);
            }
            else
                System.out.println("invalid command");
        }
    }

    private void setProduction(int indexOfCity, String username, Scanner scanner) {
        int index = 1;
        Request request = new Request();
        request.setMenu("city menu");
        request.setAction("city products");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("index of city", String.valueOf(indexOfCity));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }

        String productInput;
        System.out.println("you are in production bar");
        while (true) {
            productInput = scanner.nextLine();
            if (productInput.trim().equals("production exit"))
                return;
            else if (Pattern.matches("\\d+", productInput)) {
                int numberOfProduct = Integer.parseInt(productInput);
                if (numberOfProduct >= 1) {

                    request = new Request();
                    request.setMenu("city menu");
                    request.setAction("set production");
                    parameters = new HashMap<>();
                    parameters.put("index", String.valueOf(numberOfProduct));
                    parameters.put("cheat", String.valueOf(false));
                    request.setParameters(parameters);

                    response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }
                else
                    System.out.println("invalid number");
            }
            else if ((matcher = RegexEnums.getMatcher(productInput, RegexEnums.ADD_PRODUCT1)) != null ||
                    (matcher = RegexEnums.getMatcher(productInput, RegexEnums.ADD_PRODUCT2)) != null) {
                index = Integer.parseInt(matcher.group("index"));
                if (index >= 1) {

                    request = new Request();
                    request.setMenu("city menu");
                    request.setAction("set production");
                    parameters = new HashMap<>();
                    parameters.put("index", String.valueOf(index));
                    parameters.put("cheat", String.valueOf(true));
                    request.setParameters(parameters);

                    response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }
                else
                    System.out.println("invalid number");
            }
            else
                System.out.println("invalid command");
        }
    }

    public void alternativeFunction(Scanner scanner, int indexOfCity, String username) {
        int index = 1;
        System.out.println("choose one of the citizens number");

        Request request = new Request();
        request.setMenu("city menu");
        request.setAction("citizen information");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("index of city", String.valueOf(indexOfCity));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }
        String input;
        System.out.println("just numbers, back to return to city panel");
        int max = Integer.parseInt(response.getParameters().get("max"));

        while (true) {
            input = scanner.nextLine();
            if (input.equals("back")) {
                System.out.println("back to city panel");
                return;
            }
            else if (Pattern.matches("\\d+", input)) {
                index = Integer.parseInt(input);
                if (index >= 1 && index <= max) {
                    break;
                }
                else
                    System.out.println("invalid index");
            }
            else
                System.out.println("invalid command, just put numbers");
        }
        int indexOfCitizen = index - 1;

        index = 1;

        System.out.println("choose one tile number to employ the citizen on the tile");
        request = new Request();
        request.setMenu("city menu");
        request.setAction("tile citizen information");
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("index of city", String.valueOf(indexOfCity));
        response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }
        max = Integer.parseInt(request.getParameters().get("max"));

        while (true) {
            input = scanner.nextLine();
            if (input.equals("back")) {
                System.out.println("back to city panel");
                return;
            }
            else if (Pattern.matches("\\d+", input)) {
                index = Integer.parseInt(input);
                if (index >= 1 && index <= max) {
                    setCitizen(username, indexOfCity, index - 1, indexOfCitizen);
                    return;
                }
                else
                    System.out.println("invalid index");
            }
        }




    }

/*
    private void cityInformation(City city) {
        System.out.println(Colors.YELLOW + "Name : " + city.getName());
        System.out.println("Owner : " + city.getOwner().getUsername());
        System.out.println("Health point : " + city.getHP());
        System.out.println("Coordinate : -x " + city.getTile().getX() + " -y " + city.getTile().getY());
        System.out.println("Possible products :");
        for (Product product : city.getProducts()) {
            System.out.println("Name : " + product.getName());
            System.out.println("Price : " + product.getTurnCost());
        }
        System.out.println("Ownership tile :");
        int index = 1;
        for (Tile ownerShipTile : city.getOwnerShipTiles()) {
            System.out.println(index + "- : -x " + ownerShipTile.getX() + " -y " + ownerShipTile.getY());
            //PlayGame.tileDetails(ownerShipTile);
            index++;
        }
        System.out.println(Colors.RESET);
    }

 */

    private void setCitizen(String username, int indexOfCity, int indexOfTileInOwnerShips, int indexOfCitizen) {
        Request request = new Request();
        request.setMenu("city menu");
        request.setAction("set citizen");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("index of city", String.valueOf(indexOfCity));
        parameters.put("index of tile", String.valueOf(indexOfTileInOwnerShips));
        parameters.put("index of citizen", String.valueOf(indexOfCitizen));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }

    private void buyTile(String username, int indexOfCity, int indexOfTileInNeighborsOfCity, boolean cheat) {
        Request request = new Request();
        request.setMenu("city menu");
        request.setAction("buy tile");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("cheat", String.valueOf(cheat));
        parameters.put("username", username);
        parameters.put("index of city", String.valueOf(indexOfCity));
        parameters.put("index of tile", String.valueOf(indexOfTileInNeighborsOfCity));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }
}
