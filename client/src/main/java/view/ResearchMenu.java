package view;

import view.enums.RegexEnums;
import model.Improvement;
import model.Technology;
import model.Unit;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResearchMenu {

    private Matcher matcher;

//
//    public void run(Scanner scanner, User user) {
//        System.out.println("welcome to research panel dear " + user.getUsername());
//        System.out.println("to see technology tree press -show tree of technologies-");
//        System.out.println("to research on a technology press -select technology-");
//        System.out.println("cheat code in research bar is -add (--research | -r) <index> -");
//        System.out.println("to terminate current tech press -terminate current-");
//        String researchInput;
//        if (user.getTechnologies() != null) {
//            System.out.println("player has done this technologies");
//            for (Technology technology : user.getTechnologies()) {
//                System.out.println(technology.getName());
//            }
//        }
//        while (true) {
//            researchInput = scanner.nextLine();
//            if (researchInput.equals("research exit"))
//                return;
//            else if (researchInput.equals("select technology")) {
//                selectTech(user, scanner);
//            }
//            else if (researchInput.equals("show tree of technologies")) {
//                showTree(user, scanner);
//            }
//            else if (researchInput.equals("pause current")) {
//                if (user.isResearching()) {
//                    user.setResearching(false);
//                }
//                else
//                    System.out.println("you are not researching right now!");
//            }
//            else if (researchInput.equals("resume current")) {
//                if (user.getCurrentTechnology() != null) {
//                    user.setResearching(true);
//                }
//                else
//                    System.out.println("you have not any research in queue");
//            }
//            else
//                System.out.println("invalid command");
//        }
//    }
//
//    public void selectTech(User user, Scanner scanner) {
//
//        ArrayList<Technology> technologies = techController.getUserResearches(user);
//        int index = 1;
//        for (Technology technology : technologies) {
//            System.out.println(index + "- " + technology.getName());
//            if (technology.getGivenImprovement() != null) {
//                System.out.println("given improvements : ");
//                for (Improvement improvement : technology.getGivenImprovement())
//                    System.out.println("name: " + improvement.getName() + " | production: " + improvement.getProductionRate() + " | food: " + improvement.getFoodRate() + " | gold: " + improvement.getGoldRate());
//
//            }
//            index++;
//        }
//        System.out.println("choose an index | <tech exit> to get out");
//        String techInput;
//        boolean researchBar = true;
//        while (researchBar) {
//            techInput = scanner.nextLine();
//            if (techInput.trim().equals("bar exit"))
//                researchBar = false;
//            else if (Pattern.matches("[\\d+]", techInput)) {
//                index = Integer.parseInt(techInput);
//                if (index >= 1 && index <= technologies.size()) {
//                    // choose the tech and research on it
//                    user.setResearching(true);
//                    user.setCurrentTechnology(technologies.get(index - 1));
//                    user.setResearchTurnLeft(technologies.get(index - 1).getSciencePrice());
//                }
//                else
//                    System.out.println("invalid number");
//                researchBar = false;
//            }
//            // cheat code
//            else if ((matcher = RegexEnums.getMatcher(techInput, RegexEnums.ADD_RESEARCH1)) != null || (matcher = RegexEnums.getMatcher(techInput, RegexEnums.ADD_RESEARCH2)) != null) {
//                index = Integer.parseInt(matcher.group("index"));
//                if (index >= 1 && index <= technologies.size()) {
//                    user.setResearchTurnLeft(1);
//                    user.setResearching(true);
//                    user.setCurrentTechnology(technologies.get(index - 1));
//                    gameController.userTurnResearch(user);
//                    researchBar = false;
//                }
//                else
//                    System.out.println("invalid number");
//            }
//            else if (techInput.equals("terminate current")) {
//
//            }
//            else
//                System.out.println("invalid command");
//        }
//    }
//
//    private void showTree(User user, Scanner scanner) {
//        ArrayList<Technology> prerequisites;
//        System.out.println("**********");
//        for (Technology ancientTechnology : techController.getTechnologies()) {
//            System.out.println(ancientTechnology.getName() + " :");
//            if (ancientTechnology.getGivenUnits() != null) {
//                System.out.println("given units :");
//                for (Unit givenUnit : ancientTechnology.getGivenUnits())
//                    System.out.println(givenUnit.getName());
//
//            }
//            if (ancientTechnology.getGivenImprovement() != null) {
//                System.out.println("given improvements :");
//                for (Improvement improvement : ancientTechnology.getGivenImprovement())
//                    System.out.println(improvement.getName());
//
//            }
//            prerequisites = techController.getPrerequisitesTech(ancientTechnology);
//            if (prerequisites.size() >= 1) {
//                System.out.println("prerequisites :");
//                for (Technology prerequisite : prerequisites)
//                    System.out.println(prerequisite.getName());
//
//            }
//            else
//                System.out.println("this technology do not have any prerequisites");
//            System.out.println("**********");
//        }
//    }
}
