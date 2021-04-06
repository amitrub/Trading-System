package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.DummyUser;
import org.json.JSONArray;
import org.json.JSONException;

public class Client {
//    private String urlbase = "http://localhost:8080/api/" ;
    private String urlbase = "http://10.100.102.59:8080/api/" ;
    private int id = -1;

    public boolean isLogin() {
        return id != -1;
    }

    public Client() {
    }

    public void Register(){
        String path = "register" ;
        String response = HttpRequest.sendGetRequest(urlbase + path, Integer.toString(this.id));
        System.out.println(response);
        this.id = Integer.parseInt(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("id"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
    }

    public void Login(String userName, String pass){
        String path = "login" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        String response = HttpRequest.sendPOSTGETRequest(urlbase + path, dummyUser.toString(), Integer.toString(this.id));
        System.out.println(response);
        this.id = Integer.parseInt(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("id"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
    }

    public void Logout(String userName, String pass){
        String path = "logout" ;
        DummyUser dummyUser = new DummyUser(userName, pass);
        String response = HttpRequest.sendPOSTRequest(urlbase + path, dummyUser.toString(), Integer.toString(this.id));
        System.out.println(response);
        this.id = Integer.parseInt(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("id"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
    }


//    private void handleJSON() {
//        JSONArray out = new JSONArray(response.toString());
//        for (int i = 0; i<out.length(); i++)
//        {
//            System.out.println(out.getJSONObject(i).getString("id"));
//        }
//    }

//    public void getTest1(){
//        String response = HttpRequest.sendGetRequest(urlbase + "test1");
//        System.out.println(response);
//        try {
//            JSONArray out = new JSONArray(response.toString());
//            for (int i = 0; i<out.length(); i++)
//            {
//                System.out.println(out.getJSONObject(i).getString("id"));
//            }
//        }
//        catch (Exception e){
//            System.out.println("Error in Making Get Request");
//        }
//    }



}
