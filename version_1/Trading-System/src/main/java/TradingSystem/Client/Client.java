package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.DummyUser;
import TradingSystem.Server.Service_Layer.Response;

public class Client {
//    private String urlbase = "http://localhost:8080/api/" ;
    private String urlbase = "http://10.100.102.59:8080/api/" ;
    private int connID = -1;

    public boolean isLogin() {
        return connID != -1;
    }

    public Client() {
    }

    public int Register(String userName, String pass){
        String path = "register" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), Integer.toString(this.connID));
        System.out.println(response);
        this.connID = Integer.parseInt("1");
        return connID;
    }

    public int Login(String userName, String pass){
        String path = "login" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        Response response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), Integer.toString(this.connID));
        System.out.println(response);
        this.connID = Integer.parseInt("1");
        return connID;
    }

    public int Logout(String userName, String pass){
        String path = "logout" ;
        Response response = HttpRequest.sendGetRequest(urlbase + path, Integer.toString(this.connID));
        System.out.println(response);
        this.connID = Integer.parseInt("1");
        return connID;
    }



    //        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("connID"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
//    private void handleJSON() {
//        JSONArray out = new JSONArray(response.toString());
//        for (int i = 0; i<out.length(); i++)
//        {
//            System.out.println(out.getJSONObject(i).getString("connID"));
//        }
//    }

//    public void getTest1(){
//        String response = HttpRequest.sendGetRequest(urlbase + "test1");
//        System.out.println(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("connID"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
//    }



}
