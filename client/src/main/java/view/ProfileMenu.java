package view;


import controller.NetworkController;

import model.Request;
import model.Response;
import view.enums.Colors;
import view.enums.RegexEnums;
import model.User;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu {
    public ProfileMenu() {
    }

    //provide some information for user
    private void manProfileMenu(String username) {
        //String userColor = new ColorsController().getColorOfUser(user);
        String boldColor = Colors.YELLOW_BOLD;
        String color = Colors.CYAN;
        System.out.println("welcome to Profile Menu dear "  + username + Colors.RESET + "!");
        System.out.println(boldColor + "to change nickname :" + Colors.RESET);
        System.out.println(color + "profile change --nickname <new nickname>");
        System.out.println("profile change -n <new nickname" + Colors.RESET);
        System.out.println(boldColor + "to change password :" + Colors.RESET);
        System.out.println(color + "profile change --password --current <current password> --new <new password>");
        System.out.println("profile change -p -c <current password> -n <new password>" + Colors.RESET);
    }
    public void run(String username, Scanner scanner) {
        String input;
        Matcher matcher;
        manProfileMenu(username);
        Request request;
        while (true) {
            request = new Request();
            request.setMenu("profile menu");
            HashMap<String, String> parameters = new HashMap<>();

            input = scanner.nextLine();
            //exit
            if (Pattern.matches("\\s*menu\\s+exit\\s*", input))
                return;
                //show current menu
            else if (Pattern.matches("\\s*menu\\s+show-current\\s*", input))
                System.out.println("Profile");
                //change the nickname
            else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_NICKNAME1)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_NICKNAME2)) != null) {
                String nickname = matcher.group("nickname");

                parameters.put("new nickname", nickname);
                parameters.put("username", username);
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
            }
            //change the password
            else if ((matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_PASSWORD1)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_PASSWORD2)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_PASSWORD3)) != null ||
                    (matcher = RegexEnums.getMatcher(input, RegexEnums.CHANGE_PASSWORD4)) != null) {
                String currentPassword = matcher.group("currentPassword");
                String newPassword = matcher.group("newPassword");
                parameters.put("username", username);
                parameters.put("new password", newPassword);
                parameters.put("old password", currentPassword);
                request.setParameters(parameters);
                Response response = NetworkController.getInstance().sendRequest(request);
                System.out.println(response.getMessage());
            }
        }
    }
}
