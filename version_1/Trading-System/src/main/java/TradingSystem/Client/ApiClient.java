//package TradingSystem.Client;
//
//import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
//import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import static TradingSystem.Server.ServiceLayer.Configuration.*;
//
//public class ApiClient {
//    private static Scanner sc = new Scanner(System.in);
//    public static Client client = new Client();
//
//    public static void main(String[] args) {
////        Client client = new Client();
//        boolean system_on = true;
//        System.out.println("----Welcome to Trading-System!!----");
//        client.connectSystem();
//
//        while (system_on)
//        {
//            if(client.isSubscriber()){
//                System.out.println("Hi " + client.getUserName() + ", choose what you want to do:");
//            } else {
//                System.out.println("Hi guest, choose what you want to do:");
//            }
//            int userChoose = HomePage();
//            sc = new Scanner(System.in);
//            switch (userChoose)
//            {
//                case 0:
//                    System.out.println("Exit");
//                    client.exitSystem();
//                    system_on = false;
//                    break;
//                case 1: {
//                    if(!client.isSubscriber())
//                        register();
//                    else {
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "32", "User is logged in, cant press register"));
//                    }
//                    break;
//                }
//                case 2: {
//                    if(!client.isSubscriber())
//                        login();
//                    else {
//                        client.Logout();
//                    }
//                    break;
//                }
//                case 3:
//                    search();
//                    break;
//                case 4:
//                    showStores();
//                    break;
//                case 5:
//                    addProductToCart();
//                    break;
//                case 6:
//                    showShoppingCart();
//                case 7:
//                    if(client.isSubscriber()) {
//                        openStore();
//                    } else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "67", "Error: openStore, client is not subscriber"));
//                    break;
//                case 8:
//                    if(client.isSubscriber()) {
//                        showUserHistory();
//                    } else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "73", "Error: showUserHistory, client is not subscriber"));
//                    break;
//                case 9:
//                    if(client.isSubscriber()) {
//                        writeComment();
//                    } else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "79", "Error: writeComment, client is not subscriber"));
//                    break;
//                case 10:
//                    if(client.isOwner())
//                        addProduct();
//                    else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "86", "Error: addProduct, client is not owner"));
//                case 11:
//                    if(client.isOwner())
//                        removeProduct();
//                    else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "91", "Error: removeProduct, client is not owner"));
//                case 12:
//                    if(client.isOwner())
//                        editProduct();
//                    else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "96", "Error: editProduct, client is not owner"));
//                case 13:
//                    if(client.isOwner())
//                        showStoreHistory();
//                    else
//                        System.out.println(errMsgGenerator("Client", "ApiClient", "101", "Error: showStoreHistory, client is not owner"));
//                default:
//                    break;
//            }
////            System.out.println("userChoose: " + userChoose);
//        }
//    }
//
//
//    private static int HomePage() {
//        if(!client.isSubscriber()) {
//            System.out.println("\t1. Register");
//            System.out.println(ANSI_BLACK + "\t2. Login" + ANSI_RESET);
//        }
//        else
//            System.out.println(ANSI_BLACK + "\t2. Logout" + ANSI_RESET);
//        System.out.println("\t3. Search products");
//        System.out.println("\t4. Show Stores");
//        System.out.println("\t5. Add product to Shopping cart");
//        System.out.println("\t6. Show Shopping Cart");
//        //only subscriber
//        if(client.isSubscriber()){
//            System.out.println("--- Subscriber options ---");
//            System.out.println("\t7. Open Store");
//            System.out.println("\t8. Show history");
//            System.out.println("\t9. Write writeComment");
//        }
//        if(client.isOwner()) {
//            System.out.println("--- Owner options ---");
//            System.out.println("\t10. Add Product to your store");
//            System.out.println("\t11. Remove Product from your store");
//            System.out.println("\t12. Edit Product from your store");
//            System.out.println("\t13. Show store history");
//        }
//        System.out.println("0. Exit");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        return sc.nextInt();
//    }
//
//    //Guest
//    private static void register() {
//        System.out.println("Enter user name:");
//        String userName = sc.nextLine();
//        System.out.println("Enter password:");
//        String pass = sc.nextLine();
//        client.Register(userName, pass);
//    }
//    private static void login() {
//        System.out.println("Enter user name:");
//        String userName = sc.nextLine();
//        System.out.println("Enter password:");
//        String pass = sc.nextLine();
//        client.Login(userName, pass);
//    }
//    private static void search() {
//        String mode="", minPrice="", maxPrice="", p_rank="", s_rank ="";
//        int choose = 0;
//        String name = "";
//
//        while(choose != 6) {
//            sc = new Scanner(System.in);
//            System.out.println(ANSI_CYAN + "----------- Search -----------");
//            System.out.println("Choose mode type:");
//            System.out.println("\t1. By product name");
//            System.out.println("\t2. By product category");
//            System.out.println("Add filter:");
//            System.out.println("\t3. Price range:");
//            System.out.println("\t4. Product Rank");
//            System.out.println("\t5. Store Rank");
//            System.out.println("6. Give me search results...");
//            System.out.println("------------------------------");
//
//            while (!sc.hasNextInt()) {
//                System.out.println("Invalid input, please try again");
//                sc.next();
//            }
//            choose = sc.nextInt();
//            sc = new Scanner(System.in);
//            switch (choose) {
//                case 1: {
//                    mode = "Product Name";
//                    System.out.println("------------\nYou choose product name mode!\n------------");
//                    System.out.println("Enter product name:");
//                    name = sc.nextLine();
//                    break;
//                }
//                case 2: {
//                    mode = "Product Category";
//                    System.out.println("------------\nYou choose product category mode!\n------------");
//                    System.out.println("Enter product name:");
//                    name = sc.nextLine();
//                    break;
//                }
//                case 3: {
//                    int min, max;
//                    System.out.println("Enter minimum price for you:");
//                    minPrice = sc.nextLine();
//                    try {
//                        min = Integer.parseInt(minPrice);
//                    } catch (Exception e) {
//                        System.out.println(errMsgGenerator("Client", "APIClient", "105", "Cant parse to int - wrong input"));
//                        break;
//                    }
//                    if(min < 0) System.out.println(errMsgGenerator("Client", "APIClient", "108", "min price cant be negative - wrong input"));
//
//
//                    System.out.println("Enter maximum price for you:");
//                    maxPrice = sc.nextLine();
//                    try {
//                        max = Integer.parseInt(maxPrice);
//                    } catch (Exception e) {
//                        System.out.println(errMsgGenerator("Client", "APIClient", "116", "Cant parse to int - wrong input"));
//                        break;
//                    }
//                    if(max < 0) System.out.println(errMsgGenerator("Client", "APIClient", "119", "max price cant be negative - wrong input"));
//
//                    System.out.println("------------\nPrice Range: " + minPrice + "-" + maxPrice + "\n------------");
//                    break;
//                }
//                case 4: {
//                    System.out.println("Enter minimum product rank (from 1 to 5):");
//                    p_rank = sc.nextLine();
//                    while(1 < Integer.parseInt(p_rank) || Integer.parseInt(p_rank) > 5) {
//                        sc = new Scanner(System.in);
//                        System.out.println("rank should be between 1 to 5!");
//                        System.out.println("Enter minimum product rank (from 1 to 5):");
//                        p_rank = sc.nextLine();
//                    }
//                    System.out.println("------------\nRank filter: searching product in rank " + p_rank + " or above\n------------");
//                    break;
//                }
//                case 5: {
//                    System.out.println("Enter minimum store rank (from 1 to 5):");
//                    s_rank = sc.nextLine();
//                    while(1 < Integer.parseInt(s_rank) || Integer.parseInt(s_rank) > 5) {
//                        sc = new Scanner(System.in);
//                        System.out.println("rank should be between 1 to 5!");
//                        System.out.println("Enter minimum product rank (from 1 to 5):");
//                        s_rank = sc.nextLine();
//                    }
//                    System.out.println("------------\nRank filter: searching product in stores in rank " + s_rank + " or above\n------------");
//                    break;
//                }
//                case 6: {
//                    System.out.println("searching products for you..." + ANSI_RESET);
//                    break;
//                }
//            }
//        }
//
//        List<DummyProduct> dummyProducts = client.Search(mode, name, minPrice, maxPrice, p_rank, s_rank);
//
//        System.out.println(ANSI_BLUE + "Search results:");
//        //print to user and ask him things
//        int i = 1;
//        for (DummyProduct dummyProduct : dummyProducts) {
//            System.out.println(i + ") " + dummyProduct);
//            i++;
//        }
//        System.out.println("choose product to buy or store to enter to..." + ANSI_RESET);
//        //todo: maybe add option to buy or see the whole store
//    }
//    private static void showStores() {
//        List<DummyStore> dummyStores = client.showAllStores();
//        System.out.println(ANSI_PURPLE + "---------- Store list: ----------");
//        for(DummyStore dummyStore : dummyStores) {
//            System.out.println(dummyStore);
//        }
//        System.out.println(ANSI_RESET);
//        int storeID = -1;
//        while(storeID != 0) {
//            System.out.println("------------------------\n Choose the storeID you want to enter to, or (0) to back the home page:");
//            while (!sc.hasNextInt()) {
//                System.out.println("Invalid input, please try again");
//                sc.next();
//            }
//            storeID = sc.nextInt();
//            showStoresProduct(storeID);
//        }
//    }
//    private static void showStoresProduct(int storeID) {
//        List<DummyProduct> dummyProducts = client.showStoreProducts(storeID);
//        System.out.println(ANSI_PURPLE + "---------- Product list in store " + storeID + " ----------");
//        for(DummyProduct dummyProduct : dummyProducts) {
//            System.out.println(dummyProduct);
//        }
//        System.out.println("------------------\n" + ANSI_RESET);
//    }
//    private static void addProductToCart() {
//        System.out.println("Yay you want to buy!");
//
//        //StoreID
//        System.out.println("Enter storeID:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        //ProductID
//        System.out.println("Enter productID:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int productID = sc.nextInt();
//
//        //quantity
//        System.out.println("Enter quantity:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int quantity = sc.nextInt();
//
//        client.addProductToCart(storeID, productID, quantity);
//    }
//    private static void showShoppingCart() {
//        List<DummyProduct> shoppingCart = client.showShoppingCart();
//        System.out.println(ANSI_PURPLE + "---------- Shopping List: ----------");
//        for(DummyProduct dummyProduct : shoppingCart) {
//            System.out.println(dummyProduct);
//        }
//        System.out.println(ANSI_RESET);
//        System.out.println("TODO: Press here if you want to pay for your shopping cart");
//    }
//
//    //Subscriber
//    private static void openStore() {
//        System.out.println(ANSI_BLUE + "Enter store name: (store name have to be unique)");
//        String storeName = sc.nextLine();
//        if(!client.openStore(storeName))
//            System.out.println(errMsgGenerator("Client", "ApiClient", "309", "Error: open Store"));
//        else
//            System.out.println("Store " + storeName + " open successfully!");
//        System.out.println(ANSI_RESET);
//    }
//    private static void showUserHistory() {
//        client.showUserHistory();
//    }
//    private static void writeComment() {
//        System.out.println(ANSI_BLUE + "----- Review -----");
//
//        //StoreID
//        System.out.println("Enter store ID:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        //Product ID
//        sc = new Scanner(System.in);
//        System.out.println("Enter product ID:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int productID = sc.nextInt();
//
//        //Rate
//        sc = new Scanner(System.in);
//        System.out.println("Enter rate for the product (1.0 <-> 5.0):");
//        while (!sc.hasNextDouble()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        double rate = sc.nextDouble();
//
//        //Review
//        sc = new Scanner(System.in);
//        System.out.println("Write your writeComment: (-at least 5 and up to 100 characters - use clean language -)");
//        String review = sc.nextLine();
//        while(review.length() < 5 || review.length() > 100) {
//            System.out.println("Invalid input, please try again");
//            sc = new Scanner(System.in);
//            review = sc.nextLine();
//        }
//
//        //---
//        if(!client.writeComment(storeID, productID, rate, review))
//            System.out.println(errMsgGenerator("Client", "ApiClient", "355", "Error: writeComment"));
//        else
//            System.out.println("Review added succefully!" + ANSI_RESET);
//    }
//
//    //Owner
//    private static void addProduct() {
//        System.out.println(ANSI_BLUE + "----- Add product -----");
//
//        //StoreID
//        System.out.println("Enter store ID you want to add product to:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        //productName
//        sc = new Scanner(System.in);
//        System.out.println("Enter product name:");
//        String productName = sc.nextLine();

