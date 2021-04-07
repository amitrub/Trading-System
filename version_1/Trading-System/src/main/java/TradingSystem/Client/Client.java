package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.DummyUser;
import TradingSystem.Server.Service_Layer.Response;
import org.json.JSONObject;

import static TradingSystem.Server.Service_Layer.Configuration.*;

public class Client {
    private String urlbase = "http://localhost:8080/api/" ;
//    private String urlbase = "http://10.100.102.59:8080/api/" ;
    private int userID = -1;
    private String connID = "";
    private String userName;
    private String pass;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogin() {
        return !(connID.equals(""));
    }

    public Client() {
    }

    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), this.connID);
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
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), this.connID);
        System.out.println(ANSI_YELLOW + "(Login) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
        this.connID = response.getConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }

    public int Logout(){
        String path = "logout";
        Response response = HttpRequest.sendGetRequest(urlbase + path, this.connID);
        System.out.println(ANSI_YELLOW + "(Logout) response: " + response + ANSI_RESET);
        this.userID = response.getUserID();
        this.connID = response.getConnID();
        return userID;
    }

    public int Search(String mode, String minPrice, String maxPrice, String p_rank, String s_rank) {
        String path = "search";
        JSONObject jsonSearch = new JSONObject();
        try {
            jsonSearch.put("mode", mode); //Product Name || Product Category
            jsonSearch.put("minPrice", minPrice); //"" || number
            jsonSearch.put("maxPrice", maxPrice); //"" || number
            jsonSearch.put("pRank", p_rank); //"" || number 1-5
            jsonSearch.put("sRank", s_rank); //"" || number 1-5
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Client", "Client", "72", "Error in making serach JSON"));
        }
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, jsonSearch.toString(), this.connID);
        System.out.println(ANSI_YELLOW + "(Search) response: " + response + ANSI_RESET);
//        this.userID = response.getUserID();
//        this.connID = response.getConnID();
        return response.getUserID();
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
