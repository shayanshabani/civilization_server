package view;

import com.google.gson.Gson;

import controller.NetworkController;

import view.enums.Colors;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameMenu {
    // provide some information for user
    private void manGameMenu(String username) {
        //String userColor = new ColorsController().getColorOfUser(user);
        String boldColor = Colors.YELLOW_BOLD;
        String color = Colors.CYAN;
        System.out.println("welcome to Game Menu dear " + username + Colors.RESET + "!");
        System.out.println(color + "press \"menu exit\" to get back to Main Menu" + Colors.RESET);
        System.out.println(boldColor + "to Play Game :" + Colors.RESET);
        System.out.println(color + "play game --player1 <username> ... --player(k) <username>");
        System.out.println("play game -p1 <username> ... -p(k) <username>");
        System.out.println("the order of players is not important" + Colors.RESET);
    }
    public void run(String username, Scanner scanner) {
        String input;
        manGameMenu(username);
        while (true) {
            input = scanner.nextLine();
            if (Pattern.matches("\\s*menu\\s+exit\\s*", input))
                return;

            else if (Pattern.matches("\\s*menu\\s+show-current\\s*", input))
                System.out.println("Play Game");

            else if (input.startsWith("play game") && (input.contains("-p1") || input.contains("--player1"))) {

                Request request = new Request();
                request.setMenu("game menu");
                request.setAction("play game");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("input", input);
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
                if (response.getStatusCode().equals("200")) {
                    String[] usernames = new Gson().fromJson(response.getParameters().get("usernames"), String[].class);
                    startGame(usernames, scanner, username);
                }
            }

            else
                System.out.println("invalid command");
        }
    }

    // start game
    private void startGame(String[] usernames, Scanner scanner, String username2) {
        System.out.println("game started successfully!");
        Request request = new Request();
        request.setMenu("play game");
        request.setAction("set players");
        HashMap<String, String> parameters = new HashMap<>();
        int i = 1;
        for (String username : usernames) {
            parameters.put("username" + i, username);
            i++;
        }
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());

        new PlayGame().run(scanner, username2);
    }
    // get the usernames from input
