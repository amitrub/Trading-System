package com.example.TradingSystem;
//import com.example.TradingSystem.Server.User.User;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {
    public static void main(String args[]){
        System.out.println("HTTP Request Example ");
//        Making Get Request
        sendGetRequest();
//        Making Post Request
//        sendPOSTRequest();
//        Parse Json Reponse
//        ParseJsonResponse();
    }

    public static void sendGetRequest(){
        try {
            URL url = new URL("http://localhost:8080/api/v1/user");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            //adding header
            httpURLConnection.setRequestProperty("Auth","Token");
            httpURLConnection.setRequestProperty("Data1","Value1");

            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            System.out.println("Response : "+response.toString());

            JSONArray out = new JSONArray(response.toString());
            for (int i = 0; i<out.length(); i++)
            {
                System.out.println(out.getJSONObject(i).getString("id"));
            }

//            System.out.println(out.getString("id"));

        }
        catch (Exception e){
            System.out.println("Error in Making Get Request");
        }
    }

    public static void sendPOSTRequest(){
        try {
            String post_data="{\"id\":1,\"name\":\"amit\",\"password\":\"qweasd\"}";

            URL url = new URL("http://localhost:8080/api/v2/user11");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            //adding header
            httpURLConnection.setRequestProperty("Auth","Token");
            httpURLConnection.setRequestProperty("Data1","Value1");
            httpURLConnection.setDoOutput(true);

            //Adding Post Data
            OutputStream outputStream=httpURLConnection.getOutputStream();
            outputStream.write(post_data.getBytes());
            outputStream.flush();
            outputStream.close();



            System.out.println("Response Code "+httpURLConnection.getResponseCode());

            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            System.out.println("Response : "+response.toString());



        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error in Making POST Request");
        }
    }


    public static void ParseJsonResponse(){
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/photos");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            //adding header

            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            System.out.println("Response : "+response.toString());
            JSONArray jsonArray=new JSONArray(response.toString());
            for (int i=0;i<jsonArray.length();i++){
                System.out.println("Title : "+jsonArray.getJSONObject(i).getString("title"));
                System.out.println("ID : "+jsonArray.getJSONObject(i).getInt("id"));
                System.out.println("URL : "+jsonArray.getJSONObject(i).getString("url"));
                System.out.println("===========================================================\n");
            }

        }
        catch (Exception e){
            System.out.println("Error in Making Get Request");
        }
    }
}
