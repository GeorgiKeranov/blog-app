package georgi.com.BlogApp.Helpers;


import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpRequest {

    private HttpURLConnection connection;
    private String request;

    public HttpRequest(String urlToRequest, String cookie, String method) throws IOException {

        request = "";

        URL url = new URL(urlToRequest);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        if(method.equals("POST")) connection.setDoOutput(true);

        connection.setRequestProperty("Cookie", cookie);
    }

    public void addStringField(String fieldName, String value){

        if(request.equals("")) request = fieldName + "=" + value;
        else request = request + "&" + fieldName + "=" + value;
    }

    public String sendTheRequest() throws IOException {

        if(!request.equals("")) {
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(), "UTF-8")
                    );

            writer.write(request);
            writer.flush();
            writer.close();
        }

        int responseCode = connection.getResponseCode();

        if(responseCode == HTTP_OK) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8")
            );

            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) builder.append(line);
            reader.close();

            connection.disconnect();

            return builder.toString();
        }

        else {
            connection.disconnect();

            throw new IOException("Response is not OK : " + connection.getResponseMessage());
        }

    }
}
