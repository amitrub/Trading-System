package TradingSystem.Client;

import java.util.Scanner;

public class ApiClient {
    private static Scanner sc = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        Client client = new Client();
        boolean system_on = true;
        System.out.println("----Welcome to Trading-System!!----");
        while (system_on)
        {
            if(client.isLogin()){
                System.out.println("Hi " + client.getUserName() + ", choose what you want to do:");
            } else {
                System.out.println("Hi guest, choose what you want to do:");
            }
            int userChoose = HomePage(client);
            sc = new Scanner(System.in);
            switch (userChoose)
            {
                case 0:
                    System.out.println("Exit");
                    system_on = false;
                    break;
                case 1: {
                    register(client);
                    break;
                }
                case 2: {
                    if(!client.isLogin())
                        login(client);
                    else {
                        logout(client);
                    }
                    break;
                }
                case 3:
                    //search
                    break;
                case 4:
                    //show stores
                    break;
                case 5:
                    //show shopping cart
                    break;
                default:
                    break;
            }
//            System.out.println("userChoose: " + userChoose);

        }


    }

    private static void logout(Client client) {
        client.Logout();
    }

    private static void login(Client client) {
        System.out.println("Enter user name:");
        String userName = sc.nextLine();
        System.out.println("Enter password:");
        String pass = sc.nextLine();
        client.Login(userName, pass);
    }

    private static void register(Client client) {
        System.out.println("Enter user name:");
        String userName = sc.nextLine();
        System.out.println("Enter password:");
        String pass = sc.nextLine();
        client.Register(userName, pass);
    }

    private static int HomePage(Client client) {
        System.out.println("1. Register");
        if(!client.isLogin())
            System.out.println(ANSI_BLACK + "2. Login / " + ANSI_RESET + ANSI_YELLOW + "Logout" + ANSI_RESET);
        else
            System.out.println(ANSI_BLACK + "2. Logout / " + ANSI_RESET + ANSI_YELLOW + "Login" + ANSI_RESET);
        System.out.println("3. Search products");
        System.out.println("4. Show Stores");
        System.out.println("5. Show shopping cart");

        //only subscriber
        if(client.isLogin()){
            System.out.println("--- Subscriber options ---");
            System.out.println("6. Open Store");
            System.out.println("7. Show history");
            System.out.println("8. Write comment");
            System.out.println("9. Show store history");
        }
        System.out.println("0. Exit");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        return sc.nextInt();
    }
}
