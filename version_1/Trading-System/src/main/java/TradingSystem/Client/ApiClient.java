package TradingSystem.Client;

import java.util.Scanner;

public class ApiClient {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Client client = new Client();
        boolean system_on = true;
        System.out.println("----Welcome to Trading-System!!----");
        while (system_on)
        {
            int userChoose = HomePage(client);
            switch (userChoose)
            {
                case 0:
                    System.out.println("Exit");
                    system_on = false;
                    break;
                case 1:
                    client.Register();
                case 2: {
                    if(!client.isLogin())
                        client.Login("Roee", "1234");
                    else
                        client.Logout("Roee", "1234");
                }
                case 3:
                    //search
                case 4:
                    //show stores
                case 5:
                    //show shopping cart


            }
//            System.out.println("userChoose: " + userChoose);

        }


    }

    private static int HomePage(Client client) {
        System.out.println("1. Register");
        if(!client.isLogin())
            System.out.println("2. Login");
        else
            System.out.println("2. Logout");
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