/*

    private ArrayList<Technology> better() {
        ArrayList<Technology> technologies = new ArrayList<>();
        ArrayList<Improvement> improvements = new ArrayList<>();
        ArrayList<Resource> resources = new ArrayList<>();
        ArrayList<Unit> units = new ArrayList<>();
        ArrayList<String> land = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<String> requirements = new ArrayList<>();
        // ancient era
        // agriculture
        resources.add(new Resource("Wheat", "Bonus", "Farm", 0, 1, 0, 0));
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        improvements.add(new Improvement("Farm", 0, 1, 0, 5, resources, land));
        technologies.add(new Technology("Agriculture", improvements, 0, 20, null, null));
        //pottery
        buildings.add(new Building("Granary", 1, 100, 2, 0, 0, 0, 0, null));
        technologies.add(new Technology("Pottery", null, 1, 35, null, buildings));
        //animal husbandry
        improvements = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(new Resource("Sheep", "Bonus", "Pasture", 0, 2, 0, 0));
        resources.add(new Resource("Horse", "Strategic", "Pasture", 0, 0, 1, 0));
        resources.add(new Resource("Cow", "Bonus", "Pasture", 0, 1, 0, 0));
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Tundra");
        land.add("Hill");
        improvements.add(new Improvement("Pasture", 1, 0, 0, 5, resources, land));
        technologies.add(new Technology("Animal Husbandry", improvements, 2, 35, null, null));
        //archery
        units.add(new RangeMilitaryUnit("archer", null, 100, 240, 60, 1, 2, 15, 25, null, 20, 1));
        technologies.add(new Technology("Archery", null, 3, 35, units, null));
        //mining
        improvements = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(new Resource("Coal", "Strategic", "Mine", 0, 0, 1, 0));
        resources.add(new Resource("Iron", "Strategic", "Mine", 0, 0, 1, 0));
        resources.add(new Resource("Gemstones", "Luxury", "Mine", 3, 0, 0, 5));
        resources.add(new Resource("Gold", "Luxury", "Mine", 2, 0, 0, 4));
        resources.add(new Resource("Silver", "Luxury", "Mine", 2, 0, 0, 3));
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Tundra");
        land.add("Snow");
        land.add("Hill");
        land.add("Forest");
        land.add("Jungle");
        land.add("Marsh");
        improvements.add(new Improvement("Mine", 1, 0, 0, 5, resources, land));
        technologies.add(new Technology("Mining", improvements, 4, 35, null, null));
        // calendar
        improvements = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(new Resource("Banana", "Bonus", "Plantation", 0, 1, 0, 0));
        resources.add(new Resource("Cotton", "Luxury", "Plantation", 2, 0, 0, 3));
        resources.add(new Resource("Fumigation", "Luxury", "Plantation", 2, 0, 0, 2));
        resources.add(new Resource("Silk", "Luxury", "Plantation", 2, 0, 0, 3));
        resources.add(new Resource("Sugar", "Luxury", "Plantation", 2, 0, 0, 2));
        resources.add(new Resource("Color", "Luxury", "Plantation", 2, 0, 0, 2));
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Forest");
        land.add("Jungle");
        land.add("Marsh");
        land.add("FloodPlain");
        improvements.add(new Improvement("Plantation", 0, 0, 2, 5, resources, land));
        technologies.add(new Technology("Calendar", improvements, 5, 70, null, null));
        //writing
        buildings = new ArrayList<>();
        buildings.add(new Building("Library", 1, 80, 0, 0, 0, 1, 0, null));
        technologies.add(new Technology("Writing", null, 6, 55, null, buildings));
        //trapping
        improvements = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(new Resource("Gazelle", "Bonus", "Camp", 0, 1, 0, 0));
        resources.add(new Resource("Fur", "Luxury", "Camp", 2, 0, 0, 3));
        resources.add(new Resource("Ivory", "Luxury", "Camp", 2, 0, 0, 3));
        land = new ArrayList<>();
        land.add("Forest");
        land.add("Tundra");
        land.add("Plain");
        land.add("Hill");
        improvements.add(new Improvement("Camp", 0, 0, 1, 5, resources, land));
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Tundra");
        improvements.add(new Improvement("Trading Post", 0, 0, 1, 5, null, land));
        technologies.add(new Technology("Trapping", improvements, 7, 55, null, null));
        //the wheel
        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Chariot Archer", null, 100, 260, 65, 1, 2, 28, 0, null, 28, 1));
        buildings = new ArrayList<>();
        buildings.add(new Building("Water Mill", 2, 120, 2, 0, 0, 0, 0, null));
        technologies.add(new Technology("The Wheel", null, 8, 55, units, buildings));
        //bronze working
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Spearman", null, 100, 260, 65, 1, 2, 25, 0, null, 25, 1));
        buildings = new ArrayList<>();
        buildings.add(new Building("Barracks", 1, 80, 0, 0, 15, 0, 0, null));
        technologies.add(new Technology("Bronze Working", null, 9, 80, units, buildings));
        //masonry
        improvements = new ArrayList<>();
        resources = new ArrayList<>();
        resources.add(new Resource("Marble", "Luxury", "Quarry", 2, 0, 0, 3));
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Tundra");
        land.add("Hill");
        improvements.add(new Improvement("Quarry", 1, 0, 0, 5, resources, land));
        buildings = new ArrayList<>();
        buildings.add(new Building("Walls", 1, 100, 0, 5, 0, 0, 0, null));
        technologies.add(new Technology("Masonry", improvements, 10, 55, null, buildings));

        //classical era
        // construction
        improvements = new ArrayList<>();
        land = new ArrayList<>();
        land.add("Forest");
        improvements.add(new Improvement("Lumber Mill", 1, 0, 0, 5, null, land));
        buildings = new ArrayList<>();
        buildings.add(new Building("Colosseum", 3, 150, 0, 0, 0, 0, 4, null));
        technologies.add(new Technology("Construction", improvements, 11, 100, null, buildings));
        //horseback riding
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Horseman", null, 100, 320, 80, 1, 4, 36, 0, null, 36, 2));
        buildings = new ArrayList<>();
        buildings.add(new Building("Circus", 3, 150, 0, 0, 0, 0, 3, null));
        buildings.add(new Building("Stable", 1, 100, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Horseback Riding", null, 12, 100, units, buildings));
        //iron working
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Swordsman", null, 100, 360, 90, 1, 2, 35, 0, null, 35, 2));
        buildings = new ArrayList<>();
        requirements.add("Barracks");
        buildings.add(new Building("Armory", 3, 130, 0, 0, 15, 0, 0, requirements));
        technologies.add(new Technology("Iron Working", null, 13, 150, units, buildings));
        //mathematics
        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Catapult", null, 100, 480, 120, 1, 2, 25, 35, null, 25, 2));
        buildings = new ArrayList<>();
        buildings.add(new Building("Courthouse", 5, 200, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Mathematics", null, 14, 100, units, buildings));
        //philosophy
        buildings = new ArrayList<>();
        buildings.add(new Building("Burial Tomb", 0, 120, 0, 0, 0, 0, 2, null));
        requirements = new ArrayList<>();
        requirements.add("Monument");
        buildings.add(new Building("Temple", 2, 120, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Philosophy", null, 15, 100, null, buildings));

        //medieval era
        //chivalry
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Knight", null, 100, 720, 180, 1, 4, 50, 0, null, 50, 3));
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Walls");
        buildings.add(new Building("Castle", 3, 200, 0, 8, 0, 0, 0, requirements));
        technologies.add(new Technology("Chivalry", null, 16, 440, units, buildings));
        //civil service
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Pikeman", null, 100, 800, 200, 1, 2, 45, 0, null, 45, 3));
        technologies.add(new Technology("Civil Service", null, 17, 400, units, null));
        //currency
        buildings = new ArrayList<>();
        buildings.add(new Building("Market", 0, 120, 0, 0, 0, 0, 0, null));
        buildings.add(new Building("Mint", 0, 120, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Currency", null, 18, 250, null, buildings));
        //education
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Library");
        buildings.add(new Building("University", 3, 200, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Education", null, 19, 440, null, buildings));
        //engineering
        improvements = new ArrayList<>();
        land = new ArrayList<>();
        land.add("Plain");
        land.add("Desert");
        land.add("Grassland");
        land.add("Tundra");
        land.add("Snow");
        improvements.add(new Improvement("Factory", 2, 0, 0, 5, null, land));
        technologies.add(new Technology("Engineering", improvements, 20, 250, null, null));
        // machinery
        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Crossbowman", null, 100, 720, 180, 1, 2, 30, 40, null, 30, 3));
        technologies.add(new Technology("Machinery", null, 21, 440, units, null));
        //metal casting
        buildings = new ArrayList<>();
        buildings.add(new Building("Forge", 2, 150, 0, 0, 0, 0, 0, null));
        buildings.add(new Building("Workshop", 2, 100, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Metal Casting", null, 22, 240, null, buildings));
        //physics
        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Trebuchet", null, 100, 800, 200, 1, 2, 35, 45, null, 35, 3));
        technologies.add(new Technology("Physics", null, 23, 440, units, null));
        //steel
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Longswordsman", null, 100, 360, 90, 1, 2, 50, 0, null, 50, 3));
        technologies.add(new Technology("Steel", null, 24, 440, units, null));
        //theology
        buildings = new ArrayList<>();
        buildings.add(new Building("Garden", 2, 120, 0, 0, 0, 0, 0, null));
        buildings.add(new Building("Monastery", 2, 120, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Theology", null, 25, 250, null, buildings));

        //renaissance era
        //acoustics
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Temple");
        requirements.add("Burial Tomb");
        buildings.add(new Building("Opera House", 3, 220, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Acoustics", null, 26, 650, null, buildings));
        //archaeology
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Opera House");
        buildings.add(new Building("Museum", 3, 350, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Archaeology", null, 27, 1300, null, buildings));
        //banking
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Market");
        buildings.add(new Building("Bank", 0, 220, 0, 0, 0, 0, 0, requirements));
        buildings.add(new Building("Satrap's Court", 0, 220, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Banking", null, 28, 650, null, buildings));
        //chemistry

        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Canon", null, 100, 260, 0, 1, 2, 10, 26, null, 0, 1));
        technologies.add(new Technology("Chemistry", null, 29, 900, units, null));
        //economics
        buildings = new ArrayList<>();
        buildings.add(new Building("Windmill", 2, 180, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Economics", null, 30, 900, null, buildings));
        //fertilizer
        technologies.add(new Technology("Fertilizer", null, 31, 1300, null, null));
        // gunpowder
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Musketman", null, 100, 960, 240, 1, 2, 55, 0, null, 55, 4));
        technologies.add(new Technology("Gunpowder", null, 32, 680, units, null));
        //metallurgy
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Lancer", null, 100, 1000, 270, 1, 4, 58, 0, null, 58, 4));
        technologies.add(new Technology("Metallurgy", null, 33, 900, units, null));
        // military science
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Cavalry", null, 100, 1320, 330, 1, 5, 62, 0, null, 62, 5));
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Barracks");
        buildings.add(new Building("Military Academy", 3, 350, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Military Science", null, 34, 1300, units, buildings));
        // printing press
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Colosseum");
        buildings.add(new Building("Theater", 5, 300, 0, 0, 0, 0, 4, requirements));
        technologies.add(new Technology("Printing Press", null, 35, 650, null, buildings));
        // rifling
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Rifleman", null, 100, 1320, 330, 1, 2, 70, 0, null, 70, 5));
        technologies.add(new Technology("Rifling", null, 36, 1425, units, null));
        // scientific theory
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("University");
        buildings.add(new Building("Public School", 3, 350, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Scientific Theory", null, 37, 1300, null, buildings));

        //industrial era
        //biology
        buildings = new ArrayList<>();
        buildings.add(new Building("Hospital", 2, 400, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Biology", null, 38, 1680, null, buildings));
        // combustion
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Tank", null, 100, 1920, 480, 1, 4, 85, 0, null, 85, 6));
        units.add(new MeleeMilitaryUnit("Panzer", null, 100, 1820, 380, 1, 5, 60, 0, null, 60, 5));
        technologies.add(new Technology("Combustion", null, 39, 1250, units, null));
        //dynamite
        units = new ArrayList<>();
        units.add(new RangeMilitaryUnit("Artillery", null, 100, 1720, 430, 1, 2, 60, 80, null, 60, 6));
        technologies.add(new Technology("Dynamite", null, 40, 1900, units, null));
        //electricity
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Bank");
        requirements.add("Strap's Court");
        buildings.add(new Building("Stock Exchange", 0,  650, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Electricity", null, 41, 1900, null, buildings));
        //radio
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Museum");
        buildings.add(new Building("Broadcast Tower", 3, 600, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Radio", null, 42, 2200, null, buildings));
        //railroad
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Military Academy");
        buildings.add(new Building("Arsenal", 3, 350, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Railroad", null, 43, 1900, null, buildings));
        //replaceable parts
        units = new ArrayList<>();
        units.add(new MeleeMilitaryUnit("Infantry", null, 100, 1720, 430, 1, 2, 75, 0, null, 75, 6));
        units.add(new MeleeMilitaryUnit("Anti-Tank Gun", null, 100, 1720, 430, 1, 2, 75, 0, null, 75, 6));
        technologies.add(new Technology("Replaceable Parts", null, 44, 1900, units, null));
        //steam power
        buildings = new ArrayList<>();
        buildings.add(new Building("Factory", 3, 300, 0, 0, 0, 0, 0, null));
        technologies.add(new Technology("Steam Power", null, 45, 1680, null, buildings));
        //telegraph
        buildings = new ArrayList<>();
        requirements = new ArrayList<>();
        requirements.add("Castle");
        buildings.add(new Building("Military Base", 4, 450, 0, 0, 0, 0, 0, requirements));
        technologies.add(new Technology("Telegraph", null, 46, 2200, null, buildings));

        return technologies;
    }

    private int[][] techGraph2() {
        int[][] graph = new int[47][47];
        //0
        for (int i = 0; i < 47; i++)
            graph[i][0] = 0;
        //1
        for (int i = 0; i < 47; i++) {
            if (i == 0) graph[i][1] = 1;
            else graph[i][1] = 0;
        }
        //2
        for (int i = 0; i < 47; i++) {
            if (i == 0) graph[i][2] = 1;
            else graph[i][2] = 0;
        }
        //3
        for (int i = 0; i < 47; i++) {
            if (i == 0) graph[i][3] = 1;
            else graph[i][3] = 0;
        }
        //4
        for (int i = 0; i < 47; i++) {
            if (i == 0) graph[i][4] = 1;
            else graph[i][4] = 0;
        }
        //5
        for (int i = 0; i < 47; i++) {
            if (i == 1) graph[i][5] = 1;
            else graph[i][5] = 0;
        }
        //6
        for (int i = 0; i < 47; i++) {
            if (i == 1) graph[i][6] = 1;
            else graph[i][6] = 0;
        }
        //7
        for (int i = 0; i < 47; i++) {
            if (i == 2) graph[i][7] = 1;
            else graph[i][7] = 0;
        }
        //8
        for (int i = 0; i < 47; i++) {
            if (i == 2) graph[i][8] = 1;
            else graph[i][8] = 0;
        }
        //9
        for (int i = 0; i < 47; i++) {
            if (i == 4) graph[i][9] = 1;
            else graph[i][9] = 0;
        }
        //10
        for (int i = 0; i < 47; i++) {
            if (i == 4) graph[i][10] = 1;
            else graph[i][10] = 0;
        }
        //11
        for (int i = 0; i < 47; i++) {
            if (i == 10) graph[i][11] = 1;
            else graph[i][11] = 0;
        }
        //12
        for (int i = 0; i < 47; i++) {
            if (i == 8) graph[i][12] = 1;
            else graph[i][12] = 0;
        }
        //13
        for (int i = 0; i < 47; i++) {
            if (i == 9) graph[i][13] = 1;
            else graph[i][13] = 0;
        }
        //14
        for (int i = 0; i < 47; i++) {
            if (i == 3 || i == 8) graph[i][14] = 1;
            else graph[i][14] = 0;
        }
        //15
        for (int i = 0; i < 47; i++) {
            if (i == 6) graph[i][15] = 1;
            else graph[i][15] = 0;
        }
        //16
        for (int i = 0; i < 47; i++) {
            if (i == 12 || i == 17 || i == 18) graph[i][16] = 1;
            else graph[i][16] = 0;
        }
        //17
        for (int i = 0; i < 47; i++) {
            if (i == 7 || i == 15) graph[i][17] = 1;
            else graph[i][17] = 0;
        }
        //18
        for (int i = 0; i < 47; i++) {
            if (i == 14) graph[i][18] = 1;
            else graph[i][18] = 0;
        }
        //19
        for (int i = 0; i < 47; i++) {
            if (i == 25) graph[i][19] = 1;
            else graph[i][19] = 0;
        }
        //20
        for (int i = 0; i < 47; i++) {
            if (i == 11 || i == 14) graph[i][20] = 1;
            else graph[i][20] = 0;
        }
        //21
        for (int i = 0; i < 47; i++) {
            if (i == 20) graph[i][21] = 1;
            else graph[i][21] = 0;
        }
        //22
        for (int i = 0; i < 47; i++) {
            if (i == 13) graph[i][22] = 1;
            else graph[i][22] = 0;
        }
        //23
        for (int i = 0; i < 47; i++) {
            if (i == 20 || i == 22) graph[i][23] = 1;
            else graph[i][23] = 0;
        }
        //24
        for (int i = 0; i < 47; i++) {
            if (i == 22) graph[i][24] = 1;
            else graph[i][24] = 0;
        }
        //25
        for (int i = 0; i < 47; i++) {
            if (i == 5 || i == 15) graph[i][25] = 1;
            else graph[i][25] = 0;
        }
        //26
        for (int i = 0; i < 47; i++) {
            if (i == 19) graph[i][26] = 1;
            else graph[i][26] = 0;
        }
        //27
        for (int i = 0; i < 47; i++) {
            if (i == 26) graph[i][27] = 1;
            else graph[i][27] = 0;
        }
        //28
        for (int i = 0; i < 47; i++) {
            if (i == 16 || i == 19) graph[i][28] = 1;
            else graph[i][28] = 0;
        }
        //29
        for (int i = 0; i < 47; i++) {
            if (i == 32) graph[i][29] = 1;
            else graph[i][29] = 0;
        }
        //30
        for (int i = 0; i < 47; i++) {
            if (i == 28 || i == 35) graph[i][30] = 1;
            else graph[i][30] = 0;
        }
        //31
        for (int i = 0; i < 47; i++) {
            if (i == 29) graph[i][31] = 1;
            else graph[i][31] = 0;
        }
        //32
        for (int i = 0; i < 47; i++) {
            if (i == 23 || i == 24) graph[i][32] = 1;
            else graph[i][32] = 0;
        }
        //33
        for (int i = 0; i < 47; i++) {
            if (i == 32) graph[i][33] = 1;
            else graph[i][33] = 0;
        }
        //34
        for (int i = 0; i < 47; i++) {
            if (i == 29 || i == 30) graph[i][34] = 1;
            else graph[i][34] = 0;
        }
        //35
        for (int i = 0; i < 47; i++) {
            if (i == 21 || i == 23) graph[i][35] = 1;
            else graph[i][35] = 0;
        }
        //36
        for (int i = 0; i < 47; i++) {
            if (i == 33) graph[i][36] = 1;
            else graph[i][36] = 0;
        }
        //37
        for (int i = 0; i < 47; i++) {
            if (i == 26) graph[i][37] = 1;
            else graph[i][37] = 0;
        }
        //38
        for (int i = 0; i < 47; i++) {
            if (i == 27 || i == 37) graph[i][38] = 1;
            else graph[i][38] = 0;
        }
        //39
        for (int i = 0; i < 47; i++) {
            if (i == 40 || i == 43 || i == 44) graph[i][39] = 1;
            else graph[i][39] = 0;
        }
        //40
        for (int i = 0; i < 47; i++) {
            if (i == 31 || i == 36) graph[i][40] = 1;
            else graph[i][40] = 0;
        }
        //41
        for (int i = 0; i < 47; i++) {
            if (i == 38 || i == 45) graph[i][41] = 1;
            else graph[i][41] = 0;
        }
        //42
        for (int i = 0; i < 47; i++) {
            if (i == 41) graph[i][42] = 1;
            else graph[i][42] = 0;
        }
        //43
        for (int i = 0; i < 47; i++) {
            if (i == 45) graph[i][43] = 1;
            else graph[i][43] = 0;
        }
        //44
        for (int i = 0; i < 47; i++) {
            if (i == 45) graph[i][44] = 1;
            else graph[i][44] = 0;
        }
        //45
        for (int i = 0; i < 47; i++) {
            if (i == 34 || i == 37) graph[i][45] = 1;
            else graph[i][45] = 0;
        }
        //46
        for (int i = 0; i < 47; i++) {
            if (i == 41) graph[i][46] = 1;
            else graph[i][46] = 0;
        }
        return graph;
    }

 */
}