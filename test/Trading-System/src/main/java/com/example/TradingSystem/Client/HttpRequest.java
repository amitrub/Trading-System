package com.example.TradingSystem.Client;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;


public class HttpRequest {

    public static String sendGetRequest(String urlStr){
        try {
            URL url = new URL(urlStr);
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
            return response.toString();


        }
        catch (Exception e){
            return "Error in Making Get Request";

        }
    }

    public static void sendPOSTRequest(){
        try {
            String post_data="key1=value1&key2=value2";

            URL url = new URL("https://postman-echo.com/post");
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

