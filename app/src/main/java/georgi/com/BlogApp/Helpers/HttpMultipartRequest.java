package georgi.com.BlogApp.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


// Creating multipart request from which you can upload files.
public class HttpMultipartRequest {

    private OutputStream oSWriter;
    private PrintWriter writer;
    private HttpURLConnection connection;

    private String boundary;
    private final String NEW_LINE = "\r\n";

    public HttpMultipartRequest(String urlToPOST, String cookie) throws IOException {

        // That is used when you are adding new fields to the request.
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(urlToPOST);
        connection = (HttpURLConnection) url.openConnection();
        // Setting the request method for http.
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // Setting the content type of the request.
        connection.setRequestProperty("Content-Type", "multipart/form-data; "
                + "boundary=" + boundary);

        // Setting the authenticated cookie to the request.
        connection.setRequestProperty("Cookie", cookie);

        // oSWriter is used when you are uploading file to the server.
        oSWriter = connection.getOutputStream();

        // Setting up the writer who will write the actual request.
        writer = new PrintWriter(new OutputStreamWriter(oSWriter, "UTF-8"));
    }

    public void addStringField(String fieldName, String value) {

        // Adding string key value to the request.
        writer.append("--" + boundary + NEW_LINE);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"" + NEW_LINE);
        writer.append(NEW_LINE + value + NEW_LINE);

        writer.flush();
    }

    public void addFileField(String fieldName, File file) throws IOException {

        // Adding file to the request.
        writer.append("--" + boundary + NEW_LINE);

        // Getting the file name of the file.
        String fileName = file.getName();

        // Setting the name of the param and the file name of the file.
        writer.append("Content-Disposition: form-data; " +
                "name=\"" + fieldName + "\"; filename=" + "\"" + fileName + "\"" + NEW_LINE);

        // Setting the content type by ready to use method.
        writer.append("Content-Type:" + URLConnection.guessContentTypeFromName(fileName) + NEW_LINE);

        // Setting the encoding to binary.
        writer.append("Content-Transfer-Encoding: binary").append(NEW_LINE);
        writer.append(NEW_LINE);
        writer.flush();


        FileInputStream fileIS = new FileInputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        // Writing the file on parts that are max 4MB.
        while((bytesRead = fileIS.read(buffer)) != -1) {
            oSWriter.write(buffer, 0, bytesRead);
        }

        oSWriter.flush();
        fileIS.close();

        writer.append(NEW_LINE);
        writer.flush();
    }

    public String sendTheRequest() throws IOException {

        writer.append("--" + boundary + "--" + NEW_LINE);
        writer.close();
        oSWriter.close();

        // Getting the response code from the server.
        int respCode = connection.getResponseCode();

        // Response code == 200 OK
        if(respCode == HttpURLConnection.HTTP_OK) {

            // Reading the response.
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            String response = "";

            while ((line = reader.readLine()) != null) {
                response = response + line;
            }

            reader.close();

            connection.disconnect();

            return response;
        }

        // Response code != 200 OK
        else {
            throw new IOException("Server didn\'t return OK status code : " +
                    connection.getResponseMessage());
        }

    }

}
