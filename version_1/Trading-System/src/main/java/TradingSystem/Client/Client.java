package TradingSystem.Client;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.*;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class Client implements Client_Interface {

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

    public void connectSystemNew(int port) {
        String path = "home";
        String ip = "localhost";
        String urlbaseGuest = "http://localhost:"+port+"/app/";
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
    public Response showAllStores() {
        String path = "stores";
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }
    public Response showStoreProducts(int storeID) {
        String path = String.format("store/%s/products", storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
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
    public Response Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank) {

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
        //List<DummyProduct> dummyProductResponeArr = response.returnProductList();
        System.out.println(ANSI_YELLOW + "(Search) response: " + response.returnProductList() + ANSI_RESET);
//        this.userID = response.getUserID();
//        this.connID = response.getConnID();
        //return dummyProductResponeArr;
        return response;
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
        System.out.println(ANSI_YELLOW + "(addProductToCart) response: " + response + ANSI_RESET);
        if(response.getIsErr())
            System.out.println(errMsgGenerator("Client", "Client", "130", response.getMessage()));
        return response;
    }

    /**
     * @requirement 2.8
     * @return List<DummyProduct>
     */
    public Response showShoppingCart() {
        String path = "shopping_cart";
        JSONObject jsonArray = HttpRequest.sendGetRequest(urlbaseGuest+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonArray);
        System.out.println(ANSI_YELLOW + "(addProductToCart) response: " + response + ANSI_RESET);
        return response;
    }
    public Response removeFromShoppingCart(int storeID, int productID) {
        String path = "shopping_cart/remove_product";
        JSONObject post_data = new JSONObject();
        try {
            post_data.put("storeID", storeID);
            post_data.put("productID", productID);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "222", "Error: removeFromShoppingCart"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest+path, post_data.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeFromShoppingCart) response: " + response + ANSI_RESET);
        return response;
    }
    public Response editShoppingCart(int storeID, int productID, int quantity) {
        String path = "shopping_cart/edit_product";
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
        System.out.println(ANSI_YELLOW + "(editShoppingCart) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 2.9
     * @param name
     * @param credit_number
     * @param month
     * @param year
     * @param cvv
     * @param ID
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return
     */
    public Response guestPurchase(String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip) {
        String path = "shopping_cart/purchase";
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("name", name);
            jsonPost.put("credit_number", credit_number);
            jsonPost.put("month", month);
            jsonPost.put("year", year);
            jsonPost.put("cvv", cvv);
            jsonPost.put("ID", ID);
            jsonPost.put("address", address);
            jsonPost.put("city", city);
            jsonPost.put("country", country);
            jsonPost.put("zip", zip);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "157", "Error: guestPurchase, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + this.getUserName() + ": (guestPurchase) response: " + response + ANSI_RESET);
        return response;
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

    /**
     * @requirement 3.2 open store
     * @param storeName
     * @return
     */
    public Response openStore(String storeName){
        String path = String.format("%s/add_store", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("storeName", storeName);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "193", "Error: addProduct, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber + path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(openStore) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 3.7 show history
     * @return
     */
    public Response showUserHistory() {
        String path = String.format("%s/user_history", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseSubscriber + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(showUserHistory) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 3.3 write comment
     * @param storeID
     * @param productID
     * @param rate
     * @param review
     * @return
     */
    public Response writeComment(int storeID, int productID, double rate, String review) {
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
        return response;
    }

    /**
     * @requirement 3.4 subscriber (user) purchase
     * @param credit_number
     * @param month
     * @param year
     * @param cvv
     * @param ID
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return
     */
    public Response subscriberPurchase(String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip) {
        String path = String.format("%s/shopping_cart/purchase", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("credit_number", credit_number);
            jsonPost.put("month", month);
            jsonPost.put("year", year);
            jsonPost.put("cvv", cvv);
            jsonPost.put("ID", ID);
            jsonPost.put("address", address);
            jsonPost.put("city", city);
            jsonPost.put("country", country);
            jsonPost.put("zip", zip);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "197", "Error: subscriberPurchase, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseSubscriber+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(subscriberPurchase) response: " + response + ANSI_RESET);
        return response;
    }

    //Store Owner Service

    /**
     * @requirement 4.1.1
     * @param storeID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return boolean
     */
    public Response addProduct(int storeID, String productName, String category, double price, int quantity) {
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
        return response;
    }

    /**
     * @requirement 4.1.2
     * @param storeID
     * @param productID
     * @return Response
     */
    public Response removeProduct(int storeID, int productID) {
        String path = String.format("%s/store/%s/remove_product/%s", this.userID, storeID, productID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeProduce) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 4.1.3
     * @param storeID
     * @param productID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return boolean
     */
    public Response editProduct(int storeID, int productID, String productName, String category, double price, int quantity) {
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
        return response;
    }

    /**
     * @requirement 4.2.1
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response addBuyingPolicy(int storeID, Expression exp){
        String path = String.format("%s/store/%s/add_buying_policy", this.userID, storeID);
        JSONObject jsonPost = new JSONObject();
        try{
            jsonPost.put("Expression", exp);
        }
        catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "216", "Error: editProduct, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner + path,jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addBuyingPolicy) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 4.2.2
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response addDiscountPolicy(int storeID, Sale sale){
        String path = String.format("%s/store/%s/add_discount_policy", this.userID, storeID);
        JSONObject jsonPost = new JSONObject();
        try{
            jsonPost.put("Sale", sale);
        }
        catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "216", "Error: addDiscountPolicy, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner + path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * @requirement 4.2.3
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response removeBuyingPolicy(int storeID){
        String path = String.format("%s/store/%s/remove_buying_policy", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * @requirement 4.2.4
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response removeDiscountPolicy(int storeID){
        String path = String.format("%s/store/%s/remove_discount_policy", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * @requirement 4.2.5
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response getPoliciesInfo(int storeID){
        String path = String.format("%s/store/%s/get_policies_info", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * @requirement 4.3
     * @param storeID
     * @param newOwnerID
     * @return boolean
     */
    public Response addOwner(int storeID, int newOwnerID) {
        String path = String.format("%s/store/%s/add_new_owner/%s", this.userID, storeID, newOwnerID);
        /*JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("userID", newOwnerID);
            jsonPost.put("storeID", storeID);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "193", "Error: addOwner, making post json"));
        }

         */
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addOwner) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 4.4
     * @param storeID
     * @param ownerToRemoveID
     * @return Response
     */
    public Response removeOwner(int storeID, int ownerToRemoveID){
        String path = String.format("%s/store/%s/remove_owner/%s", this.userID, storeID, ownerToRemoveID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeManager) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 4.5
     * @param storeID
     * @param newManagerID
     * @return Response
     */
    public Response addManager(int storeID, int newManagerID){
        String path = String.format("%s/store/%s/add_new_manager/%s", this.userID, storeID, newManagerID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(addManager) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * @requirement 4.6
     * @param storeID storeID
     * @param managerID managerID
     * @param permissions permissions
     * @return Response
     */
    public Response editManagerPermissions(int storeID, int managerID, List<User.Permission> permissions) {
        String path = String.format("%s/store/%s/edit_manager_permissions/%s", this.userID, storeID, managerID);
        JSONObject jsonPost = new JSONObject();
        try {
            for (User.Permission permissionKey : permissions) {
                jsonPost.put("Permission", permissionKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(editManagerPermissions) response: " + response + ANSI_RESET);
        return response;
    }
    public Response GetPossiblePermissionsToManager(int storeId) {
        String path = String.format("%s/store/%s/get_permissions_to_manager", this.userID,storeId);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        //if(response.getIsErr()) {
        //    System.out.println("(GetPossiblePermissionsToManager) response: " + response);
        //    return response;
        //}
        //List<String> dummyPermissionsList = response.returnPermissionList();
        return response;
    }

    /**
     * @requirement 4.7
     * @param storeID
     * @param managerToRemoveID
     * @return boolean
     */
    public Response removeManager(int storeID, int managerToRemoveID){
        String path = String.format("%s/store/%s/remove_manager/%s", this.userID, storeID, managerToRemoveID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner+path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(removeManager) response: " + response + ANSI_RESET);
        return response;
    }
    public Response showOwnerStores() {
        String path = String.format("%s/stores_owner", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }
    public Response showManagerStores() {
        String path = String.format("%s/stores_manager", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * requirement 4.9
     * @param storeID
     * @return Response
     */
    public Response showStoreWorkers(int storeID) {
        String path = String.format("%s/store/%s/workers", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

    /**
     * requirement 4.11
     * @param storeID
     * @return List<DummyShoppingHistory>
     */
    public Response ownerStoreHistory(int storeID) {
        String path = String.format("%s/store_history_owner/%s", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(ShowStoreHistory) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }


    //Admin
    /**
     * requirement 6.4
     * @param storeID
     * @return List<DummyShoppingHistory>
     */
    public Response adminStoreHistory(int storeID) {
        String path = String.format("%s/store_history_admin/%s", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(ShowStoreHistory) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }

    /**
     * requirement 6.4
     * @param userToShow
     * @return List<DummyShoppingHistory>
     */
    public Response adminUserHistory(int userToShow) {
        String path = String.format("%s/user_history_admin/%s", this.userID, userToShow);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(ShowUserHistory) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }

    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public Response AdminAllStores() {
        String path = String.format("%s/stores", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(ShowAllStoresHistory) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }

    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public Response AdminAllUsers() {
        String path = String.format("%s/users", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(ShowAllUsersHistory) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }
    /**
     * requirement 6.6
     * @return Double
     */
    @Override
    public Response AdminDailyIncomeForSystem() {
        String path = String.format("%s/admin_daily_income_for_system", this.userID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(AdminDailyIncomeForSystem) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }

    /**
     * requirement 4.12
     * @return Double
     */
    @Override
    public Response OwnerDailyIncomeForStore(int storeID) {
        String path = String.format("%s/store/%s/store_history_owner", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseAdmin + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(OwnerDailyIncomeForStore) response: " + response.returnHistoryList() + ANSI_RESET);
        return response;
    }

    /**
     * requirement 8.3.1
     * @return Response
     */
    @Override
    public Response submissionBidding(int storeID, int productID, int quantity, Double productPrice) {
        String path = String.format("%s/submission_bidding", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("storeID", storeID);
            jsonPost.put("productID", productID);
            jsonPost.put("quantity", productPrice);
            jsonPost.put("productPrice", quantity);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "216", "Error: submissionBidding, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(submissionBidding) response: " + response + ANSI_RESET);
        return response;
    }

    /**
     * requirement 8.3.2
     * @return Response
     */
    @Override
    public Response ResponseForSubmissionBidding(int storeID, int productID, int userWhoOffer, int quantity, Double productPrice) {
        String path = String.format("%s/response_for_submission_bidding", this.userID);
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("userWhoOffer", userWhoOffer);
            jsonPost.put("storeID", storeID);
            jsonPost.put("productID", productID);
            jsonPost.put("quantity", quantity);
            jsonPost.put("productPrice", productPrice);
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "216", "Error: ResponseForSubmissionBidding, making post json"));
        }
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseOwner+path, jsonPost.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(editProduct) ResponseForSubmissionBidding: " + response + ANSI_RESET);
        return response;
    }

    /**
     * requirement 8.3- None
     * @return Response
     */
    @Override
    public Response ShowBids(int storeID) {
        String path = String.format("%s/store/%s/show_bids", this.userID, storeID);
        JSONObject jsonResponse = HttpRequest.sendGetRequest(urlbaseOwner + path, this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        return response;
    }

}
