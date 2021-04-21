package TradingSystem.Client;

import TradingSystem.Server.ServiceLayer.DummyObject.*;
import org.json.JSONObject;

import java.util.List;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class Client {

    public int getUserID() {
        return userID;
    }

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

    public void clearSystem() {
        String path = "clear_system";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
    }

    /**
     * @requirement 2.1
     *
     *
     */
    public void connectSystem() {
        String path = "home";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        if(!response.returnConnID().equals("") && !response.getIsErr()) { //because its guest
            this.connID = response.returnConnID();
        } else {
            System.out.println(errMsgGenerator("Client", "Client", "38", "connect system error"));
        }
    }

    /**
     * @requirement 2.2
     * */
    public void exitSystem() {
        String path = "exit";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        if(!response.getIsErr()) { //todo: shut down system
            this.connID = "";
        } else {
            System.out.println(errMsgGenerator("Client", "Client", "48", "exit system error"));
        }
    }

    /**
     * @requirement 2.3
     * @param userName userName
     * @param pass pass
     * @return int if ok
     */
    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, dummyUser.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Register) response: " + response + ANSI_RESET);
        this.userID = response.returnUserID();
 //       this.userID = -1;
        this.connID = response.returnConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }

    /**
     * @requirement 2.4
     * @param userName userName
     * @param pass pass
     * @return int if ok
     */
    public int Login(String userName, String pass){
        String path = "login" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, dummyUser.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Login) response: " + response + ANSI_RESET);
        this.userID = response.returnUserID();
        this.connID = response.returnConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }

     /**
     * @requirement 2.5
     * @return int userID
     */
    public List<DummyStore> showAllStores() {
        String path = "stores";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        List<DummyStore> dummySearchResponeArr = response.returnStoreList();
        return dummySearchResponeArr;
    }
    public List<DummyProduct> showStoreProducts(int storeID) {
        String path = String.format("store/%s/products", storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        return dummyProductResponeArr;
    }

    /**
     * @requirement 2.6
     * @param mode mode
     * @param name name
     * @param minPrice minPrice
     * @param maxPrice maxPrice
     * @param p_rank p_rank
     * @param s_rank s_rank
     * @return List<DummyProduct>
     */
    public List<DummyProduct> Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank) {

        String path = "search";
        double min = Double.parseDouble(minPrice);
        double max = Double.parseDouble(maxPrice);
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
        JSONObject jsonArray = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, jsonSearch.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonArray);
        List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        System.out.println(ANSI_YELLOW + "(Search) response: " + dummyProductResponeArr + ANSI_RESET);
//        this.userID = response.getUserID();
//        this.connID = response.getConnID();
        return dummyProductResponeArr;
    }


    /**
     * @requirement 2.7
     * @param storeID  storeID
     * @param productID productID
     * @param quantity quantity
     * @return response
     */
    public Response addProductToCart(int storeID, int productID, int quantity) {
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
        if(response.getIsErr())
            System.out.println(errMsgGenerator("Client", "Client", "130", response.getMessage()));
        return response;
    }


    public List<DummyProduct> showShoopingCart() {
        String path = "shopping_cart";
        JSONObject jsonArray = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonArray);
        List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        return dummyProductResponeArr;
    }
    public boolean guestPurchase(String name, String credit_number, String phone_number, String address) {
        String path = "shopping_cart/purchase";
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("name", name);
            jsonPost.put("credit_number", credit_number);
            jsonPost.put("phone_number", phone_number);
            jsonPost.put("address", address);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "157", "Error: guestPurchase, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(guestPurchase) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }

    //Subscriber

    /**
     * @requirement 3.1
     *
     * @return String userID
     */
    public Response Logout(){
        String path = String.format("%s/logout",this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseSubscriber + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Logout) response: " + response + ANSI_RESET);
        this.userID = -1;
        this.connID = response.returnConnID();
        return response;
    }
    public boolean openStore(String storeName){
        String path = String.format("%s/add_store", this.userID);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber + path, storeName, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(openStore) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public List<DummyProduct> showUserHistory() {
        String path = String.format("%s/user_history", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseSubscriber + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        System.out.println(ANSI_YELLOW + "(showUserHistory) response: " + dummyProductResponeArr + ANSI_RESET);
        return dummyProductResponeArr;
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
        return response.getIsErr();
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
        return response.getIsErr();
    }

    //Store Owner Service
    public boolean addProduct(int storeID, String productName, String category, double price, int quantity) {
        String path = String.format("%s/store/%s/add_new_product", this.userID, storeID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("productName", productName);
            jsonPost.put("category", category);
            jsonPost.put("price", price);
            jsonPost.put("quantity", quantity);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "193", "Error: addProduct, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addProduct) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public boolean removeProduct(int storeID, int productID) {
        String path = String.format("%s/store/%s/remove_product/%s", this.userID, storeID, productID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeProduce) response: " + response + ANSI_RESET);
        return response.getIsErr();
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
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(editProduct) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public List<DummyProduct> showStoreHistory(int storeID) {
        String path = String.format("%s/store_history/%s", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        System.out.println(ANSI_YELLOW + "(removeProduce) response: " + dummyProductResponeArr + ANSI_RESET);
        return dummyProductResponeArr;
    }
    public boolean addOwner(int storeID, int newOwnerID) {
        String path = String.format("%s/store/%s/add_new_owner/%s", this.userID, storeID, newOwnerID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addOwner) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public boolean addManager(int storeID, int newManagerID){
        String path = String.format("%s/store/%s/add_new_manager/%s", this.userID, storeID, newManagerID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addManager) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public boolean removeManager(int storeID, int managerToRemoveID){
        String path = String.format("%s/store/%s/remove_manager/%s", this.userID, storeID, managerToRemoveID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeManager) response: " + response + ANSI_RESET);
        return response.getIsErr();
    }
    public List<DummyStore> showOwnerStores() {
        String path = String.format("%s/stores", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        List<DummyStore> dummySearchResponeArr = response.returnStoreList();
        return dummySearchResponeArr;
    }


    //Admin
    public int showAllUsersHistory() {
        return 0;
    }

}
