package view;

import controller.*;
import view.enums.Colors;
import view.enums.RegexEnums;
import model.*;

import java.util.*;
import java.util.regex.*;

public class PlayGame {
    //private final ArrayList<User> players;
    private Maps map;
    private Matcher matcher;

    private int role;
    private int height;
    private int width;
    private ArrayList<Tile> firstTurnsSettlers;
    private CityMenu cityMenu;
    private ResearchMenu researchMenu;
    private ArrayList<Technology> technologies;

    private int[][] graph;


    public PlayGame() {
        this.height = 26;
        this.width = 80;
    }

    // provide some information for players
    private void manPlayGame() {
        Request request = new Request();
        request.setMenu("play game");
        request.setAction("man page");
        Response response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }
    }

    public void run(Scanner scanner, String username) {
        cityMenu = new CityMenu();
        researchMenu = new ResearchMenu();
        //UserPanel userPanel = new UserPanel();
        manPlayGame();
        String input;
        int role = 0;

        int turn = 1;

        boolean nextTurn = true;
        while (true) {
            //String color = new ColorsController().getColorOfUser(user);
            //System.out.println("it's " + color + user.getNickname() + Colors.RESET + " turn");
            // handle production turn in cities and research turn of user
            //pre turn actions

            while (nextTurn) {

                input = scanner.nextLine();
                if (input.equals("game exit"))
                    return;

                else if (input.trim().startsWith("next turn")) {
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("next turn");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    if (input.trim().equals("next turn")) {
                        parameters.put("cheat", String.valueOf(false));
                    }
                    else if (input.trim().equals("next turn --force")) {
                        parameters.put("cheat", String.valueOf(true));
                    }
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                    if (Boolean.parseBoolean(response.getParameters().get("next turn")))
                        nextTurn = false;
                }
                else if (input.trim().equals("city menu")) {
                    cityMenu.run(scanner, username);
                }
                else if (input.trim().equals("research panel")) {
                    researchMenu.run(scanner, username);
                }
                else if (input.trim().equals("user panel")) {
                    //userPanel.run(scanner);
                }
                //cheat codes
                else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_TURN1)) != null ||
                        (matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_TURN2)) != null) {
                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase turn");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }
                else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_GOLD1)) != null ||
                        (matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_GOLD2)) != null) {
                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase gold");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }
                else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_FOOD)) != null) {

                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase food");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_FAITH)) != null){

                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase faith");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_SCIENCE)) != null){

                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase science");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_CAPITAL_CITIZENS)) != null){

                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase capital citizens");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_CAPITAL_DEFENCE)) != null){

                    int amount = Integer.parseInt(matcher.group("amount"));
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase capital defence");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_CULTURE)) != null){
                    int amount = Integer.parseInt(matcher.group("amount"));

                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase culture");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.INCREASE_HAPPINESS)) != null){
                    int amount = Integer.parseInt(matcher.group("amount"));

                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("increase culture");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }else if((matcher = RegexEnums.getMatcher(input, RegexEnums.DECREASE_RESEARCH_TURN_LEFT)) != null){
                    int amount = Integer.parseInt(matcher.group("amount"));

                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("decrease research turn");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    parameters.put("amount", String.valueOf(amount));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }
                // selecting tile
                else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.SELECT_TILE1)) != null ||
                        (matcher = RegexEnums.getMatcher(input, RegexEnums.SELECT_TILE2)) != null) {
                    int xOrigin = Integer.parseInt(matcher.group("x"));
                    int yOrigin = Integer.parseInt(matcher.group("y"));

                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("select tile");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("x", String.valueOf(xOrigin));
                    parameters.put("y", String.valueOf(yOrigin));
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                    if (response.getStatusCode().equals("200")) {
                        //Tile origin = map.getSpecificTile(xOrigin, yOrigin);
                        selectedTile(scanner, xOrigin, yOrigin, username);
                    }
                }

                else if (input.trim().equals("show board")) {
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("show map");
                    HashMap<String, String> parameters = new HashMap<>();
                    parameters.put("username", username);
                    request.setParameters(parameters);
                    Response response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());
                }

                else if (input.trim().equals("show players")) {
                    Request request = new Request();
                    request.setMenu("play game");
                    request.setAction("show players");
                    Response response = NetworkController.getInstance().sendRequest(request);
                    for (String notification : response.getNotifications()) {
                        System.out.println(notification);
                    }
                } else
                    System.out.println("invalid command");
            }
            nextTurn = true;
        }
    }


    public void selectedTile(Scanner scanner, int xOrigin, int yOrigin, String username) {

        Request request;
        String tileInput;
        while (true) {

            request = new Request();
            request.setMenu("tile menu");

            tileInput = scanner.nextLine();
            if (tileInput.equals("tile exit")) {
                System.out.println("get out of tile");
                return;
            }

            else if ((matcher = RegexEnums.getMatcher(tileInput, RegexEnums.SELECT_TILE1)) != null ||
                    (matcher = RegexEnums.getMatcher(tileInput, RegexEnums.SELECT_TILE2)) != null) {
                if (selectAnotherTile(xOrigin, yOrigin, scanner, username, matcher))
                    return;
            }
            // move the unit in this tile to destination
            else if ((matcher = RegexEnums.getMatcher(tileInput, RegexEnums.MOVE1)) != null ||
                    (matcher = RegexEnums.getMatcher(tileInput, RegexEnums.MOVE2)) != null) {

                request.setAction("move unit");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("xDestination", matcher.group("x"));
                parameters.put("yDestination", matcher.group("y"));
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
                if (!Boolean.parseBoolean(response.getParameters().get("arrived"))) {
                    //it's not over, wait till next turn to move to destination
                }
            }
            // order settler to place city
            else if ((matcher = RegexEnums.getMatcher(tileInput, RegexEnums.CITY1)) != null ||
                    (matcher = RegexEnums.getMatcher(tileInput, RegexEnums.CITY2)) != null) {



                request.setAction("place city");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("xDestination", String.valueOf(xOrigin));
                parameters.put("yDestination", String.valueOf(yOrigin));
                parameters.put("username", username);
                parameters.put("name", matcher.group("city"));

                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
            }
            // order worker to improve the tile
            else if (tileInput.trim().equals("show possible improvements")) {
                request.setAction("show possible improvements");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("xOrigin", String.valueOf(xOrigin));
                parameters.put("yOrigin", String.valueOf(yOrigin));
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                if (Boolean.parseBoolean(response.getParameters().get("yes")))
                    showImprovements(xOrigin, yOrigin, username, scanner);
                else
                    System.out.println(response.getMessage());

            }
            else if (tileInput.trim().equals("tile information")) {
                tileInformation(xOrigin, yOrigin);
            }
            else if ((matcher = RegexEnums.getMatcher(tileInput, RegexEnums.ATTACK_CITY1)) != null ||
                    (matcher = RegexEnums.getMatcher(tileInput, RegexEnums.ATTACK_CITY2)) != null) {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));

                request.setAction("attack city");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("xOrigin", String.valueOf(xOrigin));
                parameters.put("yOrigin", String.valueOf(yOrigin));
                parameters.put("xDestination", matcher.group("x"));
                parameters.put("yDestination", matcher.group("y"));
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
                if (Boolean.parseBoolean(response.getParameters().get("ruined"))) {
                    //the city has been destroyed , decision on what to do
                    parameters = new HashMap<>();
                    request = new Request();
                    request.setMenu("tile menu");
                    request.setAction("decision on what to do with city");
                    decisionOnWhatToDoWithCity(scanner, parameters);
                    parameters.put("xOrigin", String.valueOf(xOrigin));
                    parameters.put("yOrigin", String.valueOf(yOrigin));
                    parameters.put("xDestination", String.valueOf(x));
                    parameters.put("yDestination", String.valueOf(y));
                    request.setParameters(parameters);

                    response = NetworkController.getInstance().sendRequest(request);
                    System.out.println(response.getMessage());

                }
            }

            else if (((matcher = RegexEnums.getMatcher(tileInput, RegexEnums.ATTACK_UNIT1)) != null ||
                    (matcher = RegexEnums.getMatcher(tileInput, RegexEnums.ATTACK_UNIT2)) != null)) {
                request.setAction("attack unit");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("xOrigin", String.valueOf(xOrigin));
                parameters.put("yOrigin", String.valueOf(yOrigin));
                parameters.put("xDestination", matcher.group("x"));
                parameters.put("yDestination", matcher.group("y"));
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
                if (Boolean.parseBoolean(response.getParameters().get("notification"))) {
                    for (String notification : response.getNotifications()) {
                        System.out.println(notification);
                    }
                }
            }

            else if (tileInput.equals("delete unit")) {

                request.setAction("delete unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("sleep unit")) {
                request.setAction("sleep unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("alert unit")) {
                request.setAction("alert unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("garrison unit")) {
                request.setAction("garrison unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("fortify unit")) {
                request.setAction("fortify unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("pillage unit")) {
                request.setAction("pillage unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("repair improvement")) {
                request.setAction("pillage unit");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("repair improvement cheat")) {
                request.setAction("repair improvement");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("pause improving")){
                request.setAction("pause improvement");
                unitActions(xOrigin, yOrigin, request);
            }
            else if (tileInput.equals("resume improving")) {
                request.setAction("resume improvement");
                unitActions(xOrigin, yOrigin, request);
            }
            else
                System.out.println("invalid command");
        }
    }

    private void unitActions(int xOrigin, int yOrigin, Request request) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }

    private void showImprovements(int xOrigin, int yOrigin, String username, Scanner scanner) {

        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("improvement information");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }
        boolean deleted = Boolean.parseBoolean(response.getParameters().get("deleted"));
        boolean road = Boolean.parseBoolean(response.getParameters().get("road"));
        int max = Integer.parseInt(response.getParameters().get("max"));
        int index;
        String improvementInput;
        while (true) {
            improvementInput = scanner.nextLine();
            if (improvementInput.equals("improvement exit"))
                return;
            if (deleted) {
                if (Pattern.matches("[\\d+]", improvementInput)) {
                    index = Integer.parseInt(improvementInput);
                    if (index <= max && index >= 1) {

                        addImprovement(xOrigin, yOrigin, false, username);

                        System.out.println("get back to tile page");
                        return;
                    } else
                        System.out.println("invalid number");
                } else if ((matcher = RegexEnums.getMatcher(improvementInput, RegexEnums.ADD_IMPROVEMENT1)) != null ||
                        (matcher = RegexEnums.getMatcher(improvementInput, RegexEnums.ADD_IMPROVEMENT2)) != null) {
                    index = Integer.parseInt(matcher.group("index"));
                    if (index >= 1 && index <= max) {

                        addImprovement(xOrigin, yOrigin, true, username);

                        System.out.println("get back to tile page");
                        return;
                    } else
                        System.out.println("invalid number");
                }
                else if (improvementInput.equals("build road") && road) {
                    // later
                    buildRoad(xOrigin, yOrigin, false, username);
                    System.out.println("get back to the tile page");
                    return;
                }
                else if (improvementInput.equals("build road cheat") && road) {
                    buildRoad(xOrigin, yOrigin, true, username);
                    System.out.println("get back to the tile page");
                    return;
                }
                else
                    System.out.println("invalid command");
            }
            else if (improvementInput.equals("delete the current")) {
                deleteImprovement(xOrigin, yOrigin, username);
                deleted = true;
            }
            else
                System.out.println("delete the current");
        }

    }

    private boolean selectAnotherTile(int xOrigin, int yOrigin, Scanner scanner, String username, Matcher matcher) {

        int xDestination = Integer.parseInt(matcher.group("x"));
        int yDestination = Integer.parseInt(matcher.group("y"));

        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("select another tile");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        parameters.put("xDestination", String.valueOf(xDestination));
        parameters.put("yDestination", String.valueOf(yDestination));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
        boolean change = Boolean.parseBoolean(response.getParameters().get("change"));
        if (change) {
            selectedTile(scanner, xDestination, yDestination, username);
            return true;
        }
        return false;
    }

    private void decisionOnWhatToDoWithCity(Scanner scanner, HashMap<String, String> parameters) {
        System.out.println("which one do you choose?");
        System.out.println("1- completely destroy city");
        System.out.println("2- annex city");
        System.out.println("please press one of this numbers");
        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("what to do with city");
        String input;
        while (true) {
            input = scanner.nextLine();
            if (Pattern.matches("\\d+", input)) {
                int index = Integer.parseInt(input);
                switch (index) {
                    case 1 -> {
                        parameters.put("index", String.valueOf(1));
                        System.out.println();
                        return;
                    }
                    case 2 -> {
                        parameters.put("index", String.valueOf(2));
                        return;
                    }
                    default -> System.out.println("invalid number");
                }
            }
            else
                System.out.println("invalid command");
        }
    }

    public void addImprovement(int xOrigin, int yOrigin, boolean cheat, String username) {
        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("add improvement");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        parameters.put("cheat", String.valueOf(cheat));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }

    public void buildRoad(int xOrigin, int yOrigin , boolean cheat, String username) {
        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("build road");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        parameters.put("cheat", String.valueOf(cheat));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
        if (response.getNotifications().size() > 0) {
            for (String notification : response.getNotifications()) {
                System.out.println(notification);
            }
        }
    }

    public void deleteImprovement(int xOrigin, int yOrigin, String username) {
        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("delete improvement");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }

    public void tileInformation(int xOrigin, int yOrigin) {
        Request request = new Request();
        request.setMenu("tile menu");
        request.setAction("tile information");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("xOrigin", String.valueOf(xOrigin));
        parameters.put("yOrigin", String.valueOf(yOrigin));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        for (String notification : response.getNotifications()) {
            System.out.println(notification);
        }
    }

}
