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
            System.out.println("1. Register");
//            System.out.println("2. Add new store");
//            System.out.println("3. Nevermind...");
            System.out.println("0. Exit");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            int userChoose = sc.nextInt();
            switch (userChoose)
            {
                case 0:
                    System.out.println("Exit");
                    system_on = false;
                    break;
                case 1:
                    client.Register();


            }
//            System.out.println("userChoose: " + userChoose);

        }


    }
}
