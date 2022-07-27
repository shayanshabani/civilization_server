package view;

import model.Response;
import model.Tile;
import model.User;
import view.Colors;

import java.util.ArrayList;

public class Notifications {

    public static ArrayList<String> sendNotificationToInvader(User first, User second) {
        ArrayList<String> notification = new ArrayList<>();
        //ArrayList<String> responseNotifications = new ArrayList<>();
        //responseNotifications.add(view.enums.Colors.RED + "NOTICE!!!" + view.enums.Colors.RESET);
        notification.add(Colors.RED + "NOTICE!!!" + Colors.RESET);
        //responseNotifications.add(view.enums.Colors.PURPLE + "Dear " + first.getUsername() + view.enums.Colors.RESET);
        notification.add(Colors.PURPLE + "Dear " + first.getUsername() + Colors.RESET);
        //responseNotifications.add(view.enums.Colors.YELLOW + "you have started a war with " + second.getUsername() + view.enums.Colors.RESET);
        notification.add(Colors.YELLOW + "you have started a war with " + second.getUsername() + Colors.RESET);
        first.addHistoryOfNotification(notification);
        return notification;
    }

    public static ArrayList<String> sendNotificationToDefender(User first, User second) {
        ArrayList<String> notification = new ArrayList<>();
        //ArrayList<String> responseNotifications = new ArrayList<>();
        //responseNotifications.add(view.enums.Colors.RED + "NOTICE!!!" + view.enums.Colors.RESET);
        notification.add(Colors.RED + "NOTICE!!!" + Colors.RESET);
        //responseNotifications.add(view.enums.Colors.PURPLE + "Dear " + second.getUsername() + view.enums.Colors.RESET);
        notification.add(Colors.PURPLE + "Dear " + second.getUsername() + Colors.RESET);
        //responseNotifications.add(view.enums.Colors.YELLOW + "User : " + first.getUsername() + " have attacked you!" + view.enums.Colors.RESET);
        notification.add(Colors.YELLOW + "User : " + first.getUsername() + " have attacked you!" + Colors.RESET);
        second.addHistoryOfNotification(notification);
        return notification;
    }

    public static ArrayList<String> roadNotification(User user, Tile tile) {
        ArrayList<String> notification = new ArrayList<>();
        //System.out.println(view.enums.Colors.RED + "NOTICE!!!" + view.enums.Colors.RESET);
        notification.add(Colors.RED + "NOTICE!!!" + Colors.RESET);
        //System.out.println("tile in coordinate : -x " + tile.getX() + " -y " + tile.getY() + " has a road on it right now!");
        notification.add("tile in coordinate : -x " + tile.getX() + " -y " + tile.getY() + " has a road on it right now!");
        user.addHistoryOfNotification(notification);
        return notification;
    }
}
