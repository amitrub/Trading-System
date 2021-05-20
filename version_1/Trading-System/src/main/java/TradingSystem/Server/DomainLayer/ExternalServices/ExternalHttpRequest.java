package TradingSystem.Server.DomainLayer.ExternalServices;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;


public class ExternalHttpRequest {
    public static JSONObject sendPOSTRequest(String urlStr, String post_data) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            //adding header
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            //httpURLConnection.setRequestProperty("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
            httpURLConnection.setDoOutput(true);

//            //add form data
//            Map<String,String> arguments = new HashMap<>();
//            arguments.put("action_type", "handshake");
//
//            DataOutputStream outT = new DataOutputStream(httpURLConnection.getOutputStream());
//            outT.writeBytes(getParamsString(arguments));
//            outT.flush();
//            outT.close();
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(httpURLConnection.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();

//            StringJoiner sj = new StringJoiner("&");
//            for(Map.Entry<String,String> entry : arguments.entrySet())
//                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
//                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
//            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
//            int length = out.length;
//
//            httpURLConnection.setFixedLengthStreamingMode(length);
//            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            httpURLConnection.connect();
//            try(OutputStream os = httpURLConnection.getOutputStream()) {
//                os.write(out);
//            }

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

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

}
