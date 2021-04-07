package TradingSystem.Client;

import static TradingSystem.Server.ServiceLayer.Configuration.*;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


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
    public boolean isLogin() {
        return !(connID.equals(""));
    }

    //Guest
    public int connectSystem() {
        
        return 0;
    }
    public int exitSystem() { return 0; }
    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        JSONObject jsonResponse = HttpRequest.sendPOSTGETRequest(urlbaseGuest + path, dummyUser.toString(), this.connID);
        Response response = Response.makeResponseFromJSON(jsonResponse);
        System.out.println(ANSI_YELLOW + "(Register) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
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
    public ArrayList<DummySearch> Search(String mode, String minPrice, String maxPrice, String p_rank, String s_rank) {
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
            jsonSearch.put("minPrice", min); //"" || number //int
            jsonSearch.put("maxPrice", max); //"" || number //int
            jsonSearch.put("pRank", pRank); //"" || number 1-5 //int
            jsonSearch.put("sRank", sRank); //"" || number 1-5  //int
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "72", "Error in making serach JSON"));
        }
        JSONArray jsonArray = HttpRequest.sendPOSTGETRequestArr(urlbaseGuest + path, jsonSearch.toString(), this.connID);
        ArrayList<DummySearch> dummySearchResponeArr = DummySearch.makeDummySearchFromJSON(jsonArray);
        System.out.println(ANSI_YELLOW + "(Search) response: " + dummySearchResponeArr + ANSI_RESET);
//        this.userID = response.getUserID();
//        this.connID = response.getConnID();
        return dummySearchResponeArr;
    }
    public int showAllStores() { return 0; }
    public int showStoreProducts() { return 0; }
    public int addProductToCart() { return 0; }
    public int shopShoopingCart() { return 0; }

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
    public int addStore(){
        return 0;
    }
    public int showUserHistory() { return 0; }
    public int review() { return 0; }

    //Store Owner Service
    public int addProduct() { return 0; }
    public int removeProduct() { return 0; }
    public int editProduct() { return 0; }
    public int showStoreHistory() { return 0; }

    //Admin
    public int showAllUsersHistory() { return 0; }


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
