package view;

import controller.NetworkController;
import model.*;
import view.enums.RegexEnums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResearchMenu {

    private Matcher matcher;


    public void run(Scanner scanner, String username) {
        System.out.println("welcome to research panel dear " + username);
        System.out.println("to see technology tree press -show tree of technologies-");
        System.out.println("to research on a technology press -select technology-");
        System.out.println("cheat code in research bar is -add (--research | -r) <index> -");
        System.out.println("to terminate current tech press -terminate current-");
        String researchInput;

        System.out.println("player has done this technologies");
        for (Technology technology : user.getTechnologies()) {
            System.out.println(technology.getName());
        }

        while (true) {
            researchInput = scanner.nextLine();
            if (researchInput.equals("research exit"))
                return;
            else if (researchInput.equals("select technology")) {
                selectTech(username, scanner);
            }
            else if (researchInput.equals("show tree of technologies")) {
                Request request = new Request();
                request.setMenu("technology menu");
                request.setAction("show tree");
                Response response = NetworkController.getInstance().sendRequest(request);
                for (String notification : response.getNotifications()) {
                    System.out.println(notification);
                }
            }
            else if (researchInput.equals("pause current")) {

            }
            else if (researchInput.equals("resume current")) {

            }
            else
                System.out.println("invalid command");
        }
    }

    public void selectTech(String username, Scanner scanner) {

        ArrayList<Technology> technologies = techController.getUserResearches(user);
        int index = 1;
        for (Technology technology : technologies) {
            System.out.println(index + "- " + technology.getName());
            if (technology.getGivenImprovement() != null) {
                System.out.println("given improvements : ");
                for (Improvement improvement : technology.getGivenImprovement())
                    System.out.println("name: " + improvement.getName() + " | production: " + improvement.getProductionRate() + " | food: " + improvement.getFoodRate() + " | gold: " + improvement.getGoldRate());

            }
            index++;
        }
        System.out.println("choose an index | <tech exit> to get out");
        String techInput;
        boolean researchBar = true;
        while (researchBar) {
            techInput = scanner.nextLine();
            if (techInput.trim().equals("bar exit"))
                researchBar = false;
            else if (Pattern.matches("[\\d+]", techInput)) {
                index = Integer.parseInt(techInput);
                if (index >= 1 && index <= technologies.size()) {
                    // choose the tech and research on it
                    addResearch(username, index, false);
                }
                else
                    System.out.println("invalid number");
                researchBar = false;
            }
            // cheat code
            else if ((matcher = RegexEnums.getMatcher(techInput, RegexEnums.ADD_RESEARCH1)) != null || (matcher = RegexEnums.getMatcher(techInput, RegexEnums.ADD_RESEARCH2)) != null) {
                index = Integer.parseInt(matcher.group("index"));
                if (index >= 1 && index <= technologies.size()) {
                    addResearch(username, index, true);
                    researchBar = false;
                }
                else
                    System.out.println("invalid number");
            }
            else if (techInput.equals("terminate current")) {

            }
            else
                System.out.println("invalid command");
        }
    }

    private void addResearch(String username, int index, boolean cheat) {
        Request request = new Request();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("index", String.valueOf(index));
        parameters.put("cheat", String.valueOf(cheat));
        request.setParameters(parameters);
        Response response = NetworkController.getInstance().sendRequest(request);
        System.out.println(response.getMessage());
    }

}
