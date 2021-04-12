package TradingSystem.Client;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class Client {

    private int userID = -1;
    private String connID = "";
    private String userName;
    private String pass;

    public Client() {
    }
    public String getUserName() {
        return userName;
    }
    public boolean isSubscriber() {
        return this.userID != -1 && !this.connID.equals("");
    }
    public boolean isOwner() {
//        Todo: ADD OWNER FIELD
        return true;
    }
    public String getConnID() {
        return connID;
    }

    //Guest
    public String connectSystem() {
        String path = "home";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        if(response.getUserID() == -1 && !response.getConnID().equals("") && !response.isErr()) { //because its guest
            this.connID = response.getConnID();
        } else {
            System.out.println(errMsgGenerator("Client", "Client", "38", "connect system error"));
        }
        return this.connID;
    }
    public String exitSystem() {
        String path = "exit";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        if(!response.isErr()) { //todo: shut down system
            this.connID = "";
        } else {
            System.out.println(errMsgGenerator("Client", "Client", "48", "exit system error"));
        }
        return this.connID;
    }
    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, dummyUser.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Register) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
 //       this.userID = -1;
        this.connID = response.getConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }
    public int Login(String userName, String pass){
        String path = "login" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, dummyUser.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Login) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
        this.connID = response.getConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }
    public ArrayList<DummyProduct> Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank) {
        String path = "search";
        int min = Integer.parseInt(minPrice);
        int max = Integer.parseInt(maxPrice);
        int pRank = Integer.parseInt(p_rank);
        int sRank = Integer.parseInt(s_rank);
        JSONObject jsonSearch = new JSONObject();
        try {
            if(mode.equals("Product Name")) { //Product Name || Product Category
                jsonSearch.put("Product Name", true); //Boolean
                jsonSearch.put("Product Category", false); //Boolean
            } else {
                jsonSearch.put("Product Name", false); //Boolean
                jsonSearch.put("Product Category", true); //Boolean
            }
            jsonSearch.put("name", name);
            jsonSearch.put("minPrice", min); //"" || number //int
            jsonSearch.put("maxPrice", max); //"" || number //int
            jsonSearch.put("pRank", pRank); //"" || number 1-5 //int
            jsonSearch.put("sRank", sRank); //"" || number 1-5  //int
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "72", "Error in making serach JSON"));
        }
        JSONArray jsonArray = HttpRequest.sendPOSTGETRequestArr(urlbaseGuest + path, jsonSearch.toString(), this.connID);
        ArrayList<DummyProduct> dummyProductResponeArr = DummyProduct.makeDummySearchFromJSON(jsonArray);
        System.out.println(ANSI_YELLOW + "(Search) response: " + dummyProductResponeArr + ANSI_RESET);
