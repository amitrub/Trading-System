package TradingSystem.Client;

import TradingSystem.Server.Service_Layer.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;


public class HttpRequest {

    public static Response sendGetRequest(String urlStr, String connID){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            //adding header
            httpURLConnection.setRequestProperty("connID", connID);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            JSONObject JO = new JSONObject(response.toString());
            return new Response(JO.getString("id"), JO.getString("errMsg"));


        }
        catch (Exception e){
            return new Response(1, "");

        }
    }

    public static Response sendPOSTGETRequest(String urlStr, String post_data, String connID){

        try {
//            String post_data="key1=value1&key2=value2";

            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            //adding header
//            httpURLConnection.setRequestProperty("Auth","Token");
//            httpURLConnection.setRequestProperty("Data1","Value1");
            httpURLConnection.setRequestProperty("connID", connID);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);

            //Adding Post Data
            OutputStream outputStream=httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            osw.write(post_data);
            osw.flush();
            osw.close();
//            outputStream.write(post_data.toString());
//            outputStream.flush();
//            outputStream.close();

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

//            return response.toString();
            return new Response(1, "");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error in Making POST Request");
        }
        return new Response(1, "");
    }

    public static String sendPOSTRequest(String urlStr, String post_data, String connID){

        try {
//            String post_data="key1=value1&key2=value2";

            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            //adding header
//            httpURLConnection.setRequestProperty("Auth","Token");
//            httpURLConnection.setRequestProperty("Data1","Value1");
            httpURLConnection.setRequestProperty("connID", connID);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);

            //Adding Post Data
            OutputStream outputStream=httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            osw.write(post_data);
            osw.flush();
            osw.close();
//            outputStream.write(post_data.toString());
//            outputStream.flush();
//            outputStream.close();

            System.out.println("Response Code "+httpURLConnection.getResponseCode());

//            String line="";
//            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
//            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//            StringBuilder response=new StringBuilder();
//            while ((line=bufferedReader.readLine())!=null){
//                response.append(line);
//            }
//            bufferedReader.close();
//            System.out.println("Response : "+response.toString());
            return "";
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error in Making POST Request");
        }
        return ";;";
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

