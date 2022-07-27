import controller.NetworkController;
import view.RegisterMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        NetworkController.getInstance().initialize();
        Scanner scanner = new Scanner(System.in);
        new RegisterMenu().run(scanner);
    }
}
