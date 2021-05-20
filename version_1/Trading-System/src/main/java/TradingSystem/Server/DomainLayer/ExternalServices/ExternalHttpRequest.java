package TradingSystem.Server.DomainLayer.ExternalServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;


public class ExternalHttpRequest {
    public static JSONObject sendPOSTRequest(String urlStr, String post_data) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            //adding header
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);

            //Adding Post Data
            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
            osw.write(post_data);
            osw.flush();
            osw.close();

            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

}
