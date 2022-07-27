package controller;

import model.*;


import java.util.ArrayList;
import java.util.Scanner;

public class TechController {
    private final ArrayList<Technology> technologies;
    private final int[][] technologiesGraph;

    private static TechController techController;

    private TechController(int[][] technologiesGraph, ArrayList<Technology> technologies) {
        this.technologiesGraph = technologiesGraph;
        this.technologies = technologies;

    }

    public static TechController getInstance(int[][] technologiesGraph, ArrayList<Technology> technologies) {
        if (techController == null)
            techController = new TechController(technologiesGraph, technologies);
        return techController;
    }

    public ArrayList<Technology> getTechnologies() {
        return technologies;
    }

//    public int[][] getAncientEraTechnologiesGraph() {
//        return ancientEraTechnologiesGraph;
//    }

    public ArrayList<Technology> getPrerequisitesTech(Technology technology) {
        int index = technology.getIndex();
        ArrayList<Technology> prerequisites = new ArrayList<>();
        for (int i = 0; i < technologies.size(); i++) {
            if (technologiesGraph[i][index] == 1) {
                prerequisites.add(technologies.get(i));
            }
        }
        return prerequisites;
    }

    public ArrayList<Technology> getUserResearches(User user) {
        ArrayList<Technology> userTechnologies = user.getTechnologies();
        ArrayList<Technology> prerequisites;
        boolean treeTech = true;
        for (Technology ancientTechnology : technologies) {
            // first check user do not have this
            if (!userTechnologies.contains(ancientTechnology)) {
                prerequisites = getPrerequisitesTech(ancientTechnology);
                if (prerequisites != null) {
                    // check if all prerequisites are exists in user technologies
                    for (int i = 0; i < prerequisites.size(); i++) {
                        if (!userTechnologies.contains(prerequisites.get(i))) {
                            treeTech = false;
                            i = prerequisites.size();
                        }
                    }
                }
                if (treeTech)
                    technologies.add(ancientTechnology);
                treeTech = true;
            }
        }
        return technologies;
    }

    public Response showTree() {

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        ArrayList<Technology> prerequisites;
        notifications.add("**********");
        for (Technology ancientTechnology : getTechnologies()) {
            notifications.add(ancientTechnology.getName() + " :");
            if (ancientTechnology.getGivenUnits() != null) {
                notifications.add("given units :");
                for (Unit givenUnit : ancientTechnology.getGivenUnits())
                    notifications.add(givenUnit.getName());

            }
            if (ancientTechnology.getGivenImprovement() != null) {
                notifications.add("given improvements :");
                for (Improvement improvement : ancientTechnology.getGivenImprovement())
                    notifications.add(improvement.getName());

            }
            prerequisites = getPrerequisitesTech(ancientTechnology);
            if (prerequisites.size() >= 1) {
                notifications.add("prerequisites :");
                for (Technology prerequisite : prerequisites)
                    notifications.add(prerequisite.getName());

            }
            else
                notifications.add("this technology do not have any prerequisites");
            notifications.add("**********");
        }
        response.setNotifications(notifications);
        return response;
    }

    public Response addResearch(Request request) {
        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        boolean cheat = Boolean.parseBoolean(request.getParameters().get("cheat"));
        int indexOfTech = Integer.parseInt(request.getParameters().get("index of technology"));
        if (cheat) {
            user.setResearchTurnLeft(1);
            user.setResearching(true);
            user.setCurrentTechnology(technologies.get(indexOfTech - 1));
            GameController.getInstance().userTurnResearch(user);
        }
        else {
            user.setResearching(true);
            user.setCurrentTechnology(technologies.get(indexOfTech - 1));
            user.setResearchTurnLeft(technologies.get(indexOfTech - 1).getSciencePrice());
        }
        Response response = new Response();
        response.setMessage("successfully!");
        return response;
    }

    public Response selectTech(Request request) {
        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        ArrayList<String> notifications = new ArrayList<>();
        ArrayList<Technology> technologies = techController.getUserResearches(user);
        int index = 1;
        for (Technology technology : technologies) {
            notifications.add(index + "- " + technology.getName());
            if (technology.getGivenImprovement() != null) {
                notifications.add("given improvements : ");
                for (Improvement improvement : technology.getGivenImprovement())
                    notifications.add("name: " + improvement.getName() + " | production: " + improvement.getProductionRate() + " | food: " + improvement.getFoodRate() + " | gold: " + improvement.getGoldRate());

            }
            index++;
        }
        Response response = new Response();
        response.setNotifications(notifications);
        return response;
    }

}
