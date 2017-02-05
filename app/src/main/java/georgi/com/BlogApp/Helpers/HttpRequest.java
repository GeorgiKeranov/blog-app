package georgi.com.BlogApp.Helpers;


import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpRequest {

    private String request;
    private String urlToRequest;
    private String cookie;
    private String method;


    public HttpRequest(String urlToRequest, String cookie, String method) {

        request = "";

        this.urlToRequest = urlToRequest;
        this.cookie = cookie;
        this.method = method;
    }

    public void addStringField(String fieldName, String value){

        if(request.equals("")) request = fieldName + "=" + value;
        else request = request + "&" + fieldName + "=" + value;
    }

    public String sendTheRequest() throws IOException {

        URL url;

        // Changing the url if the request method is GET
        // and if there are request params.
        if(method.equals("GET") && !request.equals("")) url = new URL(urlToRequest + "?" + request);

        else url = new URL(urlToRequest);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Cookie", cookie);

        // If the request is POST and if there are request params
        // it is writing the params to the request.
        if(method.equals("POST") && !request.equals("")) {
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(), "UTF-8")
                    );

            writer.write(request);
            writer.flush();
            writer.close();
        }

        // Response code from the connection.
        int responseCode = connection.getResponseCode();

        // Response code == 200 OK
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

        // Response code != 200 OK
        else {
            connection.disconnect();

            throw new IOException("Response is not OK : " + connection.getResponseMessage());
        }

    }
}
