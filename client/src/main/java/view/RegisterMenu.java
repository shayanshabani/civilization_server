package view;
import com.sun.tools.javac.Main;

import controller.NetworkController;

import model.Request;
import model.Response;
import view.enums.Colors;
import view.enums.RegexEnums;
import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.*;

public class RegisterMenu {
    private final HashMap<String , Boolean> availableColors = new HashMap<>();
    public RegisterMenu(){
        availableColors.put("white" , true);
        availableColors.put("red" , true);
        availableColors.put("green" , true);
        availableColors.put("yellow" , true);
        availableColors.put("blue" , true);
        availableColors.put("purple" , true);
        availableColors.put("cyan" , true);
    }
    // provide some information for user
    private void manRegisterMenu() {
        String boldColor = Colors.YELLOW_BOLD;
        String Color = Colors.CYAN;
        System.out.println(boldColor + "to create new user :" + Colors.RESET);
        System.out.println(Color + "the order of username and nickname and password is not important");
        System.out.println("user create --username <username> --nickname <nickname> --password <password>");
        System.out.println("user create -u <username> -n <nickname> -p <password>" + Colors.RESET);
        System.out.println("**********************");
        System.out.println(boldColor + "to login :" + Colors.RESET);
        System.out.println(Color + "the order of username and password is not important");
        System.out.println("user login --username <username> --password <password>");
        System.out.println("user login -u <username> -p <password>" + Colors.RESET);
        System.out.println("**********************");
        System.out.println(Color + "press \"menu show-current\" to see the menu you are in");
        System.out.println("press \"menu exit\" to exit the menu" + Colors.RESET);
    }
    public void run(Scanner scanner) {
        String input;
        Matcher matcher;
        manRegisterMenu();
        //ArrayList<User> usersFromJson = UsersController.getInstance().readFromJson();
        //if (usersFromJson != null)
            //UsersController.getInstance().setUsers(usersFromJson);
        while (true) {
            input = scanner.nextLine();
            //exit the game
            if (Pattern.matches("\\s*menu\\s+exit\\s*", input)) {
                //UsersController.getInstance().writeToJson();
                return;
            }
                //show current menu
            else if (Pattern.matches("\\s*menu\\s+show-current\\s*", input))
                System.out.println("Login Menu");
                //register user
            else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER1)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER2)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER3)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER4)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER5)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER6)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER7)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER8)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER9)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER10)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER11)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.REGISTER12)) != null) {
                String username = matcher.group("username");
                String nickname = matcher.group("nickname");
                String password = matcher.group("password");
                Request request = new Request();
                request.setMenu("register menu");
                request.setAction("sign up");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("nickname", nickname);
                parameters.put("password", password);
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
            }
            //login handling
            else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.LOGIN1)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.LOGIN2)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.LOGIN3)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.LOGIN4)) != null) {
                String username = matcher.group("username");
                String password = matcher.group("password");
                Request request = new Request();
                request.setMenu("register menu");
                request.setAction("login");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", username);
                parameters.put("password", password);
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
                if (response.getStatusCode().equals("200"))
                    new MainMenu().run(username, scanner);

            }
            //if the user tried to change the menu
            else if (RegexEnums.getMatcher(input, RegexEnums.CHANGE_MENU) != null)
                System.out.println("please login first");
            else
                System.out.println("invalid command");
        }
    }

    private void printRemainColors(){
        AtomicInteger number = new AtomicInteger(1);
        availableColors.forEach((key , value) -> {
            if (value){
                //String color = new ColorsController().getColorOfString(key);
                //System.out.println(number + "- " + color + key + Colors.RESET);
                number.getAndIncrement();
            }
        });
    }

    private Boolean isSelectedColorValid(String color){
        if (!availableColors.containsKey(color)){
            System.out.println("The selected color is not an available color");
            return false;
        } else if (!availableColors.get(color)){
            System.out.println("The selected color is taken by another user");
            return false;
        } else return true;
    }
}
