package com.example.TradingSystem.Client;

import org.json.JSONArray;
import org.json.JSONException;

public class Client {
    private String urlbase = "http://localhost:8080/api/" ;
    private int id = -1;

    public Client() {
    }

    public void setId(int id) {
        this.id = id;
    }
    public void getTest1(){
        String response = HttpRequest.sendGetRequest(urlbase + "test1");
        System.out.println(response);
        try {
            JSONArray out = new JSONArray(response.toString());
            for (int i = 0; i<out.length(); i++)
            {
                System.out.println(out.getJSONObject(i).getString("id"));
            }
        }
        catch (Exception e){
            System.out.println("Error in Making Get Request");
        }


    }



}
