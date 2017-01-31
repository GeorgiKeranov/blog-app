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

public class MultipartDataHelper {

    private OutputStream oSWriter;
    private PrintWriter writer;
    private HttpURLConnection connection;

    private String boundary;
    private static String NEW_LINE = "\r\n";

    public MultipartDataHelper(String urlToPOST, String cookie) throws IOException {

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
        connection.setRequestProperty("Cookie", cookie);

        oSWriter = connection.getOutputStream();
        // Setting up the writer who will write the actual request.
        writer = new PrintWriter(new OutputStreamWriter(oSWriter, "UTF-8"));
    }

    public void addStringField(String fieldName, String value) {

        writer.append("--" + boundary + NEW_LINE);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"" + NEW_LINE);
        writer.append(NEW_LINE + value + NEW_LINE);

        writer.flush();
    }

    public void addFileField(String fieldName, File file) throws IOException {

        writer.append("--" + boundary + NEW_LINE);

        String fileName = file.getName();
        writer.append("Content-Disposition: form-data; " +
                "name=\"" + fieldName + "\"; filename=" + "\"" + fileName + "\"" + NEW_LINE);
        writer.append("Content-Type:" + URLConnection.guessContentTypeFromName(fileName) + NEW_LINE);
        writer.append("Content-Transfer-Encoding: binary").append(NEW_LINE);
        writer.append(NEW_LINE);
        writer.flush();

        FileInputStream fileIS = new FileInputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

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

        int respCode = connection.getResponseCode();

        if(respCode == HttpURLConnection.HTTP_OK) {

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

        else {
            throw new IOException("Server didn\'t return OK status code : " +
                    connection.getResponseMessage());
        }

    }

}