//        while(productName.length() < 2) {
//            System.out.println("Invalid input, please try again");
//            sc = new Scanner(System.in);
//            productName = sc.nextLine();
//        }
//
//        //Category
//        sc = new Scanner(System.in);
//        System.out.println("Enter product category:");
//        String productCategory = sc.nextLine();
//        while(productCategory.length() < 2) {
//            System.out.println("Invalid input, please try again");
//            sc = new Scanner(System.in);
//            productCategory = sc.nextLine();
//        }
//
//        //Price
//        sc = new Scanner(System.in);
//        System.out.println("Enter product price:");
//        while (!sc.hasNextDouble()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        double price = sc.nextDouble();
//
//        //Quantity
//        sc = new Scanner(System.in);
//        System.out.println("Enter product quantity:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int quantity = sc.nextInt();
//
//        //---
//        if(!client.addProduct(storeID, productName, productCategory, price, quantity))
//            System.out.println(errMsgGenerator("Client", "ApiClient", "436", "Error: add product failed"));
//        else
//            System.out.println("Product added successfully");
//        System.out.println(ANSI_RESET);
//    }
//    private static void removeProduct() {
//        System.out.println(ANSI_BLUE + "----- Remove product -----");
//
//        //StoreID
//        System.out.println("Enter store ID you want to remove product from:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        //ProductID
//        System.out.println("Enter product ID you want to remove:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int productID = sc.nextInt();
//
//        //---
//        if(!client.removeProduct(storeID, productID))
//            System.out.println(errMsgGenerator("Client", "ApiClient", "461", "Error: remove product failed"));
//        else
//            System.out.println("Remove product successfully!");
//        System.out.println(ANSI_RESET);
//    }
//    private static void editProduct() {
//        System.out.println(ANSI_BLUE + "----- Edit product -----");
//
//        //StoreID
//        System.out.println("Enter store ID you want to edit product from:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        //ProductID
//        System.out.println("Enter product ID you want to edit:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int productID = sc.nextInt();
//
//        System.out.println("If you dont want to change one the options, press ENTER or write the origin again..");
//
//        //productName
//        sc = new Scanner(System.in);
//        System.out.println("Enter product name: (new/old/empty)");
//        String productName = sc.nextLine();
////        while(productName.length() < 2) {
////            System.out.println("Invalid input, please try again");
////            sc = new Scanner(System.in);
////            productName = sc.nextLine();
////        }
//
//        //Category
//        sc = new Scanner(System.in);
//        System.out.println("Enter product category: (new/old/empty)");
//        String productCategory = sc.nextLine();
//        while(productCategory.length() < 2) {
//            System.out.println("Invalid input, please try again");
//            sc = new Scanner(System.in);
//            productCategory = sc.nextLine();
//        }
//
//        //Price
//        sc = new Scanner(System.in);
//        System.out.println("Enter product price: (new/old/empty)");
//        while (!sc.hasNextDouble()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        double price = sc.nextDouble();
//
//        //Quantity
//        sc = new Scanner(System.in);
//        System.out.println("Enter product quantity: (new/old/empty)");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int quantity = sc.nextInt();
//
//        //---
//        if(!client.editProduct(storeID, productID, productName, productCategory, price, quantity))
//            System.out.println(errMsgGenerator("Client", "ApiClient", "521", "Error: editProduct failed"));
//        else
//            System.out.println("Product edited successfully");
//        System.out.println(ANSI_RESET);
//    }
//    private static void showStoreHistory() {
//        //StoreID
//        System.out.println("Enter store ID you want to add product to:");
//        while (!sc.hasNextInt()) {
//            System.out.println("Invalid input, please try again");
//            sc.next();
//        }
//        int storeID = sc.nextInt();
//
//        List<DummyProduct> dummyProducts = client.showStoreHistory(storeID);
//
//        System.out.println(ANSI_GREEN + "---- Store " + storeID + " History Results ----");
//        for(DummyProduct dummyProduct : dummyProducts) {
//            System.out.println(dummyProduct);
//        }
//        System.out.println("--------------------------" + ANSI_RESET);
//    }
//
//    //Admin
//    private static void showAllUsersHistory() {}
//}
