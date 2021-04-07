package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.DummySearch;
import TradingSystem.Server.Service_Layer.DummyUser;

import java.util.ArrayList;
import java.util.Scanner;
import static TradingSystem.Server.Service_Layer.Configuration.*;

public class ApiClient {
    private static Scanner sc = new Scanner(System.in);
    public static Client client = new Client();

    public static void main(String[] args) {
//        Client client = new Client();
        boolean system_on = true;
        System.out.println("----Welcome to Trading-System!!----");
        while (system_on)
        {
            if(client.isLogin()){
                System.out.println("Hi " + client.getUserName() + ", choose what you want to do:");
            } else {
                System.out.println("Hi guest, choose what you want to do:");
            }
            int userChoose = HomePage();
            sc = new Scanner(System.in);
            switch (userChoose)
            {
                case 0:
                    System.out.println("Exit");
                    system_on = false;
                    break;
                case 1: {
                    if(!client.isLogin())
                        register();
                    else {
                        System.out.println(errMsgGenerator("Client", "ApiClient", "32", "User is logged in, cant press register"));
                    }
                    break;
                }
                case 2: {
                    if(!client.isLogin())
                        login();
                    else {
                        logout();
                    }
                    break;
                }
                case 3:
                    search();
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

    private static void search() {
        String mode="", minPrice="", maxPrice="", p_rank="", s_rank ="";
        int choose = 0;
        while(choose != 6) {
            sc = new Scanner(System.in);
            System.out.println(ANSI_CYAN + "----------- Search -----------");
            System.out.println("Choose mode type:");
            System.out.println("\t1. By product name");
            System.out.println("\t2. By product category");
            System.out.println("Add filter:");
            System.out.println("\t3. Price range:");
            System.out.println("\t4. Product Rank");
            System.out.println("\t5. Store Rank");
            System.out.println("6. Give me search results...");
            System.out.println("------------------------------" + ANSI_RESET);

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            choose = sc.nextInt();
            sc = new Scanner(System.in);

            switch (choose) {
                case 1: {
                    mode = "Product Name";
                    System.out.println("------------\nYou choose product name mode!\n------------");
                    break;
                }
                case 2: {
                    mode = "Product Category";
                    System.out.println("------------\nYou choose product category mode!\n------------");
                    break;
                }
                case 3: {
                    int min, max;
                    System.out.println("Enter minimum price for you:");
                    minPrice = sc.nextLine();
                    try {
                        min = Integer.parseInt(minPrice);
                    } catch (Exception e) {
                        System.out.println(errMsgGenerator("Client", "APIClient", "105", "Cant parse to int - wrong input"));
                        break;
                    }
                    if(min < 0) System.out.println(errMsgGenerator("Client", "APIClient", "108", "min price cant be negative - wrong input"));


                    System.out.println("Enter maximum price for you:");
                    maxPrice = sc.nextLine();
                    try {
                        max = Integer.parseInt(maxPrice);
                    } catch (Exception e) {
                        System.out.println(errMsgGenerator("Client", "APIClient", "116", "Cant parse to int - wrong input"));
                        break;
                    }
                    if(max < 0) System.out.println(errMsgGenerator("Client", "APIClient", "119", "max price cant be negative - wrong input"));

                    System.out.println("------------\nPrice Range: " + minPrice + "-" + maxPrice + "\n------------");
                    break;
                }
                case 4: {
                    System.out.println("Enter minimum product rank (from 1 to 5):");
                    p_rank = sc.nextLine();
                    while(1 < Integer.parseInt(p_rank) || Integer.parseInt(p_rank) > 5) {
                        sc = new Scanner(System.in);
                        System.out.println("rank should be between 1 to 5!");
                        System.out.println("Enter minimum product rank (from 1 to 5):");
                        p_rank = sc.nextLine();
                    }
                    System.out.println("------------\nRank filter: searching product in rank " + p_rank + " or above\n------------");
                    break;
                }
                case 5: {
                    System.out.println("Enter minimum store rank (from 1 to 5):");
                    s_rank = sc.nextLine();
                    while(1 < Integer.parseInt(s_rank) || Integer.parseInt(s_rank) > 5) {
                        sc = new Scanner(System.in);
                        System.out.println("rank should be between 1 to 5!");
                        System.out.println("Enter minimum product rank (from 1 to 5):");
                        s_rank = sc.nextLine();
                    }
                    System.out.println("------------\nRank filter: searching product in stores in rank " + s_rank + " or above\n------------");
                    break;
                }
                case 6: {
                    System.out.println("searching products for you...");
                    break;
                }
            }
        }

        ArrayList<DummySearch> dummySearches = client.Search(mode, minPrice, maxPrice, p_rank, s_rank);
//        Todo: print to user and ask him things

    }

    private static void logout() {
        client.Logout();
    }

    private static void login() {
        System.out.println("Enter user name:");
        String userName = sc.nextLine();
        System.out.println("Enter password:");
        String pass = sc.nextLine();
        client.Login(userName, pass);
    }

    private static void register() {
        System.out.println("Enter user name:");
        String userName = sc.nextLine();
        System.out.println("Enter password:");
        String pass = sc.nextLine();
        client.Register(userName, pass);
    }

    private static int HomePage() {
        if(!client.isLogin()) {
            System.out.println("1. Register");
            System.out.println(ANSI_BLACK + "2. Login" + ANSI_RESET);
        }
        else
            System.out.println(ANSI_BLACK + "2. Logout" + ANSI_RESET);
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
