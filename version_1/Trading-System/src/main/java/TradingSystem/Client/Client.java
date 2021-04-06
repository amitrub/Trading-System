package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.DummyUser;
import TradingSystem.Server.Service_Layer.Response;
import jdk.nashorn.internal.scripts.JO;
import org.json.JSONObject;

public class Client {
//    private String urlbase = "http://localhost:8080/api/" ;
    private String urlbase = "http://10.100.102.59:8080/api/" ;
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
        return userID != -1;
    }

    public Client() {
    }

    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), this.connID);
        System.out.println("(Register) response: " + response);
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
        System.out.println("(Login) response: " + response);
        this.userID = response.getUserID();
        this.connID = response.getConnID();
        this.userName = userName;
        this.pass = pass;
        return userID;
    }

    public int Logout(){
        String path = "logout";
        Response response = HttpRequest.sendGetRequest(urlbase + path, this.connID);
        System.out.println("(Logout) response: " + response);
        this.userID = response.getUserID();
        return userID;
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