//        this.userID = response.getUserID();
//        this.connID = response.getConnID();
        return dummyProductResponeArr;
    }
    public ArrayList<DummyStore> showAllStores() {
        String path = "stores";
        JSONArray jsonArray = HttpRequest.sendGetRequestArr(urlbaseGuest+path, this.connID);
        ArrayList<DummyStore> dummySearchResponeArr = DummyStore.makeDummyStoreFromJSON(jsonArray);
        return dummySearchResponeArr;
    }
    public ArrayList<DummyProduct> showStoreProducts(int storeID) {
        String path = String.format("store/%s/products", storeID);
        JSONArray jsonArray = HttpRequest.sendGetRequestArr(urlbaseGuest+path, this.connID);
        ArrayList<DummyProduct> dummyProductResponeArr = DummyProduct.makeDummySearchFromJSON(jsonArray);
        return dummyProductResponeArr;
    }
    public void addProductToCart(int storeID, int productID, int quantity) {
        String path = "shopping_cart/add_product" ;
        JSONObject post_data = new JSONObject();
        try {
            post_data.put("storeID", storeID);
            post_data.put("productID", productID);
            post_data.put("quantity", quantity);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "125", "Error: addProductToCart"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, post_data.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        if(response.isErr())
            System.out.println(errMsgGenerator("Client", "Client", "130", response.getMessage()));
    }
    public ArrayList<DummyProduct> showShoopingCart() {
        String path = "shoppint_cart";
        JSONArray jsonArray = HttpRequest.sendGetRequestArr(urlbaseGuest+path, this.connID);
        ArrayList<DummyProduct> dummyProductResponeArr = DummyProduct.makeDummySearchFromJSON(jsonArray);
        return dummyProductResponeArr;
    }
    public boolean guestPurchase(String name, String credit_number, String phone_number, String address) {
        String path = String.format("shopping_cart/purchase", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("name", name);
            jsonPost.put("credit_number", credit_number);
            jsonPost.put("phone_number", phone_number);
            jsonPost.put("address", address);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "157", "Error: guestPurchase, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(guestPurchase) response: " + response + ANSI_RESET);
        return response.isErr();
    }

    //Subscriber
    public int Logout(){
        String path = "logout";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseSubscriber + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Logout) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
        this.connID = response.getConnID();
        return userID;
    }
    public boolean openStore(String storeName){
        String path = String.format("%s/add_store", this.userID);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber + path, storeName, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(openStore) response: " + response + ANSI_RESET);
        return response.isErr();
    }
    public ArrayList<DummyProduct> showUserHistory() {
        String path = String.format("%s/user_history", this.userID);
        JSONArray jsonResponseArr = HttpRequest.sendGetRequestArr(urlbaseSubscriber + path, this.connID);
        ArrayList<DummyProduct> dummyProducts = DummyProduct.makeDummySearchFromJSON(jsonResponseArr);
        System.out.println(ANSI_YELLOW + "(showUserHistory) response: " + dummyProducts + ANSI_RESET);
        return dummyProducts;
    }
    public boolean writeComment(int storeID, int productID, double rate, String review) {
        String path = String.format("%s/write_comment", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("storeID", storeID);
            jsonPost.put("productID", productID);
            jsonPost.put("comment", review);
            jsonPost.put("rate", rate);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "172", "Error: writeComment, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(writeComment) response: " + response + ANSI_RESET);
        return response.isErr();
    }
    public boolean subscriberPurchase(String credit_number, String phone_number, String address) {
        String path = String.format("%s/shopping_cart/purchase", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("credit_number", credit_number);
            jsonPost.put("phone_number", phone_number);
            jsonPost.put("address", address);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "197", "Error: subscriberPurchase, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(subscriberPurchase) response: " + response + ANSI_RESET);
        return response.isErr();
    }

    //Store Owner Service
    public boolean addProduct(int storeID, String productName, String category, double price, int quantity) {
        String path = String.format("%s/store/%s/add_product", this.userID, storeID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("productName", productName);
            jsonPost.put("category", category);
            jsonPost.put("price", price);
            jsonPost.put("quantity", quantity);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "193", "Error: addProduct, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addProduct) response: " + response + ANSI_RESET);
        return response.isErr();
    }
    public boolean removeProduct(int storeID, int productID) {
        String path = String.format("%s/store/%s/remove_product/%s", this.userID, storeID, productID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseSubscriber + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeProduce) response: " + response + ANSI_RESET);
        return response.isErr();
    }
    public boolean editProduct(int storeID, int productID, String productName, String category, double price, int quantity) {
        String path = String.format("%s/store/%s/edit_product/%s", this.userID, storeID, productID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("productName", productName);
            jsonPost.put("category", category);
            jsonPost.put("price", price);
            jsonPost.put("quantity", quantity);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "216", "Error: editProduct, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addProduct) response: " + response + ANSI_RESET);
        return response.isErr();
    }
    public ArrayList<DummyProduct> showStoreHistory(int storeID) {
        String path = String.format("%s/store_history/%s", this.userID, storeID);
        JSONArray jsonResponseArr = HttpRequest.sendGetRequestArr(urlbaseSubscriber + path, this.connID);
        ArrayList<DummyProduct> dummyProducts = DummyProduct.makeDummySearchFromJSON(jsonResponseArr);
        System.out.println(ANSI_YELLOW + "(removeProduce) response: " + dummyProducts + ANSI_RESET);
        return dummyProducts;
    }

    //Admin
    public int showAllUsersHistory() {
        return 0;
    }


    //        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("userID"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
//    private void handleJSON() {
//        JSONArray out = new JSONArray(response.toString());
//        for (int i = 0; i<out.length(); i++)
//        {
//            System.out.println(out.getJSONObject(i).getString("userID"));
//        }
//    }

//    public void getTest1(){
//        String response = HttpRequest.sendGetRequest(urlbase + "test1");
//        System.out.println(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("userID"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
//    }



}
